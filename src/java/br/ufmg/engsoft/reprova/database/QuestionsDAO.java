package br.ufmg.engsoft.reprova.database;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoCollection;

import br.ufmg.engsoft.reprova.mime.json.Json;
import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.model.Question;

/**
 * DAO for Question class on mongodb.
 */
public class QuestionsDAO {
    /**
     * Logger instance.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(QuestionsDAO.class);

    /**
     * Json formatter.
     */
    protected final Json json;

    /**
     * Questions collection.
     */
    protected final MongoCollection<Document> collection;

    /**
     * Basic constructor.
     *
     * @param mongoDB   the database, mustn't be null
     * @param json the json formatter for the database's documents, mustn't be null
     * @throws IllegalArgumentException if any parameter is null
     */
    public QuestionsDAO(Mongo mongoDB, Json json) {
        if (mongoDB == null) {
            throw new IllegalArgumentException("db mustn't be null");
        }

        if (json == null) {
            throw new IllegalArgumentException("json mustn't be null");
        }

        this.collection = mongoDB.getCollection("questions");

        this.json = json;
    }

    /**
     * Parse the given document.
     *
     * @param document the question document, mustn't be null
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if the given document is an invalid Question
     */
    protected Question parseDoc(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("document mustn't be null");
        }

        var doc = document.toJson();

        LOGGER.info("Fetched question: " + doc);

        try {
            var question = json.parse(doc, Question.Builder.class).build();

            return question;
        } catch (Exception e) {
            LOGGER.error("Invalid document in database!", e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Get the question with the given identifier.
     *
     * @param identifier the question's identifier in the database.
     * @return The question, or null if no such question.
     * @throws IllegalArgumentException if any parameter is null
     */
    public Question get(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("identifier mustn't be null");
        }

        var question = this.collection.find(eq(new ObjectId(identifier))).map(this::parseDoc).first();

        if (question == null) {
            LOGGER.info("No such question " + identifier);
        }

        return question;
    }

    /**
     * List all the questions that match the given non-null parameters. The
     * question's statement is ommited.
     *
     * @param theme the expected theme, or null
     * @param pvt   the expected privacy, or null
     * @return The questions in the collection that match the given parameters,
     *         possibly empty.
     * @throws IllegalArgumentException if there is an invalid Question
     */
    public Collection<Question> list(String theme, Boolean pvt) {
        var filters = Arrays.asList(theme == null ? null : eq("theme", theme), pvt == null ? null : eq("pvt", pvt))
                .stream().filter(Objects::nonNull) // mongo won't allow null filters.
                .collect(Collectors.toList());

        var doc = filters.isEmpty() // mongo won't take null as a filter.
                ? this.collection.find()
                : this.collection.find(and(filters));

        var result = new ArrayList<Question>();

        doc.projection(fields(exclude("statement"))).map(this::parseDoc).into(result);
        
        if(Environments.getInstance().getenQuestStats()){
            for (var question : result){
                question.getStatistics();
            }
        }

        return result;
    }

    /**
     * Adds or updates the given question in the database. If the given question has
     * an identifier, update, otherwise add.
     *
     * @param question the question to be stored
     * @return Whether the question was successfully added.
     * @throws IllegalArgumentException if any parameter is null
     */
    public boolean add(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("question mustn't be null");
        }

        question.calculatediffclty();
        Map<String, Object> record = null;
        if (question.record != null) {
            record = question.record // Convert the keys to string,
                    .entrySet() // and values to object.
                    .stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
        }

        Document doc = new Document()
                .append("theme", question.theme)
                .append("description", question.description)
                .append("statement", question.statement)
                .append("record", record == null ? null : new Document(record))
                .append("pvt", question.pvt);

        if (Environments.getInstance().getEnableEstimatedTime()){
            doc = doc.append("estimatedTime", question.estimatedTime);
        }

        if (Environments.getInstance().getdiffcltyGroup() != 0){
            doc = doc.append("diffclty", question.diffclty);
        }
        
        if (Environments.getInstance().getenMpleChoice()) {
            doc = doc.append("choices", question.getChoices());
        }
        if (Environments.getInstance().getenQuestStats()) {
            doc = doc.append("statistics", question.getStatistics());
        }

        var identifier = question.identifier;
        if (identifier != null) {
            var result = this.collection.replaceOne(eq(new ObjectId(identifier)), doc);

            if (!result.wasAcknowledged()) {
                LOGGER.warn("Failed to replace question " + identifier);
                return false;
            }
        } else {
            this.collection.insertOne(doc);
        }

        LOGGER.info("Stored question " + doc.get("_id"));

        return true;
    }

    /**
     * Remove the question with the given identifier from the collection.
     *
     * @param identifier the question identifier
     * @return Whether the given question was removed.
     * @throws IllegalArgumentException if any parameter is null
     */
    public boolean remove(String identifier) {
        if (identifier == null){
            throw new IllegalArgumentException("identifier mustn't be null");
        }

        var result = this.collection.deleteOne(eq(new ObjectId(identifier))).wasAcknowledged();

        if (result) {
            LOGGER.info("Deleted question " + identifier);
        } else {
            LOGGER.warn("Failed to delete question " + identifier);
        }

        return result;
    }
}

package br.ufmg.engsoft.reprova.database;

import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;

import org.bson.Document;
import org.bson.types.ObjectId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufmg.engsoft.reprova.mime.json.Json;
import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.model.Questionnaire;


/**
 * DAO for Questionnaire class on mongodb.
 */
public class QuestionnairesDAO {
  /**
   * Logger instance.
   */
  protected static final Logger LOGGER = LoggerFactory.getLogger(QuestionnairesDAO.class);

  /**
   * Json formatter.
   */
  protected final Json json;

  /**
   * Questionnaires collection.
   */
  protected final MongoCollection<Document> collection;


  /**
   * Basic constructor.
   * @param mongoDB    the database, mustn't be null
   * @param json  the json formatter for the database's documents, mustn't be null
   * @throws IllegalArgumentException  if any parameter is null
   */
  public QuestionnairesDAO(Mongo mongoDB, Json json) {
    if (mongoDB == null)
      throw new IllegalArgumentException("db mustn't be null");

    if (json == null)
      throw new IllegalArgumentException("json mustn't be null");

    this.collection = mongoDB.getCollection("questionnaires");

    this.json = json;
  }



  /**
   * Parse the given document.
   * @param document  the question document, mustn't be null
   * @throws IllegalArgumentException  if any parameter is null
   * @throws IllegalArgumentException  if the given document is an invalid Questionnaire
   */
  protected Questionnaire parseDoc(Document document) {
    if (document == null)
      throw new IllegalArgumentException("document mustn't be null");

    var doc = document.toJson();

    LOGGER.info("Fetched questionnaire: " + doc);

    try {
      var questionnaire = json
        .parse(doc, Questionnaire.Builder.class)
        .build();

      LOGGER.info("Parsed questionnaire: " + questionnaire);

      return questionnaire;
    }
    catch (Exception e) {
      LOGGER.error("Invalid document in database!", e);
      throw new IllegalArgumentException(e);
    }
  }


  /**
   * Get the questionnaire with the given identifier.
   * @param identifier  the questionnaire's identifier in the database.
   * @return The questionnaire, or null if no such questionnaire.
   * @throws IllegalArgumentException  if any parameter is null
   */
  public Questionnaire get(String identifier) {
    if (identifier == null)
      throw new IllegalArgumentException("identifier mustn't be null");

    var questionnaire = this.collection
      .find(eq(new ObjectId(identifier)))
      .map(this::parseDoc)
      .first();

    if (questionnaire == null)
      LOGGER.info("No such questionnaire " + identifier);

    return questionnaire;
  }

  /**
   * List all the questionnaires that match the given non-null parameters.
   * The questionnaire's statement is ommited.
   * @return The questionnaires in the collection that match the given parameters, possibly
   *         empty.
   * @throws IllegalArgumentException  if there is an invalid Questionnaire
   */
  public Collection<Questionnaire> list() {
    var doc = this.collection.find();

    var result = new ArrayList<Questionnaire>();

    doc.projection(fields())
      .map(this::parseDoc)
      .into(result);

    return result;
  }

  /**
   * Adds or updates the given questionnaire in the database.
   * If the given questionnaire has an identifier, update, otherwise add.
   * @param questionnaire  the questionnaire to be stored
   * @return Whether the questionnaire was successfully added.
   * @throws IllegalArgumentException  if any parameter is null
   */
  public boolean add(Questionnaire questionnaire) {
    if (questionnaire == null){
      throw new IllegalArgumentException("questionnaire mustn't be null");
    }

    ArrayList<Document> questions = new ArrayList<Document>();
    for (var question : questionnaire.questions){
      Map<String, Object> record = null;
      if (question.record != null){
        record = question.record // Convert the keys to string,
        .entrySet()                                // and values to object.
        .stream()
        .collect(
          Collectors.toMap(
            e -> e.getKey().toString(),
            Map.Entry::getValue
          )
        );
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
      
      if (Environments.getInstance().getenMpleChoice()){
        doc = doc.append("choices", question.getChoices());
      }

      if (Environments.getInstance().getdiffcltyGroup() != 0){
        doc = doc.append("diffclty", question.diffclty);
      }
      
      questions.add(doc);
    }

    Document doc = new Document()
        .append("avrgdiffclty", questionnaire.avrgdiffclty)
        .append("questions", questions);

    if (Environments.getInstance().getEnableEstimatedTime()){
      doc = doc.append("totEstmtdTime", questionnaire.totEstmtdTime);
    }
    
    var identifier = questionnaire.identifier;
    if (identifier != null) {
      var result = this.collection.replaceOne(
        eq(new ObjectId(identifier)),
        doc
      );

      if (!result.wasAcknowledged()) {
        LOGGER.warn("Failed to replace questionnaire " + identifier);
        return false;
      }
    }
    else
      this.collection.insertOne(doc);

    LOGGER.info("Stored questionnaire " + doc.get("_id"));

    return true;
  }


  /**
   * Remove the questionnaire with the given identifier from the collection.
   * @param identifier  the questionnaire identifier
   * @return Whether the given questionnaire was removed.
   * @throws IllegalArgumentException  if any parameter is null
   */
  public boolean remove(String identifier) {
    if (identifier == null)
      throw new IllegalArgumentException("identifier mustn't be null");

    var result = this.collection.deleteOne(
      eq(new ObjectId(identifier))
    ).wasAcknowledged();

    if (result)
      LOGGER.info("Deleted questionnaire " + identifier);
    else
      LOGGER.warn("Failed to delete questionnaire " + identifier);

    return result;
  }
}

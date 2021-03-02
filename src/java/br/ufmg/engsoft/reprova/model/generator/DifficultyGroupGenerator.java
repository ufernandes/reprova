package br.ufmg.engsoft.reprova.model.generator;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;
import br.ufmg.engsoft.reprova.model.diffclty.diffcltyFactory;

public class diffcltyGroupGenerator implements IQuestionnaireGenerator{

  /**
   * Auxiliary function that returns a list of questions with the given diffclty.
   * Attempts to fill the returned list with the given count of questions.
   */
  private List<Question> getQuestionsOfdiffclty(ArrayList<Question> allQuestions, int count, String diffclty){
    List<Question> questions = new ArrayList<Question>();
    List<Question> questionsOfdiffclty = allQuestions.stream()
                                        .filter(q -> q.diffclty.equals(diffclty))
                                        .collect(Collectors.toList());

    Collections.shuffle(questionsOfdiffclty);
    for (int i = 0; i < count; i++){
      if (i >= questionsOfdiffclty.size()){
        break;
      }
      questions.add(questionsOfdiffclty.get(i));
    }

    return questions;
  }

  /**
   * Generate a new Quesitonnaire based on the parameters.
   * Selects a collection of questions the best fit the parameters.
   * Calls the Questionnaire's Builder.
   */
  @Override
  public Questionnaire generate(QuestionsDAO questionsDAO, String avrgdiffclty, int questionsCount, int totEstmtdTime){
    Environments environments = Environments.getInstance();
    int valdiffcltyGroup = environments.getdiffcltyGroup();
    List<String> diffcltyGroup = new diffcltyFactory()
                                .getdiffclty(valdiffcltyGroup)
                                .getDifficulties();

    if (avrgdiffclty == null){
      avrgdiffclty = "Average";
    } else {
      if (!diffcltyGroup.contains(avrgdiffclty)){
        throw new IllegalArgumentException("invalid average diffclty");
      }
    }
    if (totEstmtdTime == 0){
      totEstmtdTime = Questionnaire.ESTMTD_TIME_MINS;
    }
    if (questionsCount == 0){
      questionsCount = Questionnaire.QUESTIONS_COUNT;
    }

    ArrayList<Question> questions = new ArrayList<Question>();
    ArrayList<Question> allQuestions = new ArrayList<Question>(questionsDAO.list(null, null));

    if (allQuestions.size() <= questionsCount){
      for(Question question : allQuestions){
        questions.add(question);
      }
    } else {
      int avrgQuestsCount = (int)Math.ceil(questionsCount * 0.5);
      List<Question> averageQuestions = getQuestionsOfdiffclty(allQuestions, avrgQuestsCount, avrgdiffclty);
      questions.addAll(averageQuestions);

      int remQuestsCount = questionsCount - questions.size();
      int easierQuestsCount = remQuestsCount % 2 == 1 ? remQuestsCount/2 + 1 : remQuestsCount/2;
      int hardrQuestsCount = remQuestsCount - easierQuestsCount;

      int easrdiffcltyIndx = diffcltyGroup.indexOf(avrgdiffclty);
      int hardrdiffcltyIndx = easrdiffcltyIndx;
      while (remQuestsCount > 0){
        if (hardrdiffcltyIndx == 0){
          easierQuestsCount += hardrQuestsCount;
          hardrQuestsCount = -1;
          hardrdiffcltyIndx = -1;
        } else {
          hardrdiffcltyIndx--;
        }
        
        if (easrdiffcltyIndx == diffcltyGroup.size()-1){
          hardrQuestsCount += easierQuestsCount;
          easierQuestsCount = -1;
          easrdiffcltyIndx = -1;
        } else {
          easrdiffcltyIndx++;
        }

        if (hardrQuestsCount != -1){
          List<Question> harderQuestions = getQuestionsOfdiffclty(allQuestions, hardrQuestsCount, diffcltyGroup.get(hardrdiffcltyIndx));
          hardrQuestsCount -= harderQuestions.size();
          remQuestsCount -= harderQuestions.size();
          questions.addAll(harderQuestions);
        }

        if (easierQuestsCount != -1){
          List<Question> easierQuestions = getQuestionsOfdiffclty(allQuestions, easierQuestsCount, diffcltyGroup.get(easrdiffcltyIndx));
          easierQuestsCount -= easierQuestions.size();
          remQuestsCount -= easierQuestions.size();
          questions.addAll(easierQuestions);
        }
      }
    }

    return new Questionnaire.Builder()
                .avrgdiffclty(avrgdiffclty)
                .totEstmtdTime(totEstmtdTime)
                .questions(questions)
                .build();
  }
}
package br.ufmg.engsoft.reprova.model.generator;

import java.util.ArrayList;
import java.util.Collections;

import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;

public class DefaultGenerator implements IQuestionnaireGenerator{

  @Override
  public Questionnaire generate(QuestionsDAO questionsDAO, String avrgdiffclty, int questionsCount, int totEstmtdTime){
    if (totEstmtdTime == 0){
      totEstmtdTime = Questionnaire.ESTMTD_TIME_MINS;
    }
    if (questionsCount == 0){
      questionsCount = Questionnaire.QUESTIONS_COUNT;
    }

    ArrayList<Question> questions = new ArrayList<Question>();
    ArrayList<Question> allQuestions = new ArrayList<Question>(questionsDAO.list(null, null));

    Collections.shuffle(allQuestions);
    for (int i = 0; i < questionsCount; i++){
      if (i >=  allQuestions.size()){
        break;
      }

      questions.add(allQuestions.get(i));
    }

    return new Questionnaire.Builder()
                .avrgdiffclty(avrgdiffclty)
                .totEstmtdTime(totEstmtdTime)
                .questions(questions)
                .build();
  };
}
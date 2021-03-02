package br.ufmg.engsoft.reprova.model.generator;

import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.model.Questionnaire;

public class EstimatedTimeCalculator extends ChainQuestionnaireGeneration{

  @Override
  public Questionnaire generate(Questionnaire questionnaire){
    int totEstmtdTime = 0;
    
    for (Question question : questionnaire.questions){
      totEstmtdTime += question.estimatedTime;
    }

    questionnaire.totEstmtdTime = totEstmtdTime;
    return handleGeneration(questionnaire);
  }
}
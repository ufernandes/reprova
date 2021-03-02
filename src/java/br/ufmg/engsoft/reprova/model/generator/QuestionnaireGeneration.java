package br.ufmg.engsoft.reprova.model.generator;

import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.model.Environments;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;

public class QuestionnaireGeneration extends ChainQuestionnaireGeneration {
  public QuestionnaireGeneration(){
    Environments environments = Environments.getInstance();
    int valdiffcltyGroup = environments.getdiffcltyGroup();

    this.generator = new GeneratorFactory().getGenerator(valdiffcltyGroup);
  }
  
  public Questionnaire generate(QuestionsDAO questionsDAO, String avrgdiffclty, int questionsCount, int totEstmtdTime){
    Questionnaire questionnaire = this.generator.generate(questionsDAO, avrgdiffclty, questionsCount, totEstmtdTime);
    return handleGeneration(questionnaire);
  }
}
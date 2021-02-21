package br.ufmg.engsoft.reprova.model.generator;

import br.ufmg.engsoft.reprova.model.Questionnaire;
import br.ufmg.engsoft.reprova.database.IQuestionsDAO;

public interface IQuestionnaireGenerator {
  public Questionnaire generate(IQuestionsDAO questionsDAO, String averageDifficulty, int questionsCount, int totalEstimatedTime);
}

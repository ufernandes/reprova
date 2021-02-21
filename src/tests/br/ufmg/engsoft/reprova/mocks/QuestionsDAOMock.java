package br.ufmg.engsoft.reprova.mocks;

import java.util.Collection;
import java.util.ArrayList;

import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.database.IQuestionsDAO;

public class QuestionsDAOMock implements IQuestionsDAO {
  private ArrayList<Question> _questions;

  public QuestionsDAOMock(int questionsCount, int estimatedTime) {
    _questions = new ArrayList<Question>();

    for (int i = 0; i < questionsCount; i++) {
	    _questions.add(
	  		new Question.Builder()
	    		.theme("theme " + (i+1))
	    		.description("description " + (i+1))
          .difficulty("Average")
          .estimatedTime(estimatedTime)
	    		.build()
			);
    }
  }

  public Question get(String id) {
    return null;
  }

  public Collection<Question> list(String theme, Boolean pvt) {
    return _questions;
  }

  public boolean add(Question question) {
    return true;
  }

  public boolean remove(String id) {
    return true;
  }
}
package br.ufmg.engsoft.reprova.database;

import java.util.Collection;

import br.ufmg.engsoft.reprova.model.Question;


public interface IQuestionsDAO {
  public Question get(String id);
  public Collection<Question> list(String theme, Boolean pvt);
  public boolean add(Question question);
  public boolean remove(String id);
}
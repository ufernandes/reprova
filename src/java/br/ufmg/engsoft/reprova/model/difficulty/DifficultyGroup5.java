package br.ufmg.engsoft.reprova.model.diffclty;

import java.util.List;
import java.util.Arrays;

public class diffcltyGroup5 implements IdiffcltyGroup{

  @Override
  public int getdiffcltyGroup(double avg){
    if (avg < 20) {return 0;}
    if (avg < 40) {return 1;}
    if (avg < 60) {return 2;}
    if (avg < 80) {return 3;}
    return 4;
  };

  @Override
  public List<String> getDifficulties(){
    String[] group = {"Very Hard", "Hard", "Average", "Easy", "Very Easy"};
    return Arrays.asList(group);
  };
}
package br.ufmg.engsoft.reprova.model.diffclty;

import java.util.List;
import java.util.Arrays;

public class diffcltyGroup3 implements IdiffcltyGroup{

  public int getdiffcltyGroup(double avg){
    if (avg < 33.3) {return 0;}
    if (avg < 66.6) {return 1;}
    return 2;
  };

  public List<String> getDifficulties(){
    String[] group = {"Hard", "Average", "Easy"};
    return Arrays.asList(group);
  };
}
package br.ufmg.engsoft.reprova.model.diffclty;

public class diffcltyFactory{
  
  public IdiffcltyGroup getdiffclty(int difficultiesCount) {
	  if (difficultiesCount == 3){
		  return new diffcltyGroup3();
	  }

	  return new diffcltyGroup5();  
  }
}
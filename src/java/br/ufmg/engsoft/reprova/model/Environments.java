package br.ufmg.engsoft.reprova.model;

import java.util.Optional;

public class Environments {
	private static Environments environments;
	
	private String token;
	private int port;
	
	private int difficultyGroup;
	
	private boolean enableAnswers;
    private boolean enableQuestionnaires;
	
	private Environments() {		
		Optional<String> enableAnswersEnv = Optional.ofNullable(System.getenv("ENABLE_ANSWERS"));
		enableAnswersEnv.ifPresentOrElse(
				enableAnswers -> this.enableAnswers = enableAnswers.toLowerCase().equals("true"),
				() -> this.enableAnswers = false);
		
		Optional<String> enableQuestionnairesEnv = Optional.ofNullable(System.getenv("ENABLE_QUESTIONNAIRES"));
        enableQuestionnairesEnv.ifPresentOrElse(
                enableQuestionnaires -> this.enableQuestionnaires = enableQuestionnaires.toLowerCase().equals("true"),
                () -> this.enableQuestionnaires = false);
		
		Optional<String> envDifficultyGroup = Optional.ofNullable(System.getenv("DIFFICULTY_GROUP"));
		envDifficultyGroup.ifPresentOrElse(
		        difficultyGroup -> this.difficultyGroup = Integer.parseInt(envDifficultyGroup.get()),
		        () -> this.difficultyGroup = 0);
		
		this.port = Integer.parseInt(System.getenv("PORT"));
		
		this.token = System.getenv("REPROVA_TOKEN");
	}
	
	public static Environments getInstance() {
		if (environments == null) {
			environments = new Environments();
		}
		
		return environments;
	}
	
	public boolean getEnableAnswers() {
		return this.enableAnswers;
	}
	
	public boolean getEnableQuestionnaires() {
	    return this.enableQuestionnaires;
	}
	
	public int getDifficultyGroup() {
	    return this.difficultyGroup;
	}
	
	public String getToken() {
	    return this.token;
	}
	
	public int getPort() {
	    return this.port;
	}

}
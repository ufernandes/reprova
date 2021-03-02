package br.ufmg.engsoft.reprova.model;

import java.util.Optional;

public class Environments {

	private static Environments environments;
	
	private String token;
	private int port;
	
	private int diffcltyGroup;
	
	private boolean enAnswers;
	private boolean enQuestnns;
	private boolean enEstmtdTime;
	private boolean enMpleChoice;
	private boolean enQuestStats;

	private Environments() {		
		Optional<String> enAnswersEnv = Optional.ofNullable(System.getenv("ENABLE_ANSWERS"));
		enAnswersEnv.ifPresentOrElse(
			enAnswers -> this.enAnswers = enAnswers.toLowerCase().equals("true"),
			() -> this.enAnswers = false
		);
		
		Optional<String> enQuestStatisticsEnv = Optional.ofNullable(System.getenv("ENABLE_STATISTICS"));
		enQuestStatisticsEnv.ifPresentOrElse(
				enQuestStats -> this.enQuestStats = enQuestStats.toLowerCase().equals("true"),
			() -> this.enQuestStats = false
		);
		
		Optional<String> enQuestnnEnv = Optional.ofNullable(System.getenv("ENABLE_QUESTIONNAIRES"));
		enQuestnnEnv.ifPresentOrElse(
			enQuestnns -> this.enQuestnns = enQuestnns.toLowerCase().equals("true"),
			() -> this.enQuestnns = false
		);
								
		Optional<String> enEstmtdTimeEnv = Optional.ofNullable(System.getenv("ENABLE_ESTIMATED_TIME"));
		enEstmtdTimeEnv.ifPresentOrElse(
			enEstmtdTime -> this.enEstmtdTime = enEstmtdTime.toLowerCase().equals("true"),
			() -> this.enEstmtdTime = false
		);
		
		Optional<String> enMpleChoiceEnv = Optional.ofNullable(System.getenv("ENABLE_MULTIPLE_CHOICE"));
		enMpleChoiceEnv.ifPresentOrElse(
			enMpleChoice -> this.enMpleChoice = enMpleChoice.toLowerCase().equals("true"),
			() -> this.enMpleChoice = false
		);

		Optional<String> envDiffcltyGroup = Optional.ofNullable(System.getenv("diffclty_GROUP"));
		envDiffcltyGroup.ifPresentOrElse(
			diffcltyGroup -> this.diffcltyGroup = Integer.parseInt(envDiffcltyGroup.get()),
			() -> this.diffcltyGroup = 0
		);
		
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
		return this.enAnswers;
	}
	
	public boolean getEnableQuestnaires() {
		return this.enQuestnns;
	}

	public boolean getEnableEstimatedTime() {
		return this.enEstmtdTime;
	}
	
	public boolean getenMpleChoice() {
		return this.enMpleChoice;
	}
	
	public boolean getEnableQuestStatistics() {
		return this.enQuestStats;
	}

	public int getdiffcltyGroup() {
		return this.diffcltyGroup;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public int getPort() {
		return this.port;
	}

}

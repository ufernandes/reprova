package br.ufmg.engsoft.reprova.model;

public class Answer {
	/**
	 * Unique identifier of the answer
	 */
	private String identifier;
	
	/**
	 * Reference to the parent question
	 */
	private String questionId;

	/**
	 * Description of the answer. Mustn't be null nor empty.
	 */
	private final String description;

	/**
	 * Attachment to the answer. May be null or empty.
	 */
	private final String attachment;
	
	/**
	 * Whether the question is private.
	 */
	private final boolean isPrivate;
	
	public String getId() {
		return this.identifier;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getAttachment() {
		return this.attachment;
	}
	
	public boolean getPvt() {
	    return this.isPrivate;
	}
	
	public String getQuestionId() {
	    return this.questionId;
	}
	
	/**
     * Protected constructor, should only be used by the builder.
     */
	protected Answer(String description, String attachment, boolean isPrivate, String questionId) {
		this.description = description;
		this.attachment = attachment;
		this.isPrivate = isPrivate;
		this.questionId = questionId;
	}
	
	public static class Builder {
	    protected String identifier;
	    protected String description;
	    protected boolean isPrivate;
	    protected String questionId;
	    
	    public Builder identifier(String identifier) {
	        this.identifier = identifier;
	        return this;
	    }
	    
	    public Builder description(String description) {
	        this.description = description;
	        return this;
	    }
	    
	    public Builder isPrivate(boolean isPrivate) {
	        this.isPrivate = isPrivate;
	        return this;
	    }
	    
	    public Builder questionId(String questionId) {
	        this.questionId = questionId;
	        return this;
	    }
	    
	    public Answer build() {
	        return new Answer(
                this.identifier,
                this.description,
                this.isPrivate,
                this.questionId
	        );
	    }
	}
}

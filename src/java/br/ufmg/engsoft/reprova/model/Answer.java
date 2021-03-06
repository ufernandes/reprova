package br.ufmg.engsoft.reprova.model;

/* Class Answer */
public class Answer {
	/**
	 * Unique id of the answer
	 */
	private String id;
	
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
	private final boolean pvt;
	
	/* getID */
	public String getId() {
		return this.id;
	}
	/* getDescription */
	public String getDescription() {
		return this.description;
	}
	
	
	public String getAttachment() {
		return this.attachment;
	}
	
	public boolean isPvt() {
	    return this.pvt;
	}
	
	public String getQuestionId() {
	    return this.questionId;
	}
	
	/**
     * Protected constructor, should only be used by the builder.
     */
	protected Answer(String description, String attachment, boolean pvt, String questionId) {
		this.description = description;
		this.attachment = attachment;
		this.pvt = pvt;
		this.questionId = questionId;
	}
	
	/* Class Builder */
	public static class Builder {
	    protected String id;
	    protected String description;
	    protected boolean pvt;
	    protected String questionId;
	    
		/* Builder */
	    public Builder id(String id) {
	        this.id = id;
	        return this;
	    }
	    
		/* Builder description */ 
	    public Builder description(String description) {
	        this.description = description;
	        return this;
	    }
	    
	    /* Builder pvt */
	    public Builder pvt(boolean pvt) {
	        this.pvt = pvt;
	        return this;
	    }
		
		
	    /* Builder questionId */
	    public Builder questionId(String questionId) {
	        this.questionId = questionId;
	        return this;
	    }
	    
	    /* Answer build */
	    public Answer build() {
	        return new Answer(
                this.id,
                this.description,
                this.pvt,
                this.questionId
	        );
	    }
	}
}

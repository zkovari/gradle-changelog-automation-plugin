package org.zkovari.changelog.domain;

public class ChangelogEntry {

    private String message;
    private EntryType type;
    private String author;
    private String reference;

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public EntryType getType() {
	return type;
    }

    public void setType(EntryType type) {
	this.type = type;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

}

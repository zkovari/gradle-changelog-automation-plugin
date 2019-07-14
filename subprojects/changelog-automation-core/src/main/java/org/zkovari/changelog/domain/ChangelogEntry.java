package org.zkovari.changelog.domain;

public class ChangelogEntry {

    private String title;
    private EntryType type;
    private String author;
    private String reference;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
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

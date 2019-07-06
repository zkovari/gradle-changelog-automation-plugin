package org.zkovari.changelog.domain;

public enum EntryType {

    // @formatter:off
    ADDED("Added"), 
    CHANGED("Changed"), 
    DEPRECATED("Deprecated"), 
    REMOVED("Removed"), 
    FIXED("Fixed"),
    SECURITY("Security");
    // @formatter:on

    private final String value;

    private EntryType(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

}

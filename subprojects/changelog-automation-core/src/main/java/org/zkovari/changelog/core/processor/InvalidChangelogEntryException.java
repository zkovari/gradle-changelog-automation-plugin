package org.zkovari.changelog.core.processor;

public class InvalidChangelogEntryException extends Exception {

    private static final long serialVersionUID = 7904969661594993100L;

    public InvalidChangelogEntryException(String message, Throwable cause) {
	super(message, cause);
    }

    public InvalidChangelogEntryException(String message) {
	super(message);
    }

}

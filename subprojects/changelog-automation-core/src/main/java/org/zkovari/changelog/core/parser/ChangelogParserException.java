package org.zkovari.changelog.core.parser;

public class ChangelogParserException extends Exception {

    private static final long serialVersionUID = 1272215186606393828L;

    public ChangelogParserException(String message, Throwable cause) {
	super(message, cause);
    }

    public ChangelogParserException(String message) {
	super(message);
    }

}

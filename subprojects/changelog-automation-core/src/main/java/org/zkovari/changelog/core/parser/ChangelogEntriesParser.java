package org.zkovari.changelog.core.parser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.zkovari.changelog.core.collector.ChangelogEntriesFileCollector;
import org.zkovari.changelog.domain.ChangelogEntry;

public class ChangelogEntriesParser {

    private ChangelogEntriesFileCollector fileCollector;
    private YamlChangelogEntryParser parser;

    ChangelogEntriesFileCollector getFileCollector() {
	if (fileCollector == null) {
	    fileCollector = new ChangelogEntriesFileCollector();
	}
	return fileCollector;
    }

    YamlChangelogEntryParser getParser() {
	if (parser == null) {
	    parser = new YamlChangelogEntryParser();
	}
	return parser;
    }

    public List<ChangelogEntry> parse(File inputDirectory) throws ChangelogParserException {
	List<File> files;
	try {
	    files = getFileCollector().collect(inputDirectory);
	} catch (IOException ex) {
	    throw new ChangelogParserException(ex.getMessage(), ex);
	}
	LinkedList<ChangelogEntry> changelogEntries = new LinkedList<>();
	for (File file : files) {
	    changelogEntries.add(getParser().parse(file));
	}

	return changelogEntries;
    }

    // package level for testing
    void setFileCollector(ChangelogEntriesFileCollector fileCollector) {
	this.fileCollector = fileCollector;
    }

    // package level for testing
    void setParser(YamlChangelogEntryParser parser) {
	this.parser = parser;
    }

}

package org.zkovari.changelog.core.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;
import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;

public class YamlChangelogEntryParser {

    public ChangelogEntry parse(File inputYamlFile) throws ChangelogParserException {
	Yaml yaml = new Yaml();
	InputStream ios;
	try {
	    ios = new FileInputStream(inputYamlFile);
	} catch (FileNotFoundException ex) {
	    throw new ChangelogParserException(ex.getMessage(), ex);
	}
	Map<String, Object> entries = yaml.load(ios);

	ChangelogEntry changeLogEntryObj = new ChangelogEntry();
	for (Entry<String, Object> entry : entries.entrySet()) {
	    if ("message".equalsIgnoreCase(entry.getKey())) {
		changeLogEntryObj.setMessage(getValueOrNull(entry));
	    } else if ("reference".equalsIgnoreCase(entry.getKey())) {
		changeLogEntryObj.setReference(getValueOrNull(entry));
	    } else if ("author".equalsIgnoreCase(entry.getKey())) {
		changeLogEntryObj.setAuthor(getValueOrNull(entry));
	    } else if ("type".equalsIgnoreCase(entry.getKey())) {
		changeLogEntryObj.setType(EntryType.valueOf(getValueOrNull(entry).toUpperCase()));
	    }
	}

	return changeLogEntryObj;
    }

    private String getValueOrNull(Entry<String, Object> entry) {
	if (entry.getValue() == null) {
	    return null;
	}
	return entry.getValue().toString();
    }

}

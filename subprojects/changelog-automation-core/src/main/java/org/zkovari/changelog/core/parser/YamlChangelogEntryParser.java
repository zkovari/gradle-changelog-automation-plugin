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
	ChangelogEntry changeLogEntryObj = new ChangelogEntry();
	Map<String, Object> entries = loadYaml(inputYamlFile);
	if (entries == null) {
	    return changeLogEntryObj;
	}

	for (Entry<String, Object> entry : entries.entrySet()) {
	    if (entry.getValue() == null) {
		continue;
	    }
	    setChangelogEntryField(changeLogEntryObj, entry);
	}

	return changeLogEntryObj;
    }

    private Map<String, Object> loadYaml(File inputYamlFile) throws ChangelogParserException {
	Yaml yaml = new Yaml();
	InputStream ios;
	try {
	    ios = new FileInputStream(inputYamlFile);
	} catch (FileNotFoundException ex) {
	    throw new ChangelogParserException("Input file does not exist: " + inputYamlFile.getName(), ex);
	}
	return yaml.load(ios);
    }

    private void setChangelogEntryField(ChangelogEntry changeLogEntryObj, Entry<String, Object> entry) {
	String value = entry.getValue().toString();
	if ("message".equalsIgnoreCase(entry.getKey())) {
	    changeLogEntryObj.setMessage(value);
	} else if ("reference".equalsIgnoreCase(entry.getKey())) {
	    changeLogEntryObj.setReference(value);
	} else if ("author".equalsIgnoreCase(entry.getKey())) {
	    changeLogEntryObj.setAuthor(value);
	} else if ("type".equalsIgnoreCase(entry.getKey())) {
	    changeLogEntryObj.setType(EntryType.valueOf(value.toUpperCase()));
	}
    }

}

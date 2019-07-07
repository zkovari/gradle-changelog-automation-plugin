package org.zkovari.changelog.domain;

import java.util.List;
import java.util.Map;

public class Release {

    private String version;
    private String date;
    private Map<EntryType, List<ChangelogEntry>> entries;

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public Map<EntryType, List<ChangelogEntry>> getEntries() {
	return entries;
    }

    public void setEntries(Map<EntryType, List<ChangelogEntry>> entries) {
	this.entries = entries;
    }

}

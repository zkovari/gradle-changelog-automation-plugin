package org.zkovari.changelog.core.generator;

import java.text.MessageFormat;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;
import org.zkovari.changelog.domain.Release;

public class ReleaseEntryGenerator {

    private static final String NEW_LINE = System.lineSeparator();

    public String generate(Release release) {
        StringBuilder releaseContent = new StringBuilder();

        appendHeader(releaseContent, release);
        appendEntries(releaseContent, release);

        return releaseContent.toString();
    }

    private void appendEntries(StringBuilder releaseContent, Release release) {
        if (release.getEntries() == null) {
            return;
        }
        SortedMap<String, EntryType> sortedTypes = new TreeMap<>();
        for (EntryType type : EntryType.values()) {
            sortedTypes.put(type.getValue(), type);
        }

        for (EntryType type : sortedTypes.values()) {
            if (release.getEntries().containsKey(type)) {
                appendEntriesPerType(releaseContent, type, release.getEntries().get(type));
            }
        }

    }

    private void appendEntriesPerType(StringBuilder releaseContent, EntryType type, List<ChangelogEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return;
        }
        releaseContent.append("### ").append(type.getValue()).append(NEW_LINE);
        for (ChangelogEntry entry : entries) {
            releaseContent.append(MessageFormat.format("- {0}{1}{2}", stringifyReference(entry.getReference()),
                    entry.getTitle(), stringifyAuthor(entry.getAuthor()))).append(NEW_LINE);
        }
        releaseContent.append(NEW_LINE);
    }

    private String stringifyReference(String value) {
        return value == null || value.isEmpty() ? "" : value + " ";
    }

    private String stringifyAuthor(String value) {
        return value == null || value.isEmpty() ? "" : " (" + value + ")";
    }

    private void appendHeader(StringBuilder releaseContent, Release release) {
        releaseContent
                .append(MessageFormat.format("## [{0}] - {1}{2}", release.getVersion(), release.getDate(), NEW_LINE));
    }

}

package org.zkovari.changelog.core.processor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;
import org.zkovari.changelog.domain.Release;

public class ChangelogEntriesProcessor {

    public Release processChangelogs(Collection<ChangelogEntry> changelogEntries, String version)
	    throws InvalidChangelogEntryException {
	validate(changelogEntries, version);

	Release release = new Release();
	release.setVersion(version);
	release.setDate(getCurrentDate());

	Map<EntryType, List<ChangelogEntry>> entriesGroupedByType = changelogEntries.stream()
		.collect(Collectors.groupingBy(ChangelogEntry::getType));
	release.setEntries(entriesGroupedByType);

	return release;
    }

    private void validate(Collection<ChangelogEntry> changelogEntries, String version)
	    throws InvalidChangelogEntryException {
	Objects.requireNonNull(changelogEntries);
	Objects.requireNonNull(version);
	if (version.trim().isEmpty()) {
	    throw new IllegalArgumentException("Version cannot be empty");
	}

	Optional<ChangelogEntry> entryWithNullType = changelogEntries.stream().filter(entry -> entry.getType() == null)
		.findFirst();
	if (entryWithNullType.isPresent()) {
	    throw new InvalidChangelogEntryException(
		    "Type cannot be null in changelog entry: " + entryWithNullType.get());
	}
    }

    private static String getCurrentDate() {
	LocalDate today = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	return today.format(formatter);
    }

}

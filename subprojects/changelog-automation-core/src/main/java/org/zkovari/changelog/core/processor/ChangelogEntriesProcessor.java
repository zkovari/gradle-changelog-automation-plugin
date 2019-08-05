/*******************************************************************************
 * Copyright 2019 Zsolt Kovari
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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

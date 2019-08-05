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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.zkovari.changelog.domain.EntryType.ADDED;
import static org.zkovari.changelog.domain.EntryType.REMOVED;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.zkovari.changelog.core.ChangelogTestBase;
import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;
import org.zkovari.changelog.domain.Release;

public class ChangelogEntriesProcessorTest extends ChangelogTestBase {

    private ChangelogEntriesProcessor processor;
    private ChangelogEntry addedEntry1;
    private ChangelogEntry addedEntry2;
    private ChangelogEntry removedEntry1;

    @Before
    public void setUp() {
        processor = new ChangelogEntriesProcessor();

        addedEntry1 = newChangelogEntry("m1", EntryType.ADDED, "ref1", "user1");
        addedEntry2 = newChangelogEntry("m2", EntryType.ADDED, "ref1", "user1");

        removedEntry1 = newChangelogEntry("m1", EntryType.REMOVED, "ref1", "user1");
    }

    private void checkBasicInfo(Release release) {
        assertNotNull("Expected the date not to be null", release.getDate());
        assertNotNull("Expected the version not to be null", release.getVersion());
        assertNotNull("Expected the changelog entries not to be null", release.getEntries());
        assertEquals("Release version", V1_0_0, release.getVersion());
        LocalDate today = LocalDate.now();
        String expectedDate = String.format("%d-%s%d-%s%d", today.getYear(), today.getMonthValue() > 9 ? "" : "0",
                today.getMonthValue(), today.getDayOfMonth() > 9 ? "" : "0", today.getDayOfMonth());
        assertEquals("Release date", expectedDate, release.getDate());
    }

    private void checkEntries(Release release, EntryType type, ChangelogEntry... entries) {
        assertTrue("Expected type " + type + " to be in release: " + release.getEntries(),
                release.getEntries().containsKey(type));
        assertThat(release.getEntries().get(type), IsCollectionContaining.hasItems(entries));
    }

    @Test
    public void testProcessWithEmptyEntries() throws Exception {
        processor.processChangelogs(emptyList(), V1_0_0);
    }

    @Test
    public void testProcessWithNullEntries() throws Exception {
        thrown.expect(NullPointerException.class);
        processor.processChangelogs(null, V1_0_0);
    }

    @Test
    public void testProcessWithNullVersion() throws Exception {
        thrown.expect(NullPointerException.class);
        processor.processChangelogs(asList(addedEntry1), null);
    }

    @Test
    public void testProcessWithEmptyVersion() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Version cannot be empty");
        processor.processChangelogs(asList(addedEntry1), "");
    }

    @Test
    public void testProcessWithNullType() throws Exception {
        thrown.expect(InvalidChangelogEntryException.class);
        thrown.expectMessage("Type cannot be null");
        processor.processChangelogs(Arrays.asList(newChangelogEntry("title", null)), V1_0_0);
    }

    @Test
    public void testProcessWithSingleEntry() throws Exception {
        Release release = processor.processChangelogs(asList(addedEntry1), V1_0_0);

        checkBasicInfo(release);
        checkEntries(release, ADDED, addedEntry1);
    }

    @Test
    public void testProcessWithMultipleButDifferentEntries() throws Exception {
        Release release = processor.processChangelogs(asList(addedEntry1, removedEntry1), V1_0_0);

        checkBasicInfo(release);
        checkEntries(release, ADDED, addedEntry1);
        checkEntries(release, REMOVED, removedEntry1);
    }

    @Test
    public void testProcessWithMultipleAddedEntries() throws Exception {
        Release release = processor.processChangelogs(asList(addedEntry1, addedEntry2), V1_0_0);

        checkBasicInfo(release);
        checkEntries(release, ADDED, addedEntry1, addedEntry2);
    }

    @Test
    public void testProcessWithAllTypeOfEntries() throws Exception {
        Map<EntryType, List<ChangelogEntry>> entries = new HashMap<>();
        for (EntryType entryType : EntryType.values()) {
            if (!entries.containsKey(entryType)) {
                entries.put(entryType, new ArrayList<>());
            }

            entries.get(entryType).add(newChangelogEntry("title1 of type " + entryType.getValue(), entryType));
            entries.get(entryType).add(newChangelogEntry("title2 of type " + entryType.getValue(), entryType));
        }
        Release release = processor.processChangelogs(
                entries.values().stream().flatMap(Collection::stream).collect(Collectors.toList()), V1_0_0);

        checkBasicInfo(release);

        for (EntryType entryType : EntryType.values()) {
            checkEntries(release, entryType, entries.get(entryType).stream().toArray(ChangelogEntry[]::new));
        }
    }

}

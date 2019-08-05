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
package org.zkovari.changelog.core.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.zkovari.changelog.core.ChangelogTestBase;
import org.zkovari.changelog.core.collector.ChangelogEntriesFileCollector;
import org.zkovari.changelog.domain.ChangelogEntry;

public class ChangelogEntriesParserTest extends ChangelogTestBase {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ChangelogEntriesFileCollector collector;
    @Mock
    private YamlChangelogEntryParser yamlParser;
    @Mock
    private File rootDir;
    @Mock
    private File entryFile1;
    @Mock
    private File entryFile2;
    @Mock
    private ChangelogEntry entry1;
    @Mock
    private ChangelogEntry entry2;

    private ChangelogEntriesParser parser;

    @Before
    public void setUp() {
	parser = new ChangelogEntriesParser();
	parser.setFileCollector(collector);
	parser.setParser(yamlParser);
    }

    @Test
    public void testNullSafeGetters() {
	ChangelogEntriesParser newParser = new ChangelogEntriesParser();
	assertNotNull("Expected file collector not to be empty", newParser.getFileCollector());
	assertNotNull("Expected parser not to be empty", newParser.getParser());
    }

    @Test
    public void testParseWithNoFiles() throws Exception {
	when(collector.collect(rootDir)).thenReturn(Collections.emptyList());
	List<ChangelogEntry> entries = parser.parse(rootDir);

	assertTrue("Expected empty but got " + entries, entries.isEmpty());
    }

    @Test
    public void testParseEntriesSuccessfully() throws Exception {
	when(collector.collect(rootDir)).thenReturn(Arrays.asList(entryFile1, entryFile2));
	when(yamlParser.parse(entryFile1)).thenReturn(entry1);
	when(yamlParser.parse(entryFile2)).thenReturn(entry2);

	List<ChangelogEntry> entries = parser.parse(rootDir);
	assertThat(entries, IsCollectionContaining.hasItems(entry1, entry2));
    }

    @Test
    public void testParseWhenCollectorThrowsIOException() throws Exception {
	when(collector.collect(rootDir)).thenThrow(new IOException("test exception"));

	thrown.expect(ChangelogParserException.class);
	thrown.expectMessage("test exception");
	parser.parse(rootDir);
    }

}

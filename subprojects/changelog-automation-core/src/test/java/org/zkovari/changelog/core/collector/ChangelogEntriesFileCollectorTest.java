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
package org.zkovari.changelog.core.collector;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.zkovari.changelog.core.ChangelogTestBase;

public class ChangelogEntriesFileCollectorTest extends ChangelogTestBase {

    private ChangelogEntriesFileCollector collector;

    @Before
    public void setUp() {
        collector = new ChangelogEntriesFileCollector();
    }

    private List<File> collect() throws IOException {
        List<File> files = collector.collect(temp.getRoot());
        assertNotNull("Collected files collection was null", files);
        return files;
    }

    @Test
    public void testCollectWithNullParam() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("rootDirectory cannot be null");
        collector.collect(null);
    }

    @Test
    public void testCollectInEmptyFolder() throws IOException {
        List<File> files = collect();
        assertTrue("Expected collected files to be empty, instead found: " + files, files.isEmpty());
    }

    @Test
    public void testCollectWithNotExistingFolder() throws IOException {
        String dir = "test-non-existing-folder-for-changelog-file-collectors-test";

        thrown.expect(IOException.class);
        thrown.expectMessage("Given root directory does not exist: " + dir);
        collector.collect(new File(dir));
    }

    @Test
    public void testCollectWithYamlFiles() throws IOException {
        File yamlEntry1 = temp.newFile("entry1.yml");
        File yamlEntry2 = temp.newFile("entry2.yaml");

        List<File> files = collect();

        assertFalse(files.isEmpty());
        assertThat(files, hasItem(yamlEntry1));
        assertThat(files, hasItem(yamlEntry2));
    }

    @Test
    public void testCollectWithOtherNonYamlFiles() throws IOException {
        File yamlEntry1 = temp.newFile("entry1.yml");
        File otherNonYaml = temp.newFile("other_file");

        List<File> files = collect();

        assertFalse(files.isEmpty());
        assertThat(files, hasItem(yamlEntry1));
        assertThat(files, not(hasItem(otherNonYaml)));
    }

    @Test
    public void testCollectFromNestedFolders() throws IOException {
        File yamlEntry1 = temp.newFile("entry1.yml");
        File nestedFolder = temp.newFolder("nested");
        File nestedYamlEntry = Files.createFile(nestedFolder.toPath().resolve("entry-in-nested-dir.yml")).toFile();
        List<File> files = collect();

        assertFalse(files.isEmpty());
        assertThat(files, hasItem(yamlEntry1));
        assertThat(files, not(hasItem(nestedYamlEntry)));

    }

}

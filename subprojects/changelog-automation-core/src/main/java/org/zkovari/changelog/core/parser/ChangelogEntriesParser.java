/*******************************************************************************
 * Copyright 2019-2020 Zsolt Kovari
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

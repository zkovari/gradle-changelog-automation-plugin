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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    // TODO better error handling when input fiel is invalid. See
    // testParseNonInvalidYamlFile test case
    private Map<String, Object> loadYaml(File inputYamlFile) throws ChangelogParserException {
        Yaml yaml = new Yaml();
        try (InputStream ios = new FileInputStream(inputYamlFile)) {
            return yaml.load(ios);
        } catch (FileNotFoundException ex) {
            throw new ChangelogParserException("Input file does not exist: " + inputYamlFile.getName(), ex);
        } catch (IOException ex) {
            throw new ChangelogParserException("Could not close the input stream of file: " + inputYamlFile, ex);
        }
    }

    private void setChangelogEntryField(ChangelogEntry changeLogEntryObj, Entry<String, Object> entry) {
        String value = entry.getValue().toString();
        if ("title".equalsIgnoreCase(entry.getKey())) {
            changeLogEntryObj.setTitle(value);
        } else if ("reference".equalsIgnoreCase(entry.getKey())) {
            changeLogEntryObj.setReference(value);
        } else if ("author".equalsIgnoreCase(entry.getKey())) {
            changeLogEntryObj.setAuthor(value);
        } else if ("type".equalsIgnoreCase(entry.getKey())) {
            changeLogEntryObj.setType(EntryType.valueOf(value.toUpperCase()));
        }
    }

}

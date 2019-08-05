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
package org.zkovari.changelog.gradle.api.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.zkovari.changelog.gradle.api.tasks.FetchChangelogScript;
import org.zkovari.changelog.gradle.api.tasks.ProcessChangelogEntries;

public class ChangelogAutomationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        ProcessChangelogEntries processTask = project.getTasks().create("processChangelogEntries",
                ProcessChangelogEntries.class);
        processTask.setInputDirectory(project.file("changelogs/unreleased"));
        processTask.setOutputfile(project.file("CHANGELOG.md"));
        processTask.setGroup("Changelog");
        processTask.setDescription("Processes changelog entries and combine them into CHANGELOG.md");

        FetchChangelogScript fetchChangelogScriptTask = project.getTasks().create("fetchChangelogScript",
                FetchChangelogScript.class);
        fetchChangelogScriptTask.setOutputDirectory(project.getRootProject().file("scripts"));
        fetchChangelogScriptTask.getOutputs().upToDateWhen(t -> false);
        fetchChangelogScriptTask.setGroup("Changelog");
        fetchChangelogScriptTask.setDescription("Fetches the changelog.sh script into your project");
    }

}

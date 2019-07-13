package org.zkovari.changelog.gradle.api.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.zkovari.changelog.gradle.api.tasks.ProcessChangelogEntries;

public class ChangelogAutomationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        ProcessChangelogEntries processTask = project.getTasks().create("processChangelogEntries",
                ProcessChangelogEntries.class);
        processTask.setInputDirectory(project.file("changelog/unreleased"));
        processTask.setOutputfile(project.file("CHANGELOG.md"));
    }

}

package org.zkovari.changelog.gradle.api.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.zkovari.changelog.gradle.api.tasks.ProcessChangelogEntries;

public class ChangelogAutomationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
	TaskProvider<ProcessChangelogEntries> processTaskProvider = project.getTasks()
		.register("processChangelogEntries", ProcessChangelogEntries.class);
	processTaskProvider.get().setInputDirectory(project.file("changelog/unreleased"));
	processTaskProvider.get().setOutputfile(project.file("CHANGELOG.md"));
    }

}

package org.zkovari.changelog.gradle.api.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.zkovari.changelog.gradle.api.tasks.ProcessChangelogEntries;

public class ChangelogAutomationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
	project.getTasks().register("processChangelogEntries", ProcessChangelogEntries.class);
    }

}

package org.zkovari.changelog.gradle.api.plugins;

import java.io.File;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.OutputDirectory;
import org.zkovari.changelog.gradle.api.tasks.FetchChangelogScript;
import org.zkovari.changelog.gradle.api.tasks.ProcessChangelogEntries;

public class ChangelogAutomationPlugin implements Plugin<Project> {

    private File outputDirectory;

    @OutputDirectory
    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

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

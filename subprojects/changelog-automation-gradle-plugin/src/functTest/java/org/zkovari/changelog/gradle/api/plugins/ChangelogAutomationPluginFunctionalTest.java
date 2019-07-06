package org.zkovari.changelog.gradle.api.plugins;

import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE;
import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ChangelogAutomationPluginFunctionalTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File settingsFile;
    private File buildFile;

    @Before
    public void setup() throws IOException {
	settingsFile = testProjectDir.newFile("settings.gradle");
	buildFile = testProjectDir.newFile("build.gradle");
    }

    @Test
    public void testApply() throws IOException {
	writeFile(settingsFile, "rootProject.name = 'test-project'");
	String buildFileContent = "plugins {id 'org.zkovari.changelog'}";
	writeFile(buildFile, buildFileContent);

	BuildResult result = GradleRunner.create().withProjectDir(testProjectDir.getRoot())
		.withArguments("processChangelogEntries").withPluginClasspath().build();

	assertEquals(UP_TO_DATE, result.task(":processChangelogEntries").getOutcome());
    }

    private void writeFile(File destination, String content) throws IOException {
	try (BufferedWriter output = new BufferedWriter(new FileWriter(destination))) {
	    output.write(content);
	}
    }

}

package org.zkovari.changelog.gradle.api.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ChangelogAutomationPluginFunctionalTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File settingsFile;
    private File buildFile;
    private File entry1;
    private String testKitGradleProperties;

    @Before
    public void setup() throws IOException, URISyntaxException {
	settingsFile = testProjectDir.newFile("settings.gradle");
	buildFile = testProjectDir.newFile("build.gradle");
	Path unreleasedDir = Files.createDirectories(testProjectDir.getRoot().toPath().resolve("changelog/unreleased"));
	entry1 = unreleasedDir.resolve("entry1.yml").toFile();

	File testKitGradlePropertiesResource = new File(
		getClass().getClassLoader().getResource("testkit-gradle.properties").toURI());
	testKitGradleProperties = new String(Files.readAllBytes(testKitGradlePropertiesResource.toPath()));
    }

    private void writeFile(File destination, String content) throws IOException {
	try (BufferedWriter output = new BufferedWriter(new FileWriter(destination))) {
	    output.write(content);
	}
    }

    @Test
    public void testApply() throws Exception {
	writeFile(settingsFile, "rootProject.name = 'test-project'");
	String buildFileContent = "plugins {id 'org.zkovari.changelog'}";
	writeFile(buildFile, buildFileContent);
	writeFile(entry1, "message: test\ntype: added");

	writeFile(new File(testProjectDir.getRoot(), "gradle.properties"), testKitGradleProperties);

	BuildResult result = GradleRunner.create().withProjectDir(testProjectDir.getRoot())
		.withTestKitDir(testProjectDir.newFolder()).withPluginClasspath()
		.withArguments("processChangelogEntries").build();

	assertEquals(TaskOutcome.SUCCESS, result.task(":processChangelogEntries").getOutcome());
	assertThat(testProjectDir.getRoot().toPath().resolve("CHANGELOG.md").toFile()).exists();
    }

}

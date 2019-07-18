package org.zkovari.changelog.gradle.api.plugins

import java.nio.file.Files

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class ChangelogAutomationPluginFunctionalTest extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    private String testKitGradleProperties
    File settingsFile
    File propertiesFile
    File buildFile

    File changelogEntry1

    def setup() {
        File testKitGradlePropertiesResource = new File(
                getClass().getClassLoader().getResource("testkit-gradle.properties").toURI())
        testKitGradleProperties = new String(Files.readAllBytes(testKitGradlePropertiesResource.toPath()))
        def changlogEntriesDir = testProjectDir.newFolder('changelogs', 'unreleased')
        settingsFile = testProjectDir.newFile('settings.gradle')
        propertiesFile = testProjectDir.newFile('gradle.properties')
        buildFile = testProjectDir.newFile('build.gradle')
        changelogEntry1 = new File(changlogEntriesDir, "entry1.yml")

        settingsFile << "rootProject.name = 'test-project'"
        propertiesFile << testKitGradleProperties
        buildFile << """
            plugins {
                id 'org.zkovari.changelog'
            }
            version='1.0.0'
        """
    }

    BuildResult getRunner(String command) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withArguments(command)
                .build()
    }

    def "run processChangelogEntries when input folder is empty"() {
        when:
        BuildResult result = getRunner('processChangelogEntries')

        then:
        assert result.task(":processChangelogEntries").outcome == TaskOutcome.SUCCESS
    }

    def "run processChangelogEntries with changelog entry "(){
        given:
        changelogEntry1 << """
            title: Title
            type: added
            reference:
            author:
        """

        when:
        BuildResult result = getRunner('processChangelogEntries')

        then:
        assert result.task(":processChangelogEntries").outcome == TaskOutcome.SUCCESS
        def changelog = new File(testProjectDir.root, "CHANGELOG.md")
        assert changelog.exists()
        assert changelog.text.contains("Title") && changelog.text.contains("Added") && changelog.text.contains("1.0.0")
        // TODO original entry should be removed
        //assert !changelogEntry1.exists()
    }

    def "run fetchChangelogScript"() {
        when:
        BuildResult result = getRunner('fetchChangelogScript')

        then:
        assert result.task(":fetchChangelogScript").outcome == TaskOutcome.SUCCESS
        def changelogScript = new File(testProjectDir.root, "scripts/changelog.sh")
        assert changelogScript.exists()
    }

    def "run fetchChangelogScript twice to update script"() {
        when:
        BuildResult result1 = getRunner('fetchChangelogScript')
        BuildResult result2 = getRunner('fetchChangelogScript')

        then:
        assert result1.task(":fetchChangelogScript").outcome == TaskOutcome.SUCCESS
        assert result2.task(":fetchChangelogScript").outcome == TaskOutcome.SUCCESS
        def changelogScript = new File(testProjectDir.root, "scripts/changelog.sh")
        assert changelogScript.exists()
    }
}

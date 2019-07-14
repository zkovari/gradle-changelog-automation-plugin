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

    def setup() {
        File testKitGradlePropertiesResource = new File(
                getClass().getClassLoader().getResource("testkit-gradle.properties").toURI())
        testKitGradleProperties = new String(Files.readAllBytes(testKitGradlePropertiesResource.toPath()))
        testProjectDir.newFolder('changelogs', 'unreleased')
        settingsFile = testProjectDir.newFile('settings.gradle')
        propertiesFile = testProjectDir.newFile('gradle.properties')
        buildFile = testProjectDir.newFile('build.gradle')
    }

    BuildResult getRunner(String command) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withArguments(command)
                .build()
    }

    def "run processChangelogEntries when input folder is empty"() {
        given:
        settingsFile << "rootProject.name = 'test-project'"
        propertiesFile << testKitGradleProperties
        buildFile << """
            plugins {
                id 'org.zkovari.changelog'
            }
        """

        when:
        BuildResult result = getRunner('processChangelogEntries')

        then:
        result.task(":processChangelogEntries").outcome == TaskOutcome.SUCCESS
    }
}

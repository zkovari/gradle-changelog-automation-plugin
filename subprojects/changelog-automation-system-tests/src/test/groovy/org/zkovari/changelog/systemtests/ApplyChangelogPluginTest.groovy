package org.zkovari.changelog.systemtests

import static org.gradle.testkit.runner.TaskOutcome.*

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class ApplyChangelogPluginTest extends Specification{

    @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
    File settingsFile
    File buildFile

    def setup() {
        settingsFile = testProjectDir.newFile('settings.gradle')
        buildFile = testProjectDir.newFile('build.gradle')
        testProjectDir.newFolder('changelog', 'unreleased')
    }

    private writeBuildFile() {
        buildFile << """
            buildscript {
                repositories {
                    mavenCentral()
                }
            }

            plugins {
                id 'org.zkovari.changelog' version '0.1.0'
            } 
        """
    }

    def "apply plugin in old way"() {
        given:
        settingsFile << "rootProject.name = 'test-project'"
        buildFile << """
            buildscript {
                repositories {
                    mavenCentral()
                    maven {
                        url 'https://gitlab.com/api/v4/projects/13196104/packages/maven'
                    }
                }
                dependencies {
                    classpath 'org.zkovari.changelog:changelog-automation-gradle-plugin:0.1.0'
                }
            }

            apply plugin: 'org.zkovari.changelog'
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('tasks')
                .build()

        then:
        //        result.output.contains('processChangelogEntries')
        result.task(":tasks").outcome == SUCCESS
    }

    def "run processChangelogEntries when unreleased changelog dir is empty"() {
        given:
        settingsFile << """
            pluginManagement {
                repositories {
                    maven {
                        url 'https://gitlab.com/api/v4/projects/13196104/packages/maven'
                    }
                }
             }
             rootProject.name = 'test-project'
        """
        writeBuildFile()

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('processChangelogEntries')
                .build()
        then:
        result.task(":processChangelogEntries").outcome == SUCCESS
    }
}

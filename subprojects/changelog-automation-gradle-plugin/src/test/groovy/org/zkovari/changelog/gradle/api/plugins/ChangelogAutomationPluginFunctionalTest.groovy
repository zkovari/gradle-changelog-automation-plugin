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
    File changelogEntry2

    def setup() {
        File testKitGradlePropertiesResource = new File(
                getClass().getClassLoader().getResource("testkit-gradle.properties").toURI())
        testKitGradleProperties = new String(Files.readAllBytes(testKitGradlePropertiesResource.toPath()))
        def changlogEntriesDir = testProjectDir.newFolder('changelogs', 'unreleased')
        settingsFile = testProjectDir.newFile('settings.gradle')
        propertiesFile = testProjectDir.newFile('gradle.properties')
        buildFile = testProjectDir.newFile('build.gradle')
        changelogEntry1 = new File(changlogEntriesDir, "entry1.yml")
        changelogEntry2 = new File(changlogEntriesDir, "entry2.yml")

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

    def "run processChangelogEntries with changelog entry"(){
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
        assert changelog.text.contains("### Added") && changelog.text.contains("- Title") && changelog.text.contains("1.0.0")
        assert !changelogEntry1.exists()
    }

    def "run processChangelogEntries with multiple changelog entry with same type"(){
        given:
        changelogEntry1 << """
            title: Title 1
            type: added
            reference:
            author:
        """
        changelogEntry2 << """
            title: Title 2
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
        assert changelog.text.contains("1.0.0")
        assert changelog.text.contains("### Added")
        assert changelog.text.contains("- Title 1") && changelog.text.contains("- Title 2")
        assert !changelogEntry1.exists()
        assert !changelogEntry2.exists()
    }

    def "run processChangelogEntries with multiple changelog entry with different type"(){
        given:
        changelogEntry1 << """
            title: Title 1
            type: added
            reference:
            author:
        """
        changelogEntry2 << """
            title: Title 2
            type: changed
            reference:
            author:
        """

        when:
        BuildResult result = getRunner('processChangelogEntries')

        then:
        assert result.task(":processChangelogEntries").outcome == TaskOutcome.SUCCESS
        def changelog = new File(testProjectDir.root, "CHANGELOG.md")
        assert changelog.exists()
        assert changelog.text.contains("1.0.0")
        assert changelog.text.contains("### Added")
        assert changelog.text.contains("### Changed")
        assert changelog.text.contains("- Title 1") && changelog.text.contains("- Title 2")
        assert !changelogEntry1.exists()
        assert !changelogEntry2.exists()
    }

    def "run processChangelogEntries twice with new changelog entries"(){
        given:
        changelogEntry1 << """
            title: Title 1
            type: added
            reference:
            author:
        """

        BuildResult result = getRunner('processChangelogEntries')
        assert result.task(":processChangelogEntries").outcome == TaskOutcome.SUCCESS

        changelogEntry2 << """
        title: Title 2
        type: changed
        reference:
        author:
        """
        changelogEntry1.delete()

        when:
        result = getRunner('processChangelogEntries')

        then:
        assert result.task(":processChangelogEntries").outcome == TaskOutcome.SUCCESS
        def changelog = new File(testProjectDir.root, "CHANGELOG.md")
        assert changelog.exists()
        assert changelog.text.contains("1.0.0")
        assert changelog.text.contains("### Added")
        assert changelog.text.contains("### Changed")
        assert changelog.text.contains("- Title 1")
        assert changelog.text.contains("- Title 2")
        assert !changelogEntry1.exists()
        assert !changelogEntry2.exists()
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

package org.zkovari.changelog.bin

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class ChangelogBashTest extends Specification {

    private static final String CHANGELOG_SH = "changelog.sh"

    @Rule final TemporaryFolder testDir = new TemporaryFolder()

    private File changelog
    private File unreleasedDir
    private StringBuilder standardError
    private StringBuilder standardOutput

    def setup() {
        def changelogUrl = getClass().getClassLoader().getResource(CHANGELOG_SH)
        InputStream inputStream = changelogUrl.openStream()
        changelog = testDir.newFile(CHANGELOG_SH)
        changelog << inputStream
        unreleasedDir = new File(testDir.root, "changelogs/unreleased")

        standardOutput = new StringBuilder()
        standardError = new StringBuilder()
    }

    def cleanup() {
        assert standardError.toString() == ""
    }

    private run(String args = "") {
        def proc = "bash ${testDir.root}/changelog.sh $args".execute(null, testDir.root)
        proc.consumeProcessOutput(standardOutput, standardError)
        proc.waitForOrKill(1000)
    }

    private String getGeneratedChangelogContent(String expectedFileName=null) {
        assert standardOutput.toString().contains("New changelog was generated to:")
        def generatedEntries = (unreleasedDir.list() as List)
        assert unreleasedDir.exists() && unreleasedDir.directory && !generatedEntries.empty
        assert generatedEntries.size == 1
        File changelogFile = new File(unreleasedDir, generatedEntries.get(0))
        if (expectedFileName) {
            assert changelogFile.name == expectedFileName
        } else {
            // e.g. for 2019-07-14_15-19-01_changelog_entry.yml:
            //                              2019   - 07     -  14    _   15   -    19  -    01  _changelog_entry.yml
            assert changelogFile.name =~ "^[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}-[0-9]{2}_changelog_entry\\.yml"
        }
        def actualChangelogContent = changelogFile.text
        return actualChangelogContent
    }

    def "show help if run with -h"() {
        when:
        run('-h')

        then:
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
        assert !unreleasedDir.exists()
    }

    def "show help if run with --help"() {
        when:
        run('--help')

        then:
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
        assert !unreleasedDir.exists()
    }

    def "show help if run without arguments"() {
        when:
        run()

        then:
        assert standardOutput.toString().contains("Title must be specified")
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
        assert !unreleasedDir.exists()
    }

    def "show help if run without --type"() {
        when:
        run("title")

        then:
        assert standardOutput.toString().contains("Type must be specified")
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
        assert !unreleasedDir.exists()
    }

    def "show help if run without title"() {
        when:
        run("--type added --dry-run --reference 123")

        then:
        assert standardOutput.toString().contains("Title must be specified")
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
        assert !unreleasedDir.exists()
    }

    def "show version if run with -v"() {
        when:
        run('-v')

        then:
        assert standardOutput.toString().contains("Version:")
        assert !unreleasedDir.exists()
    }

    def "show version if run with --version"() {
        when:
        run('--version')

        then:
        assert standardOutput.toString().contains("Version:")
        assert !unreleasedDir.exists()
    }

    def "abort if --type is invalid"() {
        when:
        run("--type other \"title\"")

        then:
        assert standardOutput.toString().contains("Invalid value was specified for --type: other")
        assert !unreleasedDir.exists()
    }

    def "abort if -t is invalid"() {
        when:
        run("-t other \"title\"")

        then:
        assert standardOutput.toString().contains("Invalid value was specified for --type: other")
        assert !unreleasedDir.exists()
    }

    def "generate added changelog"() {
        when:
        run("--type added \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: ""')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "added"')
    }

    def "generate changed changelog"() {
        when:
        run("--type changed \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: ""')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "changed"')
    }

    def "generate deprecated changelog"() {
        when:
        run("--type deprecated \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: ""')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "deprecated"')
    }

    def "generate fixed changelog"() {
        when:
        run("--type fixed \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: ""')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "fixed"')
    }

    def "generate removed changelog"() {
        when:
        run("--type removed \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: ""')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "removed"')
    }

    def "generate security changelog"() {
        when:
        run("--type security \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: ""')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "security"')
    }

    def "generate changelog with reference -r"() {
        when:
        run("--type added -r 123 \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: "123"')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "added"')
    }

    def "generate changelog with reference --reference"() {
        when:
        run("--type added --reference 123 \"Title message\"")

        then:
        def changelogContent = getGeneratedChangelogContent()
        assert changelogContent.contains('# Auto-generated by changelog.sh script. Version:')
        assert changelogContent.contains('title: "Title message"')
        assert changelogContent.contains('reference: "123"')
        assert changelogContent.contains('author: ""')
        assert changelogContent.contains('type: "added"')
    }
}

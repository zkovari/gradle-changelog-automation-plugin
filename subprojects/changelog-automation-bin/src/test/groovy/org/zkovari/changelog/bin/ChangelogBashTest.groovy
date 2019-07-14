package org.zkovari.changelog.bin

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class ChangelogBashTest extends Specification {

    private static final String CHANGELOG_SH = "changelog.sh"

    @Rule final TemporaryFolder testDir = new TemporaryFolder()

    private File changelog
    private StringBuilder standardError
    private StringBuilder standardOutput

    def setup() {
        def changelogUrl = getClass().getClassLoader().getResource(CHANGELOG_SH)
        InputStream inputStream = changelogUrl.openStream()
        changelog = testDir.newFile(CHANGELOG_SH)
        changelog << inputStream

        standardOutput = new StringBuilder()
        standardError = new StringBuilder()
    }

    def cleanup() {
        assert standardError.toString() == ""
    }

    private run(String args = "") {
        def proc = "bash $testDir.root/changelog.sh $args".execute()
        proc.consumeProcessOutput(standardOutput, standardError)
        proc.waitForOrKill(1000)
    }

    def "show help if run with -h"() {
        when:
        run('-h')

        then:
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
    }

    def "show help if run with --help"() {
        when:
        run('--help')

        then:
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
    }

    def "show help if run without arguments"() {
        when:
        run()

        then:
        assert standardOutput.toString().contains("Title and type must be specified")
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
    }

    def "show help if run without --type"() {
        when:
        run("title")

        then:
        assert standardOutput.toString().contains("Title and type must be specified")
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
    }

    def "show help if run without title"() {
        when:
        run("--type added")

        then:
        assert standardOutput.toString().contains("Title and type must be specified")
        assert standardOutput.toString().contains("Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]")
    }

    def "show version if run with -v"() {
        when:
        run('-v')

        then:
        assert standardOutput.toString().contains("Version:")
    }

    def "show version if run with --version"() {
        when:
        run('--version')

        then:
        assert standardOutput.toString().contains("Version:")
    }
}

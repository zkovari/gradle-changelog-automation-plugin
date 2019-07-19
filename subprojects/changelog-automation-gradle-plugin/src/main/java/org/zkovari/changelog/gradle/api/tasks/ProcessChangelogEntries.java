package org.zkovari.changelog.gradle.api.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.zkovari.changelog.core.collector.ChangelogEntriesFileCollector;
import org.zkovari.changelog.core.generator.ChangelogGenerator;
import org.zkovari.changelog.core.parser.ChangelogEntriesParser;
import org.zkovari.changelog.core.parser.ChangelogParserException;
import org.zkovari.changelog.core.processor.ChangelogEntriesProcessor;
import org.zkovari.changelog.core.processor.InvalidChangelogEntryException;
import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.Release;

public class ProcessChangelogEntries extends DefaultTask {

    private File inputDirectory;
    private File outputfile;

    @InputDirectory
    public File getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(File inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    @OutputFile
    public File getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(File outputfile) {
        this.outputfile = outputfile;
    }

    @TaskAction
    public void process() {
        List<ChangelogEntry> entries = getEntries();
        Release release = processEntries(entries);
        String newChangelogContent = generate(release);
        write(newChangelogContent);
        clean();
    }

    private List<ChangelogEntry> getEntries() {
        ChangelogEntriesParser parser = new ChangelogEntriesParser();
        List<ChangelogEntry> entries;
        try {
            entries = parser.parse(inputDirectory);
        } catch (ChangelogParserException ex) {
            throw new GradleException(
                    "Could not parse new changelog entries under " + inputDirectory + ". Reason: " + ex.getMessage(),
                    ex);
        }
        return entries;
    }

    private Release processEntries(List<ChangelogEntry> entries) {
        ChangelogEntriesProcessor processor = new ChangelogEntriesProcessor();
        try {
            return processor.processChangelogs(entries, getProject().getVersion().toString());
        } catch (InvalidChangelogEntryException ex) {
            throw new GradleException(ex.getMessage(), ex);
        }
    }

    private String generate(Release release) {
        ChangelogGenerator generator = new ChangelogGenerator();
        if (outputfile.exists()) {
            try {
                byte[] fileContentInBytes = Files.readAllBytes(outputfile.toPath());
                return generator.generate(new String(fileContentInBytes), release);
            } catch (IOException ex) {
                throw new GradleException("Could not read file: " + outputfile, ex);
            }
        }
        return generator.generate("", release);
    }

    private void write(String newChangelogContent) {
        try {
            Files.write(outputfile.toPath(), newChangelogContent.getBytes());
        } catch (IOException ex) {
            throw new GradleException(ex.getMessage(), ex);
        }
    }

    private void clean() {
        ChangelogEntriesFileCollector collector = new ChangelogEntriesFileCollector();
        List<File> files;
        try {
            files = collector.collect(inputDirectory);
        } catch (IOException ex) {
            throw new GradleException("Could not collect changelog entries to clean them up", ex);
        }

        for (File file : files) {
            try {
                Files.delete(file.toPath());
            } catch (IOException ex) {
                throw new GradleException("Could not delete file: " + file, ex);
            }
        }
    }
}

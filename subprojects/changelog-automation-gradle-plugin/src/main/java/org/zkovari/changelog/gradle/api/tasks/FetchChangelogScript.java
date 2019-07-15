package org.zkovari.changelog.gradle.api.tasks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public class FetchChangelogScript extends DefaultTask {

    private File outputDirectory;

    @OutputDirectory
    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @TaskAction
    public void fetch() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("changelog.sh");

        Path outputChangelog = outputDirectory.toPath().resolve("changelog.sh");
        try {
            Files.copy(in, outputChangelog, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new GradleException("Could not copy changelog.sh file into: " + outputDirectory, ex);
        }

        HashSet<PosixFilePermission> permissions = new HashSet<>();
        permissions.add(PosixFilePermission.OWNER_EXECUTE);
        permissions.add(PosixFilePermission.GROUP_EXECUTE);
        permissions.add(PosixFilePermission.OTHERS_EXECUTE);
        try {
            Files.setPosixFilePermissions(outputChangelog, permissions);
        } catch (IOException e) {
            getLogger().warn("Could not set executable permissions to file {}", outputChangelog);
        }
    }

}

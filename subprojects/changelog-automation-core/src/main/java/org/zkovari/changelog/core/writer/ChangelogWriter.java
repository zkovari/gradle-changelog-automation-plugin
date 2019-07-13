package org.zkovari.changelog.core.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class ChangelogWriter {

    public void write(File outputfile, String content) throws IOException {
	Files.write(outputfile.toPath(), content.getBytes(), StandardOpenOption.CREATE_NEW);
    }

}

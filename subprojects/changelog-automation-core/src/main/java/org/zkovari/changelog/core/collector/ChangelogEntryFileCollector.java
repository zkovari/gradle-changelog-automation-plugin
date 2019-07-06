package org.zkovari.changelog.core.collector;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ChangelogEntryFileCollector {

    public List<File> collect(File rootDirectory) throws IOException {
	Objects.requireNonNull(rootDirectory, "rootDirectory cannot be null");
	if (!rootDirectory.exists()) {
	    throw new IOException("Given root directory does not exist: " + rootDirectory);
	}
	LinkedList<File> collectedFiles = new LinkedList<>();

	Files.walkFileTree(rootDirectory.toPath(), new SimpleFileVisitor<Path>() {

	    @Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		if (dir != rootDirectory.toPath()) {
		    return FileVisitResult.SKIP_SUBTREE;
		}
		return super.preVisitDirectory(dir, attrs);
	    }

	    @Override
	    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
		if (path.toString().endsWith(".yml") || path.toString().endsWith(".yaml")) {
		    collectedFiles.add(path.toFile());
		}
		return super.visitFile(path, attrs);
	    }

	});

	return collectedFiles;
    }

}

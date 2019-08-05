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

public class ChangelogEntriesFileCollector {

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

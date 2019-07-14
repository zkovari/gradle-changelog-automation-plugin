package org.zkovari.changelog.core;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;

public abstract class ChangelogTestBase {

    protected static final String V1_0_0 = "1.0.0";

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    protected ChangelogEntry newChangelogEntry(String title, EntryType type) {
	return newChangelogEntry(title, type, null, null);
    }

    protected ChangelogEntry newChangelogEntry(String title, EntryType type, String reference, String author) {
	ChangelogEntry entry = new ChangelogEntry();
	entry.setTitle(title);
	entry.setType(type);
	entry.setReference(reference);
	entry.setAuthor(author);

	return entry;
    }

}

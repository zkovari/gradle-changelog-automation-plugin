package org.zkovari.changelog.core;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public abstract class ChangelogTestBase {

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

}

package org.zkovari.changelog.core.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkovari.changelog.core.ChangelogTestBase;
import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;
import org.zkovari.changelog.domain.Release;

public class ChangelogGeneratorTest extends ChangelogTestBase {

    private static final String NL = System.lineSeparator();
    private static String currentWithoutEntries;
    private static String expectedWithoutEntriesBefore;
    private static String currentWithEntries;
    private static String expectedWithEntriesBefore;

    private ChangelogGenerator generator;
    private Release release;

    @BeforeClass
    public static void setUpClass() throws IOException, URISyntaxException {
        currentWithoutEntries = readResourceAsString("CHANGELOG.md_without_entries");
        expectedWithoutEntriesBefore = readResourceAsString("CHANGELOG.md_without_entries_expected");

        currentWithEntries = readResourceAsString("CHANGELOG.md_with_entries");
        expectedWithEntriesBefore = readResourceAsString("CHANGELOG.md_with_entries_expected");
    }

    private static String readResourceAsString(String resourceName) throws IOException, URISyntaxException {
        String resourcePath = "org/zkovari/changelog/core/generator/" + resourceName;
        URL resource = ChangelogGeneratorTest.class.getClassLoader().getResource(resourcePath);
        if (resource == null) {
            throw new IOException("Could not find resource file " + resourcePath);
        }
        return new String(Files.readAllBytes(new File(resource.toURI()).toPath()));
    }

    @Before
    public void setUp() {
        generator = new ChangelogGenerator();

        release = new Release();
        release.setVersion("1.0.0");
        release.setDate("2019-07-07");
        ChangelogEntry entry = new ChangelogEntry();
        entry.setTitle("Message");
        entry.setType(EntryType.ADDED);
        release.setEntries(new HashMap<>());
        release.getEntries().put(EntryType.ADDED, Arrays.asList(entry));
    }

    @Test
    public void testNullSafeGetters() {
        ChangelogGenerator newGenerator = new ChangelogGenerator();
        assertNotNull("Expected ReleaseEntryGenerator not be null", newGenerator.getReleaseEntryGenerator());
    }

    @Test
    public void testGenerateWhenCurrentIsEmpty() {
        String content = generator.generate("", release);
        assertEquals(expectedWithoutEntriesBefore, content);
    }

    @Test
    public void testGenerateWhenEntriesAreMissing() {
        String content = generator.generate(currentWithoutEntries, release);
        assertEquals(expectedWithoutEntriesBefore, content);
    }

    @Test
    public void testGenerateWithPreviousEntriesPresent() {
        String content = generator.generate(currentWithEntries, release);
        assertEquals(expectedWithEntriesBefore, content);
    }

}

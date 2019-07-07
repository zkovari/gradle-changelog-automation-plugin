package org.zkovari.changelog.core.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.zkovari.changelog.core.ChangelogTestBase;
import org.zkovari.changelog.domain.Release;

public class ChangelogGeneratorTest extends ChangelogTestBase {

    private static final String NL = System.lineSeparator();
    private static String currentWithoutEntries;
    private static String expectedWithoutEntriesBefore;
    private static String currentWithEntries;
    private static String expectedWithEntriesBefore;

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ReleaseEntryGenerator releaseEntryGenerator;

    private ChangelogGenerator generator;
    private Release release;

    @BeforeClass
    public static void setUpClass() throws Exception {
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
    public void setUp() throws Exception {
	generator = new ChangelogGenerator();
	generator.setReleaseEntryGenerator(releaseEntryGenerator);

	release = new Release();
	when(releaseEntryGenerator.generate(release))
		.thenReturn("## [1.0.0] - 2019-07-07" + NL + "### Added" + NL + "- Message" + NL + NL);
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

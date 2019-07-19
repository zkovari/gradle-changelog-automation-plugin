package org.zkovari.changelog.core.generator;

import static java.text.MessageFormat.format;
import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zkovari.changelog.core.ChangelogTestBase;
import org.zkovari.changelog.domain.EntryType;
import org.zkovari.changelog.domain.Release;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class ReleaseEntryGeneratorTest extends ChangelogTestBase {

    private static final String NL = System.lineSeparator();

    private static final String DATE = "2019-07-07";

    private ReleaseEntryGenerator generator;
    private Release release;

    private String releaseHeader;

    @Before
    public void setUp() {
        generator = new ReleaseEntryGenerator();
        release = new Release();
        releaseHeader = "## [1.0.0] - 2019-07-07" + NL;
        release.setVersion(V1_0_0);
        release.setDate(DATE);
    }

    @DataProvider
    public static List<EntryType> entryTypes() {
        return Arrays.asList(EntryType.values());
    }

    @Test
    public void testGenerateWithoutEntry() {
        String content = generator.generate(release);
        assertEquals("## [1.0.0] - 2019-07-07", content);
    }

    @Test
    public void testGenerateWithEmptyTypeEntries() {
        release.setEntries(new HashMap<>());
        release.getEntries().put(EntryType.ADDED, Collections.emptyList());
        String content = generator.generate(release);
        assertEquals(releaseHeader, content);
    }

    @Test
    public void testGenerateWithNullTypeEntries() {
        release.setEntries(new HashMap<>());
        release.getEntries().put(EntryType.ADDED, null);
        String content = generator.generate(release);
        assertEquals(releaseHeader, content);
    }

    @Test
    @UseDataProvider("entryTypes")
    public void testGenerateSingleEntry(EntryType type) {
        release.setEntries(new HashMap<>());
        release.getEntries().put(type, Arrays.asList(newChangelogEntry("title1", type)));

        String content = generator.generate(release);
        String expectedContent = MessageFormat.format("{0}### {1}{2}- title1{3}", releaseHeader, type.getValue(), NL,
                NL);
        assertEquals(expectedContent, content);
    }

    @Test
    @UseDataProvider("entryTypes")
    public void testGenerateSingleEntryWithReferenceAndAuthor(EntryType type) {
        release.setEntries(new HashMap<>());
        release.getEntries().put(type, Arrays.asList(newChangelogEntry("title1", type, "ref1", "author")));

        String content = generator.generate(release);
        String expectedContent = format("{0}### {1}{2}- ref1 title1 (author){3}", releaseHeader, type.getValue(), NL,
                NL);
        assertEquals(expectedContent, content);
    }

    @Test
    @UseDataProvider("entryTypes")
    public void testGenerateSingleEntryWithEmptyReferenceAndAuthor(EntryType type) {
        release.setEntries(new HashMap<>());
        release.getEntries().put(type, Arrays.asList(newChangelogEntry("title1", type, "", "")));

        String content = generator.generate(release);
        String expectedContent = format("{0}### {1}{2}- title1{3}", releaseHeader, type.getValue(), NL, NL);
        assertEquals(expectedContent, content);
    }

    @Test
    @UseDataProvider("entryTypes")
    public void testGenerateMultipleEntriesPerType(EntryType type) {
        release.setEntries(new HashMap<>());
        release.getEntries().put(type,
                Arrays.asList(newChangelogEntry("title1", type), newChangelogEntry("title2", type)));

        String content = generator.generate(release);
        String expectedContent = releaseHeader + format("### {0}{1}- title1{2}", type.getValue(), NL, NL)
                + format("- title2{0}", NL);
        assertEquals(expectedContent, content);
    }

    @Test
    public void testGenerateMultipleTypes() {
        release.setEntries(new HashMap<>());
        for (EntryType type : EntryType.values()) {
            release.getEntries().put(type,
                    Arrays.asList(newChangelogEntry("title1", type), newChangelogEntry("title2", type)));
        }

        String content = generator.generate(release);
        String expectedContent = releaseHeader + format("### Added{0}- title1{1}- title2{2}{3}", NL, NL, NL, NL)
                + format("### Changed{0}- title1{1}- title2{2}{3}", NL, NL, NL, NL)
                + format("### Deprecated{0}- title1{1}- title2{2}{3}", NL, NL, NL, NL)
                + format("### Fixed{0}- title1{1}- title2{2}{3}", NL, NL, NL, NL)
                + format("### Removed{0}- title1{1}- title2{2}{3}", NL, NL, NL, NL)
                + format("### Security{0}- title1{1}- title2{2}", NL, NL, NL);
        assertEquals(expectedContent, content);
    }

}

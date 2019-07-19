package org.zkovari.changelog.core.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zkovari.changelog.core.ChangelogTestBase;
import org.zkovari.changelog.domain.ChangelogEntry;
import org.zkovari.changelog.domain.EntryType;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class YamlChangelogEntryParserTest extends ChangelogTestBase {

    private YamlChangelogEntryParser parser;
    private File entryFile;
    ChangelogEntry changelogEntry;

    @Before
    public void setUp() throws IOException {
        parser = new YamlChangelogEntryParser();
        entryFile = temp.newFile("entry.yaml");
    }

    @DataProvider
    public static List<String> stringYamlKeysProvider() {
        return Arrays.asList("title", "reference", "author");
    }

    @DataProvider
    public static List<String> entryTypeYamlValuesProvider() {
        return Arrays.asList("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security");
    }

    private void write(String content) throws IOException {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(entryFile))) {
            output.write(content);
        }
    }

    private void parse() throws ChangelogParserException {
        changelogEntry = parser.parse(entryFile);
        assertNotNull("parsed changelog entry was null", changelogEntry);
    }

    private void checkStringValue(String key, String expectedValue) {
        if (key.equals("title")) {
            assertEquals(expectedValue, changelogEntry.getTitle());
        } else if (key.equals("reference")) {
            assertEquals(expectedValue, changelogEntry.getReference());
        } else if (key.equals("author")) {
            assertEquals(expectedValue, changelogEntry.getAuthor());
        } else {
            fail("unexpected key in check: " + key);
        }
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntry(String key) throws Exception {
        write(key + ": value");
        parse();
        checkStringValue(key, "value");
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryIfCapital(String key) throws Exception {
        String capitalizedKey = key.substring(0, 1).toUpperCase() + key.substring(1);
        write(capitalizedKey + ": value");
        parse();
        checkStringValue(key, "value");
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryIfUpperCase(String key) throws Exception {
        write(key.toUpperCase() + ": value");
        parse();
        checkStringValue(key, "value");
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryWithApostrophe(String key) throws Exception {
        write(key + ": 'value'");
        parse();
        checkStringValue(key, "value");
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryWithQuotationMarks(String key) throws Exception {
        write(key + ": \"value\"");
        parse();
        checkStringValue(key, "value");
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryWithWhitespace(String key) throws Exception {
        write(key + ": longer value with whitespace");
        parse();
        checkStringValue(key, "longer value with whitespace");
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryIfEmpty(String key) throws Exception {
        write(key + ": ");
        parse();
        checkStringValue(key, null);
    }

    @Test
    @UseDataProvider("stringYamlKeysProvider")
    public void testParseStringEntryWithSpecialCharacters(String key) throws Exception {
        write(key + ": value 1234 [] !@#$%^&*(");
        parse();
        checkStringValue(key, "value 1234 [] !@#$%^&*(");
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnum(String value) throws Exception {
        write("type: " + value);
        parse();
        assertEquals(EntryType.valueOf(value.toUpperCase()), changelogEntry.getType());
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnumIfValueIsLowerCase(String value) throws Exception {
        write("type: " + value.toLowerCase());
        parse();
        assertEquals(EntryType.valueOf(value.toUpperCase()), changelogEntry.getType());
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnumIfKeyIsUpperCase(String value) throws Exception {
        write("TYPE: " + value);
        parse();
        assertEquals(EntryType.valueOf(value.toUpperCase()), changelogEntry.getType());
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnumIfValueIsUpperCase(String value) throws Exception {
        write("type: " + value.toUpperCase());
        parse();
        assertEquals(EntryType.valueOf(value.toUpperCase()), changelogEntry.getType());
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnumIfEmpty(String value) throws Exception {
        write("type: ");
        parse();
        assertEquals(null, changelogEntry.getType());
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnumWithApostrophe(String value) throws Exception {
        write("type: '" + value + "'");
        parse();
        assertEquals(EntryType.valueOf(value.toUpperCase()), changelogEntry.getType());
    }

    @Test
    @UseDataProvider("entryTypeYamlValuesProvider")
    public void testParseEntryTypeEnumQuotationMarks(String value) throws Exception {
        write("type: \"" + value + "\"");
        parse();
        assertEquals(EntryType.valueOf(value.toUpperCase()), changelogEntry.getType());
    }

    @Test
    public void testParseMultipleEntries() throws Exception {
        String content = new StringBuilder("title: title value\n").append("type: added\n").append("reference: 1\n")
                .append("author: tester").toString();
        write(content);

        parse();

        assertEquals("title value", changelogEntry.getTitle());
        assertEquals(EntryType.ADDED, changelogEntry.getType());
        assertEquals("1", changelogEntry.getReference());
        assertEquals("tester", changelogEntry.getAuthor());
    }

    @Test
    public void testParseNonExistingFile() throws Exception {
        String fileName = "test-non-existing-file-for-changelog-file-parsing-test";
        thrown.expect(ChangelogParserException.class);
        thrown.expectMessage("Input file does not exist: " + fileName);
        parser.parse(new File(fileName));
    }

    // TODO better error handling
    @Ignore
    @Test
    public void testParseNonInvalidYamlFile() throws Exception {
        write("title:no-whitespace");

        thrown.expect(ChangelogParserException.class);
        parse();
    }

    @Test
    public void testParseEmptyYamlFile() throws Exception {
        write("");

        parse();
        assertNull(changelogEntry.getTitle());
        assertNull(changelogEntry.getType());
        assertNull(changelogEntry.getReference());
        assertNull(changelogEntry.getAuthor());
    }

}

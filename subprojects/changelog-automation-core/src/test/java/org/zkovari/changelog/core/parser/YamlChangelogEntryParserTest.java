package org.zkovari.changelog.core.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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
    private File entry;
    ChangelogEntry changelogEntry;

    @Before
    public void setUp() throws IOException {
	parser = new YamlChangelogEntryParser();
	entry = temp.newFile("entry.yaml");
    }

    @DataProvider
    public static List<String> stringYamlKeysProvider() {
	return Arrays.asList("message", "reference", "author");
    }

    @DataProvider
    public static List<String> entryTypeYamlValuesProvider() {
	return Arrays.asList("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security");
    }

    private void write(String content) throws IOException {
	try (BufferedWriter output = new BufferedWriter(new FileWriter(entry))) {
	    output.write(content);
	}
    }

    private void parse() throws ChangelogParserException {
	changelogEntry = parser.parse(entry);
	assertNotNull("parsed changelog entry was null", changelogEntry);
    }

    private void checkStringValue(String key, String expectedValue) {
	if (key.equals("message")) {
	    assertEquals(expectedValue, changelogEntry.getMessage());
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

}

package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_TELEGRAM_HANDLE = "ab";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_TELEGRAM_HANDLE = "rachel_walker";

    private static final String WHITESPACE = " \t\r\n";

    private static final Prefix[] DISALLOWED_PREFIXES = {PREFIX_PHONE, PREFIX_TELEGRAM_HANDLE, PREFIX_INDEX};

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTelegramHandle_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTelegramHandle((String) null));
    }

    @Test
    public void parseTelegramHandle_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTelegramHandle(INVALID_TELEGRAM_HANDLE));
    }

    @Test
    public void parseTelegramHandle_validValueWithoutWhitespace_returnsTelegramHandle() throws Exception {
        TelegramHandle expectedTelegramHandle = new TelegramHandle(VALID_TELEGRAM_HANDLE);
        assertEquals(expectedTelegramHandle, ParserUtil.parseTelegramHandle(VALID_TELEGRAM_HANDLE));
    }

    @Test
    public void parseTelegramHandle_validValueWithWhitespace_returnsTrimmedTelegramHandle() throws Exception {
        String telegramHandleWithWhitespace = WHITESPACE + VALID_TELEGRAM_HANDLE + WHITESPACE;
        TelegramHandle expectedTelegramHandle = new TelegramHandle(VALID_TELEGRAM_HANDLE);
        assertEquals(expectedTelegramHandle, ParserUtil.parseTelegramHandle(telegramHandleWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null, null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG, TagType.GENERAL));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1, TagType.GENERAL);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1, TagType.GENERAL));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1, TagType.GENERAL);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace, TagType.GENERAL));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null, null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(
                Arrays.asList(VALID_TAG_1, INVALID_TAG), TagType.GENERAL));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList(), TagType.GENERAL).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2), TagType.GENERAL);
        Set<Tag> expectedTagSet = new HashSet<>(
                Arrays.asList(
                        new Tag(VALID_TAG_1, TagType.GENERAL),
                        new Tag(VALID_TAG_2, TagType.GENERAL)
                )
        );
        assertEquals(expectedTagSet, actualTagSet);
    }

    //================== Tests for findUnexpectedExtraInput ==================

    @Test
    public void findUnexpectedExtraInput_noDisallowedPrefix_returnsEmpty() {
        String args = "n/Alice e/alice@example.com";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_singleDisallowedPrefix_returnsToken() {
        String args = "n/Alice p/12345";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_multipleDisallowedPrefixes_returnsEarliest() {
        String args = "n/Alice h/@handle i/3 p/12345";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("h/@handle", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_disallowedAtEnd_returnsToken() {
        String args = "n/Alice e/alice@nus.edu p/98765432";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/98765432", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_disallowedAtStart_returnsToken() {
        String args = " p/12345 n/Alice";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_emptyArgs_returnsEmpty() {
        String args = "";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_onlyWhitespace_returnsEmpty() {
        String args = "   ";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_prefixWithoutValue_returnsToken() {
        String args = "n/Alice p/";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_prefixWithSpaceInValue_returnsTokenUpToSpace() {
        String args = "n/Alice p/123 456";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/123", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_caseInsensitivePrefix_returnsToken() {
        String args = "n/Alice P/12345";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("P/12345", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_emptyDisallowedArray_returnsEmpty() {
        String args = "n/Alice p/12345";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, new Prefix[]{});
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_multipleOccurrencesOfSamePrefix_returnsToken() {
        String args = "n/Alice p/111 p/222";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/111", result.get());
    }

    //================== Tests for findPrefixPosition (via findUnexpectedExtraInput) ==================

    @Test
    public void findPrefixPosition_prefixAtStart_returnsNegativeOne() {
        //Prefix must be preceded by whitespace, so it won't be found at the start
        String args = "p/value";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findPrefixPosition_prefixWithWhitespaceBefore_returnsCorrectToken() {
        String args = "some text p/value";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/value", result.get());
    }

    @Test
    public void findPrefixPosition_prefixNoWhitespaceBefore_isNotRecognized() {
        String args = "textwithoutp/value";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findPrefixPosition_multipleWhitespacesBefore_recognizesPrefix() {
        String args = "some    text   p/value";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/value", result.get());
    }

    //================== Tests for extractToken (via findUnexpectedExtraInput) ==================

    @Test
    public void extractToken_tokenWithoutTrailingSpace_returnsFullToken() {
        String args = "n/Alice p/12345";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void extractToken_tokenWithTrailingSpace_returnsTokenBeforeSpace() {
        String args = "n/Alice p/123 e/example";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/123", result.get());
    }

    @Test
    public void extractToken_tokenAtEnd_returnsFullToken() {
        String args = "n/Alice e/example p/12345";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void extractToken_tokenWithSpecialCharacters_returnsCorrectly() {
        String args = "n/Alice h/@handle123!";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("h/@handle123!", result.get());
    }

    @Test
    public void extractToken_tokenWithMultipleSpaces_returnsUpToFirstSpace() {
        String args = "n/Alice p/123   456";
        Optional<String> result = ParserUtil.findUnexpectedExtraInput(args, DISALLOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("p/123", result.get());
    }
}

package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_NO_INPUT;
import static seedu.address.logic.Messages.MESSAGE_PREAMBLE_NOT_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
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
    private static final String VALID_PHONE = "1234 56";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_TELEGRAM_HANDLE = "rachel_walker";

    private static final String WHITESPACE = " \t\r\n";

    private static final Prefix[] AllOWED_PREFIXES = {PREFIX_ROLE_TAG, PREFIX_COURSE_TAG, PREFIX_GENERAL_TAG};

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
    public void parsePhone_invalidValueWithLessThanThreeDigitsIgnoringSpaces_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone("1 2"));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithInternalSpaces_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone("9312 1534");
        assertEquals(expectedPhone, ParserUtil.parsePhone("9312 1534"));
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

    // ==================== Tests for parseAllTypeOfTags ====================

    @Test
    public void parseAllTypeOfTags_noTags_returnsEmptySet() throws Exception {
        String args = "1";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Set<Tag> actualTags = ParserUtil.parseAllTypeOfTags(argMultimap);

        assertTrue(actualTags.isEmpty());
    }

    @Test
    public void parseAllTypeOfTags_oneTypeOfTags_success() throws Exception {
        String args = "1 tg/friends tg/groupmates";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GENERAL_TAG);

        Set<Tag> expectedTags = new HashSet<>(Arrays.asList(
                new Tag("friends", TagType.GENERAL),
                new Tag("groupmates", TagType.GENERAL)
        ));

        Set<Tag> actualTags = ParserUtil.parseAllTypeOfTags(argMultimap);

        assertEquals(expectedTags, actualTags);
    }

    @Test
    public void parseAllTypeOfTags_allTagTypesPresent_success() throws Exception {
        String args = "1 tr/tutor tc/cs2103 tg/friends";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Set<Tag> expectedTags = new HashSet<>(Arrays.asList(
                new Tag("tutor", TagType.ROLE),
                new Tag("cs2103", TagType.COURSE),
                new Tag("friends", TagType.GENERAL)
        ));

        Set<Tag> actualTags = ParserUtil.parseAllTypeOfTags(argMultimap);

        assertEquals(expectedTags, actualTags);
    }

    //============= Tests for findEmptyPrefixValues =================

    @Test
    public void findEmptyPrefixValues_noEmptyPrefixValues_returnsEmptyOptional() {
        String args = " n/Alice p/12345";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE);
        Optional<String> result = ParserUtil.findEmptyPrefixValues(argMultimap, PREFIX_NAME, PREFIX_PHONE);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findEmptyPrefixValues_oneEmptyPrefixValue_returnsNonEmptyOptional() {
        String args = " p/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE);
        Optional<String> result = ParserUtil.findEmptyPrefixValues(argMultimap, PREFIX_NAME, PREFIX_PHONE);

        assertTrue(result.isPresent());
        assertEquals(PREFIX_PHONE.getPrefix(), result.get());
    }

    @Test
    public void findEmptyPrefixValues_mixOfEmptyAndNonPrefixValue_returnsNonEmptyOptional() {
        String args = " n/john p/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE);
        Optional<String> result = ParserUtil.findEmptyPrefixValues(argMultimap, PREFIX_NAME, PREFIX_PHONE);

        assertTrue(result.isPresent());
        assertEquals(PREFIX_PHONE.getPrefix(), result.get());
    }

    //================== Tests for validateNoEmptyPrefixValues ==================

    @Test
    public void validateNoEmptyPrefixValues_singlePrefixWithValue_success() throws Exception {
        String args = " tr/tutor";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE_TAG);

        assertDoesNotThrow(() -> ParserUtil.validateNoEmptyPrefixValues(argMultimap, PREFIX_ROLE_TAG));
    }

    @Test
    public void validateNoEmptyPrefixValues_allPrefixesHaveValues_success() throws Exception {
        String args = " tr/tutor tc/cs2103 tg/friends";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        assertDoesNotThrow(() -> ParserUtil.validateNoEmptyPrefixValues(argMultimap, AllOWED_PREFIXES));
    }

    @Test
    public void validateNoEmptyPrefixValues_rolePrefixEmpty_throwsParseException() {
        String args = " tr/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE_TAG);

        ParseException exception = Assertions.assertThrows(ParseException.class, () ->
                ParserUtil.validateNoEmptyPrefixValues(argMultimap, PREFIX_ROLE_TAG)
        );
        assertTrue(exception.getMessage().contains(String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "tr/")));
    }

    @Test
    public void validateNoEmptyPrefixValues_multiplePrefixesOneEmpty_throwsParseException() {
        String args = " tr/tutor tc/ tg/friends";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        ParseException exception = Assertions.assertThrows(ParseException.class, () ->
                ParserUtil.validateNoEmptyPrefixValues(argMultimap, AllOWED_PREFIXES)
        );
        assertTrue(exception.getMessage().contains(String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "tc/")));
    }

    //================== Tests for findInvalidPrefixInput ==================

    @Test
    public void findInvalidPrefixInput_noPrefixes_returnsNonEmptyOptional() {
        String args = "1";
        Optional<String> result = ParserUtil.findInvalidPrefixInput(args, AllOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findInvalidPrefixInput_onlyAllowedPrefixes_returnsNonEmptyOptional() {
        String args = "1 tr/tutor tc/cs2103 tg/friends";
        Optional<String> result = ParserUtil.findInvalidPrefixInput(args, AllOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findInvalidPrefixInput_singleInvalidPrefix_returnsInvalidToken() {
        String args = "1 n/alice";
        Optional<String> result = ParserUtil.findInvalidPrefixInput(args, AllOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("n/alice", result.get());
    }

    @Test
    public void findInvalidPrefixInput_multipleInvalidPrefixes_returnsFirstInvalidToken() {
        String args = "1 n/alice p/12345678 e/test@example.com";
        Optional<String> result = ParserUtil.findInvalidPrefixInput(args, AllOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("n/alice", result.get());
    }

    @Test
    public void findInvalidPrefixInput_mixedAllowedAndInvalid_returnsFirstInvalidToken() {
        String args = "1 tr/tutor n/alice tg/friends";
        Optional<String> result = ParserUtil.findInvalidPrefixInput(args, AllOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("n/alice", result.get());
    }

    //================== Tests for validateNoInvalidPrefixInputs ==================

    @Test
    public void validateNoInvalidPrefixInputs_noPrefixes_success() throws Exception {
        String args = "1";
        assertDoesNotThrow(() -> ParserUtil.validateNoInvalidPrefixInputs(args, AllOWED_PREFIXES));
    }

    @Test
    public void validateNoInvalidPrefixInputs_allowedSinglePrefixesOnly_success() throws Exception {
        String args = "tr/tutor";
        assertDoesNotThrow(() -> ParserUtil.validateNoInvalidPrefixInputs(args, PREFIX_ROLE_TAG));
    }

    @Test
    public void validateNoInvalidPrefixInputs_allowedPrefixesOnly_success() throws Exception {
        String args = "tr/tutor tc/cs2103 tg/friends";
        assertDoesNotThrow(() -> ParserUtil.validateNoInvalidPrefixInputs(args, AllOWED_PREFIXES));
    }

    @Test
    public void validateNoInvalidPrefixInputs_namePrefixNotAllowed_throwsParseException() {
        String args = "tr/tutor n/alice";

        ParseException exception = Assertions.assertThrows(ParseException.class, () ->
                ParserUtil.validateNoInvalidPrefixInputs(args, AllOWED_PREFIXES));

        assertTrue(exception.getMessage().contains(String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "n/alice")));
    }

    //================== Tests for findNoValuesAfterPrefix ==================

    @Test
    public void findNoValuesAfterPrefix_noPrefixes_returnsNonEmptyOptional() {
        String args = "1";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Optional<String> result = ParserUtil.findNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findNoValuesAfterPrefix_singlePrefixEmpty_returnsNonEmptyOptional() {
        String args = "1 tr/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE_TAG);

        Optional<String> result = ParserUtil.findNoValuesAfterPrefix(argMultimap, PREFIX_ROLE_TAG);
        assertFalse(result.isPresent());
    }

    @Test
    public void findNoValuesAfterPrefix_allPrefixesEmpty_returnsNonEmptyOptional() {
        String args = "1 tr/ tc/ tg/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Optional<String> result = ParserUtil.findNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES);
        assertFalse(result.isPresent());
    }

    @Test
    public void findNoValuesAfterPrefix_rolePrefixWithValue_returnsRoleToken() {
        String args = "1 tr/tutor";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Optional<String> result = ParserUtil.findNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("tr/tutor", result.get());
    }

    @Test
    public void findNoValuesAfterPrefix_mixedEmptyAndNonEmpty_returnsFirstNonEmpty() {
        String args = "1 tr/ tc/cs2103 tg/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Optional<String> result = ParserUtil.findNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("tc/cs2103", result.get());
    }

    @Test
    public void findNoValuesAfterPrefix_mixedEmptyAndNonEmptyWithMultiple_returnsFirstNonEmpty() {
        String args = "1 tr/tutor tc/cs2103 tg/friends";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        Optional<String> result = ParserUtil.findNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES);
        assertTrue(result.isPresent());
        assertEquals("tr/tutor", result.get());
    }

    //================== Tests for validateNoValuesAfterPrefix ==================

    @Test
    public void validateNoValuesAfterPrefix_noPrefixesAtAll_success() throws Exception {
        // EP: no prefixes present in input
        String args = "1";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        assertDoesNotThrow(() ->
                ParserUtil.validateNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES));
    }

    @Test
    public void validateNoValuesAfterPrefix_singlePrefixEmpty_success() throws Exception {
        String args = "1 tr/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE_TAG);

        assertDoesNotThrow(() ->
                ParserUtil.validateNoValuesAfterPrefix(argMultimap, PREFIX_ROLE_TAG));
    }

    @Test
    public void validateNoValuesAfterPrefix_allPrefixesEmpty_success() throws Exception {
        String args = "1 tr/ tc/ tg/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        assertDoesNotThrow(() ->
                ParserUtil.validateNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES));
    }

    @Test
    public void validateNoValuesAfterPrefix_rolePrefixWithValue_throwsParseException() {
        String args = "1 tr/tutor";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE_TAG);

        ParseException exception = Assertions.assertThrows(ParseException.class, () ->
                ParserUtil.validateNoValuesAfterPrefix(argMultimap, PREFIX_ROLE_TAG));

        assertTrue(exception.getMessage().contains(
                String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, "tr/tutor")));
    }

    @Test
    public void validateNoValuesAfterPrefix_mixedEmptyAndNonEmpty_throwsParseExceptionOnFirst() {
        String args = "1 tr/ tc/cs2103 tg/";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        ParseException exception = Assertions.assertThrows(ParseException.class, () ->
                ParserUtil.validateNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES));

        assertTrue(exception.getMessage().contains(
                String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, "tc/cs2103")));
    }

    @Test
    public void validateNoValuesAfterPrefix_multipleNonEmptyValues_throwsParseExceptionOnFirst() {
        String args = "1 tr/tutor tc/cs2103 tg/friends";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, AllOWED_PREFIXES);

        ParseException exception = Assertions.assertThrows(ParseException.class, () ->
                ParserUtil.validateNoValuesAfterPrefix(argMultimap, AllOWED_PREFIXES));

        assertTrue(exception.getMessage().contains(
                String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, "tr/tutor")));
    }

    //================== Tests for validateEmptyPreamble ==================
    /*
     Valid equivalence partitions for argMultimap:
    - Empty preamble
    - Non-empty preamble

    Possible Boundary values:
    - Prefix containing all whitespaces (should not throw exception as it is considered empty)
     */

    @Test
    public void validateEmptyPreamble_emptyPreamble_noExceptionThrown() {
        String args = " n/alice";

        assertDoesNotThrow(() -> {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);
            ParserUtil.validateEmptyPreamble(argMultimap, "Usage message");
        });
    }

    @Test
    public void validateEmptyPreamble_preambleAllWhitespaces_noExceptionThrown() {
        // Boundary case
        String args = " \t \t \n \t \t n/alice";

        assertDoesNotThrow(() -> {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);
            ParserUtil.validateEmptyPreamble(argMultimap, "Usage message");
        });
    }

    @Test
    public void validateEmptyPreamble_nonEmptyPreamble_throwsParseException() {
        String args = " bob n/alice";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);
        String errorMessage = String.format(MESSAGE_PREAMBLE_NOT_EMPTY, "bob", "Usage message");

        assertThrows(ParseException.class,
                errorMessage, () -> ParserUtil.validateEmptyPreamble(argMultimap, "Usage message"));
    }

    //================== Tests for validateKeywordContainsAlphanumeric ==================

    /*
    Valid equivalence partitions for token:
    - token contains only alphanumeric characters
    - token contains only special characters
    - token contains only alphanumeric characters and special characters (boundary)
     */

    @Test
    public void validateKeywordContainsAlphanumeric_alphanumericCharactersOnly_noExceptionThrown() {
        assertDoesNotThrow(() -> {
            ParserUtil.validateKeywordContainsAlphanumeric(PREFIX_NAME, "Alice123");
        });
    }

    @Test
    public void validateKeywordContainsAlphanumeric_specialCharactersOnly_throwsParseException() {
        String errorMessage = String.format(MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS,
                PREFIX_NAME.getPrefix(), "!!!");

        assertThrows(ParseException.class,
                errorMessage, () -> ParserUtil.validateKeywordContainsAlphanumeric(PREFIX_NAME, "!!!"));
    }

    @Test
    public void validateKeywordContainsAlphanumeric_alphanumericAndSpecialCharacters_noExceptionThrown() {
        assertDoesNotThrow(() -> {
            ParserUtil.validateKeywordContainsAlphanumeric(PREFIX_NAME, "#Alice123!!!");
        });
    }
}

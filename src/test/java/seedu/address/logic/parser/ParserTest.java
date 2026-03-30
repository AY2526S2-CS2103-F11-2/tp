package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class ParserTest {

    //Test data
    private final Prefix[] disallowedPrefixes = {PREFIX_PHONE, PREFIX_TELEGRAM_HANDLE, PREFIX_INDEX};

    //================== Tests for findUnexpectedExtraInput ==================

    @Test
    public void findUnexpectedExtraInput_noDisallowedPrefixes_returnsEmpty() {
        String args = "n/Alice e/alice@example.com";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_singleDisallowedPrefix_returnsToken() {
        String args = "n/Alice p/12345";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_multipleDisallowedPrefixes_returnsEarliest() {
        String args = "n/Alice h/@handle i/3 p/12345";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("h/@handle", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_disallowedAtEnd_returnsToken() {
        String args = "n/Alice e/alice@nus.edu p/98765432";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/98765432", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_disallowedAtStart_returnsToken() {
        String args = "find p/12345 n/Alice";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_emptyArgs_returnsEmpty() {
        String args = "";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_onlyWhitespace_returnsEmpty() {
        String args = "   ";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_prefixWithoutValue_returnsToken() {
        String args = "n/Alice p/";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_prefixWithSpaceInValue_returnsTokenUpToSpace() {
        String args = "n/Alice p/123 456";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/123", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_caseInsensitivePrefix_returnsToken() {
        String args = "n/Alice P/12345";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("P/12345", result.get());
    }

    @Test
    public void findUnexpectedExtraInput_emptyDisallowedArray_returnsEmpty() {
        String args = "n/Alice p/12345";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, new Prefix[]{});
        assertFalse(result.isPresent());
    }

    @Test
    public void findUnexpectedExtraInput_multipleOccurrencesOfSamePrefix_returnsToken() {
        String args = "n/Alice p/111 p/222";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/111", result.get());
    }

    //================== Tests for findPrefixPosition (via findUnexpectedExtraInput) ==================

    @Test
    public void findPrefixPosition_prefixAtStart_returnsNegativeOne() {
        //Prefix must be preceded by whitespace, so it won't be found at the start
        String args = "p/value";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertFalse(result.isPresent());
    }

    @Test
    public void findPrefixPosition_prefixWithWhitespaceBefore_returnsCorrectToken() {
        String args = "some text p/value";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/value", result.get());
    }

    @Test
    public void findPrefixPosition_prefixNoWhitespaceBefore_isNotRecognized() {
        String args = "textwithoutp/value";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertFalse(result.isPresent());
    }

    @Test
    public void findPrefixPosition_multipleWhitespacesBefore_recognizesPrefix() {
        String args = "some    text   p/value";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/value", result.get());
    }

    //================== Tests for extractToken (via findUnexpectedExtraInput) ==================

    @Test
    public void extractToken_tokenWithoutTrailingSpace_returnsFullToken() {
        String args = "n/Alice p/12345";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void extractToken_tokenWithTrailingSpace_returnsTokenBeforeSpace() {
        String args = "n/Alice p/123 e/example";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/123", result.get());
    }

    @Test
    public void extractToken_tokenAtEnd_returnsFullToken() {
        String args = "n/Alice e/example p/12345";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/12345", result.get());
    }

    @Test
    public void extractToken_tokenWithSpecialCharacters_returnsCorrectly() {
        String args = "n/Alice h/@handle123!";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("h/@handle123!", result.get());
    }

    @Test
    public void extractToken_tokenWithMultipleSpaces_returnsUpToFirstSpace() {
        String args = "n/Alice p/123   456";
        Optional<String> result = Parser.findUnexpectedExtraInput(args, disallowedPrefixes);
        assertTrue(result.isPresent());
        assertEquals("p/123", result.get());
    }
}

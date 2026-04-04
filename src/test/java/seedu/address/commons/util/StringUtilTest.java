package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null));
    }

    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB"));
    }

    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb")); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1")); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa")); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa")); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ")); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for normalize ----------------------------------------
    @Test
    public void normalize_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.normalize(null));
    }

    @Test
    public void normalize_validString_returnsNormalizedString() {
        // all lowercase
        assertEquals("bob", StringUtil.normalize("bob"));

        // Mixed cases
        assertEquals("bob", StringUtil.normalize("bOb"));

        // Lowercase String with non-alphanumeric characters
        assertEquals("bob c prim", StringUtil.normalize("bob c. prim"));
        assertEquals("bob hi", StringUtil.normalize("bob hi."));

        // Mixed case String with non-alphanumeric characters
        assertEquals("bob c prim", StringUtil.normalize("bob C. PrIm"));

        // String with all punctuation
        assertEquals("", StringUtil.normalize("#$%^&*()"));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }


    //---------------- Tests for dameraudamerauLevenshteinDistance --------------------------------------

    @Test
    public void damerauLevenshteinDistance_identicalStrings_returnsZero() {
        assertEquals(0, StringUtil.damerauLevenshteinDistance("a", "a"));
        assertEquals(0, StringUtil.damerauLevenshteinDistance("test", "test"));
        assertEquals(0, StringUtil.damerauLevenshteinDistance("kitten", "kitten"));
    }

    @Test
    public void damerauLevenshteinDistance_emptyStrings_returnsCorrectDistance() {
        assertEquals(0, StringUtil.damerauLevenshteinDistance("", ""));
        assertEquals(4, StringUtil.damerauLevenshteinDistance("", "test"));
        assertEquals(6, StringUtil.damerauLevenshteinDistance("", "kitten"));
    }

    @Test
    public void damerauLevenshteinDistance_differentStrings_returnsCorrectDistance() {
        // 1 substitution
        assertEquals(1, StringUtil.damerauLevenshteinDistance("cat", "cut"));

        // 1 insertion
        assertEquals(1, StringUtil.damerauLevenshteinDistance("cat", "cats"));

        // 1 deletion
        assertEquals(1, StringUtil.damerauLevenshteinDistance("cats", "cat"));

        // 1 transposition
        assertEquals(1, StringUtil.damerauLevenshteinDistance("alex", "alxe"));

        // multiple of same operation (3 substitutions)
        assertEquals(3, StringUtil.damerauLevenshteinDistance("kitten", "sitting"));

        // Mix of operations: Example from https://www.youtube.com/watch?v=We3YDTzNXEk
        // 2 substitutions, 1 deletion
        assertEquals(3, StringUtil.damerauLevenshteinDistance("abcdef", "azced"));

        // incremental differences
        assertEquals(3, StringUtil.damerauLevenshteinDistance("abc", "axcde"));

        // More realistic real-world examples
        assertEquals(2, StringUtil.damerauLevenshteinDistance("robert", "rupert"));
        assertEquals(1, StringUtil.damerauLevenshteinDistance("email", "e-mail"));
    }

    @Test
    public void damerauLevenshteinDistance_caseInsensitiveComparison_returnsCorrectDistance() {
        // identical strings
        assertEquals(0, StringUtil.damerauLevenshteinDistance("Test", "test"));
        assertEquals(0, StringUtil.damerauLevenshteinDistance("Kitten", "kitten"));

        // different strings
        assertEquals(3, StringUtil.damerauLevenshteinDistance("Kitten", "SITTING"));
    }

    @Test
    public void damerauLevenshteinDistance_symmetryProperty_holds() {
        assertEquals(
                StringUtil.damerauLevenshteinDistance("kitten", "sitting"),
                StringUtil.damerauLevenshteinDistance("sitting", "kitten")
        );
    }

    @Test
    public void damerauLevenshteinDistance_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.damerauLevenshteinDistance(null, "abc"));
        assertThrows(NullPointerException.class, () -> StringUtil.damerauLevenshteinDistance("abc", null));
        assertThrows(NullPointerException.class, () -> StringUtil.damerauLevenshteinDistance(null, null));
    }

    //---------------- Tests for matchesFuzzy ----------------------------------------

    /*
     * Invalid equivalence partitions for query/word: null
     * Invalid equivalence partitions for threshold: negative values
     * The test cases below test these invalid inputs.
     */

    @Test
    public void matchesFuzzy_nullQuery_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.matchesFuzzy(null, "word", 1));
    }

    @Test
    public void matchesFuzzy_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.matchesFuzzy("query", null, 1));
    }

    @Test
    public void matchesFuzzy_negativeThreshold_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Threshold cannot be negative", () ->
                StringUtil.matchesFuzzy("query", "word", -1));
    }

    /*
     * Valid equivalence partitions for query/word:
     *   - empty strings
     *   - single character
     *   - multiple characters
     *   - identical strings
     *   - strings with whitespace
     *   - strings with different cases
     *   - strings with special characters
     *
     * Valid equivalence partitions for threshold:
     *   - zero (exact match only)
     *   - positive values
     *   - large positive values
     *
     * Possible scenarios returning true:
     *   - exact match (distance = 0)
     *   - within threshold (distance <= threshold)
     *   - both strings empty (distance = 0)
     *   - case-insensitive match
     *
     * Possible scenarios returning false:
     *   - distance exceeds threshold
     *   - one string empty, other non-empty (except both empty)
     *   - completely different words
     *
     * The test methods below try to verify these scenarios comprehensively.
     */

    @Test
    public void matchesFuzzy_exactMatches_returnsTrue() {
        // Identical strings
        assertTrue(StringUtil.matchesFuzzy("john", "john", 0));
        assertTrue(StringUtil.matchesFuzzy("test", "test", 1));
        assertTrue(StringUtil.matchesFuzzy("hello", "hello", 2));

        // Case-insensitive exact match
        assertTrue(StringUtil.matchesFuzzy("John", "john", 0));
        assertTrue(StringUtil.matchesFuzzy("TEST", "test", 0));
        assertTrue(StringUtil.matchesFuzzy("HeLLo", "HELLO", 0));
    }

    @Test
    public void matchesFuzzy_withinThreshold_returnsTrue() {
        // 1 character difference (substitution)
        assertTrue(StringUtil.matchesFuzzy("john", "joan", 1)); // substitution: h->a
        assertTrue(StringUtil.matchesFuzzy("cat", "cut", 1)); // substitution: a->u

        // 1 character difference (insertion/deletion)
        assertTrue(StringUtil.matchesFuzzy("cat", "cats", 1)); // 1 insertion
        assertTrue(StringUtil.matchesFuzzy("cats", "cat", 1)); // 1 deletion: s
        assertTrue(StringUtil.matchesFuzzy("jhon", "john", 2)); // 1 transposition

        // 2 character differences
        assertTrue(StringUtil.matchesFuzzy("kitten", "sitting", 3)); // Multiple operations

        // Threshold larger than needed
        assertTrue(StringUtil.matchesFuzzy("cat", "dog", 5)); // threshold is generous

        // Case-insensitive with threshold
        assertTrue(StringUtil.matchesFuzzy("john", "JOAN", 1));
        assertTrue(StringUtil.matchesFuzzy("TEST", "TeSt", 0));
    }

    @Test
    public void matchesFuzzy_exceedsThreshold_returnsFalse() {
        // Significantly different words with small threshold
        assertFalse(StringUtil.matchesFuzzy("john", "dog", 1)); // distance = 3
        assertFalse(StringUtil.matchesFuzzy("cat", "elephant", 1)); // very different
        assertFalse(StringUtil.matchesFuzzy("test", "abc", 2)); // distance = 4

        // Distance exactly exceeds threshold
        assertFalse(StringUtil.matchesFuzzy("kitten", "sitting", 2)); // distance = 3
        assertFalse(StringUtil.matchesFuzzy("abc", "xyz", 2)); // all different
    }

    @Test
    public void matchesFuzzy_zeroThreshold_exactMatchOnly() {
        // Exact matches pass
        assertTrue(StringUtil.matchesFuzzy("john", "john", 0));
        assertTrue(StringUtil.matchesFuzzy("TEST", "test", 0));

        // Any difference fails
        assertFalse(StringUtil.matchesFuzzy("john", "joan", 0));
        assertFalse(StringUtil.matchesFuzzy("robert", "rupert", 0));
    }

    @Test
    public void matchesFuzzy_emptyStrings_handledCorrectly() {
        // Both empty - should match
        assertTrue(StringUtil.matchesFuzzy("", "", 0));
        assertTrue(StringUtil.matchesFuzzy("", "", 1));
        assertTrue(StringUtil.matchesFuzzy("", "", 10));

        // One empty, other non-empty - should not match
        assertFalse(StringUtil.matchesFuzzy("", "word", 0));
        assertFalse(StringUtil.matchesFuzzy("", "word", 10));
        assertFalse(StringUtil.matchesFuzzy("word", "", 0));
        assertFalse(StringUtil.matchesFuzzy("word", "", 10));
    }

    @Test
    public void matchesFuzzy_withLeadingTrailingSpaces_trimmedCorrectly() {
        // Strings with spaces are trimmed
        assertTrue(StringUtil.matchesFuzzy("  john  ", "john", 0));
        assertTrue(StringUtil.matchesFuzzy("john", "  john  ", 0));
        assertTrue(StringUtil.matchesFuzzy("  john  ", "  john  ", 0));

        // Spaces trimmed, then fuzzy match applied
        assertTrue(StringUtil.matchesFuzzy("  john  ", "joan", 1));
        assertTrue(StringUtil.matchesFuzzy("  cat  ", "  cut  ", 1));

        // Trimmed to empty
        assertTrue(StringUtil.matchesFuzzy("   ", "   ", 0));
        assertFalse(StringUtil.matchesFuzzy("   ", "word", 0));
    }

    @Test
    public void matchesFuzzy_singleCharacters_worksCorrectly() {
        // Same single character
        assertTrue(StringUtil.matchesFuzzy("a", "a", 0));
        assertTrue(StringUtil.matchesFuzzy("X", "x", 0));

        // Different single characters
        assertFalse(StringUtil.matchesFuzzy("a", "b", 0));
        assertTrue(StringUtil.matchesFuzzy("a", "b", 1)); // substitution distance = 1

        // Single character to multi-character
        assertFalse(StringUtil.matchesFuzzy("a", "abc", 0));
        assertTrue(StringUtil.matchesFuzzy("a", "abc", 2)); // need deletions/insertions
    }

    @Test
    public void matchesFuzzy_realWorldTypoExamples_worksCorrectly() {
        // Common typos with threshold 1
        assertTrue(StringUtil.matchesFuzzy("name", "nme", 1)); // deletion
        assertTrue(StringUtil.matchesFuzzy("address", "adress", 1)); // deletion

        // Typos that exceed threshold
        assertFalse(StringUtil.matchesFuzzy("johnny", "jhny", 1)); // 2 deletions
    }

    @Test
    public void matchesFuzzy_largeThreshold_alwaysMatches() {
        // With very large threshold, almost any strings match
        assertTrue(StringUtil.matchesFuzzy("completely", "different", 100));
        assertTrue(StringUtil.matchesFuzzy("a", "zzzzzzzzzzz", 100));

        // Except when one is empty and other is not, unless both are empty
        assertFalse(StringUtil.matchesFuzzy("", "word", 100));
    }

    @Test
    public void matchesFuzzy_caseInsensitive_worksCorrectly() {
        // Various case combinations should work
        assertTrue(StringUtil.matchesFuzzy("john", "John", 0));
        assertTrue(StringUtil.matchesFuzzy("JOHN", "john", 0));
        assertTrue(StringUtil.matchesFuzzy("JoHn", "jOhN", 0));
        assertTrue(StringUtil.matchesFuzzy("TEST@123", "test@123", 0));

        // Case-insensitive fuzzy matching
        assertTrue(StringUtil.matchesFuzzy("John", "Joan", 1));
        assertTrue(StringUtil.matchesFuzzy("TEST", "TOST", 1));
    }

    @Test
    public void matchesFuzzy_specialCharacters_handled() {
        // Strings with special characters
        assertTrue(StringUtil.matchesFuzzy("test@email.com", "test@email.com", 0));
        assertTrue(StringUtil.matchesFuzzy("test-name", "test-name", 0));

        // Special character differences
        // requires 1 deletion
        assertFalse(StringUtil.matchesFuzzy("test@email.com", "testemail.com", 0));
        assertTrue(StringUtil.matchesFuzzy("test@email.com", "testemail.com", 1));
    }
}

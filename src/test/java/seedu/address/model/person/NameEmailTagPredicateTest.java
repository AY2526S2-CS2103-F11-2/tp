package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.List;

import org.junit.jupiter.api.Test;

public class NameEmailTagPredicateTest {
    @Test
    public void equals() {
        List<String> firstNameKeywordList = List.of("first");
        List<String> firstEmailKeywordList = List.of("u.nus.edu");
        List<String> firstTagList = List.of("friends");

        // different name keywords
        List<String> secondNameKeywordList = List.of("first", "second");
        List<String> secondEmailKeywordList = List.of("u.nus.edu");
        List<String> secondTagList = List.of("friends");

        // different email keywords
        List<String> thirdNameKeywordList = List.of("first");
        List<String> thirdEmailKeywordList = List.of("gmail.com");
        List<String> thirdTagList = List.of("friends");

        // different tags
        List<String> fourthNameKeywordList = List.of("first");
        List<String> fourthEmailKeywordList = List.of("u.nus.edu");
        List<String> fourthTagList = List.of("cs2103");

        NameEmailTagPredicate firstPredicate = new NameEmailTagPredicate(
                firstNameKeywordList, firstEmailKeywordList, firstTagList);
        NameEmailTagPredicate secondPredicate = new NameEmailTagPredicate(
                secondNameKeywordList, secondEmailKeywordList, secondTagList);
        NameEmailTagPredicate thirdPredicate = new NameEmailTagPredicate(
                thirdNameKeywordList, thirdEmailKeywordList, thirdTagList);
        NameEmailTagPredicate fourthPredicate = new NameEmailTagPredicate(
                fourthNameKeywordList, fourthEmailKeywordList, fourthTagList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameEmailTagPredicate firstPredicateCopy = new NameEmailTagPredicate(
                firstNameKeywordList, firstEmailKeywordList, firstTagList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicates -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different emails -> returns false
        assertFalse(firstPredicate.equals(thirdPredicate));

        // different tags -> returns false
        assertFalse(firstPredicate.equals(fourthPredicate));
    }

    @Test
    public void test_allFieldsMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"),
                List.of("example.com"),
                List.of("friends"));

        assertTrue(predicate.test(ALICE)); // matches all

        predicate = new NameEmailTagPredicate(
                List.of("Benson"),
                List.of("example.com"),
                List.of("owesMoney"));
        assertTrue(predicate.test(BENSON));
    }

    @Test
    public void test_nameOnlyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"), List.of(), List.of());

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_nameFuzzyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alie"), List.of(), List.of());
        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_emailOnlyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of("example.com"), List.of());

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_tagOnlyMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of(), List.of("friends"));

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_multipleNameKeywordsMatch_returnsTrue() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice", "Benson"), List.of(), List.of());

        assertTrue(predicate.test(ALICE));
        assertTrue(predicate.test(BENSON));
    }

    @Test
    public void test_nameOnlyNoMatch_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"), List.of(), List.of());

        assertFalse(predicate.test(BENSON));
    }

    @Test
    public void test_tagOnlyNoMatch_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of(), List.of("nonexistent"));

        assertFalse(predicate.test(BENSON));
    }

    @Test
    public void test_oneFieldFails_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of("Alice"),
                List.of("nonexistent"),
                List.of("friends"));

        assertFalse(predicate.test(ALICE));
    }

    @Test
    public void test_emailOnlyNoMatch_returnsFalse() {
        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                List.of(), List.of("alice"), List.of());

        assertFalse(predicate.test(BENSON));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("person1", "person2");
        List<String> emailKeywords = List.of("email1", "email2");
        List<String> tagList = List.of("friends");

        NameEmailTagPredicate predicate = new NameEmailTagPredicate(
                nameKeywords, emailKeywords, tagList);

        String expected = NameEmailTagPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords
                + ", emailKeywords=" + emailKeywords
                + ", tags=" + tagList + "}";

        assertEquals(expected, predicate.toString());
    }
}

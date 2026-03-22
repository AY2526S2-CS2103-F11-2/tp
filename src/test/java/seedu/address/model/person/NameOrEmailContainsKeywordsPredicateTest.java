package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NameOrEmailContainsKeywordsPredicateTest {
    @Test
    public void equals() {
        List<String> firstNameKeywordList = List.of("first");
        List<String> firstEmailKeywordList = List.of("first@gmail.com");

        List<String> secondNameKeywordList = List.of("first", "second");
        List<String> secondEmailKeywordList = List.of("first@gmail.com", "second@gmail.com");

        List<String> thirdNameKeywordList = List.of("first");
        List<String> thirdEmailKeywordList = List.of("gmail");

        NameOrEmailContainsKeywordsPredicate firstPredicate = new NameOrEmailContainsKeywordsPredicate(
                firstNameKeywordList, firstEmailKeywordList);
        NameOrEmailContainsKeywordsPredicate secondPredicate = new NameOrEmailContainsKeywordsPredicate(
                secondNameKeywordList, secondEmailKeywordList);
        NameOrEmailContainsKeywordsPredicate thirdPredicate = new NameOrEmailContainsKeywordsPredicate(
                thirdNameKeywordList, thirdEmailKeywordList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same values -> returns true
        NameOrEmailContainsKeywordsPredicate firstPredicateCopy = new NameOrEmailContainsKeywordsPredicate(
                firstNameKeywordList, firstEmailKeywordList);
        assertEquals(firstPredicate, firstPredicateCopy);

        // different types -> returns false
        assertNotEquals(1, firstPredicate);
        assertFalse(firstPredicate.equals("first"));
        assertFalse(firstPredicate.equals("first@gmail.com"));

        // null -> returns false
        assertNotEquals(null, firstPredicate);

        // different person -> returns false
        assertNotEquals(firstPredicate, secondPredicate);

        // same name keywords but different email keywords -> returns false
        assertNotEquals(firstPredicate, thirdPredicate);
    }

    @Test
    public void test_nameMatches_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(List.of("Alice"), Collections.emptyList());

        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_nameSubstringMatches_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(List.of("Ali"), Collections.emptyList());

        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_emailMatches_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(Collections.emptyList(), List.of("gmail"));

        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_nameOrEmailMatches_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(List.of("Alice"), List.of("gmail"));

        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_multipleKeywords_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(List.of("Bob", "Alice"), List.of("yahoo", "gmail"));

        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_caseInsensitiveMatching_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(List.of("aLIce"), List.of("GMAIL"));

        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_neitherNameNorEmailMatches_returnsFalse() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(List.of("Bob"), List.of("yahoo"));

        assertFalse(predicate.test(alice));
    }

    @Test
    public void test_emptyKeywordLists_returnsFalse() {
        Person alice = new PersonBuilder()
                .withName("Alice Tan")
                .withEmail("alice@gmail.com")
                .build();

        NameOrEmailContainsKeywordsPredicate predicate =
                new NameOrEmailContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList());

        assertFalse(predicate.test(alice));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("keyword1", "keyword2");
        List<String> emailKeywords = List.of("email1", "email2");
        NameOrEmailContainsKeywordsPredicate predicate = new NameOrEmailContainsKeywordsPredicate(
                nameKeywords, emailKeywords);

        String expected = NameOrEmailContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords + ", emailKeywords=" + emailKeywords + "}";

        assertEquals(expected, predicate.toString());
    }
}

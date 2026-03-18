package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class EmailContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first@gmail.com");
        List<String> secondPredicateKeywordList = Arrays.asList("first@gmail.com", "second@gmail.com");

        EmailContainsKeywordsPredicate firstPredicate = new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        EmailContainsKeywordsPredicate secondPredicate = new EmailContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        EmailContainsKeywordsPredicate firstPredicateCopy = new EmailContainsKeywordsPredicate(
                firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        Person alice = new PersonBuilder()
                .withName("Alice")
                .withEmail("alice@gmail.com")
                .build();

        // Exact email match
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(List.of("alice@gmail.com"));
        assertTrue(predicate.test(alice));

        // Partial keyword match
        predicate = new EmailContainsKeywordsPredicate(List.of("gmail"));
        assertTrue(predicate.test(alice));

        // Multiple exact keywords
        predicate = new EmailContainsKeywordsPredicate(List.of("alice@gmail.com", "bob@gmail.com"));
        assertTrue(predicate.test(alice));

        // Multiple partial keywords
        predicate = new EmailContainsKeywordsPredicate(List.of("gmail", "alice"));
        assertTrue(predicate.test(alice));

        // One of multiple keywords matches
        predicate = new EmailContainsKeywordsPredicate(List.of("gmail", "yahoo"));
        assertTrue(predicate.test(alice));

        // Case-insensitive match
        predicate = new EmailContainsKeywordsPredicate(List.of("aLIce@GMAIL.com"));
        assertTrue(predicate.test(alice));
    }

    @Test
    public void test_emailDoesNotContainKeywords_returnsFalse() {
        Person alice = new PersonBuilder()
                .withName("Alice")
                .withEmail("alice@gmail.com")
                .build();

        // Empty keyword list
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(alice));

        // Non-matching keyword
        predicate = new EmailContainsKeywordsPredicate(List.of("alice@yahoo.com"));
        assertFalse(predicate.test(alice));

        // Keywords match other fields but not email
        Person person = new PersonBuilder()
                .withName("Alice")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .build();

        predicate = new EmailContainsKeywordsPredicate(List.of("12345", "Main", "Street"));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(keywords);

        String expected = EmailContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}


package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class PersonContainsTagsPredicateTest {

    @Test
    public void equals() {
        PersonContainsTagsPredicate firstPredicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));
        PersonContainsTagsPredicate secondPredicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("colleagues")));

        // same object -> true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> true
        PersonContainsTagsPredicate firstPredicateCopy =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> false
        assertFalse(firstPredicate.equals(1));

        // null -> false
        assertFalse(firstPredicate.equals(null));

        // different tags -> false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_singleMatchingTag_returnsTrue() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));

        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_multipleTagsPersonMatchesOne_returnsTrue() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(
                        new Tag("friends"), new Tag("colleagues")));

        assertTrue(predicate.test(ALICE));
        assertTrue(predicate.test(BENSON));
    }

    @Test
    public void test_noMatchingTags_returnsFalse() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("unknown")));

        assertFalse(predicate.test(ALICE));
    }

    @Test
    public void test_caseInsensitiveMatching_returnsTrue() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("FrIeNdS")));

        // 你的实现用了 toLowerCase → 应该匹配成功
        assertTrue(predicate.test(ALICE));
    }

    @Test
    public void test_emptyTagSet_returnsFalse() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of());

        // anyMatch over empty set → 永远 false
        assertFalse(predicate.test(ALICE));
    }

    @Test
    public void toStringMethod() {
        PersonContainsTagsPredicate predicate =
                new PersonContainsTagsPredicate(Set.of(new Tag("friends")));

        String expected = PersonContainsTagsPredicate.class.getCanonicalName()
                + "{tags=" + predicate.toString().split("tags=")[1];

        assertTrue(predicate.toString().contains("friends"));
    }
}

package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} or {@code Email} matches some given keywords.
 */
public class NameOrEmailContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> emailKeywords;

    /**
     * Constructs the Predicate using a list of name keywords and a list of email keywords.
     *
     * @param nameKeywords A list of name keywords
     * @param emailKeywords A list of email keywords
     */
    public NameOrEmailContainsKeywordsPredicate(List<String> nameKeywords, List<String> emailKeywords) {
        this.nameKeywords = nameKeywords;
        this.emailKeywords = emailKeywords;
    }

    @Override
    public boolean test(Person person) {
        return new NameContainsKeywordsPredicate(this.nameKeywords).test(person)
                || new EmailContainsKeywordsPredicate(this.emailKeywords).test(person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameOrEmailContainsKeywordsPredicate otherPredicate)) {
            return false;
        }

        return this.nameKeywords.equals(otherPredicate.nameKeywords)
                && this.emailKeywords.equals(otherPredicate.emailKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", this.nameKeywords)
                .add("emailKeywords", this.emailKeywords)
                .toString();
    }
}

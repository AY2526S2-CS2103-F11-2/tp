package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@link Person} matches any of the given name keywords,
 *  email keywords, and tags. Empty keyword/tag collections are treated as "match all" for that field.
 *
 *  <p>
 *  The predicate applies:
 *  <ul>
 *     <li>OR logic within each field (e.g. multiple name keywords)</li>
 *     <li>AND logic across different fields (name, email, tags)</li>
 *  </ul>
 */
public class NameEmailTagPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> emailKeywords;
    private final List<String> tags;

    /**
     * Constructs a {@code NameEmailTagPredicate}.
     *
     * @param nameKeywords List of name keywords to match against.
     * @param emailKeywords List of email keywords to match against.
     * @param tags List of tags names to match against.
     */
    public NameEmailTagPredicate(List<String> nameKeywords, List<String> emailKeywords, List<String> tags) {
        this.nameKeywords = nameKeywords;
        this.emailKeywords = emailKeywords;
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        boolean nameMatches = nameKeywords.isEmpty()
                || new NameContainsKeywordsPredicate(nameKeywords).test(person);

        boolean emailMatches = emailKeywords.isEmpty()
                || new EmailContainsKeywordsPredicate(emailKeywords).test(person);

        boolean tagMatches = tags.isEmpty()
                || new PersonContainsTagsPredicate(tags).test(person);

        // AND across fields
        return nameMatches && emailMatches && tagMatches;
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameEmailTagPredicate otherPred)) {
            return false;
        }

        return this.nameKeywords.equals(otherPred.nameKeywords)
                && this.emailKeywords.equals(otherPred.emailKeywords)
                && this.tags.equals(otherPred.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("emailKeywords", emailKeywords)
                .add("tags", tags)
                .toString();
    }
}

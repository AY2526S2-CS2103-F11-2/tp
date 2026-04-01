package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@link Person} matches any of the given name keywords,
 * email keywords, and tags. Empty keyword/tag collections are treated as "match all" for that field.
 *
 * <p>Matching behaviors by field:
 * <ul>
 *    <li><b>Name keywords:</b> Uses both exact substring matching and fuzzy matching (with dynamic
 *        Levenshtein distance threshold) to handle typos and variations. See
 *        {@link NameContainsKeywordsPredicate} for details.</li>
 *    <li><b>Email keywords:</b> Uses exact substring matching for email addresses.</li>
 *    <li><b>Tags:</b> Uses exact matching on tag names.</li>
 * </ul>
 * </p>
 *
 * <p>Logic combination:
 * <ul>
 *    <li>OR logic within each field (e.g., multiple name keywords match if ANY match)</li>
 *    <li>AND logic across different fields (all non-empty field criteria must match)</li>
 * </ul>
 * </p>
 *
 * @see NameContainsKeywordsPredicate for name matching details including fuzzy matching
 * @see EmailContainsKeywordsPredicate for email matching details
 * @see PersonContainsTagsPredicate for tag matching details
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
        requireAllNonNull(nameKeywords, emailKeywords, tags);

        this.nameKeywords = List.copyOf(nameKeywords);
        this.emailKeywords = List.copyOf(emailKeywords);
        this.tags = List.copyOf(tags);
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

package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 *
 * <p>The predicate performs both exact and fuzzy matching on individual name tokens:
 * <ul>
 *   <li><b>Exact substring matching:</b> A keyword is considered a match if it is a
 *       substring of any name token (e.g., "john" matches tokens containing "john")</li>
 *   <li><b>Fuzzy matching:</b> Additionally, keywords can fuzzy-match name tokens within
 *       a Damerau–Levenshtein distance threshold to handle typos and variations</li>
 * </ul>
 * </p>
 *
 * <p>The fuzzy matching threshold is calculated dynamically based on keyword length:
 * <pre>
 *   threshold = max(MIN_ALLOWED_EDITS, keyword.length() * EDIT_DISTANCE_RATIO)
 * </pre>
 * By default, this allows at least 1 edit (substitution, insertion, or deletion)
 * for short keywords, scaling up for longer keywords (e.g., 20% of keyword length).
 * </p>
 *
 * <p>All matching is case-insensitive and performed on normalized strings
 * (lowercase with special characters removed).</p>
 *
 * <p>Examples (with default threshold settings):
 * <pre>
 *   Keyword "john" matches name tokens: "john", "jon", "joan" (fuzzy)
 *   Keyword "alice" matches name tokens: "alice", "aliec", "alie"
 * </pre>
 * </p>
 *
 * @see StringUtil#normalize(String) for normalization details
 * @see StringUtil#matchesFuzzy(String, String, int) for fuzzy matching details
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {

    // Minimum number of edits allowed in fuzzy matching (prevents zero edits for very short keywords)
    private static final int MIN_ALLOWED_EDITS = 1;
    // Ratio of keyword length to determine allowed edits in fuzzy matching (e.g., 20% of the keyword length)
    private static final double EDIT_DISTANCE_RATIO = 0.2;

    private final List<String> keywords;

    /**
     * Constructs a {@code NameContainsKeywordsPredicate} using a list of name keywords.
     *
     * <p>The predicate stores a defensive copy of the keywords list to prevent external modification.
     * Keywords will be normalized during matching (see {@link #test(Person)}).</p>
     *
     * @param keywords The list of name keywords to match against (cannot be null or contain null elements)
     * @throws NullPointerException if {@code keywords} is null or contains null elements
     */
    public NameContainsKeywordsPredicate(List<String> keywords) {
        requireAllNonNull(keywords);

        this.keywords = List.copyOf(keywords);
    }

    @Override
    public boolean test(Person person) {
        // Split name based on white spaces
        String[] nameTokens = StringUtil.normalize(person.getName().fullName)
                .split("\\s+");

        return keywords.stream()
                .map(StringUtil::normalize)
                .anyMatch(keyword -> matchesAnyToken(nameTokens, keyword));
    }

    private boolean matchesAnyToken(String[] nameTokens, String keyword) {
        return Arrays.stream(nameTokens)
                .anyMatch(token -> token.contains(keyword)
                        || isFuzzyMatch(token, keyword));
    }

    /**
     * Checks whether the given {@code token} approximately matches the {@code keyword}
     * using the Damerau-Levenshtein algorithm.
     *
     * <p>The allowed number of edits is calculated as the maximum of:
     * <ul>
     *     <li>{@code MIN_ALLOWED_EDITS} (minimum allowed edits)</li>
     *     <li>{@code ceil(keyword.length() * EDIT_DISTANCE_RATIO)}</li>
     * </ul>
     * This ensures that longer keywords can tolerate more mismatches proportionally,
     * while still enforcing a minimum edit allowance.</p>
     *
     * @param token the string to test
     * @param keyword the target keyword
     * @return {@code true} if {@code token} matches {@code keyword} within the allowed edit distance
     */
    private boolean isFuzzyMatch(String token, String keyword) {
        int threshold = Math.max(MIN_ALLOWED_EDITS,
                (int) Math.ceil(keyword.length() * EDIT_DISTANCE_RATIO));

        return StringUtil.matchesFuzzy(token, keyword, threshold);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate)) {
            return false;
        }

        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}

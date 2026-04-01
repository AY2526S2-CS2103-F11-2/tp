package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Normalizes the input string by converting it to lowercase and removing special characters such as punctuation.
     *
     * <p>This method standardizes strings for comparison and storage by:
     * <ul>
     *   <li>Converting all characters to lowercase for case-insensitive operations</li>
     *   <li>Removing all special characters (punctuation, symbols, etc.)</li>
     *   <li>Preserving alphanumeric characters (a-z, 0-9) and whitespace</li>
     * </ul>
     * </p>
     *
     * <p>This is useful for fuzzy matching, search preprocessing, or comparing names
     * that may contain special characters or inconsistent casing.</p>
     *
     * @param s the string to normalize (cannot be null)
     * @return the normalized string with lowercase letters, digits, and spaces only
     * @throws NullPointerException if {@code s} is {@code null}
     */
    public static String normalize(String s) {
        requireNonNull(s);

        return s.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "");
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


    /**
     * Computes the Levenshtein distance between two strings.
     *
     * <p>The Levenshtein distance is the minimum number of single-character edits
     * (insertions, deletions, or substitutions) required to transform one string
     * into the other.</p>
     *
     * <p>This implementation is case-insensitive and uses a dynamic programming
     * approach with O(n × m) time and space complexity, where n and m are the
     * lengths of the input strings.</p>
     *
     * @param a the first string
     * @param b the second string
     * @return the Levenshtein distance between {@code a} and {@code b}
     * @throws NullPointerException if either input string is {@code null}
     */
    static int levenshteinDistance(String a, String b) {
        requireNonNull(a);
        requireNonNull(b);

        int n = a.length();
        int m = b.length();

        int[][] dpMatrix = new int[n + 1][m + 1];

        // first row (steps to transform "" → b[0..col])
        for (int col = 0; col <= m; col++) {
            dpMatrix[0][col] = col;
        }

        // first column (steps to transform "" → a[0..row])
        for (int row = 0; row <= n; row++) {
            dpMatrix[row][0] = row;
        }

        // Fill up the matrix using dynamic programming
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char chA = Character.toLowerCase(a.charAt(i - 1));
                char chB = Character.toLowerCase(b.charAt(j - 1));

                int cost = (chA == chB) ? 0 : 1;
                dpMatrix[i][j] = Math.min(
                        dpMatrix[i - 1][j] + 1, // deletion
                        Math.min(dpMatrix[i][j - 1] + 1, // insertion
                                dpMatrix[i - 1][j - 1] + cost) // substitution/match
                );
            }
        }

        return dpMatrix[n][m];
    }

    /**
     * Returns true if the {@code query} matches the {@code word} using fuzzy matching.
     *
     * <p>Fuzzy matching uses Levenshtein/edit distance to allow approximate matches.
     * The method returns true if the Levenshtein/edit distance between the query and word
     * is less than or equal to the specified threshold.</p>
     *
     * <p>This is useful for typo-tolerant searches. For example, with a threshold of 2,
     * "john" would match "joan" or "jhon".</p>
     *
     * <p>The matching is case-insensitive.</p>
     *
     * @param query the search query string (will be trimmed)
     * @param word the word to match against (will be trimmed)
     * @param threshold the maximum Levenshtein distance allowed for a match
     * @return true if the words match within the threshold, false otherwise
     * @throws NullPointerException if {@code query} or {@code word} is {@code null}
     * @throws IllegalArgumentException if {@code threshold} is negative
     */
    public static boolean matchesFuzzy(String query, String word, int threshold) {
        requireNonNull(query, "Query cannot be null");
        requireNonNull(word, "Word cannot be null");
        checkArgument(threshold >= 0, "Threshold cannot be negative");

        // Normalize inputs by trimming whitespace
        String trimmedQuery = query.trim();
        String trimmedWord = word.trim();

        // Handle empty strings after trimming
        if (trimmedQuery.isEmpty() || trimmedWord.isEmpty()) {
            // Empty query should not match non-empty word and vice versa
            // except when both are empty (distance = 0)
            return trimmedQuery.isEmpty() && trimmedWord.isEmpty();
        }

        // Calculate Levenshtein distance and check if within threshold
        int distance = levenshteinDistance(trimmedQuery, trimmedWord);
        return distance <= threshold;
    }

}

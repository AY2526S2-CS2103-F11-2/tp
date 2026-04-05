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

    //====================== Fuzzy String Matching ============================/

    /**
     * Normalizes the input string by converting it to lowercase, removing trailing whitespaces
     * and special characters such as punctuation.
     *
     * <p>This method standardizes strings for comparison and storage by:
     * <ul>
     *   <li>Converting all characters to lowercase for case-insensitive operations</li>
     *   <li>Replacing non-alphanumeric characters (punctuation, symbols, etc.) with spaces</li>
     *   <li>Removing any trailing whitespaces</li>
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
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * Computes the Damerau–Levenshtein distance (OSA variant) between {@code keyword} and {@code target}.
     *
     * <p>This distance measures the minimum number of single-character edits (insertion, deletion,*
     * substitution, or transposition of adjacent characters) required to transform {@code keyword} into
     * {@code target}.</p>
     *
     * <p>This implementation uses a dynamic programming approach with O(n × m) time and space complexity,
     * where n and m are the lengths of the input strings.</p>
     *
     * Preconditions:
     * <ul>
     *   <li>{@code keyword} and {@code target} must be normalized
     *       (e.g. trimmed, lowercased and with non-alphanumeric characters handled consistently)</li>
     * </ul>
     *
     * @param keyword input string (e.g. user search query)
     * @param target the string to compare against (e.g. contact name)
     * @return the edit distance between {@code keyword} and {@code target}
     * @throws NullPointerException if either argument is {@code null}
     */
    static int damerauLevenshteinDistance(String keyword, String target) {
        requireNonNull(keyword);
        requireNonNull(target);

        int n = keyword.length();
        int m = target.length();

        // dynamic programming matrix
        int[][] dp = new int[n + 1][m + 1];

        // initialize the first row of the matrix
        for (int col = 0; col <= m; col++) {
            dp[0][col] = col;
        }

        // initialize the first col of the matrix
        for (int row = 0; row <= n; row++) {
            dp[row][0] = row;
        }

        // Fill up the matrix using dynamic programming
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char currentKeywordChar = keyword.charAt(i - 1);
                char currentTargetChar = target.charAt(j - 1);

                int cost = (currentKeywordChar == currentTargetChar) ? 0 : 1;
                dp[i][j] = Math.min(
                        dp[i - 1][j] + 1, // deletion
                        Math.min(dp[i][j - 1] + 1, // insertion
                                dp[i - 1][j - 1] + cost) // substitution/match
                );


                // Handles transpositions (swapping adjacent characters)
                if (i > 1 && j > 1) {
                    char prevKeywordChar = keyword.charAt(i - 2);
                    char prevTargetChar = target.charAt(j - 2);

                    if (currentKeywordChar == prevTargetChar
                            && prevKeywordChar == currentTargetChar) {
                        dp[i][j] = Math.min(
                                dp[i][j],
                                dp[i - 2][j - 2] + 1 // transposition
                        );
                    }
                }
            }
        }

        return dp[n][m];
    }

    /**
     * Returns true if the {@code target} is within a given edit distance from the {@code keyword}.
     *
     * <p>This method uses the Damerau–Levenshtein distance (Optimal String Alignment variant) to
     * compute the minimum number of edits required to transform {@code keyword} into {@code target}.
     * The method returns true if this distance is less than or equal to {@code threshold}.</p>
     *
     * <p>Preconditions:
     *   <ul>
     *       <li>{@code keyword} and {@code target} must be non-null</li>
     *       <li>{@code keyword} and {@code target} must already be normalized
     *           (trimmed, lowercased, and with non-alphanumeric characters handled consistently)</li>
     *       <li>{@code threshold} must be non-negative</li>
     *   </ul>
     *
     * @param keyword the search term (user input)
     * @param target the string to match against (e.g., contact name)
     * @param threshold the maximum allowed edit distance for a match
     * @return true if {@code target} is within {@code threshold} edits of {@code keyword}, false otherwise
     * @throws NullPointerException if {@code keyword} or {@code target} is null
     * @throws IllegalArgumentException if {@code threshold} is negative
     */
    public static boolean isWithinEditDistance(String keyword, String target, int threshold) {
        requireNonNull(keyword);
        requireNonNull(target);
        checkArgument(threshold >= 0, "Threshold cannot be negative");

        // Handle empty strings after trimming
        if (keyword.isEmpty() || target.isEmpty()) {
            // Empty query should not match non-empty word and vice versa
            // except when both are empty (distance = 0)
            return keyword.isEmpty() && target.isEmpty();
        }

        // Calculate Damerau Levenshtein Distance and check if within threshold
        int distance = damerauLevenshteinDistance(keyword, target);
        return distance <= threshold;
    }

}

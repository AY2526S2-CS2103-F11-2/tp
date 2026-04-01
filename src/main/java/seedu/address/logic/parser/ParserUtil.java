package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer (1, 2, 3...).";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String telegramHandle} into a {@code TelegramHandle}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code telegramHandle} is invalid.
     */
    public static TelegramHandle parseTelegramHandle(String telegramHandle) throws ParseException {
        requireNonNull(telegramHandle);
        String trimmedTelegramHandle = telegramHandle.trim();
        if (!TelegramHandle.isValidTelegramHandle(trimmedTelegramHandle)) {
            throw new ParseException(TelegramHandle.MESSAGE_CONSTRAINTS);
        }
        return new TelegramHandle(trimmedTelegramHandle);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag} of the specified {@code TagType}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param tag  The tag name to be parsed.
     * @param type The type of the tag (e.g., ROLE, COURSE, GENERAL).
     * @return A {@code Tag} object containing the trimmed tag name and its type.
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag, TagType type) throws ParseException {
        requireNonNull(tag);
        requireNonNull(type);

        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag, type);
    }

    /**
     * Parses a {@code Collection<String>} of tag names into a {@code Set<Tag>} of the specified {@code TagType}.
     */
    public static Set<Tag> parseTags(Collection<String> tags, TagType type) throws ParseException {
        requireNonNull(tags);
        requireNonNull(type);

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName, type));
        }
        return tagSet;
    }

    /**
     * Returns the first disallowed prefixed token in input order, if any.
     * A prefix is only recognized when preceded by whitespace, matching ArgumentTokenizer behavior.
     */
    static Optional<String> findUnexpectedExtraInput(String args, Prefix[] disallowedPrefixes) {
        int earliestPosition = -1;
        String unexpectedToken = null;

        for (Prefix prefix : disallowedPrefixes) {
            int position = findPrefixPosition(args, prefix);
            if (position != -1 && (earliestPosition == -1 || position < earliestPosition)) {
                earliestPosition = position;
                unexpectedToken = extractToken(args, position);
            }
        }

        return Optional.ofNullable(unexpectedToken);
    }

    private static int findPrefixPosition(String args, Prefix prefix) {
        int prefixIndex = args.toLowerCase(Locale.ROOT)
                .indexOf(" " + prefix.getPrefix().toLowerCase(Locale.ROOT));
        return prefixIndex == -1 ? -1 : prefixIndex + 1;
    }

    private static String extractToken(String args, int startPosition) {
        int endPosition = args.indexOf(' ', startPosition);
        if (endPosition == -1) {
            return args.substring(startPosition);
        }
        return args.substring(startPosition, endPosition);
    }

    /**
     * Returns a prefix from the given arguments that has an empty string as one of its values, if any.
     *
     * <p>This method checks the provided {@code prefixes} in order. For each prefix,
     * it looks up all values in the given {@link ArgumentMultimap}. If any value for that prefix
     * is empty (after trimming whitespace), that prefix is returned wrapped in an {@link Optional}.
     * If none of the provided prefixes have empty values, an empty {@code Optional} is returned.
     *
     * @param argMultimap the {@link ArgumentMultimap} containing the mapping of prefixes to values
     * @param prefixes the prefixes to check for empty values
     * @return an {@link Optional} containing the first prefix from {@code prefixes} that has
     *         an empty value, or {@code Optional.empty()} if none of them do
     */
    public static Optional<String> findEmptyPrefixValues(
            ArgumentMultimap argMultimap,
            Prefix... prefixes) {

        for (Prefix prefix : prefixes) {
            // Only validate if the prefix is actually present
            if (!argMultimap.getAllValues(prefix).isEmpty()) {
                for (String value : argMultimap.getAllValues(prefix)) {
                    if (value.trim().isEmpty()) {
                        return Optional.of(prefix.getPrefix());
                    }
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the first prefixed token that is NOT in the allowed prefixes list.
     */
    public static Optional<String> findInvalidPrefixInput(String args, Prefix[] allowedPrefixes) {
        Set<String> allowedPrefixSet = Arrays.stream(allowedPrefixes)
                .map(prefix -> prefix.getPrefix().toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        // Split into tokens and check each one
        String[] tokens = args.split("\\s+");
        for (String token : tokens) {
            int slashIndex = token.indexOf('/');
            if (slashIndex != -1) {
                String prefix = token.substring(0, slashIndex + 1).toLowerCase(Locale.ROOT);
                if (!allowedPrefixSet.contains(prefix)) {
                    return Optional.of(token);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Validates that all given prefix values are empty (no text after the prefix).
     * This is used for commands like cleartag where only the prefix should be provided
     * without any values.
     *
     * @param argMultimap The ArgumentMultimap containing the tokenized arguments
     * @param prefixes    The prefixes to check for empty values
     * @return Optional containing the first prefix that has a non-empty value, if any
     */
    public static Optional<String> validateEmptyPrefixValues(ArgumentMultimap argMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            String value = argMultimap.getValue(prefix).orElse("");
            if (!value.isEmpty()) {
                // found a prefix with a non-empty value
                return Optional.of(prefix.getPrefix() + value);
            }
        }
        return Optional.empty();
    }
}

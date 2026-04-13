package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_NO_INPUT;
import static seedu.address.logic.Messages.MESSAGE_PREAMBLE_NOT_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
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

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(".*[a-zA-Z0-9].*");

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
     * Parses all type of tags from the ArgumentMultimap and returns them as a Set.
     *
     * @param argMultimap the ArgumentMultimap containing the tokenized arguments.
     * @return a Set of parsed Tags.
     * @throws ParseException if any tag fails validation.
     */
    public static Set<Tag> parseAllTypeOfTags(ArgumentMultimap argMultimap) throws ParseException {
        Set<Tag> tagList = new HashSet<>();

        tagList.addAll(parseTags(argMultimap.getAllValues(PREFIX_ROLE_TAG), TagType.ROLE));
        tagList.addAll(parseTags(argMultimap.getAllValues(PREFIX_COURSE_TAG), TagType.COURSE));
        tagList.addAll(parseTags(argMultimap.getAllValues(PREFIX_GENERAL_TAG), TagType.GENERAL));

        return tagList;
    }

    //================================= Input Validation ==============================================/

    /**
     * Returns a prefix from the given arguments that has an empty string as one of its values, if any.
     *
     * <p>This method checks the provided {@code prefixes} in order. For each prefix,
     * it looks up all values in the given {@link ArgumentMultimap}. If any value for that prefix
     * is empty (after trimming whitespace), that prefix is returned wrapped in an {@link Optional}.
     * If none of the provided prefixes have empty values, an empty {@code Optional} is returned.
     *
     * @param argMultimap the {@link ArgumentMultimap} containing the mapping of prefixes to values
     * @param prefixes    the prefixes to check for empty values
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
     * Validates that no prefix has an empty value.
     *
     * @param argMultimap the ArgumentMultimap containing the tokenized arguments
     * @param prefixes    the prefixes to check for empty values
     * @throws ParseException if an empty prefix value is found
     */
    public static void validateNoEmptyPrefixValues(
            ArgumentMultimap argMultimap, Prefix... prefixes
    ) throws ParseException {
        Optional<String> emptyPrefix = findEmptyPrefixValues(argMultimap, prefixes);
        if (emptyPrefix.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, emptyPrefix.get()));
        }
    }

    /**
     * Returns the first prefixed token that is NOT in the allowed prefixes list, if any.
     *
     * @param args            the raw input arguments string
     * @param allowedPrefixes the prefixes that are allowed in the command
     * @return Optional containing the first invalid prefixed token, if any
     */
    public static Optional<String> findInvalidPrefixInput(String args, Prefix... allowedPrefixes) {
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
     * Validates that no invalid prefixes are present in the input.
     *
     * @param args            the raw input arguments string
     * @param allowedPrefixes the prefixes that are allowed in the command
     * @throws ParseException if an invalid prefix is found
     */
    public static void validateNoInvalidPrefixInputs(String args, Prefix... allowedPrefixes) throws ParseException {
        Optional<String> invalidPrefix = findInvalidPrefixInput(args, allowedPrefixes);
        if (invalidPrefix.isPresent()) {
            throw new ParseException(String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, invalidPrefix.get()));
        }
    }

    /**
     * Returns the first prefix that has a non-empty value, if any.
     *
     * <p>This method is used for commands where prefixes should appear without any following
     * values, such as the {@code cleartag} command. For example:</p>
     * <ul>
     *     <li>{@code cleartag 1 tg/} - valid, as {@code tg/} has no value</li>
     *     <li>{@code cleartag 1 tg/friends} - invalid, as {@code tg/} has the value "friends"</li>
     * </ul>
     *
     * @param argMultimap the ArgumentMultimap containing the tokenized arguments
     * @param prefixes    the prefixes to check for empty values
     * @return Optional containing the first prefix that has a non-empty value, if any
     */
    public static Optional<String> findNoValuesAfterPrefix(ArgumentMultimap argMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            String value = argMultimap.getValue(prefix).orElse("");
            if (!value.isEmpty()) {
                // found a prefix with a non-empty value
                return Optional.of(prefix.getPrefix() + value);
            }
        }
        return Optional.empty();
    }

    /**
     * Validates that all prefixes have empty values (no text after the prefix).
     *
     * @param argMultimap the ArgumentMultimap containing the tokenized arguments
     * @param prefixes    the prefixes to check for non-empty values
     * @throws ParseException if a prefix has a non-empty value
     */
    public static void validateNoValuesAfterPrefix(
            ArgumentMultimap argMultimap, Prefix... prefixes
    ) throws ParseException {
        Optional<String> nonEmptyPrefix = findNoValuesAfterPrefix(argMultimap, prefixes);
        if (nonEmptyPrefix.isPresent()) {
            throw new ParseException(String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, nonEmptyPrefix.get()));
        }
    }

    /**
     * Validates that the preamble of the given {@code ArgumentMultimap} is empty,
     * i.e., that there is no unexpected text before the first valid prefix.
     *
     * @param argMultimap the ArgumentMultimap containing the tokenized arguments
     * @param usageMessage the command usage message to include in the exception
     * @throws ParseException if the preamble is not empty
     */
    public static void validateEmptyPreamble(
            ArgumentMultimap argMultimap, String usageMessage) throws ParseException {

        if (!argMultimap.getPreamble().isBlank()) {
            throw new ParseException(
                    String.format(MESSAGE_PREAMBLE_NOT_EMPTY, argMultimap.getPreamble(), usageMessage));
        }
    }

    /**
     * Validates that the given token contains at least one alphanumeric character.
     * <p>
     * A token consisting only of special characters is considered invalid and
     * will cause a {@code ParseException} to be thrown.
     *
     * @param prefix the {@code Prefix} associated with the token (used in the error message)
     * @param token the string token to validate
     * @throws ParseException if the token contains only non-alphanumeric characters
     */
    public static void validateKeywordContainsAlphanumeric(Prefix prefix, String token)
            throws ParseException {
        // If the token does not contain any alphanumeric characters, it is invalid
        if (!ALPHANUMERIC_PATTERN.matcher(token).matches()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS,
                    prefix.getPrefix(),
                    token));
        }
    }

    /**
     * Validates that the preamble of the given {@code ArgumentMultimap} contains
     * exactly one non-empty token suitable for parsing as an index.
     *
     * @param argMultimap the ArgumentMultimap containing the tokenized arguments
     * @param usageMessage the command usage message to include in the exception
     * @throws ParseException if the preamble is empty or contains more than one token
     */
    public static void validatePreambleAsIndex(ArgumentMultimap argMultimap, String usageMessage)
            throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty() || preamble.contains(" ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }
    }
}

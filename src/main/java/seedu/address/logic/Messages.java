package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_SORT_ORDER =
                "Invalid order value. Supported values: %1$s";
    public static final String MESSAGE_INVALID_REVERSE_FLAG =
                "The r/ flag does not accept a value. Use r/ alone to reverse sort order.";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_EMAIL = "The person email provided is invalid";
    public static final String MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT =
                "Invalid command format! \nThe prefix should not be followed by any values. \n%1$s";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName());
        if (person.getPhone() != null) {
            builder.append("; Phone: ").append(person.getPhone());
        }
        builder.append("; Email: ").append(person.getEmail());
        if (person.getTelegramHandle() != null) {
            builder.append("; Telegram: ").append(person.getTelegramHandle());
        }
        builder.append("; Tags: ");

        String tags = person.getTags().stream()
                .map(Tag::toString)
                .collect(Collectors.joining(", ", "[", "]"));

        builder.append(tags);

        return builder.toString();
    }

}

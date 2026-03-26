package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        if (person.getTelegramHandle() != null) {
            sb.append(PREFIX_TELEGRAM_HANDLE).append(person.getTelegramHandle().value).append(" ");
        }
        person.getTags().stream().forEach(tag -> {
            switch (tag.getType()) {
            case ROLE:
                sb.append(PREFIX_ROLE_TAG).append(tag.tagName).append(" ");
                break;
            case COURSE:
                sb.append(PREFIX_COURSE_TAG).append(tag.tagName).append(" ");
                break;
            case GENERAL:
                sb.append(PREFIX_GENERAL_TAG).append(tag.tagName).append(" ");
                break;
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tag.getType());
            }
        });
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getTelegramHandle().ifPresent(telegramHandle -> sb.append(PREFIX_TELEGRAM_HANDLE)
                .append(telegramHandle.value).append(" "));
        // Handle role tags
        if (descriptor.getRoleTags().isPresent()) {
            Set<Tag> tags = descriptor.getRoleTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_ROLE_TAG).append(" ");;
            } else {
                tags.forEach(s -> sb.append(PREFIX_ROLE_TAG).append(s.tagName).append(" "));
            }
        }

        // Handle course tags
        if (descriptor.getCourseTags().isPresent()) {
            Set<Tag> tags = descriptor.getCourseTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_COURSE_TAG).append(" ");;
            } else {
                tags.forEach(s -> sb.append(PREFIX_COURSE_TAG).append(s.tagName).append(" "));
            }
        }

        // Handle general tags
        if (descriptor.getGeneralTags().isPresent()) {
            Set<Tag> tags = descriptor.getGeneralTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_GENERAL_TAG).append(" ");;
            } else {
                tags.forEach(s -> sb.append(PREFIX_GENERAL_TAG).append(s.tagName).append(" "));
            }
        }

        return sb.toString();
    }
}

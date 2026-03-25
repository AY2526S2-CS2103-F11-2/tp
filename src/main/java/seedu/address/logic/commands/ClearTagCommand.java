package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Clear all tags of the specified type from an existing person in the address book.
 */
public class ClearTagCommand extends Command {

    public static final String COMMAND_WORD = "cleartag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Clears all tags of the specified type from the person identified by the index number "
            + "used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ROLE_TAG + "] or "
            + "[" + PREFIX_COURSE_TAG + "] or "
            + "[" + PREFIX_GENERAL_TAG + "]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ROLE_TAG;

    public static final String MESSAGE_SUCCESS = "All %1$s tags removed: %2$s";
    public static final String MESSAGE_NO_TAGS_FOUND = "No %1$s tags found to clear";

    private final Index index;
    private final TagType typeToClear;

    /**
     * @param index       of the person in the filtered person list to clear tags.
     * @param typeToClear the type of tags to clear
     */
    public ClearTagCommand(Index index, TagType typeToClear) {
        requireNonNull(index);
        requireNonNull(typeToClear);

        this.index = index;
        this.typeToClear = typeToClear;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToClearTag = lastShownList.get(index.getZeroBased());

        // collect all tags of the type to remove
        Set<Tag> removedTags = personToClearTag.getTags().stream()
                .filter(tag -> tag.getType() == typeToClear)
                .collect(Collectors.toSet());

        if (removedTags.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_TAGS_FOUND, typeToClear));
        }

        Set<Tag> updatedTags = personToClearTag.getTags().stream()
                .filter(tag -> tag.getType() != typeToClear)
                .collect(Collectors.toSet());

        Person editedPerson = personToClearTag.withTags(updatedTags);

        model.setPerson(personToClearTag, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, typeToClear, removedTags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClearTagCommand)) {
            return false;
        }

        ClearTagCommand otherClearTagCommand = (ClearTagCommand) other;
        return index.equals(otherClearTagCommand.index)
                && typeToClear.equals(otherClearTagCommand.typeToClear);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("typeToClear", typeToClear)
                .toString();
    }
}

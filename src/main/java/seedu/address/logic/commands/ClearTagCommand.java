package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
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
            + "Exactly one tag type prefix must be provided.\n"
            + "Parameters: INDEX " + PREFIX_ROLE_TAG + " or "
            + "INDEX " + PREFIX_COURSE_TAG + " or "
            + "INDEX " + PREFIX_GENERAL_TAG + "\n"
            + "Note: INDEX (must be a positive integer).\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ROLE_TAG;

    public static final String MESSAGE_SUCCESS = "All %1$s tags cleared: %2$s";
    public static final String MESSAGE_NO_TAGS_FOUND = "No %1$s tags found to clear.";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo clear %1$s tags for: %2$s";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo clear tag before command execution.";

    private static final Logger logger = LogsCenter.getLogger(ClearTagCommand.class);

    private final Index index;
    private final TagType typeToClear;
    private Person originalPerson;
    private Person updatedPerson;

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
            throw new CommandException(
                    String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, index.getOneBased())
            );
        }

        Person personToClearTag = lastShownList.get(index.getZeroBased());
        originalPerson = personToClearTag;

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
        updatedPerson = editedPerson;

        model.setPerson(personToClearTag, editedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, typeToClear, removedTags));
    }

    /**
     * @return {@code true} since clearing tags can be undone by restoring
     *     the person's original tags.
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Restores the person to their original state before the specified tags were cleared.
     *
     * @param model The model containing the current state of the address book.
     * @return A {@code CommandResult} indicating the result of the undo operation.
     * @throws CommandException If the original or updated person state was not recorded,
     *                          meaning the clear tag operation cannot be undone.
     */
    @Override
    public CommandResult undo(Model model) throws CommandException {
        return undoPersonChange(model, originalPerson, updatedPerson,
                MESSAGE_UNDO_FAILURE, logger,
                this::getUndoLogMessage,
                this::getUndoResult);
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

    private String getUndoLogMessage() {
        return "Undid " + COMMAND_WORD + ": " + typeToClear + " for " + originalPerson.getName();
    }

    private CommandResult getUndoResult() {
        return createUndoResultWithFilterNote(
                String.format(MESSAGE_UNDO_SUCCESS, typeToClear, Messages.format(originalPerson)),
                MESSAGE_RESTORED_CONTACT_FILTER_NOTE);
    }
}

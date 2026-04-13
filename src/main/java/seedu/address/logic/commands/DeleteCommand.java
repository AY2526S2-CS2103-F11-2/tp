package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using their displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer).\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo delete person: %1$s";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo delete because the person already exists.";

    private final Index targetIndex;
    private Person deletedPerson;
    private int deletedPersonIndex = -1;

    /**
     * Creates a DeleteCommand using index
     */
    public DeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, targetIndex.getOneBased())
            );
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        deletedPersonIndex = model.getAddressBook().getPersonList().indexOf(personToDelete);
        model.deletePerson(personToDelete);
        deletedPerson = personToDelete;
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    /**
     * @return {@code true} since deleting a person can be undone by restoring
     *      the deleted person to the address book.
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Restores the deleted person to the address book, inserting them back
     * at their original position if possible.
     *
     * @param model The model containing the current state of the address book.
     * @return A {@code CommandResult} indicating the result of the undo operation.
     * @throws CommandException If the delete operation was not previously executed,
     *                          or if the person already exists in the model.
     */
    @Override
    public CommandResult undo(Model model) throws CommandException {
        requireNonNull(model);
        validateUndoable(model);
        restoreDeletedPerson(model);
        return createUndoPersonResultWithFilterNote(MESSAGE_UNDO_SUCCESS, deletedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return Objects.equals(targetIndex, otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    /**
     * Verifies that this command can be undone.
     *
     * @param model The model containing the current state of the address book.
     * @throws CommandException If the command has not been executed before,
     *                          or if the deleted person already exists in the model.
     */
    private void validateUndoable(Model model) throws CommandException {
        if (deletedPerson == null) {
            throw new CommandException("Cannot undo delete before command execution.");
        }
        if (model.hasPerson(deletedPerson)) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }
    }

    /**
     * Restores the previously deleted person to the address book.
     * Inserts the person back at their original index when it is still valid;
     * otherwise appends them to the end of the list.
     *
     * @param model The model containing the current state of the address book.
     */
    private void restoreDeletedPerson(Model model) {
        if (deletedPersonIndex < 0 || deletedPersonIndex > model.getAddressBook().getPersonList().size()) {
            model.addPerson(deletedPerson);
            return;
        }
        model.addPerson(deletedPersonIndex, deletedPerson);
    }
}

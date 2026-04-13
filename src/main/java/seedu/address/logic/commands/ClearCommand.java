package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_NO_PARAMETER = "Clear command does not take in any parameter.";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo clear address book.";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo clear before command execution.";

    private AddressBook previousAddressBook;


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        previousAddressBook = new AddressBook(model.getAddressBook());
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * @return {@code true} since a clear operation can be undone by restoring
     *     the previous state of the address book.
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Restores the address book to its state before the clear command was executed.
     *
     * @param model The model containing the current state of the address book.
     * @return A {@code CommandResult} indicating the result of the undo operation.
     * @throws CommandException If there is no previously saved address book state,
     *                          meaning the clear command cannot be undone.
     */
    @Override
    public CommandResult undo(Model model) throws CommandException {
        requireNonNull(model);
        if (previousAddressBook == null) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }
        model.setAddressBook(previousAddressBook);
        return createUndoResultWithFilterNote(MESSAGE_UNDO_SUCCESS, MESSAGE_RESTORED_CONTACTS_FILTER_NOTE);
    }
}

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
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public CommandResult undo(Model model) throws CommandException {
        requireNonNull(model);
        if (previousAddressBook == null) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }
        model.setAddressBook(previousAddressBook);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_UNDO_SUCCESS);
    }
}

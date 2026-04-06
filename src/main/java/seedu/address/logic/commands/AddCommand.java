package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.DuplicateConflict;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a person to the address book. \n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_TELEGRAM_HANDLE + "TELEGRAM_HANDLE].\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_TELEGRAM_HANDLE + "johndoe123";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_EMAIL = Messages.MESSAGE_DUPLICATE_EMAIL;
    public static final String MESSAGE_DUPLICATE_TELEGRAM_HANDLE = Messages.MESSAGE_DUPLICATE_TELEGRAM_HANDLE;
    public static final String MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE =
            Messages.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE;
    public static final String MESSAGE_UNDO_SUCCESS = "Undo add person: %1$s";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo add because the person no longer exists.";
    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        DuplicateConflict duplicateConflict = model.getDuplicateConflict(toAdd);

        String duplicateMessage = Messages.getDuplicateConflictMessage(duplicateConflict);
        if (duplicateMessage != null) {
            throw new CommandException(duplicateMessage);
        }

        model.addPerson(toAdd);

        String resultMessage = String.format(MESSAGE_SUCCESS, Messages.format(toAdd));
        if (!toAdd.getEmail().isNusDomain()) {
            resultMessage += "\n" + Messages.MESSAGE_NON_NUS_EMAIL;
        }
        return new CommandResult(resultMessage);
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public CommandResult undo(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }
        model.deletePerson(toAdd);
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}

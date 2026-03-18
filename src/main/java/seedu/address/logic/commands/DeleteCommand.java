package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number or email used in the displayed person list.\n"
            + "Parameters: " + PREFIX_INDEX + "INDEX (must be a positive integer) or " + PREFIX_EMAIL + "EMAIL.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_INDEX + "1, "
            + COMMAND_WORD + " " + PREFIX_EMAIL + "johnd@exampl.com";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;
    private final Email targetEmail;

    /**
     * Creates a DeleteCommand using index
     */
    public DeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.targetEmail = null;
    }

    /**
     * Creates a DeleteCommand using email
     */
    public DeleteCommand(Email email) {
        requireNonNull(email);
        this.targetEmail = email;
        this.targetIndex = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToDelete;

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            personToDelete = lastShownList.get(targetIndex.getZeroBased());
        } else {
            personToDelete = findPersonByEmail(lastShownList);
        }

        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
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
        return Objects.equals(targetIndex, otherDeleteCommand.targetIndex)
                && Objects.equals(targetEmail, otherDeleteCommand.targetEmail);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetEmail", targetEmail)
                .toString();
    }

    private Person findPersonByEmail(List<Person> personList) throws CommandException {
        return personList.stream()
                .filter(person -> person.getEmail().equals(targetEmail))
                .findFirst()
                .orElseThrow(() -> new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_EMAIL));
    }
}

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.DuplicateConflict;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;

/**
 * Edits the details of an existing person in the address book.
 * <p>
 * Identifies the target person by their displayed index, then overwrites the specified fields
 * with the values provided in an {@link EditPersonDescriptor}. This command is undoable.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_TELEGRAM_HANDLE + "TELEGRAM_HANDLE].\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "john doe "
            + PREFIX_EMAIL + "johndoe@example.com "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_TELEGRAM_HANDLE + "johndoe123";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo edit before command execution.";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo edit person: %1$s";

    private static final Logger logger = LogsCenter.getLogger(EditCommand.class);
    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;
    private Person originalPerson;
    private Person updatedPerson;

    /**
     * Creates an {@code EditCommand} to edit the person at the specified index.
     *
     * @param index of the person in the filtered person list to edit.
     * @param editPersonDescriptor details to edit the person with.
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Replaces the target person's fields with those specified in the {@link EditPersonDescriptor},
     * provided the edit does not introduce duplicate emails or telegram handles.
     *
     * @throws CommandException if the index is out of range or the edit would create a duplicate.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.fine("Executing edit command for index: " + index.getOneBased());
        Person personToEdit = getPersonToEdit(model);

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        assert editedPerson != null : "Edited person should not be null";

        throwIfDuplicate(model, personToEdit, editedPerson);

        model.setPerson(personToEdit, editedPerson);
        originalPerson = personToEdit;
        updatedPerson = editedPerson;
        logger.info("Edited person: " + personToEdit.getName() + " -> " + editedPerson.getName());

        String resultMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
        if (editPersonDescriptor.getEmail().isPresent() && !editedPerson.getEmail().isNusDomain()) {
            resultMessage += "\n" + Messages.MESSAGE_NON_NUS_EMAIL;
        }

        return new CommandResult(resultMessage);
    }

    /**
     * Returns the person at the stored index from the model's filtered person list.
     *
     * @param model the model to retrieve the person from.
     * @return the person at the specified index.
     * @throws CommandException if the index is out of range.
     */
    private Person getPersonToEdit(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered person list should not be null";

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, index.getOneBased()));
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        assert personToEdit != null : "Person from filtered list should not be null";
        return personToEdit;
    }

    /**
     * @return {@code true} since editing a person can be undone by restoring
     *      the person's original details.
     */
    @Override
    public boolean isUndoable() {
        return true;
    }

    /**
     * Restores the person to their original state before the edit.
     *
     * @param model The model containing the current state of the address book.
     * @return A {@code CommandResult} indicating the result of the undo operation.
     * @throws CommandException If the edit operation was not previously executed,
     *                          or if restoring the original person would create a duplicate.
     */
    @Override
    public CommandResult undo(Model model) throws CommandException {
        if (originalPerson == null || updatedPerson == null) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }

        throwIfDuplicate(model, updatedPerson, originalPerson);
        return undoPersonChange(model, originalPerson, updatedPerson,
                MESSAGE_UNDO_FAILURE, logger,
                this::getUndoLogMessage,
                this::getUndoResult);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     * Fields not present in the descriptor retain their original values.
     *
     * @param personToEdit the person whose details are to be used as defaults.
     * @param editPersonDescriptor the descriptor containing the new field values.
     * @return a new {@code Person} with the updated details.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;
        assert editPersonDescriptor != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        TelegramHandle updatedTelegramHandle = editPersonDescriptor.getTelegramHandle()
                .orElse(personToEdit.getTelegramHandle());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedTelegramHandle, personToEdit.getTags());
    }

    /**
     * Throws a {@link CommandException} if setting {@code target} in the model (while excluding
     * {@code excluded}) would create a duplicate email or telegram handle.
     *
     * @param model the model to check against.
     * @param excluded the person to exclude from the duplicate check.
     * @param target the person to check for duplicates.
     * @throws CommandException if a duplicate conflict is found.
     */
    private void throwIfDuplicate(Model model, Person excluded, Person target) throws CommandException {
        DuplicateConflict conflict = model.getDuplicateConflictExcluding(excluded, target);
        String message = Messages.getDuplicateConflictMessage(conflict);
        if (message != null) {
            logger.warning("Duplicate conflict detected for: " + target.getName() + " - " + message);
            throw new CommandException(message);
        }
    }

    private String getUndoLogMessage() {
        return "Undid " + COMMAND_WORD + ": " + updatedPerson.getName() + " -> " + originalPerson.getName();
    }

    private CommandResult getUndoResult() {
        return createUndoPersonResultWithFilterNote(MESSAGE_UNDO_SUCCESS, originalPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with.
     * Each non-empty field value will replace the corresponding field value of the person.
     * Fields left as {@code null} indicate that the corresponding value should not be changed.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private TelegramHandle telegramHandle;

        public EditPersonDescriptor() {}

        /**
         * Creates a copy of the given {@code EditPersonDescriptor}.
         *
         * @param toCopy the descriptor to copy.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTelegramHandle(toCopy.telegramHandle);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, telegramHandle);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setTelegramHandle(TelegramHandle telegramHandle) {
            this.telegramHandle = telegramHandle;
        }

        public Optional<TelegramHandle> getTelegramHandle() {
            return Optional.ofNullable(telegramHandle);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(telegramHandle, otherEditPersonDescriptor.telegramHandle);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("telegramHandle", telegramHandle)
                    .toString();
        }
    }
}

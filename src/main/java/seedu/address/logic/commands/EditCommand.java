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
    public static final String MESSAGE_DUPLICATE_EMAIL = Messages.MESSAGE_DUPLICATE_EMAIL;
    public static final String MESSAGE_DUPLICATE_TELEGRAM_HANDLE = Messages.MESSAGE_DUPLICATE_TELEGRAM_HANDLE;
    public static final String MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE =
            Messages.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE;
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_UNDO_FAILURE = "Cannot undo edit before command execution.";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo edit person: %1$s";

    private static final Logger logger = LogsCenter.getLogger(EditCommand.class);
    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;
    private Person originalPerson;
    private Person updatedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        int oneBasedIndex = index.getOneBased();
        int lastShownListSizeOneBased = lastShownList.size() + 1;

        if (oneBasedIndex >= lastShownListSizeOneBased) {
            throw new CommandException(String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, oneBasedIndex));
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        DuplicateConflict duplicateConflict = model.getDuplicateConflictExcluding(personToEdit, editedPerson);

        String duplicateMessage = Messages.getDuplicateConflictMessage(duplicateConflict);
        if (duplicateMessage != null) {
            throw new CommandException(duplicateMessage);
        }

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

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public CommandResult undo(Model model) throws CommandException {
        requireNonNull(model);
        if (originalPerson == null || updatedPerson == null) {
            throw new CommandException(MESSAGE_UNDO_FAILURE);
        }

        DuplicateConflict duplicateConflict = model.getDuplicateConflictExcluding(updatedPerson, originalPerson);

        String duplicateMessage = Messages.getDuplicateConflictMessage(duplicateConflict);
        if (duplicateMessage != null) {
            throw new CommandException(duplicateMessage);
        }

        model.setPerson(updatedPerson, originalPerson);

        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, Messages.format(originalPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;
        assert editPersonDescriptor != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        TelegramHandle telegramHandle = editPersonDescriptor.getTelegramHandle()
                .orElse(personToEdit.getTelegramHandle());

        return new Person(updatedName, updatedPhone, updatedEmail, telegramHandle, personToEdit.getTags());
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
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private TelegramHandle telegramHandle;
        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
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

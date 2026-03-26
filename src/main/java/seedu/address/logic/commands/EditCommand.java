package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

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
            + "[" + PREFIX_TELEGRAM_HANDLE + "TELEGRAM HANDLE] "
            + "[" + PREFIX_ROLE_TAG + "ROLE_TAG]... "
            + "[" + PREFIX_COURSE_TAG + "COURSE_TAG]... "
            + "[" + PREFIX_GENERAL_TAG + "GENERAL_TAG]...\n"
            + "(Use t/ alone to clear all tags)\n" + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_EMAIL + "johndoe@example.com "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_TELEGRAM_HANDLE + "johndoe123 "
            + PREFIX_ROLE_TAG + "Teammate"
            + PREFIX_COURSE_TAG + "CS2103"
            + PREFIX_GENERAL_TAG + "Friendly";


    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
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

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        try {
            model.setPerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        originalPerson = personToEdit;
        updatedPerson = editedPerson;

        logger.info("Edited person: " + personToEdit.getName() + " -> " + editedPerson.getName());

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String resultMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
        if (!editedPerson.getEmail().isNusDomain()) {
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

        try {
            model.setPerson(updatedPerson, originalPerson);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
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

        Set<Tag> existingTags = personToEdit.getTags();
        Set<Tag> existingRoleTags = Tag.filterByType(existingTags, TagType.ROLE);
        Set<Tag> existingCourseTags = Tag.filterByType(existingTags, TagType.COURSE);
        Set<Tag> existingGeneralTags = Tag.filterByType(existingTags, TagType.GENERAL);

        Set<Tag> updatedRoleTags = editPersonDescriptor.getRoleTags().orElse(existingRoleTags);
        Set<Tag> updatedCourseTags = editPersonDescriptor.getCourseTags().orElse(existingCourseTags);
        Set<Tag> updatedGeneralTags = editPersonDescriptor.getGeneralTags().orElse(existingGeneralTags);

        Set<Tag> updatedTags = new HashSet<>();
        updatedTags.addAll(updatedRoleTags);
        updatedTags.addAll(updatedCourseTags);
        updatedTags.addAll(updatedGeneralTags);

        return new Person(updatedName, updatedPhone, updatedEmail, telegramHandle, updatedTags);
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
        private Set<Tag> roleTags;
        private Set<Tag> courseTags;
        private Set<Tag> generalTags;
        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTelegramHandle(toCopy.telegramHandle);
            setRoleTags(toCopy.roleTags);
            setCourseTags(toCopy.courseTags);
            setGeneralTags(toCopy.generalTags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, telegramHandle, roleTags, courseTags, generalTags);
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

        public void setRoleTags(Set<Tag> roleTags) {
            this.roleTags = (roleTags != null) ? new HashSet<>(roleTags) : null;
        }

        public Optional<Set<Tag>> getRoleTags() {
            return (roleTags != null) ? Optional.of(Collections.unmodifiableSet(roleTags)) : Optional.empty();
        }

        public void setCourseTags(Set<Tag> courseTags) {
            this.courseTags = (courseTags != null) ? new HashSet<>(courseTags) : null;
        }

        public Optional<Set<Tag>> getCourseTags() {
            return (courseTags != null) ? Optional.of(Collections.unmodifiableSet(courseTags)) : Optional.empty();
        }

        public void setGeneralTags(Set<Tag> generalTags) {
            this.generalTags = (generalTags != null) ? new HashSet<>(generalTags) : null;
        }

        public Optional<Set<Tag>> getGeneralTags() {
            return (generalTags != null) ? Optional.of(Collections.unmodifiableSet(generalTags)) : Optional.empty();
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
                    && Objects.equals(telegramHandle, otherEditPersonDescriptor.telegramHandle)
                    && Objects.equals(roleTags, otherEditPersonDescriptor.roleTags)
                    && Objects.equals(courseTags, otherEditPersonDescriptor.courseTags)
                    && Objects.equals(generalTags, otherEditPersonDescriptor.generalTags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("telegramHandle", telegramHandle)
                    .add("roleTags", roleTags)
                    .add("courseTags", courseTags)
                    .add("generalTags", generalTags)
                    .toString();
        }

    }
}

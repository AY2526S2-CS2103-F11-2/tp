package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY_NO_TAGS;
import static seedu.address.testutil.TypicalPersons.HOON;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.DuplicateConflict;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson))
                + "\n" + Messages.MESSAGE_NON_NUS_EMAIL;
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicateEmail_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_EMAIL, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateTelegramHandle_throwsCommandException() {
        Person existingPerson = new PersonBuilder().withTelegramHandle("alice123").build();
        Person personWithSameTelegramHandle = new PersonBuilder(existingPerson)
                .withEmail("different@example.com")
                .build();

        AddCommand addCommand = new AddCommand(personWithSameTelegramHandle);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_TELEGRAM_HANDLE, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateTelegramHandleDifferentCase_throwsCommandException() {
        Person existingPerson = new PersonBuilder().withTelegramHandle("test1").build();
        Person personWithSameTelegramHandle = new PersonBuilder(existingPerson)
                .withEmail("different@example.com")
                .withTelegramHandle("TEST1")
                .build();

        AddCommand addCommand = new AddCommand(personWithSameTelegramHandle);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_TELEGRAM_HANDLE, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateEmailAndTelegramHandle_throwsCommandException() {
        Person existingPerson = new PersonBuilder().withTelegramHandle("alice123").build();
        Person duplicatePerson = new PersonBuilder(existingPerson).build();

        AddCommand addCommand = new AddCommand(duplicatePerson);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person amy = new PersonBuilder().withName("Amy").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAmyCommand = new AddCommand(amy);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAmyCommand.equals(addAmyCommand));

        // same values -> returns true
        AddCommand addAmyCommandCopy = new AddCommand(amy);
        assertTrue(addAmyCommand.equals(addAmyCommandCopy));

        // different types -> returns false
        assertFalse(addAmyCommand.equals(1));

        // null -> returns false
        assertFalse(addAmyCommand.equals(null));

        // different command -> returns false
        assertFalse(addAmyCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(AMY_NO_TAGS);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + AMY_NO_TAGS + "}";
        assertEquals(expected, addCommand.toString());
    }

    @Test
    public void execute_nusStaffEmail_addsWithoutWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person personWithNusEmail = new PersonBuilder().withEmail("john@nus.edu.sg").build();

        CommandResult commandResult = new AddCommand(personWithNusEmail).execute(modelStub);

        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(personWithNusEmail));
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
    }

    public void undo_afterExecute_removesPerson() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Model expectedBefore = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person toAdd = new PersonBuilder(HOON).build();
        AddCommand addCommand = new AddCommand(toAdd);

        Model expectedAfterAdd = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterAdd.addPerson(toAdd);

        assertCommandSuccess(addCommand, model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(toAdd)),
                expectedAfterAdd);

        assertUndoSuccess(addCommand, model,
                String.format(AddCommand.MESSAGE_UNDO_SUCCESS, Messages.format(toAdd)),
                expectedBefore);
    }


    @Test
    public void undo_personNoLongerInModel_throwsCommandException() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Person toAdd = new PersonBuilder(HOON).withEmail("stefan@u.nus.edu").build();
        AddCommand addCommand = new AddCommand(toAdd);

        Model expectedAfterAdd = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterAdd.addPerson(toAdd);
        assertCommandSuccess(addCommand, model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(toAdd)),
                expectedAfterAdd);

        model.deletePerson(toAdd);
        assertUndoFailure(addCommand, model, AddCommand.MESSAGE_UNDO_FAILURE);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(int index, Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public DuplicateConflict getDuplicateConflict(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public DuplicateConflict getDuplicateConflictExcluding(Person target, Person person) {
            throw new AssertionError("This method should not be called.");
        }
    }


    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }

        @Override
        public DuplicateConflict getDuplicateConflict(Person person) {
            requireNonNull(person);

            boolean hasDuplicateEmail = this.person.hasSameEmail(person);
            boolean hasDuplicateTelegramHandle = this.person.hasSameTelegramHandle(person);

            if (hasDuplicateEmail && hasDuplicateTelegramHandle) {
                return DuplicateConflict.EMAIL_AND_TELEGRAM_HANDLE;
            }
            if (hasDuplicateEmail) {
                return DuplicateConflict.EMAIL;
            }
            if (hasDuplicateTelegramHandle) {
                return DuplicateConflict.TELEGRAM_HANDLE;
            }
            return DuplicateConflict.NONE;
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public DuplicateConflict getDuplicateConflict(Person person) {
            requireNonNull(person);

            boolean hasDuplicateEmail = personsAdded.stream()
                    .anyMatch(existingPerson -> existingPerson.hasSameEmail(person));
            boolean hasDuplicateTelegramHandle = personsAdded.stream()
                    .anyMatch(existingPerson -> existingPerson.hasSameTelegramHandle(person));

            if (hasDuplicateEmail && hasDuplicateTelegramHandle) {
                return DuplicateConflict.EMAIL_AND_TELEGRAM_HANDLE;
            }
            if (hasDuplicateEmail) {
                return DuplicateConflict.EMAIL;
            }
            if (hasDuplicateTelegramHandle) {
                return DuplicateConflict.TELEGRAM_HANDLE;
            }
            return DuplicateConflict.NONE;
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}

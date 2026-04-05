package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_EMAIL;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_TELEGRAM_HANDLE;
import static seedu.address.logic.Messages.MESSAGE_NON_NUS_EMAIL;
import static seedu.address.logic.Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // EP: all four fields (name, phone, email, telegram) provided
        Person editedPerson = new PersonBuilder()
                .withGeneralTags("friends")
                .withRoleTags("student")
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson))
                + "\n" + MESSAGE_NON_NUS_EMAIL;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        // EP: subset of fields (name and phone only)
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        // EP: no fields provided — person remains unchanged
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateEmailUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void execute_duplicateEmailFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void execute_nusStudentEmail_noWarning() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withEmail("john@u.nus.edu").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nusStaffEmail_noWarning() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withEmail("john@u.nus.edu").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTelegramHandleUnfilteredList_failure() {
        Person firstPersonWithTelegram = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withTelegramHandle("alice123")
                .build();
        Person secondPersonWithTelegram = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()))
                .withTelegramHandle("bob123")
                .build();

        model.setPerson(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()), firstPersonWithTelegram);
        model.setPerson(model.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()), secondPersonWithTelegram);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTelegramHandle("alice123")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
    }

    @Test
    public void execute_duplicateTelegramHandleDifferentCaseUnfilteredList_failure() {
        // EP: case-insensitive duplicate detection for telegram handle
        Person firstPersonWithTelegram = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withTelegramHandle("test1")
                .build();
        Person secondPersonWithTelegram = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()))
                .withTelegramHandle("other1")
                .build();

        model.setPerson(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()), firstPersonWithTelegram);
        model.setPerson(model.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()), secondPersonWithTelegram);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTelegramHandle("TEST1")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
    }

    @Test
    public void execute_duplicateEmailAndTelegramHandleUnfilteredList_failure() {
        Person firstPersonWithTelegram = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()))
                .withTelegramHandle("alice123")
                .build();

        model.setPerson(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()), firstPersonWithTelegram);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withEmail(firstPersonWithTelegram.getEmail().value)
                .withTelegramHandle("alice123")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        // BVA: index = list size + 1 (one beyond the last valid index)
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        int index = outOfBoundIndex.getOneBased();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                String.format(MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, index));
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        // BVA: index valid in full list but out of range in filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        int index = outOfBoundIndex.getOneBased();
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model,
                String.format(MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX, index));
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        assertTrue(standardCommand.equals(standardCommand));

        assertFalse(standardCommand.equals(null));

        assertFalse(standardCommand.equals(new ClearCommand()));

        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void undo_afterExecute_restoresOriginalPerson() {
        Model expectedOriginal = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedAlice = new PersonBuilder(alice).withName("Temporary Name").withEmail("alice@u.nus.edu").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName("Temporary Name").withEmail("alice@u.nus.edu").build());

        String executeMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedAlice));

        Model expectedAfterEdit = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedAfterEdit.setPerson(alice, editedAlice);

        assertCommandSuccess(editCommand, model, executeMessage, expectedAfterEdit);

        String undoMessage = String.format(EditCommand.MESSAGE_UNDO_SUCCESS, Messages.format(alice));
        assertUndoSuccess(editCommand, model, undoMessage, expectedOriginal);
    }

    @Test
    public void undo_beforeExecute_throwsCommandException() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());
        assertUndoFailure(editCommand, model, EditCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void undo_duplicatePerson_throwsCommandException() {
        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withEmail("temp@u.nus.edu").build());
        Person editedAlice = new PersonBuilder(alice).withEmail("temp@u.nus.edu").build();

        String executeMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedAlice));

        Model expectedAfterEdit = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedAfterEdit.setPerson(alice, editedAlice);

        assertCommandSuccess(editCommand, model, executeMessage, expectedAfterEdit);

        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.setPerson(benson, new PersonBuilder(benson).withEmail(alice.getEmail().value).build());

        assertUndoFailure(editCommand, model, MESSAGE_DUPLICATE_EMAIL);
    }

    @Test
    public void undo_duplicateTelegramHandle_throwsCommandException() {
        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person aliceWithTelegram = new PersonBuilder(alice).withTelegramHandle("alice123").build();
        model.setPerson(alice, aliceWithTelegram);

        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withTelegramHandle("temp123").build());
        Person editedAlice = new PersonBuilder(aliceWithTelegram).withTelegramHandle("temp123").build();

        String executeMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedAlice));

        Model expectedAfterEdit = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedAfterEdit.setPerson(aliceWithTelegram, editedAlice);

        assertCommandSuccess(editCommand, model, executeMessage, expectedAfterEdit);

        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.setPerson(benson, new PersonBuilder(benson).withTelegramHandle("alice123").build());

        assertUndoFailure(editCommand, model, MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
    }

    @Test
    public void undo_duplicateEmailAndTelegramHandle_throwsCommandException() {
        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person aliceWithTelegram = new PersonBuilder(alice).withTelegramHandle("alice123").build();
        model.setPerson(alice, aliceWithTelegram);

        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder()
                        .withEmail("temp@example.com")
                        .withTelegramHandle("temp123")
                        .build());
        Person editedAlice = new PersonBuilder(aliceWithTelegram)
                .withEmail("temp@example.com")
                .withTelegramHandle("temp123")
                .build();

        String executeMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedAlice)) + "\n" + MESSAGE_NON_NUS_EMAIL;

        Model expectedAfterEdit = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedAfterEdit.setPerson(aliceWithTelegram, editedAlice);

        assertCommandSuccess(editCommand, model, executeMessage, expectedAfterEdit);

        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.setPerson(benson, new PersonBuilder(benson)
                .withEmail(aliceWithTelegram.getEmail().value)
                .withTelegramHandle("alice123")
                .build());

        assertUndoFailure(editCommand, model, MESSAGE_DUPLICATE_EMAIL_AND_TELEGRAM_HANDLE);
    }

    @Test
    public void execute_nonNusEmail_showsWarning() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withEmail("john@gmail.com").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withEmail("john@gmail.com")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson))
                + "\n" + MESSAGE_NON_NUS_EMAIL;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleFieldName_success() {
        // EP: only name field edited
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withName(VALID_NAME_BOB).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_lastValidIndex_success() {
        // BVA: index = list size (last valid index, boundary)
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person editedPerson = new PersonBuilder(lastPerson).withName(VALID_NAME_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_firstValidIndex_success() {
        // BVA: index = 1 (first valid index, boundary)
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withName(VALID_NAME_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editToSameValues_success() {
        // EP: editing a person with their own existing values — no actual change
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(personToEdit.getName().fullName)
                .withPhone(personToEdit.getPhone().value)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(personToEdit));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editOwnEmail_success() {
        // EP: editing a person's email to their own email — should not trigger duplicate
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withEmail(personToEdit.getEmail().value).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(personToEdit));
        if (!personToEdit.getEmail().isNusDomain()) {
            expectedMessage += "\n" + MESSAGE_NON_NUS_EMAIL;
        }

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }
}

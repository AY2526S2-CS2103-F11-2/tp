package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validEmailFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getEmail());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidEmailFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        DeleteCommand deleteCommand = new DeleteCommand(new Email(VALID_EMAIL_BOB));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_EMAIL);
    }

    @Test
    public void equals() {
        // Index-based delete commands
        DeleteCommand deleteFirstIndex = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondIndex = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteFirstIndexCopy = new DeleteCommand(INDEX_FIRST_PERSON);

        // Email-based DeleteCommands
        Email emailAmy = new Email(VALID_EMAIL_AMY);
        Email emailBob = new Email(VALID_EMAIL_BOB);
        DeleteCommand deleteEmailAmy = new DeleteCommand(emailAmy);
        DeleteCommand deleteEmailAmyCopy = new DeleteCommand(emailAmy);
        DeleteCommand deleteEmailBob = new DeleteCommand(emailBob);

        // --- Index command tests ---
        // same object -> returns true
        assertTrue(deleteFirstIndex.equals(deleteFirstIndex));

        // same values -> returns true
        assertTrue(deleteFirstIndex.equals(deleteFirstIndexCopy));

        // different types -> returns false
        assertFalse(deleteFirstIndex.equals(1));

        // null -> returns false
        assertFalse(deleteFirstIndex.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstIndex.equals(deleteSecondIndex));

        // index vs email -> returns false
        assertFalse(deleteFirstIndex.equals(deleteEmailAmy));

        // --- Email command tests ---
        // same object -> returns true
        assertTrue(deleteEmailAmy.equals(deleteEmailAmy));

        // same email -> returns true
        assertTrue(deleteEmailAmy.equals(deleteEmailAmyCopy));

        // different email -> returns false
        assertFalse(deleteEmailAmy.equals(deleteEmailBob));

        // email vs null -> false
        assertFalse(deleteEmailAmy.equals(null));

        // email vs index -> false
        assertFalse(deleteEmailAmy.equals(deleteFirstIndex));
    }


    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);

        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", targetEmail=null}";

        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void toStringMethod_email() {
        Email email = new Email("test@example.com");
        DeleteCommand deleteCommand = new DeleteCommand(email);

        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=null, targetEmail=" + email + "}";

        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}

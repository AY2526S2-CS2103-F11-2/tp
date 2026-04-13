package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

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

        String expected = String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased());
        assertCommandFailure(deleteCommand, model, expected);
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
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        String expected = String.format(Messages.MESSAGE_PERSON_NOT_FOUND_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased());
        assertCommandFailure(deleteCommand, model, expected);
    }

    @Test
    public void equals() {
        // Index-based delete commands
        DeleteCommand deleteFirstIndex = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondIndex = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteFirstIndexCopy = new DeleteCommand(INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstIndex.equals(deleteFirstIndex));

        // same values -> returns true
        assertTrue(deleteFirstIndex.equals(deleteFirstIndexCopy));

        // different types -> returns false
        assertFalse(deleteFirstIndex.equals(1));

        // null -> returns false
        assertFalse(deleteFirstIndex.equals(null));

        // different index -> returns false
        assertFalse(deleteFirstIndex.equals(deleteSecondIndex));
    }


    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);

        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + "}";

        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void undo_afterExecute_restoresPerson() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedBefore = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String deleteMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedAfterDelete = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, deleteMessage, expectedAfterDelete);

        String undoMessage = String.format(DeleteCommand.MESSAGE_UNDO_SUCCESS, Messages.format(personToDelete));
        assertUndoSuccess(deleteCommand, model, undoMessage, expectedBefore);
    }

    @Test
    public void undo_beforeExecute_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        assertUndoFailure(deleteCommand, model, "Cannot undo delete before command execution.");
    }

    @Test
    public void undo_personAlreadyExists_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        Model expectedAfterDelete = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(personToDelete);
        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)),
                expectedAfterDelete);

        Person benson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(benson, new PersonBuilder(benson).withEmail(ALICE.getEmail().value).build());

        assertUndoFailure(deleteCommand, model, DeleteCommand.MESSAGE_UNDO_FAILURE);
    }

    @Test
    public void undo_deletedPersonIndexInvalid_restoresPersonAtEnd() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        Model expectedAfterDelete = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterDelete.deletePerson(personToDelete);
        assertCommandSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)),
                expectedAfterDelete);

        Field deletedPersonIndexField = DeleteCommand.class.getDeclaredField("deletedPersonIndex");
        deletedPersonIndexField.setAccessible(true);
        deletedPersonIndexField.set(deleteCommand, model.getAddressBook().getPersonList().size() + 1);

        Model expectedAfterUndo = new ModelManager(expectedAfterDelete.getAddressBook(), new UserPrefs());
        expectedAfterUndo.addPerson(personToDelete);
        assertUndoSuccess(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_UNDO_SUCCESS, Messages.format(personToDelete)),
                expectedAfterUndo);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}

package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertUndoSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void undo_afterExecute_restoresAddressBook() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedBeforeClear = new ModelManager(model.getAddressBook(), new UserPrefs());

        ClearCommand clearCommand = new ClearCommand();
        Model expectedAfterClear = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedAfterClear.setAddressBook(new AddressBook());

        assertCommandSuccess(clearCommand, model, ClearCommand.MESSAGE_SUCCESS, expectedAfterClear);
        assertUndoSuccess(clearCommand, model, ClearCommand.MESSAGE_UNDO_SUCCESS, expectedBeforeClear);
    }

    @Test
    public void undo_beforeExecute_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        ClearCommand clearCommand = new ClearCommand();
        assertUndoFailure(clearCommand, model, ClearCommand.MESSAGE_UNDO_FAILURE);
    }

}


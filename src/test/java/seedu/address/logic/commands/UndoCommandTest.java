package seedu.address.logic.commands;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.ModelManager;

public class UndoCommandTest {

    @Test
    public void execute_alwaysThrowsAssertionError() {
        UndoCommand undoCommand = new UndoCommand();
        assertThrows(AssertionError.class, "UndoCommand should be handled by LogicManager.", () ->
                undoCommand.execute(new ModelManager()));
    }
}

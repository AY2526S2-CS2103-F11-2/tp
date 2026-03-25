package seedu.address.logic.commands;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;

/**
 * Ensures commands that do not override {@link Command#isUndoable()} use the default undo behaviour from
 * {@link Command}.
 */
public class NonUndoableCommandUndoTest {

    @Test
    public void listCommand_undo_throwsCommandException() {
        ListCommand listCommand = new ListCommand();
        assertThrows(CommandException.class, "This command cannot be undone.", ()
                -> listCommand.undo(new ModelManager()));
    }
}

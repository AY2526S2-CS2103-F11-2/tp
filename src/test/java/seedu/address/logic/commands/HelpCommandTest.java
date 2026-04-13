package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.FALLBACK_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(
                FALLBACK_HELP_MESSAGE, true, false, HelpCommand.USERGUIDE_URL);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpAdd_success() {
        String expectedUrl = HelpCommand.USERGUIDE_URL + HelpCommand.COMMAND_URL_FRAGMENTS.get("add");
        CommandResult expectedCommandResult = new CommandResult(
                HelpCommand.COMMAND_USAGE_MESSAGES.get("add"), true, false, expectedUrl);
        assertCommandSuccess(new HelpCommand("add"), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        HelpCommand cmd = new HelpCommand();
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        assertEquals(new HelpCommand(), new HelpCommand());
        assertEquals(new HelpCommand("add"), new HelpCommand("add"));
    }

    @Test
    public void equals_notHelpCommand_returnsFalse() {
        HelpCommand cmd = new HelpCommand();
        assertFalse(cmd.equals("help"));
    }

    @Test
    public void equals_differentTargetCommand_returnsFalse() {
        assertNotEquals(new HelpCommand(), new HelpCommand("add"));
        assertNotEquals(new HelpCommand("add"), new HelpCommand("edit"));
    }

    @Test
    public void hashCode_consistency() {
        assertEquals(new HelpCommand().hashCode(), new HelpCommand().hashCode());
        assertEquals(new HelpCommand("add").hashCode(), new HelpCommand("add").hashCode());
    }
}

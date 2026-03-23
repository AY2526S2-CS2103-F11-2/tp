package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.ui.HelpWindow;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(
                SHOWING_HELP_MESSAGE, true, false, HelpWindow.USERGUIDE_URL);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpAdd_success() {
        String expectedUrl = HelpWindow.USERGUIDE_URL + HelpCommand.COMMAND_URL_FRAGMENTS.get("add");
        CommandResult expectedCommandResult = new CommandResult(
                String.format(HelpCommand.SHOWING_HELP_COMMAND_MESSAGE, "add"), true, false, expectedUrl);
        assertCommandSuccess(new HelpCommand("add"), model, expectedCommandResult, expectedModel);
    }
}

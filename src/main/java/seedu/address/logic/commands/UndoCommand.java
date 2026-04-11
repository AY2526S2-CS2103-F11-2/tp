package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Requests the application to undo the most recent undoable command.
 * This command is coordinated by {@code LogicManager}.
 * The actual undo behavior is implemented by each undoable {@code Command}.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_NO_PARAMETER = "Undo command does not take in any parameter.";
    public static final String MESSAGE_SUCCESS = "Undo successful.";
    public static final String MESSAGE_NO_HISTORY = "No undoable command in history.";

    /**
    * This command should not be executed directly because undo handling is delegated to
    * {@code LogicManager}. This method throws an AssertionError to indicate that the command
    * should not be executed directly.
    */
    @Override
    public CommandResult execute(Model model) {
        throw new AssertionError("UndoCommand should be handled by LogicManager.");
    }
}

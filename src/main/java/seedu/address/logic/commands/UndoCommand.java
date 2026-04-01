package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Requests the application to undo the most recent undoable command.
 * This command is handled by {@code LogicManager}.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_NO_PARAMETER = "Undo command does not take in any parameter.";
    public static final String MESSAGE_SUCCESS = "Undo successful.";
    public static final String MESSAGE_NO_HISTORY = "No undoable command in history.";

    @Override
    public CommandResult execute(Model model) {
        throw new AssertionError("UndoCommand should be handled by LogicManager.");
    }
}

package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Returns whether this command supports undo.
     */
    public boolean isUndoable() {
        return false;
    }

    /**
     * Undoes the effect of a previously executed command.
     *
     * @throws CommandException If undo cannot be completed.
     */
    public CommandResult undo(Model model) throws CommandException {
        throw new CommandException("This command cannot be undone.");
    }
}

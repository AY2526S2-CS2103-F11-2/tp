package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new HelpCommand object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public HelpCommand parse(String args) throws ParseException {
        String trimmed = args.trim().toLowerCase();
        if (trimmed.isEmpty()) {
            return new HelpCommand();
        }
        if (trimmed.contains(" ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        if (!HelpCommand.VALID_COMMAND_NAMES.contains(trimmed)) {
            throw new ParseException(String.format(HelpCommand.MESSAGE_UNKNOWN_COMMAND, trimmed));
        }
        return new HelpCommand(trimmed);
    }
}

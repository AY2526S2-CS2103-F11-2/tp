package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_REVERSE_FLAG;
import static seedu.address.logic.Messages.MESSAGE_INVALID_SORT_ORDER;
import static seedu.address.logic.commands.SortCommand.SORT_COMPARATORS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVERSE;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser implements Parser<SortCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ORDER, PREFIX_REVERSE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ORDER, PREFIX_REVERSE);

        if (argMultimap.getValue(PREFIX_ORDER).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String order = argMultimap.getValue(PREFIX_ORDER).get().trim();

        if (order.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        if (!SORT_COMPARATORS.containsKey(order.toLowerCase())) {
            throw new ParseException(String.format(MESSAGE_INVALID_SORT_ORDER,
                    String.join(", ", SORT_COMPARATORS.keySet())));
        }

        if (argMultimap.getValue(PREFIX_REVERSE).isPresent()
                && !argMultimap.getValue(PREFIX_REVERSE).get().trim().isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_REVERSE_FLAG);
        }

        boolean reverse = argMultimap.getValue(PREFIX_REVERSE).isPresent();

        return new SortCommand(order.toLowerCase(), reverse);
    }
}

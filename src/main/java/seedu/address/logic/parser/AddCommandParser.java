package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.ADD_COMMAND_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.NON_ADD_COMMAND_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;

import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TelegramHandle;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    private static final String MESSAGE_UNEXPECTED_EXTRA_INPUT =
            "Invalid command format! \nUnexpected extra input in add command: '%s'.";

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        Optional<String> unexpectedInput = findUnexpectedExtraInput(args);
        if (unexpectedInput.isPresent()) {
            throw new ParseException(String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, unexpectedInput.get()));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, ADD_COMMAND_PREFIXES);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(ADD_COMMAND_PREFIXES);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = null;
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        }
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        TelegramHandle telegramHandle = null;
        if (argMultimap.getValue(PREFIX_TELEGRAM_HANDLE).isPresent()) {
            telegramHandle = ParserUtil.parseTelegramHandle(argMultimap.getValue(PREFIX_TELEGRAM_HANDLE).get());
        }

        Person person = new Person(name, phone, email, telegramHandle);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns the first non-add prefixed token in input order, if any.
     * A prefix is only recognized when preceded by whitespace, matching ArgumentTokenizer behavior.
     */
    private static Optional<String> findUnexpectedExtraInput(String args) {
        int earliestPosition = -1;
        String unexpectedToken = null;

        for (Prefix prefix : NON_ADD_COMMAND_PREFIXES) {
            int position = findPrefixPosition(args, prefix);
            if (position != -1 && (earliestPosition == -1 || position < earliestPosition)) {
                earliestPosition = position;
                unexpectedToken = extractToken(args, position);
            }
        }

        return Optional.ofNullable(unexpectedToken);
    }

    private static int findPrefixPosition(String args, Prefix prefix) {
        int prefixIndex = args.indexOf(" " + prefix.getPrefix());
        return prefixIndex == -1 ? -1 : prefixIndex + 1;
    }

    private static String extractToken(String args, int startPosition) {
        int endPosition = args.indexOf(' ', startPosition);
        if (endPosition == -1) {
            return args.substring(startPosition);
        }
        return args.substring(startPosition, endPosition);
    }
}

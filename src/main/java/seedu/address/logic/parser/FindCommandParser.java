package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameOrEmailContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        // ArgumentTokenizer recognizes prefixes only when preceded by whitespace.
        // Add a leading space so first prefix at start of argument string is recognized.
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_NAME, PREFIX_EMAIL);

        List<String> nameKeywords = parseKeywords(argumentMultimap, PREFIX_NAME);

        List<String> emailKeywords = parseKeywords(argumentMultimap, PREFIX_EMAIL);

        // Throw exception if preamble is not empty, eg "find alice n/bob"
        // Both name or email keywords are not specified
        if (!argumentMultimap.getPreamble().isBlank()
            || (nameKeywords.isEmpty() && emailKeywords.isEmpty())) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new NameOrEmailContainsKeywordsPredicate(nameKeywords, emailKeywords));
    }

    /**
     * Extracts and processes keywords associated with the specified {@code prefix}
     * from the given {@code ArgumentMultimap}.
     *
     * <p>All values corresponding to the prefix are split by whitespace into individual
     * keywords. Blank or empty keywords are discarded.</p>
     *
     * <p>For example, if the input contains {@code n/alice bob n/charlie}, this method
     * returns a list containing {@code ["alice", "bob", "charlie"]}.</p>
     *
     * @param argumentMultimap The {@code ArgumentMultimap} containing parsed arguments.
     * @param prefix The {@code Prefix} whose associated values are to be processed.
     * @return A list of non-blank keywords extracted from the specified prefix.
     */
    private static List<String> parseKeywords(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getAllValues(prefix)
                .stream()
                .flatMap(keyword -> Arrays.stream(keyword.split("\\s+")))
                .filter(keyword -> !keyword.isBlank())
                .toList();
    }
}

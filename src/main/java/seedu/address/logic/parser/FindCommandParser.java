package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_NO_INPUT;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.parser.CliSyntax.NON_FIND_COMMAND_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameEmailTagPredicate;

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
        String leadingSpacedArgs = args.startsWith(" ") ? args : " " + args;
        assert leadingSpacedArgs.startsWith(" ") : "Input should start with a space for prefix recognition";

        // Check for any disallowed prefixes
        Optional<String> unexpectedInput = ParserUtil.findUnexpectedExtraInput(leadingSpacedArgs,
                NON_FIND_COMMAND_PREFIXES);
        if (unexpectedInput.isPresent()) {
            throw new ParseException(String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT,
                    unexpectedInput.get()));
        }

        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(leadingSpacedArgs,
                PREFIX_NAME, PREFIX_EMAIL, PREFIX_TAG);

        // Check for any prefixes with no value eg. find n/john e/ t/
        Optional<String> emptyPrefix = ParserUtil.findEmptyPrefixValues(argumentMultimap,
                PREFIX_NAME, PREFIX_EMAIL, PREFIX_TAG);
        if (emptyPrefix.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, emptyPrefix.get()));
        }

        // parse keywords for name, email and tags. Keywords for each field are split by whitespace.
        List<String> nameKeywords = parseKeywords(argumentMultimap, PREFIX_NAME);
        List<String> emailKeywords = parseKeywords(argumentMultimap, PREFIX_EMAIL);
        List<String> tags = parseKeywords(argumentMultimap, PREFIX_TAG);

        // Throw exception if preamble is not empty, eg "find alice n/bob"
        // If no name or email or tag keywords are specified
        if (!argumentMultimap.getPreamble().isBlank()
            || (nameKeywords.isEmpty() && emailKeywords.isEmpty() && tags.isEmpty())) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new NameEmailTagPredicate(nameKeywords, emailKeywords, tags));
    }

    /**
     * Extracts and processes keywords associated with the specified {@code prefix}
     * from the given {@code ArgumentMultimap}.
     *
     * <p>All values corresponding to the prefix are split by whitespace into individual
     * keywords. Keywords are validated to ensure they contain at least one alphanumeric
     * character. Keywords that contain only special characters (punctuation) are rejected.</p>
     *
     * <p>For example, if the input contains {@code n/alice bob n/charlie}, this method
     * returns a list containing {@code ["alice", "bob", "charlie"]}.
     * However, {@code n/!@# alice} would throw a ParseException for the invalid keyword.</p>
     *
     * @param argumentMultimap The {@code ArgumentMultimap} containing parsed arguments.
     * @param prefix The {@code Prefix} whose associated values are to be processed.
     * @return A list of valid keywords extracted from the specified prefix.
     * @throws ParseException if any keyword contains only special characters (no alphanumeric characters)
     */
    private static List<String> parseKeywords(ArgumentMultimap argumentMultimap, Prefix prefix)
            throws ParseException {
        List<String> validKeywords = new ArrayList<>();

        for (String value : argumentMultimap.getAllValues(prefix)) {
            String[] tokens = value.split("\\s+");
            for (String token : tokens) {
                if (token.isBlank()) {
                    // Skip blank tokens (from multiple spaces)
                    continue;
                }

                // Check if token contains only punctuation (no alphanumeric characters)
                if (!token.matches(".*[a-zA-Z0-9].*")) {
                    throw new ParseException(String.format(
                            MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS,
                            prefix.getPrefix(),
                            token));
                }

                validKeywords.add(token);
            }
        }

        // Return an unmodifiable copy of the valid keywords list
        return List.copyOf(validKeywords);
    }
}

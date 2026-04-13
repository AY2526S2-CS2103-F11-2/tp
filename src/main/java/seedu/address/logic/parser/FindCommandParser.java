package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.FIND_COMMAND_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // Check for any disallowed prefixes
        ParserUtil.validateNoInvalidPrefixInputs(leadingSpacedArgs, FIND_COMMAND_PREFIXES);

        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(leadingSpacedArgs, FIND_COMMAND_PREFIXES);
        // Check that preamble is empty and that no prefixes have empty values
        ParserUtil.validateEmptyPreamble(argumentMultimap, FindCommand.MESSAGE_USAGE);
        ParserUtil.validateNoEmptyPrefixValues(argumentMultimap, FIND_COMMAND_PREFIXES);

        // parse keywords for name, email and tags. Keywords for each field are split by whitespace.
        List<String> nameKeywords = parseKeywords(argumentMultimap, PREFIX_NAME);
        List<String> emailKeywords = parseKeywords(argumentMultimap, PREFIX_EMAIL);
        List<String> tags = parseKeywords(argumentMultimap, PREFIX_TAG);

        // throw exception if no keywords are specified for all 3 fields.
        requireAtLeastOneKeyword(nameKeywords, emailKeywords, tags);

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
    private static List<String> parseKeywords(ArgumentMultimap argumentMultimap, Prefix prefix) {
        List<String> validKeywords = new ArrayList<>();

        for (String value : argumentMultimap.getAllValues(prefix)) {
            String[] tokens = value.split("\\s+");
            for (String token : tokens) {
                if (token.isBlank()) {
                    // Skip blank tokens (from multiple spaces)
                    continue;
                }

                validKeywords.add(token);
            }
        }

        // Return an unmodifiable copy of the valid keywords list
        return List.copyOf(validKeywords);
    }

    /**
     * Ensures that at least one search field has keywords.
     *
     * @throws ParseException if all keyword lists are empty.
     */
    @SafeVarargs
    private void requireAtLeastOneKeyword(List<String>... keywordLists) throws ParseException {
        boolean areAllKeywordFieldsEmpty = Arrays.stream(keywordLists)
                .allMatch(List::isEmpty);

        if (areAllKeywordFieldsEmpty) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }
}

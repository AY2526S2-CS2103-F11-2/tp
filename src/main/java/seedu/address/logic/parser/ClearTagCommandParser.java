package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.TagType;

/**
 * Parses input arguments and creates a new ClearTagCommand object.
 */
public class ClearTagCommandParser implements Parser<ClearTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ClearTagCommand
     * and returns an ClearTagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public ClearTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ROLE_TAG, PREFIX_COURSE_TAG, PREFIX_GENERAL_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ROLE_TAG, PREFIX_COURSE_TAG, PREFIX_GENERAL_TAG);
        List<String> roleValues = argMultimap.getAllValues(CliSyntax.PREFIX_ROLE_TAG);
        List<String> courseValues = argMultimap.getAllValues(CliSyntax.PREFIX_COURSE_TAG);
        List<String> generalValues = argMultimap.getAllValues(CliSyntax.PREFIX_GENERAL_TAG);

        TagType typeToClear = null;
        int totalPrefixes = 0;

        if (!roleValues.isEmpty()) {
            validateNoValue(roleValues);
            typeToClear = TagType.ROLE;
            totalPrefixes++;
        }
        if (!courseValues.isEmpty()) {
            validateNoValue(courseValues);
            typeToClear = TagType.COURSE;
            totalPrefixes++;
        }
        if (!generalValues.isEmpty()) {
            validateNoValue(generalValues);
            typeToClear = TagType.GENERAL;
            totalPrefixes++;
        }

        if (totalPrefixes != 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
        }

        return new ClearTagCommand(index, typeToClear);
    }

    /**
     * Validates that the given list of prefix values contains no preceding values.
     * <p>
     * This is used for commands where prefixes act as flags (e.g. {@code tr/}, {@code tc/}, {@code tg/})
     * and should not be followed by any user input. If any value is present after a prefix,
     * a {@link ParseException} will be thrown.
     *
     * @param values The list of values associated with a specific prefix.
     * @throws ParseException if any value in the list is non-empty.
     */
    private void validateNoValue(List<String> values) throws ParseException {
        for (String value : values) {
            if (!value.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT, ClearTagCommand.MESSAGE_USAGE));
            }
        }
    }
}

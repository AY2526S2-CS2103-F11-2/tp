package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_NO_INPUT;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CliSyntax.TAG_COMMAND_PREFIXES;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Parses input arguments and creates a new UntagCommand object.
 */
public class UntagCommandParser implements Parser<UntagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UntagCommand
     * and returns an UntagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public UntagCommand parse(String args) throws ParseException {
        // check for any prefix that's not in the allowed list
        Optional<String> invalidPrefix = ParserUtil.findInvalidPrefixInput(args, TAG_COMMAND_PREFIXES);
        if (invalidPrefix.isPresent()) {
            throw new ParseException(String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, invalidPrefix.get()));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, TAG_COMMAND_PREFIXES);

        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty() || preamble.contains(" ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage());
        }

        Optional<String> emptyPrefix = ParserUtil.findEmptyPrefixValues(argMultimap, TAG_COMMAND_PREFIXES);
        if (emptyPrefix.isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, emptyPrefix.get()));
        }

        if (argMultimap.getAllValues(PREFIX_ROLE_TAG).isEmpty()
                && argMultimap.getAllValues(PREFIX_COURSE_TAG).isEmpty()
                && argMultimap.getAllValues(PREFIX_GENERAL_TAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        Set<Tag> tagList = new HashSet<>();
        tagList.addAll(ParserUtil.parseTags(
                argMultimap.getAllValues(PREFIX_ROLE_TAG), TagType.ROLE));

        tagList.addAll(ParserUtil.parseTags(
                argMultimap.getAllValues(PREFIX_COURSE_TAG), TagType.COURSE));

        tagList.addAll(ParserUtil.parseTags(
                argMultimap.getAllValues(PREFIX_GENERAL_TAG), TagType.GENERAL));

        return new UntagCommand(index, tagList);
    }
}

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CliSyntax.TAGS_COMMAND_PREFIXES;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object.
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns an TagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public TagCommand parse(String args) throws ParseException {
        ParserUtil.validateNoInvalidPrefixInputs(args, TAGS_COMMAND_PREFIXES);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, TAGS_COMMAND_PREFIXES);
        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty() || preamble.contains(" ")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        ParserUtil.validateNoEmptyPrefixValues(argMultimap, TAGS_COMMAND_PREFIXES);

        Index index = ParserUtil.parseIndex(preamble);

        if (argMultimap.getAllValues(PREFIX_ROLE_TAG).isEmpty()
                && argMultimap.getAllValues(PREFIX_COURSE_TAG).isEmpty()
                && argMultimap.getAllValues(PREFIX_GENERAL_TAG).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        Set<Tag> tagList = ParserUtil.parseAllTypeOfTags(argMultimap);

        return new TagCommand(index, tagList);
    }
}

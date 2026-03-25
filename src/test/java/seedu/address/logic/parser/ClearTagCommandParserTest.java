package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearTagCommand;
import seedu.address.model.tag.TagType;

public class ClearTagCommandParserTest {

    private ClearTagCommandParser parser = new ClearTagCommandParser();

    @Test
    public void parse_validRoleTag_success() {
        assertParseSuccess(parser, "1 tr/", new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE));
    }

    @Test
    public void parse_validCourseTag_success() {
        assertParseSuccess(parser, "1 tc/", new ClearTagCommand(INDEX_FIRST_PERSON, TagType.COURSE));
    }

    @Test
    public void parse_validGeneralTag_success() {
        assertParseSuccess(parser, "1 tg/", new ClearTagCommand(INDEX_FIRST_PERSON, TagType.GENERAL));
    }

    @Test
    public void parse_validExtraWhitespace_success() {
        assertParseSuccess(parser, "  1   tr/  ", new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE));
    }

    @Test
    public void parse_invalidValuesAfterPrefix_failure() {
        assertParseFailure(parser, "1 tr/tutor",
                String.format(MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT, ClearTagCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 tr/   testtt",
                String.format(MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT, ClearTagCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 tr/ extra",
                String.format(MESSAGE_INVALID_PREFIX_WITH_EXTRA_INPUT, ClearTagCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_invalidMultiplePrefixes_failure() {
        assertParseFailure(parser, "1 tr/ tc/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_failure() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPrefix_failure() {
        assertParseFailure(parser, "1 ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, "tr/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "1abc tr/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, "1 to/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 t/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 test/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 tr/ tr/",
                getErrorMessageForDuplicatePrefixes(new Prefix[]{PREFIX_ROLE_TAG}));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, "extraText " + "1 tr/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }
}

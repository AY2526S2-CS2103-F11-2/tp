package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
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

    // ---------------- SUCCESS CASES ----------------
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

    // ---------------- FAILURE CASES - COMMAND FORMAT VALIDATION ----------------
    @Test
    public void parse_invalidCommandFormat_failure() {
        assertParseFailure(parser,
                " tg/ " + 1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    // ---------------- FAILURE CASES - INDEX VALIDATION ----------------
    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, " tr/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexWithoutPrefix_failure() {
        assertParseFailure(parser, "0",
                ParserUtil.MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "abc",
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexWithPrefix_failure() {
        assertParseFailure(parser,
                "0 tr/",
                ParserUtil.MESSAGE_INVALID_INDEX);

        assertParseFailure(parser,
                "-5 tr/",
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexNonNumeric_failure() {
        assertParseFailure(parser,
                "abc tg/",
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexAlphanumeric_failure() {
        assertParseFailure(parser,
                "1a tr/",
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    // ---------------- FAILURE CASES - PREFIX VALIDATION ----------------
    @Test
    public void parse_missingPrefix_failure() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPrefix_failure() {
        assertParseFailure(parser, "1" + "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, "1 to/",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "to/"));

        assertParseFailure(parser, "1 t/",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "t/"));

        assertParseFailure(parser,
                "1 tr/" + " test/",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "test/"));
    }

    @Test
    public void parse_invalidValuesAfterPrefix_failure() {
        assertParseFailure(parser, "1 tr/tutor",
                String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, "tr/tutor"));

        assertParseFailure(parser, "1 tr/   testtt",
                String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, "tr/testtt"));

        assertParseFailure(parser, "1 tr/ tg/extra",
                String.format(MESSAGE_PREFIX_SHOULD_NOT_HAVE_VALUE, "tg/extra"));

    }

    @Test
    public void parse_invalidWithMultipleTagTypes_failure() {
        assertParseFailure(parser, "1 tr/ tc/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 tr/ tr/",
                getErrorMessageForDuplicatePrefixes(new Prefix[]{PREFIX_ROLE_TAG}));
    }

    // ---------------- FAILURE CASES - PREAMBLE VALIDATION ----------------
    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, "extraText " + "1 tr/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                "   extraText   1 tc/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                "1 abc tc/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    // ---------------- EMPTY AND WHITESPACE INPUT TESTS ----------------
    @Test
    public void parse_emptyInput_throwsParseException() {
        assertParseFailure(parser,
                "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyEmptySpace_throwsParseException() {
        assertParseFailure(parser,
                "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearTagCommand.MESSAGE_USAGE));
    }
}

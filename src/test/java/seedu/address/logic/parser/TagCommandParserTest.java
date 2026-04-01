package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_NO_INPUT;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

public class TagCommandParserTest {

    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_TAG_WITH_SPACES = "friend group";

    private static final String VALID_ROLE_TAG = "tutor";
    private static final String VALID_ROLE_TAG_MIXED = "TuToR";
    private static final String VALID_COURSE_TAG = "cs2103";
    private static final String VALID_COURSE_TAG_2 = "cs2109s";
    private static final String VALID_GENERAL_TAG = "friends";
    private static final String VALID_GENERAL_TAG_UPPER = "FRIENDS";

    private TagCommandParser parser = new TagCommandParser();

    // ---------------- SUCCESS CASES ----------------
    @Test
    public void parse_validSingleTag_success() {
        Index index = Index.fromOneBased(1);
        Set<Tag> expectedTags = Set.of(new Tag(VALID_GENERAL_TAG, TagType.GENERAL));

        assertParseSuccess(parser,
                "1 tg/" + VALID_GENERAL_TAG,
                new TagCommand(index, expectedTags));
    }


    @Test
    public void parse_validMultipleTags_success() {
        Index index = Index.fromOneBased(1);
        Set<Tag> expectedTags = Set.of(
                new Tag(VALID_GENERAL_TAG, TagType.GENERAL),
                new Tag(VALID_ROLE_TAG, TagType.ROLE),
                new Tag(VALID_COURSE_TAG, TagType.COURSE)
        );

        assertParseSuccess(parser,
                "1"
                        + " tg/" + VALID_GENERAL_TAG
                        + " tr/" + VALID_ROLE_TAG
                        + " tc/" + VALID_COURSE_TAG,
                new TagCommand(index, expectedTags));
    }

    @Test
    public void parse_multipleTagsWithWhitespace_success() {
        Index index = Index.fromOneBased(1);
        Set<Tag> expectedTags = Set.of(
                new Tag(VALID_GENERAL_TAG, TagType.GENERAL),
                new Tag(VALID_ROLE_TAG, TagType.ROLE)
        );

        assertParseSuccess(parser,
                "1   tg/" + VALID_GENERAL_TAG + "     tr/" + VALID_ROLE_TAG,
                new TagCommand(index, expectedTags));
    }

    @Test
    public void parse_multipleSamePrefix_success() {
        Index index = Index.fromOneBased(1);
        Set<Tag> expectedTags = Set.of(
                new Tag(VALID_COURSE_TAG, TagType.COURSE),
                new Tag(VALID_COURSE_TAG_2, TagType.COURSE)
        );

        assertParseSuccess(parser,
                "1"
                        + " tc/" + VALID_COURSE_TAG
                        + " tc/" + VALID_COURSE_TAG_2,
                new TagCommand(index, expectedTags));
    }

    @Test
    public void parse_duplicateTags_success() {
        Index index = Index.fromOneBased(1);
        Set<Tag> expectedTags = Set.of(new Tag(VALID_GENERAL_TAG, TagType.GENERAL));

        assertParseSuccess(parser,
                "1 tg/" + VALID_GENERAL_TAG + " tg/" + VALID_GENERAL_TAG,
                new TagCommand(index, expectedTags));
    }

    @Test
    public void parse_duplicateTagsCaseInsensitive_success() {
        Index index = Index.fromOneBased(1);
        Set<Tag> expectedTags = Set.of(new Tag(VALID_GENERAL_TAG, TagType.GENERAL));

        // both same tag, different cases
        assertParseSuccess(parser,
                "1 tg/" + VALID_GENERAL_TAG + " tg/" + VALID_GENERAL_TAG_UPPER,
                new TagCommand(index, expectedTags));

        // duplicate with mixed case role
        expectedTags = Set.of(new Tag(VALID_ROLE_TAG, TagType.ROLE));
        assertParseSuccess(parser,
                "1 tr/" + VALID_ROLE_TAG + " tr/" + VALID_ROLE_TAG_MIXED,
                new TagCommand(index, expectedTags));
    }

    // ---------------- FAILURE CASES - COMMAND FORMAT VALIDATION ----------------
    @Test
    public void parse_invalidCommandFormat_failure() {
        assertParseFailure(parser,
                " tg/" + VALID_GENERAL_TAG + 1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    // ---------------- FAILURE CASES - INDEX VALIDATION ----------------
    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser,
                " tg/" + VALID_GENERAL_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
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
                "0 tr/" + VALID_ROLE_TAG,
                ParserUtil.MESSAGE_INVALID_INDEX);

        assertParseFailure(parser,
                "-5 tr/" + VALID_ROLE_TAG,
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexNonNumeric_failure() {
        assertParseFailure(parser,
                "abc tg/" + VALID_GENERAL_TAG,
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexAlphanumeric_failure() {
        assertParseFailure(parser,
                "1a tr/" + VALID_ROLE_TAG,
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    // ---------------- FAILURE CASES - TAG VALIDATION ----------------
    @Test
    public void parse_noTags_failure() {
        assertParseFailure(parser,
                "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser,
                "1 tg/" + INVALID_TAG,
                Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                "1 tg/" + INVALID_TAG_WITH_SPACES,
                Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyTagValue_failure() {
        assertParseFailure(parser,
                "1 tg/",
                String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "tg/"));
    }

    @Test
    public void parse_mixedEmptyAndValidTags_failure() {
        assertParseFailure(parser,
                "1 tg/ tg/" + VALID_GENERAL_TAG,
                String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "tg/"));
    }

    // ---------------- FAILURE CASES - PREFIX VALIDATION ----------------
    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser,
                "1 to/" + VALID_ROLE_TAG,
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "to/" + VALID_ROLE_TAG));

        assertParseFailure(parser,
                "1 n/" + VALID_ROLE_TAG,
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "n/" + VALID_ROLE_TAG));

        assertParseFailure(parser,
                "1 tr/" + VALID_ROLE_TAG + " test/",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "test/"));
    }

    @Test
    public void parse_multipleInvalidPrefixes_failure() {
        assertParseFailure(parser,
                "1 n/alice p/12345678",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "n/alice"));
    }

    // ---------------- FAILURE CASES - PREAMBLE VALIDATION ----------------
    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser,
                "extraText " + "1 tc/" + VALID_COURSE_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                "   extraText   1 tc/" + VALID_COURSE_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                "1 abc tc/" + VALID_COURSE_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    // ---------------- EMPTY AND WHITESPACE INPUT TESTS ----------------
    @Test
    public void parse_emptyInput_throwsParseException() {
        assertParseFailure(parser,
                "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyEmptySpace_throwsParseException() {
        assertParseFailure(parser,
                "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }
}

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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

    private static final String VALID_ROLE_TAG = "tutor";
    private static final String VALID_COURSE_TAG = "cs2103";
    private static final String VALID_COURSE_TAG_2 = "cs2109s";
    private static final String VALID_GENERAL_TAG = "friends";

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validSingleGeneralTag_success() {
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
    public void parse_missingIndex_failure() {
        assertParseFailure(parser,
                " tg/" + VALID_GENERAL_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser,
                "abc " + "tg/" + "friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

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
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser,
                "1 to/" + VALID_ROLE_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser,
                "extraText " + "1 tc/" + VALID_COURSE_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }
}

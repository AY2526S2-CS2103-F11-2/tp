package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.person.Email;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validIndex_returnsDeleteCommand() {
        // EP: valid index (positive integer within range of displayed list)
        assertParseSuccess(parser, " i/1",
                new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validEmail_returnsDeleteCommand() {
        // EP: valid email (follows email format constraints)
        assertParseSuccess(parser, " e/" + VALID_EMAIL_AMY,
                new DeleteCommand(new Email(VALID_EMAIL_AMY)));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // EP: invalid index - non-numeric characters, alphabetic characters
        assertParseFailure(parser, " i/a",
                String.format(ParserUtil.MESSAGE_INVALID_INDEX));

        assertParseFailure(parser, " i/testtttt",
                String.format(ParserUtil.MESSAGE_INVALID_INDEX));
    }

    @Test
    public void parse_invalidEmail_throwsParseException() {
        // EP: invalid email (does not follow email format constraints)
        assertParseFailure(parser, " e/test",
                String.format(Email.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        // EP: empty input (no prefix, no arguments)
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

        // EP: empty spaces
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

        // EP: argument without any valid prefix (numeric input with no prefix)
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_bothPrefixesPresent_throwsParseException() {
        // EP: both i/ and e/ prefixes present simultaneously (mutually exclusive)
        assertParseFailure(parser, " i/1" + " e/" + VALID_EMAIL_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multiplePrefixesOfSameTypePresent_throwsParseException() {
        // EP: multiple prefixes of same type present simultaneously (mutually exclusive)
        assertParseFailure(parser, " i/1" + " i/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INDEX));

        assertParseFailure(parser, " e/" + VALID_EMAIL_AMY + " e/" + VALID_EMAIL_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser,
                "extraText i/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                "helloWorld e/" + VALID_EMAIL_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}

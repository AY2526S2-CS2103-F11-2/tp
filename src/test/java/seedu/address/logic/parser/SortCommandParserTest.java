package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_SORT_ORDER;
import static seedu.address.logic.commands.SortCommand.SORT_COMPARATORS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVERSE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_sortByName() {
        assertParseSuccess(parser, " o/name", new SortCommand("name", false));
    }

    @Test
    public void parse_validArgs_sortByNameReversed() {
        assertParseSuccess(parser, " o/name r/", new SortCommand("name", true));
    }

    @Test
    public void parse_validArgs_caseInsensitiveOrder() {
        assertParseSuccess(parser, " o/NAME", new SortCommand("name", false));
    }

    @Test
    public void parse_validArgs_caseInsensitiveOrderReversed() {
        assertParseSuccess(parser, " o/NaMe r/", new SortCommand("name", true));
    }

    @Test
    public void parse_missingOrderPrefix_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyOrderValue_throwsParseException() {
        assertParseFailure(parser, " o/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidOrderValue_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_SORT_ORDER,
                String.join(", ", SORT_COMPARATORS.keySet()));
        assertParseFailure(parser, " o/phone", expectedMessage);
    }

    @Test
    public void parse_invalidOrderAddress_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_SORT_ORDER,
                String.join(", ", SORT_COMPARATORS.keySet()));
        assertParseFailure(parser, " o/address", expectedMessage);
    }

    @Test
    public void parse_duplicateOrderPrefix_throwsParseException() {
        assertParseFailure(parser, " o/name o/name",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ORDER));
    }

    @Test
    public void parse_duplicateReversePrefix_throwsParseException() {
        assertParseFailure(parser, " o/name r/ r/",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REVERSE));
    }
}

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_REVERSE_FLAG;
import static seedu.address.logic.Messages.MESSAGE_INVALID_SORT_ORDER;
import static seedu.address.logic.Messages.MESSAGE_REVERSE_FLAG_WITH_NONE;
import static seedu.address.logic.commands.SortCommand.SORT_COMPARATORS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVERSE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.TreeMap;

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
    public void parse_validArgs_caseInsensitiveEmail() {
        assertParseSuccess(parser, " o/EMAIL", new SortCommand("email", false));
    }

    @Test
    public void parse_validArgs_caseInsensitivePhone() {
        assertParseSuccess(parser, " o/PHONE", new SortCommand("phone", false));
    }

    @Test
    public void parse_validArgs_caseInsensitiveNone() {
        assertParseSuccess(parser, " o/NONE", new SortCommand("none", false));
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
                String.join(", ", new TreeMap<>(SORT_COMPARATORS).keySet()) + ", none");
        assertParseFailure(parser, " o/invalid", expectedMessage);
    }

    @Test
    public void parse_invalidOrderAddress_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_SORT_ORDER,
                String.join(", ", new TreeMap<>(SORT_COMPARATORS).keySet()) + ", none");
        assertParseFailure(parser, " o/address", expectedMessage);
    }

    @Test
    public void parse_reverseFlagWithValue_throwsParseException() {
        assertParseFailure(parser, " o/name r/sometext", MESSAGE_INVALID_REVERSE_FLAG);
    }

    @Test
    public void parse_validArgs_sortByEmail() {
        assertParseSuccess(parser, " o/email", new SortCommand("email", false));
    }

    @Test
    public void parse_validArgs_sortByEmailReversed() {
        assertParseSuccess(parser, " o/email r/", new SortCommand("email", true));
    }

    @Test
    public void parse_validArgs_sortByPhone() {
        assertParseSuccess(parser, " o/phone", new SortCommand("phone", false));
    }

    @Test
    public void parse_validArgs_sortByPhoneReversed() {
        assertParseSuccess(parser, " o/phone r/", new SortCommand("phone", true));
    }

    @Test
    public void parse_validArgs_sortNone() {
        assertParseSuccess(parser, " o/none", new SortCommand("none", false));
    }

    @Test
    public void parse_noneWithReverseFlag_throwsParseException() {
        assertParseFailure(parser, " o/none r/", MESSAGE_REVERSE_FLAG_WITH_NONE);
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

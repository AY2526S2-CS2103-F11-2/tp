package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameOrEmailContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyNameArg_throwsParseException() {
        assertParseFailure(parser, "n/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyEmailArg_throwsParseException() {
        assertParseFailure(parser, "e/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyNameAndEmailArgs_throwsParseException() {
        assertParseFailure(parser, "n/ e/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unprefixedKeywords_throwsParseException() {
        assertParseFailure(parser, "alice bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preambleNotEmpty_throwsParseException() {
        assertParseFailure(parser, "alice n/bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validSingleNamePrefix_returnsFindCommand() {
        List<String> names = List.of("Alice");

        FindCommand expectedFindCommand =
                new FindCommand(new NameOrEmailContainsKeywordsPredicate(names, List.of()));

        assertParseSuccess(parser, "n/Alice", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleNamePrefix_returnsFindCommand() {
        List<String> names = List.of("Alice", "Bob");

        FindCommand expectedFindCommand =
                new FindCommand(new NameOrEmailContainsKeywordsPredicate(names, List.of()));

        assertParseSuccess(parser, "n/Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice n/Bob", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice \t \t \tBob", expectedFindCommand);
    }

    @Test
    public void parse_validSingleEmailPrefix_returnsFindCommand() {
        List<String> emails = List.of("gmail");

        FindCommand expectedFindCommand =
                new FindCommand(new NameOrEmailContainsKeywordsPredicate(List.of(), emails));

        assertParseSuccess(parser, "e/gmail", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleEmailsPrefix_returnsFindCommand() {
        List<String> emails = List.of("gmail", "yahoo");

        FindCommand expectedFindCommand =
                new FindCommand(new NameOrEmailContainsKeywordsPredicate(List.of(), emails));

        assertParseSuccess(parser, "e/gmail yahoo", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail e/yahoo", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail \t \t \t yahoo", expectedFindCommand);
    }

    @Test
    public void parse_validNameAndEmailPrefix_returnsFindCommand() {
        List<String> names = List.of("Alice");
        List<String> emails = List.of("gmail");

        FindCommand expectedFindCommand =
                new FindCommand(new NameOrEmailContainsKeywordsPredicate(names, emails));

        assertParseSuccess(parser, "n/Alice e/gmail", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail n/Alice", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice \t \t e/gmail", expectedFindCommand);
    }
}

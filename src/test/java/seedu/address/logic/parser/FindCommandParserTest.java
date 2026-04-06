package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX_WITH_NO_INPUT;
import static seedu.address.logic.Messages.MESSAGE_PREAMBLE_NOT_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_EXTRA_INPUT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameEmailTagPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    //========================== FAILURE CASES - Invalid Format ================================
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyNameArg_throwsParseException() {
        assertParseFailure(parser, "n/", String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "n/"));
    }

    @Test
    public void parse_emptyEmailArg_throwsParseException() {
        assertParseFailure(parser, "e/", String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "e/"));
    }

    @Test
    public void parse_emptyTagsArg_throwsParseException() {
        assertParseFailure(parser, "t/", String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "t/"));
    }

    @Test
    public void parse_nameNotEmptyEmailOrTagEmpty_throwsParseException() {
        assertParseFailure(parser, "n/john e/",
                String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "e/"));

        assertParseFailure(parser, "n/john t/",
                String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "t/"));

        assertParseFailure(parser, "e/john@unus.edu t/",
                String.format(MESSAGE_INVALID_PREFIX_WITH_NO_INPUT, "t/"));
    }

    @Test
    public void parse_preambleNotEmpty_throwsParseException() {
        assertParseFailure(parser, "alice n/bob",
                String.format(MESSAGE_PREAMBLE_NOT_EMPTY, "alice", FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_disallowedPrefix_throwsParseException() {
        assertParseFailure(parser, "n/alice tc/cs2103",
                String.format(MESSAGE_UNEXPECTED_EXTRA_INPUT, "tc/cs2103"));
    }

    @Test
    public void parse_validMultipleNamesWithStandaloneSpecialCharacters_throwsParseException() {
        assertParseFailure(parser, "n/bob . prim",
                String.format(MESSAGE_INVALID_KEYWORD_WITH_ONLY_SPECIAL_CHARACTERS, "n/", "."));
    }

    //============================== SUCCESS CASES - One Field ===================================
    @Test
    public void parse_validSingleNamePrefix_returnsFindCommand() {
        List<String> names = List.of("Alice");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(names, List.of(), List.of()));

        assertParseSuccess(parser, "n/Alice", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleNamePrefix_returnsFindCommand() {
        List<String> names = List.of("Alice", "Bob");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(names, List.of(), List.of()));

        assertParseSuccess(parser, "n/Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice n/Bob", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice \t \t \tBob", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleNamesNoStandaloneSpecialCharacters_returnsFindCommand() {
        List<String> names = List.of("Bob", "C.", "Prim");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(names, List.of(), List.of()));

        assertParseSuccess(parser, "n/Bob C. Prim", expectedFindCommand);
        assertParseSuccess(parser, "n/Bob n/C. n/Prim", expectedFindCommand);
    }

    @Test
    public void parse_validSingleEmailPrefix_returnsFindCommand() {
        List<String> emails = List.of("gmail");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(List.of(), emails, List.of()));

        assertParseSuccess(parser, "e/gmail", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleEmailsPrefix_returnsFindCommand() {
        List<String> emails = List.of("gmail", "yahoo");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(List.of(), emails, List.of()));

        assertParseSuccess(parser, "e/gmail yahoo", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail e/yahoo", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail \t \t \t yahoo", expectedFindCommand);
    }

    @Test
    public void parse_validSingleTagPrefix_returnsFindCommand() {
        List<String> tagList = List.of("friends");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(List.of(), List.of(), tagList));

        assertParseSuccess(parser, "t/friends", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleTagsPrefix_returnsFindCommand() {
        List<String> tagList = List.of("friends", "cs2103");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(List.of(), List.of(), tagList));

        assertParseSuccess(parser, "t/friends cs2103", expectedFindCommand);
        assertParseSuccess(parser, "t/friends t/cs2103", expectedFindCommand);
        assertParseSuccess(parser, "t/friends \t \t \t cs2103", expectedFindCommand);
    }

    //========================= SUCCESS CASES - Multiple different fields ===================================
    @Test
    public void parse_validNameAndEmailPrefix_returnsFindCommand() {
        List<String> names = List.of("Alice");
        List<String> emails = List.of("gmail");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(names, emails, List.of()));

        assertParseSuccess(parser, "n/Alice e/gmail", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail n/Alice", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice \t \t e/gmail", expectedFindCommand);
    }

    @Test
    public void parse_validSingleNameEmailTagPrefix_returnsFindCommand() {
        List<String> names = List.of("Alice");
        List<String> emails = List.of("gmail");
        List<String> tagList = List.of("friends");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(names, emails, tagList));

        assertParseSuccess(parser, "n/Alice e/gmail t/friends", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail t/friends n/Alice", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice \t \t e/gmail \t t/friends", expectedFindCommand);
    }

    @Test
    public void parse_validMultipleNameEmailTagPrefix_returnsFindCommand() {
        List<String> names = List.of("Alice", "Bob");
        List<String> emails = List.of("gmail", "nus");
        List<String> tagList = List.of("friends", "cs2103");

        FindCommand expectedFindCommand =
                new FindCommand(new NameEmailTagPredicate(names, emails, tagList));

        assertParseSuccess(parser, "n/Alice Bob e/gmail nus t/friends cs2103", expectedFindCommand);
        assertParseSuccess(parser, "e/gmail nus t/friends cs2103 n/Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice Bob \t e/gmail nus \t t/friends cs2103",
                expectedFindCommand);
    }
}

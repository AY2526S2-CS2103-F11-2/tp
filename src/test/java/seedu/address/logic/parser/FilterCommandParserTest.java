package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsTagsPredicate;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noTagPrefix_throwsParseException() {
        // input without t/ prefix
        assertParseFailure(parser, "friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preambleNotEmpty_throwsParseException() {
        // input with preamble
        assertParseFailure(parser, "hello t/friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validSingleTag_returnsFilterCommand() throws ParseException {
        Tag friendsTag = new Tag("friends");
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(friendsTag));
        FilterCommand expectedCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, " t/friends", expectedCommand);
    }

    @Test
    public void parse_validMultipleTags_returnsFilterCommand() throws ParseException {
        Tag friendsTag = new Tag("friends");
        Tag colleaguesTag = new Tag("colleagues");
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(friendsTag, colleaguesTag));
        FilterCommand expectedCommand = new FilterCommand(predicate);

        // multiple tags must all have t/ prefix
        assertParseSuccess(parser, " t/friends t/colleagues", expectedCommand);
    }

    @Test
    public void parse_validMultipleTagsWithExtraSpaces_returnsFilterCommand() throws ParseException {
        Tag friendsTag = new Tag("friends");
        Tag colleaguesTag = new Tag("colleagues");
        PersonContainsTagsPredicate predicate = new PersonContainsTagsPredicate(Set.of(friendsTag, colleaguesTag));
        FilterCommand expectedCommand = new FilterCommand(predicate);

        // extra spaces between tags are allowed
        assertParseSuccess(parser, "  t/friends    t/colleagues  ", expectedCommand);
    }
}

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.HelpCommand;

public class HelpCommandParserTest {

    private final HelpCommandParser parser = new HelpCommandParser();

    @Test
    public void parse_emptyArgs_returnsGeneralHelp() {
        assertParseSuccess(parser, "", new HelpCommand());
        assertParseSuccess(parser, "   ", new HelpCommand());
    }

    @Test
    public void parse_validCommand_returnsHelpCommand() {
        assertParseSuccess(parser, "add", new HelpCommand("add"));
        assertParseSuccess(parser, "edit", new HelpCommand("edit"));
        assertParseSuccess(parser, "delete", new HelpCommand("delete"));
        assertParseSuccess(parser, "find", new HelpCommand("find"));
        assertParseSuccess(parser, "list", new HelpCommand("list"));
        assertParseSuccess(parser, "sort", new HelpCommand("sort"));
        assertParseSuccess(parser, "clear", new HelpCommand("clear"));
        assertParseSuccess(parser, "exit", new HelpCommand("exit"));
        assertParseSuccess(parser, "tag", new HelpCommand("tag"));
        assertParseSuccess(parser, "untag", new HelpCommand("untag"));
        assertParseSuccess(parser, "cleartag", new HelpCommand("cleartag"));
    }


    @Test
    public void parse_invalidCommand_throwsParseException() {
        assertParseFailure(parser, "unknown",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "ADD",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }
}

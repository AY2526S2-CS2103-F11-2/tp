package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearTagCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameEmailTagPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + PREFIX_INDEX + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> names = List.of("foo", "bar", "baz");
        List<String> emails = List.of("gmail", "yahoo");

        List<String> tags = List.of("tag1", "tag2");

        String input = FindCommand.COMMAND_WORD + " "
                + PREFIX_NAME + " " + String.join(" ", names) + " " // name keywords
                + PREFIX_EMAIL + " " + String.join(" ", emails) + " " // email keywords
                + PREFIX_TAG + " " + String.join(" ", tags); // tags

        FindCommand command = (FindCommand) parser.parseCommand(input);
        assertEquals(new FindCommand(new NameEmailTagPredicate(names, emails, tags)),
                command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " add") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        SortCommand command = (SortCommand) parser.parseCommand(SortCommand.COMMAND_WORD + " o/name");
        assertEquals(new SortCommand("name", false), command);
    }

    @Test
    public void parseCommand_tag() throws Exception {
        String input = TagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_ROLE_TAG + "tutor "
                + PREFIX_COURSE_TAG + "CS2103 "
                + PREFIX_GENERAL_TAG + "friends";

        TagCommand command = (TagCommand) parser.parseCommand(input);

        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(new Tag("tutor", TagType.ROLE));
        expectedTags.add(new Tag("CS2103", TagType.COURSE));
        expectedTags.add(new Tag("friends", TagType.GENERAL));

        assertEquals(new TagCommand(INDEX_FIRST_PERSON, expectedTags), command);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD + " 1") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_untag() throws Exception {
        String input = UntagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_ROLE_TAG + "tutor "
                + PREFIX_COURSE_TAG + "CS2103 "
                + PREFIX_GENERAL_TAG + "friends";

        UntagCommand command = (UntagCommand) parser.parseCommand(input);

        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(new Tag("tutor", TagType.ROLE));
        expectedTags.add(new Tag("CS2103", TagType.COURSE));
        expectedTags.add(new Tag("friends", TagType.GENERAL));

        assertEquals(new UntagCommand(INDEX_FIRST_PERSON, expectedTags), command);
    }

    @Test
    public void parseCommand_cleartag() throws Exception {
        String input = ClearTagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_ROLE_TAG;

        ClearTagCommand command = (ClearTagCommand) parser.parseCommand(input);
        assertEquals(new ClearTagCommand(INDEX_FIRST_PERSON, TagType.ROLE), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}

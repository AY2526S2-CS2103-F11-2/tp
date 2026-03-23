package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENERAL_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds tags to an existing person in the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Add tags to the person identified by the index number used in the displayed person list. "
            + "Input values will be appended to the person.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ROLE_TAG + "TAG]... "
            + "[" + PREFIX_COURSE_TAG + "TAG]... "
            + "[" + PREFIX_GENERAL_TAG + "TAG]... \n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ROLE_TAG + "tutor "
            + PREFIX_COURSE_TAG + "CS2103 " + PREFIX_GENERAL_TAG + "friends";

    public static final String MESSAGE_SUCCESS = "New tags added: %1$s";

    private final Index index;
    private final Set<Tag> tagList;

    /**
     * @param index   of the person in the filtered person list to edit
     * @param tagList the lag list to add
     */
    public TagCommand(Index index, Set<Tag> tagList) {
        requireNonNull(index);
        requireNonNull(tagList);

        this.index = index;
        this.tagList = tagList;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToAddTag;

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToAddTag = lastShownList.get(index.getZeroBased());

        // Merge existing tags with new tags
        Set<Tag> updatedTags = new HashSet<>(personToAddTag.getTags());
        updatedTags.addAll(tagList);

        // Create edited person (immutability)
        Person editedPerson = new Person(
                personToAddTag.getName(),
                personToAddTag.getPhone(),
                personToAddTag.getEmail(),
                personToAddTag.getTelegramHandle(),
                updatedTags
        );

        model.setPerson(personToAddTag, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, tagList));
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return index.equals(otherTagCommand.index)
                && tagList.equals(otherTagCommand.tagList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tags", tagList)
                .toString();
    }
}

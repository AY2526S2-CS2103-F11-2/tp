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
 * Removes tags from an existing person in the address book.
 */
public class UntagCommand extends Command {

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes tags from the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ROLE_TAG + "ROLE]... "
            + "[" + PREFIX_COURSE_TAG + "COURSE]... "
            + "[" + PREFIX_GENERAL_TAG + "GENERAL]... \n"
            + "At least one tag must be provided.\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ROLE_TAG + "tutor "
            + PREFIX_COURSE_TAG + "cs2103 " + PREFIX_GENERAL_TAG + "friends";

    public static final String MESSAGE_SUCCESS = "Tags removed: %1$s";
    public static final String MESSAGE_PARTIAL_SUCCESS = "Tags removed: %1$s\nTags not found: %2$s";
    public static final String MESSAGE_NO_TAGS_FOUND = "None of the specified tags were found";

    private final Index index;
    private final Set<Tag> tagsToRemove;

    /**
     * @param index        of the person in the filtered person list to remove tags.
     * @param tagsToRemove the tags to remove.
     */
    public UntagCommand(Index index, Set<Tag> tagsToRemove) {
        requireNonNull(index);
        requireNonNull(tagsToRemove);

        this.index = index;
        this.tagsToRemove = tagsToRemove;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToRemoveTag = lastShownList.get(index.getZeroBased());

        RemovalResult result = removeTags(personToRemoveTag);

        if (result.removedTags.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAGS_FOUND);
        }

        Person editedPerson = personToRemoveTag.withTags(result.updatedTags);

        model.setPerson(personToRemoveTag, editedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        // partial success
        if (!result.notFoundTags.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_PARTIAL_SUCCESS, result.removedTags, result.notFoundTags));
        }

        // full success
        return new CommandResult(String.format(MESSAGE_SUCCESS, result.removedTags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;
        return index.equals(otherUntagCommand.index)
                && tagsToRemove.equals(otherUntagCommand.tagsToRemove);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tagsToRemove", tagsToRemove)
                .toString();
    }

    /**
     * Represents the result of a tag removal operation.
     */
    private record RemovalResult(Set<Tag> updatedTags, Set<Tag> removedTags, Set<Tag> notFoundTags) {
    }

    /**
     * Removes the specified tags from the given person.
     *
     * @param person the person whose tags are to be modified.
     * @return RemovalResult containing updated tags, removed tags, and tags not found.
     */
    private RemovalResult removeTags(Person person) {
        Set<Tag> updatedTags = new HashSet<>(person.getTags());
        Set<Tag> removedTags = new HashSet<>();
        Set<Tag> notFoundTags = new HashSet<>();

        for (Tag tag : tagsToRemove) {
            if (updatedTags.remove(tag)) {
                removedTags.add(tag);
            } else {
                notFoundTags.add(tag);
            }
        }

        return new RemovalResult(updatedTags, removedTags, notFoundTags);
    }
}

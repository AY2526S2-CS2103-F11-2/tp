package seedu.address.logic.commands;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";
    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103-f11-2.github.io/tp/UserGuide.html";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD + " add";

    public static final String SHOWING_HELP_MESSAGE = "Opened user guide in browser.";
    public static final String SHOWING_HELP_COMMAND_MESSAGE = "Opening user guide for '%s' command.";

    /**
     * Maps each command word to its user guide URL fragment.
     */
    public static final Map<String, String> COMMAND_URL_FRAGMENTS = Map.ofEntries(
            Map.entry("help", "#viewing-help--help"),
            Map.entry("add", "#adding-a-person--add"),
            Map.entry("list", "#listing-all-persons--list"),
            Map.entry("sort", "#sorting-persons--sort"),
            Map.entry("edit", "#editing-a-person--edit"),
            Map.entry("find", "#locating-persons-by-nameemailtag--find"),
            Map.entry("delete", "#deleting-a-person--delete"),
            Map.entry("clear", "#clearing-all-entries--clear"),
            Map.entry("exit", "#exiting-the-program--exit"),
            Map.entry("tag", "#tagging-a-person--tag"),
            Map.entry("untag", "#untagging-a-person--untag"),
            Map.entry("cleartag", "#clearing-tags-of-a-specific-type-from-a-person--cleartag"),
            Map.entry("undo", "#undoing-the-last-action--undo")
            );

    /**
     * The set of valid command names that can be used with {@code help <command>}.
     */
    public static final Set<String> VALID_COMMAND_NAMES = COMMAND_URL_FRAGMENTS.keySet();

    /**
     * Sorted list of valid command names, for display in error messages.
     */
    public static final List<String> VALID_COMMAND_NAMES_SORTED = VALID_COMMAND_NAMES.stream()
            .sorted().collect(Collectors.toList());

    public static final String MESSAGE_UNKNOWN_COMMAND =
            "Command \"%s\" does not exist.\nValid commands: "
            + String.join(", ", VALID_COMMAND_NAMES_SORTED);

    private final String targetCommand;

    /**
     * Creates a HelpCommand that opens the user guide in the system default browser.
     */
    public HelpCommand() {
        this.targetCommand = null;
    }

    /**
     * Creates a HelpCommand that opens the user guide at the section for {@code targetCommand}.
     */
    public HelpCommand(String targetCommand) {
        this.targetCommand = targetCommand;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof HelpCommand)) {
            return false;
        }
        HelpCommand otherCommand = (HelpCommand) other;
        return Objects.equals(targetCommand, otherCommand.targetCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(targetCommand);
    }

    @Override
    public CommandResult execute(Model model) {
        String fragment = targetCommand == null ? "" : COMMAND_URL_FRAGMENTS.get(targetCommand);
        String url = USERGUIDE_URL + fragment;
        String message = targetCommand == null
                ? SHOWING_HELP_MESSAGE
                : String.format(SHOWING_HELP_COMMAND_MESSAGE, targetCommand);
        return new CommandResult(message, true, false, url);
    }
}

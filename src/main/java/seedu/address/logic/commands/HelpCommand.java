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
            Map.entry("cleartag", "#clearing-all-tags-of-a-specific-type--cleartag"),
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

    /** Prefix added to offline fallback messages. */
    public static final String OFFLINE_HELP_NOTICE = "User guide cannot be opened\n";

    /** Fallback shown when general {@code help} is called offline. */
    public static final String OFFLINE_FALLBACK_GENERAL =
            OFFLINE_HELP_NOTICE
            + "Available commands: " + String.join(", ", VALID_COMMAND_NAMES_SORTED) + "\n"
            + "Use 'help <command>' for details on a specific command.";

    /**
     * Maps each command word to its offline fallback usage text.
     * Commands without a {@code MESSAGE_USAGE} constant use inline strings.
     */
    public static final Map<String, String> COMMAND_FALLBACK_MESSAGES = Map.ofEntries(
            Map.entry("add", OFFLINE_HELP_NOTICE + AddCommand.MESSAGE_USAGE),
            Map.entry("edit", OFFLINE_HELP_NOTICE + EditCommand.MESSAGE_USAGE),
            Map.entry("find", OFFLINE_HELP_NOTICE + FindCommand.MESSAGE_USAGE),
            Map.entry("delete", OFFLINE_HELP_NOTICE + DeleteCommand.MESSAGE_USAGE),
            Map.entry("sort", OFFLINE_HELP_NOTICE + SortCommand.MESSAGE_USAGE),
            Map.entry("tag", OFFLINE_HELP_NOTICE + TagCommand.MESSAGE_USAGE),
            Map.entry("untag", OFFLINE_HELP_NOTICE + UntagCommand.MESSAGE_USAGE),
            Map.entry("cleartag", OFFLINE_HELP_NOTICE + ClearTagCommand.MESSAGE_USAGE),
            Map.entry("help", OFFLINE_HELP_NOTICE + MESSAGE_USAGE),
            Map.entry("list", OFFLINE_HELP_NOTICE + "list: Lists all persons.\nExample: list"),
            Map.entry("clear", OFFLINE_HELP_NOTICE + "clear: Clears all entries.\nExample: clear"),
            Map.entry("exit", OFFLINE_HELP_NOTICE + "exit: Exits the program.\nExample: exit"),
            Map.entry("undo", OFFLINE_HELP_NOTICE + "undo: Undoes the last action.\nExample: undo")
    );

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
        String fallback = targetCommand == null
                ? OFFLINE_FALLBACK_GENERAL
                : COMMAND_FALLBACK_MESSAGES.getOrDefault(targetCommand, OFFLINE_FALLBACK_GENERAL);
        return new CommandResult(message, true, false, url, fallback);
    }
}

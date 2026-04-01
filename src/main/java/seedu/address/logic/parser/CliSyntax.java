package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_TELEGRAM_HANDLE = new Prefix("h/");
    public static final Prefix PREFIX_ORDER = new Prefix("o/");
    public static final Prefix PREFIX_REVERSE = new Prefix("r/");
    public static final Prefix PREFIX_INDEX = new Prefix("i/");
    public static final Prefix PREFIX_ROLE_TAG = new Prefix("tr/");
    public static final Prefix PREFIX_COURSE_TAG = new Prefix("tc/");
    public static final Prefix PREFIX_GENERAL_TAG = new Prefix("tg/");

    public static final Prefix[] ADD_EDIT_COMMAND_PREFIXES = {
        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM_HANDLE
    };

    public static final Prefix[] NON_ADD_COMMAND_PREFIXES = {
        PREFIX_TAG, PREFIX_ORDER, PREFIX_REVERSE, PREFIX_INDEX,
        PREFIX_ROLE_TAG, PREFIX_COURSE_TAG, PREFIX_GENERAL_TAG
    };

    public static final Prefix[] NON_FIND_COMMAND_PREFIXES = {
        PREFIX_PHONE, PREFIX_TELEGRAM_HANDLE, PREFIX_ORDER, PREFIX_REVERSE,
        PREFIX_INDEX, PREFIX_ROLE_TAG, PREFIX_COURSE_TAG, PREFIX_GENERAL_TAG
    };

    public static final Prefix[] TAG_COMMAND_PREFIXES = {
        PREFIX_ROLE_TAG, PREFIX_COURSE_TAG, PREFIX_GENERAL_TAG
    };
}

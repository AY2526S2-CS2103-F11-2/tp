package seedu.address.model.person;

/**
 * Represents the type of duplicate conflict detected for a person.
 */
public enum DuplicateConflict {
    NONE,
    EMAIL,
    TELEGRAM_HANDLE,
    EMAIL_AND_TELEGRAM_HANDLE;
}

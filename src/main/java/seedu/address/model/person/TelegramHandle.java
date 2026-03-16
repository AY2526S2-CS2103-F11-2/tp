package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Telegram handle in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTelegramHandle(String)}
 */
public class TelegramHandle {

    public static final String MESSAGE_CONSTRAINTS =
            "Telegram handles must start with a letter, contain only letters, numbers, and underscores, "
                    + "be 5 to 32 characters long, not contain consecutive underscores, "
                    + "and not end with an underscore.";
    public static final String VALIDATION_REGEX = "^(?!.*__)[a-zA-Z][a-zA-Z0-9_]{3,30}[a-zA-Z0-9]$";
    public final String value;

    /**
     * Constructs a {@code TelegramHandle}.
     *
     * @param telegramHandle A valid Telegram handle.
     */
    public TelegramHandle(String telegramHandle) {
        requireNonNull(telegramHandle);
        checkArgument(isValidTelegramHandle(telegramHandle), MESSAGE_CONSTRAINTS);
        value = telegramHandle;
    }

    /**
     * Returns true if a given string is a valid Telegram handle.
     */
    public static boolean isValidTelegramHandle(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TelegramHandle)) {
            return false;
        }

        TelegramHandle otherTelegramHandle = (TelegramHandle) other;
        return value.equals(otherTelegramHandle.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

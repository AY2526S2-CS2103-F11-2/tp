package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TelegramHandleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TelegramHandle(null));
    }

    @Test
    public void constructor_invalidTelegramHandle_throwsIllegalArgumentException() {
        String invalidTelegramHandle = "";
        assertThrows(IllegalArgumentException.class, () -> new TelegramHandle(invalidTelegramHandle));
    }

    @Test
    public void isValidTelegramHandle() {
        // null telegram handle
        assertThrows(NullPointerException.class, () -> TelegramHandle.isValidTelegramHandle(null));

        // invalid telegram handles
        assertFalse(TelegramHandle.isValidTelegramHandle("")); // empty string
        assertFalse(TelegramHandle.isValidTelegramHandle("    ")); // spaces only
        assertFalse(TelegramHandle.isValidTelegramHandle("ab")); // too short
        assertFalse(TelegramHandle.isValidTelegramHandle("abcd")); // too short
        assertFalse(TelegramHandle.isValidTelegramHandle("user!name")); // invalid character
        assertFalse(TelegramHandle.isValidTelegramHandle("user-name")); // invalid character
        assertFalse(TelegramHandle.isValidTelegramHandle("user name")); // spaces not allowed
        assertFalse(TelegramHandle.isValidTelegramHandle("1user")); // must start with a letter
        assertFalse(TelegramHandle.isValidTelegramHandle("_user1")); // must start with a letter
        assertFalse(TelegramHandle.isValidTelegramHandle("user_")); // cannot end with underscore
        assertFalse(TelegramHandle.isValidTelegramHandle("user__name")); // no consecutive underscores
        assertFalse(TelegramHandle.isValidTelegramHandle("abcd_")); // ends with underscore

        // valid telegram handles
        assertTrue(TelegramHandle.isValidTelegramHandle("abcde"));
        assertTrue(TelegramHandle.isValidTelegramHandle("rachel_walker"));
        assertTrue(TelegramHandle.isValidTelegramHandle("user123"));
        assertTrue(TelegramHandle.isValidTelegramHandle("User_Name_123"));
        assertTrue(TelegramHandle.isValidTelegramHandle("a1234")); // minimal valid length
    }

    @Test
    public void toString_validTelegramHandle_returnsValue() {
        TelegramHandle telegramHandle = new TelegramHandle("alice123");
        assertEquals("alice123", telegramHandle.toString());
    }

    @Test
    public void equals() {
        TelegramHandle first = new TelegramHandle("alice123");
        TelegramHandle second = new TelegramHandle("alice123");
        TelegramHandle third = new TelegramHandle("bob123");

        assertTrue(first.equals(first));
        assertTrue(first.equals(second));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
        assertFalse(first.equals(third));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        TelegramHandle first = new TelegramHandle("alice123");
        TelegramHandle second = new TelegramHandle("alice123");
        assertEquals(first.hashCode(), second.hashCode());
    }
}

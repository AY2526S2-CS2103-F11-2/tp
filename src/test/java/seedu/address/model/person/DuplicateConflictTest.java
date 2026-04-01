package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DuplicateConflictTest {

    @Test
    public void hasEmailConflict() {
        assertFalse(DuplicateConflict.NONE.hasEmailConflict());
        assertTrue(DuplicateConflict.EMAIL.hasEmailConflict());
        assertFalse(DuplicateConflict.TELEGRAM_HANDLE.hasEmailConflict());
        assertTrue(DuplicateConflict.EMAIL_AND_TELEGRAM_HANDLE.hasEmailConflict());
    }

    @Test
    public void hasTelegramHandleConflict() {
        assertFalse(DuplicateConflict.NONE.hasTelegramHandleConflict());
        assertFalse(DuplicateConflict.EMAIL.hasTelegramHandleConflict());
        assertTrue(DuplicateConflict.TELEGRAM_HANDLE.hasTelegramHandleConflict());
        assertTrue(DuplicateConflict.EMAIL_AND_TELEGRAM_HANDLE.hasTelegramHandleConflict());
    }
}

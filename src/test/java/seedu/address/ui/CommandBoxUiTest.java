package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * Automated UI integration tests for {@link CommandBox}.
 * Tests cover command submission, error styling, and command history navigation.
 */
public class CommandBoxUiTest extends UiTestBase {

    @BeforeEach
    void resetState() {
        submitCommand("list");
    }

    @Test
    public void execute_validCommand_clearsBox() {
        submitCommand("list");

        TextField field = lookup("#commandTextField").queryAs(TextField.class);
        assertEquals("", field.getText());
    }

    @Test
    public void execute_invalidCommand_appliesError() {
        submitCommand("notacommand");

        TextField field = lookup("#commandTextField").queryAs(TextField.class);
        assertTrue(field.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
    }

    @Test
    public void execute_errorThenType_removesError() {
        submitCommand("notacommand");

        interact(() -> {
            TextField field = lookup("#commandTextField").queryAs(TextField.class);
            field.setText("l");
        });
        WaitForAsyncUtils.waitForFxEvents();

        TextField field = lookup("#commandTextField").queryAs(TextField.class);
        assertFalse(field.getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS));
    }

    @Test
    public void pressUp_afterOneCommand_recallsCommand() {
        submitCommand("find n/Alice");

        pressKey(KeyCode.UP);

        TextField field = lookup("#commandTextField").queryAs(TextField.class);
        assertEquals("find n/Alice", field.getText());
    }

    @Test
    public void pressUp_thenDown_returnsToEmpty() {
        submitCommand("find n/Alice");

        pressKey(KeyCode.UP);
        pressKey(KeyCode.DOWN);

        TextField field = lookup("#commandTextField").queryAs(TextField.class);
        assertEquals("", field.getText());
    }
}

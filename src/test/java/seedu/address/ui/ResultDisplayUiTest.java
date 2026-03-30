package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.control.TextArea;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListCommand;

/**
 * Automated UI integration tests for {@link ResultDisplay}.
 * Tests verify that command feedback is shown correctly after each command.
 */
public class ResultDisplayUiTest extends UiTestBase {

    @BeforeEach
    void resetState() {
        submitCommand("list");
    }

    @Test
    public void execute_listCommand_showsSuccess() {
        submitCommand("list");

        TextArea display = lookup("#resultDisplay").queryAs(TextArea.class);
        assertEquals(ListCommand.MESSAGE_SUCCESS, display.getText());
    }

    @Test
    public void execute_unknownCommand_showsError() {
        submitCommand("xyzzy");

        TextArea display = lookup("#resultDisplay").queryAs(TextArea.class);
        assertTrue(display.getText().contains(Messages.MESSAGE_UNKNOWN_COMMAND));
    }

    @Test
    public void execute_findWithResults_showsCount() {
        submitCommand("find n/Alice");

        TextArea display = lookup("#resultDisplay").queryAs(TextArea.class);
        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1), display.getText());
    }

    @Test
    public void execute_findNoResults_showsZero() {
        submitCommand("find n/ZZZNoSuchPerson");

        TextArea display = lookup("#resultDisplay").queryAs(TextArea.class);
        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 0), display.getText());
    }
}

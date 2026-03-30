package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.control.ListView;

/**
 * Automated UI integration tests for {@link PersonListPanel}.
 * Tests verify that the person list renders correctly and updates after commands.
 */
public class PersonListPanelUiTest extends UiTestBase {

    /** Number of persons in {@link seedu.address.testutil.TypicalPersons#getTypicalPersons()}. */
    private static final int TYPICAL_PERSON_COUNT = 7;

    @BeforeEach
    void resetState() {
        submitCommand("list");
    }

    @Test
    public void display_afterList_showsAllPersons() {
        assertEquals(TYPICAL_PERSON_COUNT, countListedPersons());
    }

    @Test
    public void execute_findAlice_showsOneResult() {
        submitCommand("find n/Alice");

        assertEquals(1, countListedPersons());
    }

    @Test
    public void execute_findMeier_showsTwoResults() {
        submitCommand("find n/Meier");

        assertEquals(2, countListedPersons());
    }

    @Test
    public void execute_findThenList_restoresAll() {
        submitCommand("find n/Alice");
        submitCommand("list");

        assertEquals(TYPICAL_PERSON_COUNT, countListedPersons());
    }

    /**
     * Returns the number of persons currently shown in the person list by reading the
     * item count from the {@code ListView} data model, bypassing virtual cell rendering.
     */
    private int countListedPersons() {
        AtomicInteger count = new AtomicInteger();
        interact(() -> count.set(lookup("#personListView")
                .queryAs(ListView.class)
                .getItems()
                .size()));
        WaitForAsyncUtils.waitForFxEvents();
        return count.get();
    }
}

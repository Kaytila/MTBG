package net.ck.mtbg.ui.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.MapTile;

import java.awt.*;

/**
 * In order to make Controller more of what it is - listener and action caller
 * it shall be stateless - the state needs to go here into State machine.
 */
@Getter
@Setter
@Log4j2
public class UIStateMachine
{
    /**
     * is the UI open? has it finished opening?
     */
    @Getter
    @Setter
    private static boolean uiOpen;

    /**
     * select Tile is being used whenever - the game shall pause - the cursor shall switch to cross-hairs.
     * this is always used for two-step actions.
     */
    @Getter
    @Setter
    private static boolean selectTile;

    /**
     * is a Dialog open?
     */
    @Getter
    @Setter
    private static boolean dialogOpened;

    /**
     * when the mouse is in the grid, store the mouse position
     * no need to request it from the Toolkit again and again.
     */
    @Getter
    @Setter
    private static Point currentMousePosition;

    /**
     * store always what tile is currently selected or would be selected.
     * this applies to all 2-step actions.
     */
    @Getter
    @Setter
    private static MapTile currentSelectedTile;
    @Getter
    @Setter
    private static UIState uiState;

    @Getter
    @Setter
    private static boolean hitAnimationRunning;


    /**
     * is the mouse outside of the grid?
     * used for centering on player to make the mouse ways shorter
     */
    @Getter
    @Setter
    private static boolean mouseOutsideOfGrid;

    @Getter
    @Setter
    private static Point lastMousePosition;
}

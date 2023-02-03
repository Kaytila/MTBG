package net.ck.game.backend.state;

import net.ck.game.map.MapTile;

import java.awt.*;

/**
 * In order to make Controller more of what it is - listener and action caller
 * it shall be stateless - the state needs to go here into State machine.
 */
public class UIStateMachine
{
    /**
     * is the UI open? has it finished opening?
     */
    private static boolean uiOpen;

    /**
     * select Tile is being used whenever - the game shall pause - the cursor shall switch to cross-hairs.
     * this is always used for two-step actions.
     */
    private static boolean selectTile;

    /**
     * is a Dialog open?
     */
    private static boolean isDialogOpened;

    /**
     * when the mouse is in the grid, store the mouse position
     * no need to request it from the Toolkit again and again.
     */
    private static Point currentMousePosition;

    /**
     * store always what tile is currently selected or would be selected.
     * this applies to all 2-step actions.
     */
    private static MapTile currentSelectedTile;


    public static boolean isUiOpen()
    {
        return uiOpen;
    }

    public static void setUiOpen(boolean uo)
    {
        uiOpen = uo;
    }

    public static boolean isSelectTile()
    {
        return selectTile;
    }

    public static void setSelectTile(boolean sT) {
        selectTile = sT;
    }

    public static boolean isDialogOpened()
    {
        return isDialogOpened;
    }

    public static void setDialogOpened(boolean dO)
    {
        //logger.info("new value: {}", isDialogOpened);
        isDialogOpened = dO;
    }

    public static Point getCurrentMousePosition()
    {
        return currentMousePosition;
    }

    public static void setCurrentMousePosition(Point currentMousePosition)
    {
        UIStateMachine.currentMousePosition = currentMousePosition;
    }

    public static MapTile getCurrentSelectedTile()
    {
        return currentSelectedTile;
    }

    public static void setCurrentSelectedTile(MapTile currentSelectedTile)
    {
        UIStateMachine.currentSelectedTile = currentSelectedTile;
    }
}

package net.ck.game.backend.state;

public class UIStateMachine {
    /**
     * is the UI open? has it finished opening?
     */
    private static boolean uiOpen;

    /**
     * select Tile is being used whenever - the game shall pause - the cursor shall switch to cross-hairs.
     * this is always used for two-step actions.
     */
    private static boolean selectTile;
    private static boolean isDialogOpened;

    public static boolean isUiOpen() {
        return uiOpen;
    }

    public static void setUiOpen(boolean uo) {
        uiOpen = uo;
    }

    public static boolean isSelectTile() {
        return selectTile;
    }

    public static void setSelectTile(boolean sT) {
        selectTile = sT;
    }

    public static boolean isDialogOpened() {
        return isDialogOpened;
    }

    public static void setDialogOpened(boolean dO) {
        //logger.info("new value: {}", isDialogOpened);
        isDialogOpened = dO;
    }

}

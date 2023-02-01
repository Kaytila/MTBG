package net.ck.game.ui.listeners;


import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.UIStateMachine;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.game.ui.components.JGridCanvas;
import net.ck.game.ui.dialogs.AbstractDialog;
import net.ck.game.ui.dialogs.InventoryDialog;
import net.ck.game.ui.dialogs.StatsDialog;
import net.ck.util.*;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.ActionFactory;
import net.ck.util.communication.keyboard.KeyboardActionType;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;


/**
 * MainWindow is the "UI Application Class" that only keeps together the controls in order to be able to have the game work without the UI being instantiated (i.e. testing!!!) this needs to be
 * encapsulated better
 *
 * @author Claus
 */
public class Controller implements WindowListener, ActionListener, MouseListener, MouseMotionListener, FocusListener {
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    /**
     * currentAction is used for the two-step actions.
     * standard action is handled in onMessageEvent (AbstractKeyboardAction), i.e. movement.
     * Anything that needs a cross-hair is a two action event, first action only triggers
     * state change and sets some variables like currentAction.
     * currentAction is then taken in the second step and actually performed.
     * This can be with either mouse input (i.e. mouse released) or
     * keyboard input (movement keys or even pressing "a" again)
     */
    private AbstractKeyboardAction currentAction;

    /**
     * inventoryDialog
     */
    private InventoryDialog inventoryDialog;

    /**
     * set if a dialog is opened. Game is supposed to pause here
     */
    private AbstractItem currentItemInHand;

    /**
     * mouse pressed is used for moving via mouse, there is a delay defined in the timer
     * that is started pressedTimer.
     */
    private boolean mousePressed;

    /**
     * pressed mouse button timer
     */
    Timer pressedTimer;



    /**
     * this variable is being set if the numpad movement keys
     * are used for moving the cross-hairs currently.
     * i.e. it switched keyboard movement to cross-hair movement.
     */
    private boolean movementForSelectTile = false;

    /**
     * stats Dialog - there will be one dialog only with exchanging JPanels
     */
    private StatsDialog statsDialog;

    /**
     * is the mouse outside of the grid?
     * used for centering on player to make the mouse ways shorter
     */
    private boolean mouseOutsideOfGrid;

    /**
     * is drag Enabled
     */
    private boolean dragEnabled;

    /**
     * standard constructor
     */
    public Controller() {
        EventBus.getDefault().register(this);
        setMouseOutsideOfGrid(true);
        WindowBuilder.buildWindow(this);
    }


    /**
     * this is called when any button is clicked.
     * <p>
     * undo button is a little not working anymore
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equalsIgnoreCase("Undo"))
        {
            logger.info("undo");
            Game.getCurrent().getIdleTimer().stop();
            /*if (Game.getCurrent().retractTurn() == 0)
            {
                WindowBuilder.getUndoButton().setEnabled(false);
            }
            else
            {
                WindowBuilder.getUndoButton().setEnabled(true);
            }*/
        }

        if (e.getActionCommand().equalsIgnoreCase("Cancel"))
        {
            logger.info("Cancel");
        }

        if (e.getActionCommand().equalsIgnoreCase("OK"))
        {
            logger.info("OK");
        }

        if (e.getActionCommand().equalsIgnoreCase("StartMusic"))
        {
            logger.info("start music");
            if (GameConfiguration.playMusic)
            {
                //Game.getCurrent().getSoundSystem().startMusic();
                Game.getCurrent().getMusicSystemNoThread().continueMusic();
                //EventBus.getDefault().post(new GameStateChanged(GameState.WORLD));
            }

        }

        if (e.getActionCommand().equalsIgnoreCase("StopMusic"))
        {
            logger.info("stop music");
            if (GameConfiguration.playMusic)
            {
                //Game.getCurrent().getSoundSystem().stopMusic();
                Game.getCurrent().getMusicSystemNoThread().pauseMusic();
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Louder"))
        {
            logger.info("louder");
            if (GameConfiguration.playMusic)
            {
                Game.getCurrent().getMusicSystemNoThread().increaseVolume();
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Leiser"))
        {
            logger.info("leiser");
            if (GameConfiguration.playMusic)
            {
                Game.getCurrent().getMusicSystemNoThread().decreaseVolume();
            }
        }
    }

    /**
     * so createMovement takes the mouse event and generates a keyboard action out of it.
     * Keyboard action is posted on the EventBus.
     * onMessageEvent catches it and makes an action out of it which is then done by the actor.
     */
    public void createMovement()
    {
        //logger.info("mouse position: {}", MouseInfo.getPointerInfo().getLocation());
        if (CursorUtils.getCursor() != null)
        {
            switch (CursorUtils.getCursor().getName())
            {
                case "westCursor":
                    EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.WEST));
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
                case "eastCursor":
                    EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.EAST));
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
                case "northCursor":
                    EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.NORTH));
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
                case "southCursor":
                    EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.SOUTH));
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
                default:
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
            }
        }
        else
        {
            logger.error("Cursor is null");
            Game.getCurrent().stopGame();
        }
    }

    public InventoryDialog getInventoryDialog()
    {
        return inventoryDialog;
    }

    public Timer getPressedTimer()
    {
        return pressedTimer;
    }


    public StatsDialog getStatsDialog()
    {
        return statsDialog;
    }



    public boolean isMousePressed()
    {
        return mousePressed;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //logger.info("mouse clicked at: {}, {}", e.getX(), e.getY());
    }

    @Override
    /*
     * mouse dragged is being used for dragging the mouse
     * if right click, then only show cursor
     * if left click, its drag and drop, perhaps.
     */
    public void mouseDragged(MouseEvent e)
    {

        //logger.info("mouse dragged:");
        /*
         * if (!isSelectTile()) { CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), new Point(e.getX(), e.getY())); }
         */
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            if (!UIStateMachine.isSelectTile()) {
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            }
        }

        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (WindowBuilder.getGridCanvas().isDragEnabled()) {
                final TransferHandler transferHandler = WindowBuilder.getGridCanvas().getTransferHandler();
                if (transferHandler != null) {
                    // TODO here could be more "logic" to initiate the drag
                    transferHandler.exportAsDrag(WindowBuilder.getGridCanvas(), e, DnDConstants.ACTION_MOVE);
                } else {
                    logger.info("hmmm");
                }
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        setMouseOutsideOfGrid(false);
        WindowBuilder.getGridCanvas().requestFocusInWindow();
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        setMouseOutsideOfGrid(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
    }

    public boolean isMouseOutsideOfGrid()
    {
        return mouseOutsideOfGrid;
    }

    public void setMouseOutsideOfGrid(boolean mouseOutsideOfGrid)
    {
        this.mouseOutsideOfGrid = mouseOutsideOfGrid;
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (getCurrentAction() != null)
        {
            if (getCurrentAction().getType().equals(KeyboardActionType.ATTACK))
            {
                if (Game.getCurrent().getCurrentPlayer().getWeapon().getType().equals(WeaponTypes.MELEE))
                {
                    CursorUtils.limitMouseMovementToRange(1);
                }
            }
        }
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        //logger.info("mouse pressed");
        if (this.getCurrentAction() != null)
        {
            //logger.info("current action: {}", this.getCurrentAction());
            logger.info("we have a running action, dont do drag!");
        }
        else
        {
            //logger.info("if there is something draggable, drag");
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                var c = (JGridCanvas) e.getSource();
                var handler = c.getTransferHandler();
                MapTile tile = MapUtils.calculateMapTileUnderCursor(e.getPoint());
                if (tile == null)
                {
                    return;
                }
                if (tile.isHidden())
                {
                    logger.info("tile {} is not visible right now", tile);
                    return;
                }

                if (tile.getInventory().isEmpty())
                {
                    logger.info("no inventory");
                }
                else
                {
                    handler.exportAsDrag(c, e, TransferHandler.MOVE);
                }
            }

            if (e.getButton() == MouseEvent.BUTTON3)
            {
                //we are not in cross hair mode, need to figure out a way to identify drag drop!
                if (!UIStateMachine.isSelectTile()) {
                    int delay = 500; // milliseconds
                    logger.info("we are in movement mode with mouse 3");
                    ActionListener taskPerformer = new MouseActionListener(this);
                    pressedTimer = new Timer(delay, taskPerformer);
                    pressedTimer.setRepeats(true);
                    pressedTimer.start();
                }
            }
        }
    }


    /**
     * So mouseReleased creates a KeyboardAction depending on the cursor type. which is a string. which is not a safe way. but it works. ActionFactory included, this is mostly for show reasons,
     * functionality is zero <a href="https://stackoverflow.com/questions/44615276/thread-safe-find-and-remove-an-object-from-a-collection">https://stackoverflow.com/questions/44615276/thread-safe-find-and-remove-an-object-from-a-collection</a>
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        // we are in movement mode
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            logger.info("stop pressing mouse3");
            if (!UIStateMachine.isSelectTile()) {
                if (pressedTimer != null) {
                    pressedTimer.stop();
                }
                //logger.info("mouse released");
                if (isMousePressed()) {
                    // logger.info("do nothing");
                    setMousePressed(false);
                }
                else
                {
                    // logger.info("do something");
                    createMovement();
                }
            }
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        }
        if (e.getButton() == MouseEvent.BUTTON1)
        // this now only works for get/talk/drop
        {
            MapTile tile = MapUtils.calculateMapTileUnderCursor(new Point(e.getX(), e.getY()));

            if (tile == null)
            {
                logger.info("no maptile found, mouse click must have been outside of grid");
                return;
            }

            if (tile.isHidden())
            {
                logger.info("tile {} is not visible right now", tile);
                return;
            }


            if (this.getCurrentAction() != null)
            {
                switch (this.getCurrentAction().getType())
                {
                    case GET:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        break;
                    case LOOK:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        break;

                    case DROP:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        getCurrentAction().setAffectedItem(this.getCurrentItemInHand());
                        break;
                    case TALK:
                        boolean found = false;
                        getCurrentAction().setHaveNPCAction(true);
                        NPC npc = null;
                        for (NPC n : Game.getCurrent().getCurrentMap().getNpcs())
                        {
                            if (n.getMapPosition().equals(tile.getMapPosition()))
                            {
                                found = true;
                                npc = n;
                            }
                        }
                        if (found)
                        {
                            logger.info("found the npc");
                            UIStateMachine.setSelectTile(false);
                            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                            getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                            if (UIStateMachine.isDialogOpened() == true) {
                                break;
                            } else {
                                UIStateMachine.setDialogOpened(true);
                                AbstractDialog.createDialog(WindowBuilder.getFrame(), "Talk", false, getCurrentAction(), npc);
                                logger.info("talk: {}", "");
                            }
                        }
                        else
                        {
                            logger.info("no NPC here!");
                            setCurrentAction(new AbstractKeyboardAction());
                        }
                        break;

                    case MOVE:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(false);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        break;

                    case ATTACK:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                        getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + this.getCurrentAction().getType());
                }

                /*
                 * special case for movement, queque needs to be filled up and run
                 * not sure I like this
                 */
                if (getCurrentAction().getType().equals(KeyboardActionType.MOVE))
                {
                    runActions(getCurrentAction());
                    runQueue();
                }
                else
                {
                    runActions(getCurrentAction());
                }
            }
        }
    }

    public void runQueue()
    {
        Game.getCurrent().getQuequeTimer().start();
        for (AbstractKeyboardAction ac : Game.getCurrent().getCommandQueue().getActionList())
        {
            logger.info("ac: {}", ac);
            //runActions(ac, true);
        }
    }

    private void runActions(AbstractKeyboardAction action) {
        //logger.info("Current action: {}", action);
        Game.getCurrent().getCurrentPlayer().doAction(new PlayerAction(action));
        //Game.getCurrent().getIdleTimer().stop();
        WindowBuilder.getTextArea().append(action.getType().name());
        WindowBuilder.getTextArea().append("\n");
        WindowBuilder.getTextField().setText(action.getType().name());
        // move to next player
        if (Game.getCurrent().getCurrentPlayer().getNumber() < (Game.getCurrent().getPlayers().size() - 1)) {
            Game.getCurrent().setCurrentPlayer(Game.getCurrent().getPlayers().get(Game.getCurrent().getCurrentPlayer().getNumber() + 1));

        }
        // all players have moved - en is no player.
        // NPCs are handled in game because the npcs on the current map
        // are loaded into game
        else
        {
            WindowBuilder.getUndoButton().setEnabled(true);
            Game.getCurrent().setCurrentPlayer(Game.getCurrent().getPlayers().get(0));
            //Game.getCurrent().advanceTurn(hasNPCAction);
            EventBus.getDefault().post(new AdvanceTurnEvent(action.isHaveNPCAction()));
        }
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        setCurrentAction(null);
    }

    @Subscribe
    public void onMessageEvent(AbstractKeyboardAction action)
    {
        //logger.info("Event in MainWindow: {}", action.getType());
        switch (action.getType())
        {
            case EQ:
                if (UIStateMachine.isDialogOpened() == true) {
                    logger.info("We do not stack dialogs for now");
                    break;
                }
                UIStateMachine.setDialogOpened(true);
                action.setHaveNPCAction(false);
                Game.getCurrent().getIdleTimer().stop();
                AbstractDialog.createDialog(WindowBuilder.getFrame(), "Equipment", false, action);
                break;

            case SPACE:
                action.setHaveNPCAction(true);
                break;

            case OPTIONS:
            {
                if (UIStateMachine.isDialogOpened() == true) {
                    logger.info("We do not stack dialogs for now");
                    break;
                }
                UIStateMachine.setDialogOpened(true);
                action.setHaveNPCAction(false);
                Game.getCurrent().getIdleTimer().stop();
                break;
            }


            case INVENTORY:
            {
                if (UIStateMachine.isDialogOpened() == true) {
                    logger.info("dialog already open");
                    break;
                } else {
                    action.setHaveNPCAction(false);

                    logger.info("inventory as separate event type, lets not add this to the action queue");
                    Game.getCurrent().getIdleTimer().stop();
                    UIStateMachine.setDialogOpened(true);
                    AbstractDialog.createDialog(WindowBuilder.getFrame(), "Inventory", false, action);

                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
                }
            }

            case ZSTATS:
            {
                if (UIStateMachine.isDialogOpened() == true) {
                    break;
                } else {
                    action.setHaveNPCAction(false);

                    logger.info("zstats as separate event type, lets not add this to the action queue");
                    Game.getCurrent().getIdleTimer().stop();
                    UIStateMachine.setDialogOpened(true);
                    AbstractDialog.createDialog(WindowBuilder.getFrame(), "Z-Stats", false, action);
                    logger.info("stats: {}", Game.getCurrent().getCurrentPlayer().getAttributes());
                    break;
                }
            }

            case DROP:
            {
                if (UIStateMachine.isSelectTile() == true) {
                    logger.info("select tile is active, dont do anything");
                    break;
                }
                if (UIStateMachine.isDialogOpened() == true) {
                    break;
                } else {
                    Game.getCurrent().getIdleTimer().stop();
                    action.setHaveNPCAction(false);
                    UIStateMachine.setSelectTile(true);
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    setCurrentAction(action);
                    AbstractDialog.createDialog(WindowBuilder.getFrame(), "Inventory", false, action);
                    break;
                }
            }

            case ENTER:
            {
                action.setHaveNPCAction(true);
                break;
            }

            case ESC:
            {
                //logger.info("ESC Pressed");
                if (UIStateMachine.isSelectTile() == true) {
                    logger.info("selection is true");
                    UIStateMachine.setSelectTile(false);
                    setMovementForSelectTile(false);
                    Game.getCurrent().getIdleTimer().start();
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    setCurrentAction(null);
                    break;
                } else
                {
                    logger.info("stopping game");
                    WindowBuilder.getFrame().dispatchEvent(new WindowEvent(WindowBuilder.getFrame(), WindowEvent.WINDOW_CLOSING));
                    Game.getCurrent().stopGame();
                    break;
                }
            }

            case GET:
            {
                if (UIStateMachine.isSelectTile() == true) {
                    //logger.info("select tile is active, dont do anything");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    Game.getCurrent().getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                if (isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                //logger.info("get");
                Game.getCurrent().getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                setCurrentAction(action);
                break;
            }

            case LOOK:
                if (UIStateMachine.isSelectTile() == true) {
                    //logger.info("select tile is active, dont do anything");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    Game.getCurrent().getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                if (isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                //logger.info("get");
                Game.getCurrent().getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                setCurrentAction(action);
                break;

            case TALK:
            {
                if (UIStateMachine.isSelectTile() == true) {
                    logger.info("select tile is active, dont do anything");
                    break;
                }
                //logger.info("talk");
                if (UIStateMachine.isDialogOpened() == true) {
                    break;
                } else {
                    if (isMouseOutsideOfGrid() == true) {
                        CursorUtils.centerCursorOnPlayer();
                    }
                    action.setHaveNPCAction(false);
                    Game.getCurrent().getIdleTimer().stop();
                    UIStateMachine.setSelectTile(true);
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    setCurrentAction(action);
                    break;
                }
            }

            case MOVE:
            {
                logger.info("move");
                logger.info("currently broken");
                break;
               /* if (isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }

                if (UIStateMachine.isSelectTile() == true)
                {
                    logger.info("select tile is active, dont do anything");
                    break;
                }
                else
                {
                    action.setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    action.setGetWhere(new Point(tile.getX(), tile.getY()));
                    Game.getCurrent().getIdleTimer().stop();
                    runActions(getCurrentAction());
                    logger.info("move here");



                    action.setHaveNPCAction(false);
                    Game.getCurrent().getIdleTimer().stop();
                    UIStateMachine.setSelectTile(true);
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    //TODO why is get currect Action null?
                    //TODO action framework needs to be cleaned up
                    action.setGetWhere(new Point(tile.getX(), tile.getY()));
                    setCurrentAction(action);

                    break;
                }*/
            }

            case ATTACK:
                //second time a is pressed
                //action is already set
                if (UIStateMachine.isSelectTile() == true) {
                    logger.info("select tile is active, allow attack");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                    getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
                    Game.getCurrent().getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                //logger.info("attack");
                if (isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                Game.getCurrent().getIdleTimer().stop();
                //ranged
                if (Game.getCurrent().getCurrentPlayer().getWeapon() != null)
                {
                    if (Game.getCurrent().getCurrentPlayer().getWeapon().getType().equals(WeaponTypes.RANGED))
                    {
                        action.setOldMousePosition(MouseInfo.getPointerInfo().getLocation());
                        Point relativePoint = WindowBuilder.getGridCanvas().getLocationOnScreen();
                        CursorUtils.moveMouse(new Point(NPCUtils.calculatePlayerPosition().x + relativePoint.x, NPCUtils.calculatePlayerPosition().y + relativePoint.y));
                        action.setSourceCoordinates(NPCUtils.calculatePlayerPosition());
                        CursorUtils.moveMouse(action.getOldMousePosition());

                        UIStateMachine.setSelectTile(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        setCurrentAction(action);
                    }
                    //melee
                    else
                    {
                        logger.info("real melee");
                        CursorUtils.centerCursorOnPlayer();
                        UIStateMachine.setSelectTile(true);
                        setCurrentAction(action);
                    }
                }
                //no weapon, also use melee for now
                else
                {
                    logger.info("unarmed melee");
                    CursorUtils.centerCursorOnPlayer();
                    UIStateMachine.setSelectTile(true);
                    //CursorUtils.limitMouseMovementToRange(1);
                    setCurrentAction(action);
                }
                break;

            case SEARCH:
                action.setHaveNPCAction(false);
                setCurrentAction(action);
                break;


            //for movement: if its selectTile(),
            //then move cursor grid by grid :D
            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
                if (UIStateMachine.isSelectTile()) {
                    //logger.info("select tile");
                    moveCursorOnGrid(action);
                    setMovementForSelectTile(true);
                    action = new AbstractKeyboardAction();
                    break;

                } else {
                    //logger.info("movement");
                    action.setHaveNPCAction(true);
                    break;
                }
                // default is what?
            default:
            {
                logger.info("remaining action: {}", action.getType());
                break;
            }
        }

        logger.info("action: {}", action.getType());
        if (action.getType().equals(KeyboardActionType.MOVE))
        {

            runActions(action);
            runQueue();
        }
        else
        {

            if (action.isActionimmediately() == true)
            {
                runActions(action);
            }
        }
    }

    private void moveCursorOnGrid(AbstractKeyboardAction action)
    {
        //CursorUtils.centerCursorOnPlayer();
        CursorUtils.moveCursorByOneTile(action, isMovementForSelectTile());
    }


    public void setInventoryDialog(InventoryDialog inventoryDialog)
    {
        this.inventoryDialog = inventoryDialog;
    }

    public void setMousePressed(boolean mousePressed)
    {
        GameUtils.showStackTrace("setMousePressed");
        this.mousePressed = mousePressed;
    }

    public void setPressedTimer(Timer pressedTimer)
    {
        this.pressedTimer = pressedTimer;
    }

    public void setStatsDialog(StatsDialog statsDialog)
    {
        this.statsDialog = statsDialog;
    }

    @Override
    public void windowActivated(WindowEvent e)
    {
        logger.info("activated");
        if (GameConfiguration.playMusic)
        {
            //Game.getCurrent().getSoundSystem().startMusic();
            //Game.getCurrent().getSoundSystemNoThread().startMusic();
            Game.getCurrent().getMusicSystemNoThread().continueMusic();
        }
        Game.getCurrent().getIdleTimer().start();
        //focusManager.dispatchEvent(new FocusEvent(gridCanvas, FocusEvent.FOCUS_GAINED, false));
    }

    @Override
    public void windowClosed(WindowEvent e)
    {

    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        // logger.info("WindowListener method called: windowClosing.");
        // can only be null for pure UI testing
        if (Game.getCurrent() != null)
        {
            Game.getCurrent().stopGame();
        }
        // do a hard stop of the game when the UI exits
        // how much sense does this make?
        // doesnt matter for now
        else
        {
            System.exit(0);
        }

    }


    /**
     *
     */
    @Override
    public void windowDeactivated(WindowEvent e)
    {
        logger.info("deactivated");
        // if (Game.getCurrent().getSoundSystem().isMusicIsRunning())
        // {
        if (UIStateMachine.isDialogOpened() == false) {
            if (GameConfiguration.playMusic) {
                //Game.getCurrent().getSoundSystem().stopMusic();
                //Game.getCurrent().getSoundSystemNoThread().stopMusic();
                Game.getCurrent().getMusicSystemNoThread().pauseMusic();
            }
        }
        // }
        Game.getCurrent().getIdleTimer().stop();
    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
        logger.info("deiconified");
        if (GameConfiguration.playMusic)
        {
            //Game.getCurrent().getSoundSystem().startMusic();
            //Game.getCurrent().getSoundSystemNoThread().startMusic();
            Game.getCurrent().getMusicSystemNoThread().continueMusic();
        }
        Game.getCurrent().getIdleTimer().start();
    }

    @Override
    public void windowIconified(WindowEvent e)
    {
        logger.info("iconified");
        if (GameConfiguration.playMusic)
        {
            //Game.getCurrent().getSoundSystem().stopMusic();
            //Game.getCurrent().getSoundSystemNoThread().stopMusic();
            Game.getCurrent().getMusicSystemNoThread().pauseMusic();
        }
        Game.getCurrent().getIdleTimer().stop();
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
        WindowBuilder.getGridCanvas().requestFocus();
        Game.getCurrent().getHighlightTimer().start();
        //Game.getCurrent().setUiOpen(true);
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        logger.info("who gained the focus here: {}", e.getComponent().getName());
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        logger.info("who lost the focus here: {}", e.getComponent().getName());
    }

//	public void moveMouse(Point p)
//	{
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice[] gs = ge.getScreenDevices();
//
//		// Search the devices for the one that draws the specified point.
//		for (GraphicsDevice device : gs)
//		{
//			GraphicsConfiguration[] configurations = device.getConfigurations();
//			for (GraphicsConfiguration config : configurations)
//			{
//				Rectangle bounds = config.getBounds();
//				if (bounds.contains(p))
//				{
//					// Set point to screen coordinates.
//					Point b = bounds.getLocation();
//					Point s = new Point(p.x - b.x, p.y - b.y);
//
//					try
//					{
//						Robot r = new Robot(device);
//						r.mouseMove(s.x, s.y);
//					}
//					catch (AWTException e)
//					{
//						e.printStackTrace();
//					}
//
//					return;
//				}
//			}
//		}
//		// Couldn't move to the point, it may be off screen.
//	}

    public AbstractKeyboardAction getCurrentAction()
    {
        return currentAction;
    }

    public void setCurrentAction(AbstractKeyboardAction currentAction)
    {
        if (currentAction != null)
        {
            logger.info("setting current action: {}", currentAction.getType());
        }
        this.currentAction = currentAction;
    }

    public AbstractItem getCurrentItemInHand()
    {
        return currentItemInHand;
    }

    public void setCurrentItemInHand(AbstractItem currentItemInHand)
    {
        this.currentItemInHand = currentItemInHand;
    }

    public boolean isMovementForSelectTile()
    {
        return movementForSelectTile;
    }

    public void setMovementForSelectTile(boolean movementForSelectTile)
    {
        this.movementForSelectTile = movementForSelectTile;
    }
}

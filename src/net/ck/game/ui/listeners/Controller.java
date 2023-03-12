package net.ck.game.ui.listeners;


import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.game.Game;
import net.ck.game.backend.state.NoiseManager;
import net.ck.game.backend.state.TimerManager;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.WeaponTypes;
import net.ck.game.map.MapTile;
import net.ck.game.ui.components.JGridCanvas;
import net.ck.game.ui.dialogs.AbstractDialog;
import net.ck.game.ui.dialogs.InventoryDialog;
import net.ck.game.ui.dialogs.StatsDialog;
import net.ck.game.ui.state.UIState;
import net.ck.game.ui.state.UIStateMachine;
import net.ck.util.CodeUtils;
import net.ck.util.CursorUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
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
import java.io.*;


/**
 * MainWindow is the "UI Application Class" that only keeps together the controls in order to be able to have the game work without the UI being instantiated (i.e. testing!!!) this needs to be
 * encapsulated better
 *
 * @author Claus
 */
public class Controller implements WindowListener, ActionListener, MouseListener, MouseMotionListener, FocusListener
{
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
     * is drag Enabled
     */
    private boolean dragEnabled;

    /**
     * standard constructor
     */
    public Controller()
    {
        EventBus.getDefault().register(this);
        UIStateMachine.setMouseOutsideOfGrid(true);
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
        if (e.getActionCommand().equalsIgnoreCase("Debug"))
        {
            logger.info("Debug");
            TimerManager.getIdleTimer().stop();
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
                NoiseManager.getMusicSystemNoThread().continueMusic();
                //EventBus.getDefault().post(new GameStateChanged(GameState.WORLD));
            }

        }

        if (e.getActionCommand().equalsIgnoreCase("StopMusic"))
        {
            logger.info("stop music");
            if (GameConfiguration.playMusic)
            {
                //Game.getCurrent().getSoundSystem().stopMusic();
                NoiseManager.getMusicSystemNoThread().pauseMusic();
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Louder"))
        {
            logger.info("louder");
            if (GameConfiguration.playMusic)
            {
                NoiseManager.getMusicSystemNoThread().increaseVolume();
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Leiser"))
        {
            logger.info("leiser");
            if (GameConfiguration.playMusic)
            {
                NoiseManager.getMusicSystemNoThread().decreaseVolume();
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Save"))
        {
            logger.info("save");
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream;

            try
            {
                fileOutputStream = new FileOutputStream("test.txt");
            } catch (FileNotFoundException ex)
            {
                throw new RuntimeException(ex);
            }

            try
            {
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            try
            {
                objectOutputStream.writeObject(Game.getCurrent());
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            try
            {
                objectOutputStream.close();
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("Load"))
        {
            logger.info("load");
            FileInputStream fileInputStream;
            ObjectInputStream objectInputStream;

            try
            {
                fileInputStream = new FileInputStream("test.txt");
            } catch (FileNotFoundException ex)
            {
                throw new RuntimeException(ex);
            }

            try
            {
                objectInputStream = new ObjectInputStream(fileInputStream);
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            //TODO what to do here?

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
            if (!UIStateMachine.isSelectTile())
            {
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
            }
        }

        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (WindowBuilder.getGridCanvas().isDragEnabled())
            {
                final TransferHandler transferHandler = WindowBuilder.getGridCanvas().getTransferHandler();
                if (transferHandler != null)
                {
                    // TODO here could be more "logic" to initiate the drag
                    transferHandler.exportAsDrag(WindowBuilder.getGridCanvas(), e, DnDConstants.ACTION_MOVE);
                }
                else
                {
                    logger.info("hmmm");
                }
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        UIStateMachine.setCurrentMousePosition(MouseInfo.getPointerInfo().getLocation());
        UIStateMachine.setMouseOutsideOfGrid(false);
        WindowBuilder.getGridCanvas().requestFocusInWindow();
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        UIStateMachine.setCurrentMousePosition(null);
        UIStateMachine.setCurrentSelectedTile(null);
        UIStateMachine.setMouseOutsideOfGrid(true);
        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
    }



    /**
     * make sure we catch the selected tile, but only if selectTile is filled
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        UIStateMachine.setCurrentMousePosition(MouseInfo.getPointerInfo().getLocation());
        //
        if (UIStateMachine.isSelectTile() == true)
        {
            if (UIStateMachine.getCurrentSelectedTile() != MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())))
            {
                UIStateMachine.setCurrentSelectedTile(MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())));
                WindowBuilder.getGridCanvas().paint();
                //WindowBuilder.getGridCanvas().paint(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).x - 10, CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()).y - 10 , GameConfiguration.tileSize + 20, GameConfiguration.tileSize + 20 );
                //logger.debug("calling paint");
            }
            else
            {
                //logger.debug("same tile is selected, do not redraw");
            }
        }
        else
        {
            //logger.debug("select tile is not active");
        }


        if (getCurrentAction() != null)
        {
            if (getCurrentAction().getType().equals(KeyboardActionType.ATTACK))
            {
                if (Game.getCurrent().getCurrentPlayer().getWeapon() != null)
                {
                    CursorUtils.limitMouseMovementToRange(Game.getCurrent().getCurrentPlayer().getWeapon().getRange());
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
                if (!UIStateMachine.isSelectTile())
                {
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
            if (!UIStateMachine.isSelectTile())
            {
                if (pressedTimer != null)
                {
                    pressedTimer.stop();
                }
                //logger.info("mouse released");
                if (isMousePressed())
                {
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
                    case PUSH:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        break;

                    case YANK:
                        UIStateMachine.setSelectTile(false);
                        getCurrentAction().setHaveNPCAction(true);
                        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                        getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                        break;


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
                        getCurrentAction().setHaveNPCAction(false);
                        LifeForm npc = null;
                        if (tile.getLifeForm() != null)
                        {
                            found = true;
                            npc = tile.getLifeForm();
                        }

                        if (found)
                        {
                            logger.info("found the npc");
                            UIStateMachine.setSelectTile(false);

                            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                            getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                            if (UIStateMachine.isDialogOpened() == true)
                            {
                                break;
                            }
                            else
                            {
                                UIStateMachine.setDialogOpened(true);
                                AbstractDialog.createDialog(WindowBuilder.getFrame(), "Talk", false, getCurrentAction(), npc);
                                logger.info("talk: {}", "");
                                TimerManager.getIdleTimer().stop();
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
        TimerManager.getQuequeTimer().start();
        for (AbstractKeyboardAction ac : Game.getCurrent().getCurrentPlayer().getQueuedActions().getActionList())
        {
            logger.info("ac: {}", ac);
            //runActions(ac, true);
        }
    }

    private void runActions(AbstractKeyboardAction action)
    {
        //WindowBuilder.getTextArea().append(action.getType().name());
        //WindowBuilder.getTextArea().append("\n");
        //WindowBuilder.getTextField().setText(action.getType().name());

        // all players have moved - en is no player.
        // NPCs are handled in game because the npcs on the current map
        // are loaded into game
        PlayerAction ac = new PlayerAction(action);
        EventBus.getDefault().post(new AdvanceTurnEvent(ac));

        CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        setCurrentAction(null);
    }

    @Subscribe
    public void onMessageEvent(AbstractKeyboardAction action)
    {
        switch (action.getType())
        {
            case PUSH:
            {
                //CursorUtils.centerCursorOnPlayer();
                if (UIStateMachine.isSelectTile() == true)
                {
                    //logger.info("select tile is active, dont do anything");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    TimerManager.getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                if (UIStateMachine.isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                //logger.info("get");
                TimerManager.getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                setCurrentAction(action);
                break;
            }
            case YANK:
            {
                //CursorUtils.centerCursorOnPlayer();
                if (UIStateMachine.isSelectTile() == true)
                {
                    //logger.info("select tile is active, dont do anything");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    TimerManager.getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                if (UIStateMachine.isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                //logger.info("get");
                TimerManager.getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                setCurrentAction(action);
                break;
            }
            case EQ:
                if (UIStateMachine.isDialogOpened() == true)
                {
                    logger.info("We do not stack dialogs for now");
                    break;
                }
                UIStateMachine.setDialogOpened(true);
                action.setHaveNPCAction(false);
                TimerManager.getIdleTimer().stop();
                AbstractDialog.createDialog(WindowBuilder.getFrame(), "Equipment", false, action);
                break;

            case SPACE:
                action.setHaveNPCAction(true);
                break;

            case OPTIONS:
            {
                if (UIStateMachine.isDialogOpened() == true)
                {
                    logger.info("We do not stack dialogs for now");
                    break;
                }
                UIStateMachine.setDialogOpened(true);
                action.setHaveNPCAction(false);
                TimerManager.getIdleTimer().stop();
                break;
            }


            case INVENTORY:
            {
                if (UIStateMachine.isDialogOpened() == true)
                {
                    logger.info("dialog already open");
                    break;
                }
                else
                {
                    action.setHaveNPCAction(false);

                    logger.info("inventory as separate event type, lets not add this to the action queue");
                    TimerManager.getIdleTimer().stop();
                    UIStateMachine.setDialogOpened(true);
                    AbstractDialog.createDialog(WindowBuilder.getFrame(), "Inventory", false, action);

                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    break;
                }
            }

            case ZSTATS:
            {
                if (UIStateMachine.isDialogOpened() == true)
                {
                    break;
                }
                else
                {
                    action.setHaveNPCAction(false);

                    logger.info("zstats as separate event type, lets not add this to the action queue");
                    TimerManager.getIdleTimer().stop();
                    UIStateMachine.setDialogOpened(true);
                    AbstractDialog.createDialog(WindowBuilder.getFrame(), "Z-Stats", false, action);
                    logger.info("stats: {}", Game.getCurrent().getCurrentPlayer().getAttributes());
                    break;
                }
            }

            case DROP:
            {
                if (UIStateMachine.isSelectTile() == true)
                {
                    logger.info("select tile is active, dont do anything");
                    break;
                }
                if (UIStateMachine.isDialogOpened() == true)
                {
                    break;
                }
                else
                {
                    TimerManager.getIdleTimer().stop();
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
                if (UIStateMachine.isSelectTile() == true)
                {
                    logger.info("selection is true");
                    UIStateMachine.setSelectTile(false);
                    setMovementForSelectTile(false);
                    TimerManager.getIdleTimer().start();
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    setCurrentAction(null);
                    break;
                }
                else
                {
                    logger.info("stopping game");
                    WindowBuilder.getFrame().dispatchEvent(new WindowEvent(WindowBuilder.getFrame(), WindowEvent.WINDOW_CLOSING));
                    Game.getCurrent().stopGame();
                    break;
                }
            }

            case GET:
            {
                if (UIStateMachine.isSelectTile() == true)
                {
                    //logger.info("select tile is active, dont do anything");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    TimerManager.getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                if (UIStateMachine.isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                //logger.info("get");
                TimerManager.getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                setCurrentAction(action);
                break;
            }

            case LOOK:
                if (UIStateMachine.isSelectTile() == true)
                {
                    //logger.info("select tile is active, dont do anything");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    TimerManager.getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                if (UIStateMachine.isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                //logger.info("get");
                TimerManager.getIdleTimer().stop();
                UIStateMachine.setSelectTile(true);
                CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                setCurrentAction(action);
                break;

            case TALK:
            {
                if (UIStateMachine.isSelectTile() == true)
                {
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(false);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    UIStateMachine.setDialogOpened(true);
                    AbstractDialog.createDialog(WindowBuilder.getFrame(), "Talk", false, getCurrentAction(), tile.getLifeForm());
                    logger.info("talk: {}", "");
                    TimerManager.getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                //logger.info("talk");
                if (UIStateMachine.isDialogOpened() == true)
                {
                    break;
                }
                else
                {
                    if (UIStateMachine.isMouseOutsideOfGrid() == true)
                    {
                        CursorUtils.centerCursorOnPlayer();
                    }
                    action.setHaveNPCAction(false);
                    TimerManager.getIdleTimer().stop();
                    UIStateMachine.setSelectTile(true);
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    setCurrentAction(action);
                    break;
                }
            }

            case MOVE:
            {
                logger.info("move");
                if (UIStateMachine.isMouseOutsideOfGrid() == true)
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
                    action.setHaveNPCAction(false);
                    TimerManager.getIdleTimer().stop();
                    UIStateMachine.setSelectTile(true);
                    CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    //TODO why is get currect Action null?
                    //TODO action framework needs to be cleaned up
                    action.setGetWhere(new Point(tile.getX(), tile.getY()));
                    setCurrentAction(action);

                    break;
                }
            }

            case ATTACK:
                //second time a is pressed
                //action is already set
                if (UIStateMachine.isSelectTile() == true)
                {
                    logger.info("select tile is active, allow attack");
                    UIStateMachine.setSelectTile(false);
                    getCurrentAction().setHaveNPCAction(true);
                    MapTile tile = MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation()));
                    getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
                    Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
                    getCurrentAction().setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
                    TimerManager.getIdleTimer().stop();
                    runActions(getCurrentAction());
                    break;
                }
                //logger.info("attack");
                if (UIStateMachine.isMouseOutsideOfGrid() == true)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                action.setHaveNPCAction(false);
                TimerManager.getIdleTimer().stop();
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
                if (UIStateMachine.isSelectTile())
                {
                    //logger.info("select tile");
                    moveCursorOnGrid(action);
                    setMovementForSelectTile(true);
                    action = new AbstractKeyboardAction();
                    break;

                }
                else
                {
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
        //GameUtils.showStackTrace("setMousePressed");
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
        UIStateMachine.setUiState(UIState.ACTIVATED);
        logger.info("activated");
        if (GameConfiguration.playMusic)
        {
            //Game.getCurrent().getSoundSystem().startMusic();
            //Game.getCurrent().getSoundSystemNoThread().startMusic();
            NoiseManager.getMusicSystemNoThread().continueMusic();
        }
        TimerManager.getIdleTimer().start();
        //focusManager.dispatchEvent(new FocusEvent(gridCanvas, FocusEvent.FOCUS_GAINED, false));
    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        UIStateMachine.setUiState(UIState.CLOSED);
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        UIStateMachine.setUiState(UIState.CLOSED);
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
        UIStateMachine.setUiState(UIState.DEACTIVATED);
        // if (Game.getCurrent().getSoundSystem().isMusicIsRunning())
        // {
        if (UIStateMachine.isDialogOpened() == false)
        {
            if (GameConfiguration.playMusic)
            {
                NoiseManager.getMusicSystemNoThread().pauseMusic();
            }
        }
        // }
        TimerManager.getIdleTimer().stop();
    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
        UIStateMachine.setUiState(UIState.DEICONIFIED);
        logger.info("deiconified");
        if (GameConfiguration.playMusic)
        {
            NoiseManager.getMusicSystemNoThread().continueMusic();
        }
        TimerManager.getIdleTimer().start();
    }

    @Override
    public void windowIconified(WindowEvent e)
    {
        UIStateMachine.setUiState(UIState.ICONIFIED);
        logger.info("iconified");
        if (GameConfiguration.playMusic)
        {
            NoiseManager.getMusicSystemNoThread().pauseMusic();
        }
        TimerManager.getIdleTimer().stop();
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
        UIStateMachine.setUiState(UIState.OPENED);
        WindowBuilder.getGridCanvas().requestFocus();
        TimerManager.getHighlightTimer().start();
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

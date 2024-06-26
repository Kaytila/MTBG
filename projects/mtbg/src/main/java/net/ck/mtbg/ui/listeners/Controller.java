package net.ck.mtbg.ui.listeners;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.state.NoiseManager;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.ui.components.JGridCanvas;
import net.ck.mtbg.ui.dialogs.InventoryDialog;
import net.ck.mtbg.ui.dialogs.StatsDialog;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.AdvanceTurnEvent;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.ActionFactory;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import net.ck.mtbg.util.ui.ControllerDelegator;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.CursorUtils;
import net.ck.mtbg.util.utils.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;
import java.io.*;


/**
 * Controller is the "UI Application Class" that only keeps together the controls in order to be able to have the game work without the UI being instantiated (i.e. testing!!!) this needs to be
 * encapsulated better
 *
 * @author Claus
 */
@Log4j2
@Getter
@Setter
public class Controller implements WindowListener, ActionListener, MouseListener, MouseMotionListener, FocusListener
{

    /**
     * pressed mouse button timer
     */
    Timer pressedTimer;
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
     * double-click in inventory dialog leads to this being filled.
     */
    private AbstractItem currentItemInHand;
    /**
     * double-click in spell book dialog leads to this being filled.
     */
    private AbstractSpell currentSpellInHand;
    /**
     * mouse pressed is used for moving via mouse, there is a delay defined in the timer
     * that is started pressedTimer.
     */
    private boolean mousePressed;
    /**
     * this variable is being set if the num pad movement keys
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
        WindowBuilder.buildGameWindow(this);
    }

    public synchronized AbstractSpell getCurrentSpellInHand()
    {
        return currentSpellInHand;
    }

    public synchronized void setCurrentSpellInHand(AbstractSpell currentSpellInHand)
    {
        logger.debug("current spell in hand: {}", currentSpellInHand);
        this.currentSpellInHand = currentSpellInHand;
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
            /* MusicPlayerJavaFX musicPlayerJavaFX = null;
            try
            {
                musicPlayerJavaFX = new MusicPlayerJavaFX();
            } catch (InterruptedException ex)
            {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex)
            {
                throw new RuntimeException(ex);
            }
            musicPlayerJavaFX.play();
            */

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
            }
            catch (FileNotFoundException ex)
            {
                throw new RuntimeException(ex);
            }

            try
            {
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            try
            {
                objectOutputStream.writeObject(Game.getCurrent());
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
            try
            {
                objectOutputStream.close();
            }
            catch (IOException ex)
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
            }
            catch (FileNotFoundException ex)
            {
                throw new RuntimeException(ex);
            }

            try
            {
                objectInputStream = new ObjectInputStream(fileInputStream);
            }
            catch (IOException ex)
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
        if (UIStateMachine.isSelectTile())
        {
            /*
             * this can actually happen! if you start the game and keep the mouse outside the grid and then switch to spell book dialog.
             * it can be that there is no currently selected tile underneath.
             */
            if (UIStateMachine.getCurrentSelectedTile() != null)
            {
                Point relativePoint = WindowBuilder.getGridCanvas().getLocationOnScreen();
                Point p = MapUtils.calculateUIPositionFromMapOffset(UIStateMachine.getCurrentSelectedTile().getMapPosition());
                CursorUtils.moveMouse(new Point(p.x * GameConfiguration.tileSize + relativePoint.x + GameConfiguration.tileSize / 2, p.y * GameConfiguration.tileSize + relativePoint.y + GameConfiguration.tileSize / 2));
            }
        }
        else
        {
            UIStateMachine.setCurrentMousePosition(null);
            UIStateMachine.setCurrentSelectedTile(null);
            UIStateMachine.setMouseOutsideOfGrid(true);
            CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
        }
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
            if (MapUtils.calculateMapTileUnderCursor(CursorUtils.calculateRelativeMousePosition(MouseInfo.getPointerInfo().getLocation())) != null)
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
                //logger.info("no tile");
                if (UIStateMachine.getCurrentSelectedTile() == null)
                {
                    CursorUtils.centerCursorOnPlayer();
                }
                else
                {
                    Point relativePoint = WindowBuilder.getGridCanvas().getLocationOnScreen();
                    Point p = MapUtils.calculateUIPositionFromMapOffset(UIStateMachine.getCurrentSelectedTile().getMapPosition());
                    CursorUtils.moveMouse(new Point(p.x * GameConfiguration.tileSize + relativePoint.x + GameConfiguration.tileSize / 2, p.y * GameConfiguration.tileSize + relativePoint.y + GameConfiguration.tileSize / 2));
                }
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
        logger.info("mouse pressed");
        if (this.getCurrentAction() != null)
        {
            //logger.info("current action: {}", this.getCurrentAction());
            logger.info("we have a running action, dont do drag!");
        }
        else
        {
            logger.debug("get  current action: {}", this.getCurrentAction());
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
        logger.info("calling mouse released event");
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
                    {
                        ControllerDelegator.handleMouseReleasedActionPUSH(this, tile);
                        break;
                    }

                    case YANK:
                    {
                        ControllerDelegator.handleMouseReleasedActionYank(this, tile);
                        break;
                    }

                    case GET:
                    {
                        ControllerDelegator.handleMouseReleasedActionGET(this, tile);
                        break;
                    }

                    case LOOK:
                    {
                        ControllerDelegator.handleMouseReleasedActionLOOK(this, tile);
                        break;
                    }
                    case DROP:
                    {
                        ControllerDelegator.handleMouseReleasedActionDROP(this, tile);
                        break;
                    }
                    case TALK:
                    {
                        ControllerDelegator.handleMouseReleasedActionTALK(this, tile);
                        break;
                    }
                    case MOVE:
                    {
                        ControllerDelegator.handleMouseReleasedActionMOVE(this, tile);
                        break;
                    }

                    case ATTACK:
                    {
                        ControllerDelegator.handleMouseReleasedActionATTACK(this, tile);
                        break;
                    }

                    case SPELLBOOK:
                    {
                        ControllerDelegator.handleMouseReleasedActionSPELLBOOK(this, tile);
                        break;
                    }

                    case CAST:
                    {
                        ControllerDelegator.handleMouseReleasedActionCAST(this, tile);
                        break;
                    }

                    case OPEN:
                    {
                        ControllerDelegator.handleMouseReleasedActionOPEN(this, tile);
                        break;
                    }


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
            //runActions(ac);
        }
    }

    public void runActions(AbstractKeyboardAction action)
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

    /**
     * on MessageEvent is being called from
     * - createMovement() for mouse based movement in Controller
     * - JGridCanvas has a huge action map for keyboard input
     *
     * @param action a "Keyboard" (also mouse) action - which implements javax.swing.Action
     */
    @Subscribe
    public void onMessageEvent(AbstractKeyboardAction action)
    {
        /*
         * make sure that only ESC is allowed to cancel crosshairs, but no other action is valid while getCurrentAction() is not empty.
         * TODO fix this properly
         */

        if (UIStateMachine.isSelectTile())
        {
            logger.debug("cross hairs is active");
        }
        else
        {
            if (getCurrentAction() != null)
            {
                if (action.getType().equals(KeyboardActionType.ESC))
                {

                }
                else
                {
                    return;
                }
            }
        }

        if (UIStateMachine.isDialogOpened() == true)
        {
            if (action.getType().equals(KeyboardActionType.ESC))
            {

            }
            else
            {
                return;
            }
        }


        switch (action.getType())
        {
            case PUSH:
            {
                ControllerDelegator.handleKeyboardActionPUSH(this, action);
                break;
            }

            case YANK:
            {
                ControllerDelegator.handleKeyBoardActionYANK(this, action);
                break;
            }

            case EQ:
            {
                ControllerDelegator.handleKeyBoardActionEQ(this, action);
                break;
            }

            case SPACE:
            {
                ControllerDelegator.handleKeyBoardActionSPACE(this, action);
                break;
            }

            case OPTIONS:
            {
                ControllerDelegator.handleKeyBoardActionOPTIONS(this, action);
                break;
            }

            case INVENTORY:
            {
                ControllerDelegator.handleKeyBoardActionINVENTORY(this, action);
                break;
            }

            case ZSTATS:
            {
                ControllerDelegator.handleKeyBoardActionZSTATS(this, action);
                break;
            }

            case SPELLBOOK:
            {
                ControllerDelegator.handleKeyBoardActionSPELLBOOK(this, action);
                break;
            }

            case SKILLTREE:
            {
                ControllerDelegator.handleKeyBoardActionSKILLTREE(this, action);
                break;
            }


            case DROP:
            {
                ControllerDelegator.handleKeyBoardActionDROP(this, action);
                break;
            }

            case ENTER:
            {
                ControllerDelegator.handleKeyBoardActionENTER(this, action);
                break;
            }

            case ESC:
            {
                ControllerDelegator.handleKeyBoardActionESC(this, action);
                break;
            }

            case GET:
            {
                ControllerDelegator.handleKeyBoardActionGET(this, action);
                break;
            }

            case LOOK:
            {
                ControllerDelegator.handleKeyBoardActionLOOK(this, action);
                break;
            }

            case TALK:
            {
                ControllerDelegator.handleKeyBoardActionTALK(this, action);
                break;
            }

            case MOVE:
            {
                ControllerDelegator.handleKeyBoardActionMOVE(this, action);
                break;
            }

            case ATTACK:
            {
                ControllerDelegator.handleKeyBoardActionATTACK(this, action);
                break;
            }
            case SEARCH:
            {
                ControllerDelegator.handleKeyBoardActionSEARCH(this, action);
                break;
            }

            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
            {
                /*
                 * for movement: if its selectTile(),
                 * then move cursor grid by grid :D
                 * this cannot be moved, as the currently active - but not set action is being overwritten
                 */
                if (UIStateMachine.isSelectTile())
                {
                    //logger.info("select tile");
                    moveCursorOnGrid(action);
                    setMovementForSelectTile(true);
                    action = new AbstractKeyboardAction();

                }
                else
                {
                    //logger.info("movement");
                    action.setHaveNPCAction(true);
                }
                break;
            }

            case CAST:
            {
                ControllerDelegator.handleKeyBoardActionCAST(this, action);
                break;
            }

            case USE:
                logger.debug("Implement me properly");
                break;

            case OPEN:
                ControllerDelegator.handleKeyBoardActionOpen(this, action);
                break;

            default:
            {
                logger.info("remaining action: {}", action.getType());
                break;
            }
        }

        //logger.debug("selected action: {}", action.getType());
        if (this.getCurrentAction() != null)
        {
            //logger.debug("active action: {}", this.getCurrentAction().getType());
        }

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

    public void moveCursorOnGrid(AbstractKeyboardAction action)
    {
        //CursorUtils.centerCursorOnPlayer();
        CursorUtils.moveCursorByOneTile(action, isMovementForSelectTile());
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
        if (UIStateMachine.isSelectTile() == true)
        {

        }
        else
        {
            TimerManager.getIdleTimer().start();
        }
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
        TimerManager.getIdleTimer().start();
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


    public void setCurrentAction(AbstractKeyboardAction currentAction)
    {
        if (currentAction != null)
        {
            //logger.info("setting current action: {}", currentAction.getType());
        }
        this.currentAction = currentAction;
    }


}

package net.ck.game.ui;

import net.ck.game.backend.Game;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.NPC;
import net.ck.game.items.AbstractItem;
import net.ck.game.map.MapTile;
import net.ck.util.CursorUtils;
import net.ck.util.MapUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.ActionFactory;
import net.ck.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.util.Objects;

/**
 * MainWindow is the "UI Application Class" that only keeps together the controls in order to be able to have the game work without the UI being instantiated (i.e. testing!!!) this needs to be
 * encapsulated better
 * 
 * @author Claus
 *
 */
public class MainWindow implements WindowListener, ActionListener, MouseListener, MouseMotionListener, FocusListener
{

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@SuppressWarnings(value =
	{"unused"})
	public static void main(String[] args)
	{
		MainWindow window = new MainWindow();
	}

	/**
	 * mainframe
	 */
	private JFrame frame;

	/**
	 * left part, GRID Canvas
	 */
	private JGridCanvas gridCanvas;

	private AbstractKeyboardAction currentAction;

	/**
	 * inventoryDialog
	 */
	private InventoryDialog inventoryDialog;

	/**
	 * set if a dialog is opened. Game is supposed to pause here
	 */
	private boolean isDialogOpened = false;

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private AbstractItem currentItemInHand;
	/**
	 * mouse pressed is used for moving via mouse, there is a delay defined which is somewhere
	 */
	private boolean mousePressed;
	/**
	 * pressed mouse button timer
	 */
	Timer pressedTimer;

	/**
	 * select Tile is being used whenever - the game shall pause - the cursor shall switch to cross-hairs
	 */
	private boolean selectTile;

	public boolean isSelectTile()
	{
		return selectTile;
	}

	public void setSelectTile(boolean selectTile)
	{
		this.selectTile = selectTile;
	}

	public Logger getLogger()
	{
		return logger;
	}

	/**
	 * stats Dialog - there will be one dialog only with exchanging JPanels
	 */
	private StatsDialog statsDialog;

	private TextList textArea;

	private InputField textField;

	private JButton undoButton;

	/**
	 * weather canvas
	 */
	private JWeatherCanvas weatherCanvas;

	private boolean mouseOutsideOfGrid;

	private boolean dragEnabled;


	private KeyboardFocusManager focusManager;

	/**
	 * standard constructor
	 */
	public MainWindow()
	{
		buildWindow();

	}


	/**
	 * this is called when the undo button is clicked. the game retracts the last turn, clears the current turn, removes all content from the UI. if the game is at the first turn again, disable the
	 * button
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equalsIgnoreCase("Undo"))
		{
			logger.info("undo");
			Game.getCurrent().getIdleTimer().stop();
			if (Game.getCurrent().retractTurn() == 0)
			{
				getUndoButton().setEnabled(false);
			}
			else
			{
				getUndoButton().setEnabled(true);
			}
		}

		if (e.getActionCommand().equalsIgnoreCase("Cancel"))
		{
			logger.info("Cancel");
		}

		if (e.getActionCommand().equalsIgnoreCase("OK"))
		{
			logger.info("OK");
		}
	}

	public void buildWindow()
	{
		logger.info("start: build window");
		frame = new MainFrame();
		MyFocusListener myFocusListener = new MyFocusListener();
		undoButton = new UndoButton(new Point(700 - 200, 620));
		undoButton.addFocusListener(myFocusListener);
		frame.add(undoButton);




		gridCanvas = new JGridCanvas();
		gridCanvas.addFocusListener(myFocusListener);
		frame.add(gridCanvas);

		textArea = new TextList();
		textArea.addFocusListener(myFocusListener);
		frame.add(textArea.initializeScrollPane());

		textField = new InputField();
		textField.addFocusListener(myFocusListener);
		frame.add(textField);

		weatherCanvas = new JWeatherCanvas();
		weatherCanvas.addFocusListener(myFocusListener);
		frame.add(weatherCanvas);

		logger.info("setting listeners");
		frame.addWindowListener(this);

		gridCanvas.addMouseListener(this);
		gridCanvas.addMouseMotionListener(this);
		undoButton.addActionListener(this);

		setMouseOutsideOfGrid(true);
		frame.setVisible(true);

		DragGestureRecognizer dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDragGestureHandler(gridCanvas));
		DropTarget dt = new DropTarget(gridCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new JGridCanvasDropTargetHandler(gridCanvas), true);
		gridCanvas.setDropTarget(dt);

		logger.info("finish: build window");
		logger.info("setting up event bus");
		EventBus.getDefault().register(this);
		Game.getCurrent().setController(this);
	}

	/**
	 * so createMovement takes the mouse event and generates a keyboard action out of it. Keyboard action is posted on the EventBus. onMessageEvent catches it and makes an action out of it which is
	 * then done by the actor.
	 */
	public void createMovement()
	{
		//logger.info("mouse position: {}", MouseInfo.getPointerInfo().getLocation());
		if (CursorUtils.getCursor() != null)
		{
			switch (CursorUtils.getCursor().getName())
			{
				case "westCursor" :
					EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.WEST));
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					break;
				case "eastCursor" :
					EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.EAST));
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					break;
				case "northCursor" :
					EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.NORTH));
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					break;
				case "southCursor" :
					EventBus.getDefault().post(ActionFactory.createAction(KeyboardActionType.SOUTH));
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					break;
				default :
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

	public JFrame getFrame()
	{
		return frame;
	}

	public JGridCanvas getGridCanvas()
	{
		return this.gridCanvas;
	}

	public InventoryDialog getInventoryDialog()
	{
		return inventoryDialog;
	}

	public Timer getPressedTimer()
	{
		return pressedTimer;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public StatsDialog getStatsDialog()
	{
		return statsDialog;
	}

	public TextList getTextArea()
	{
		return textArea;
	}

	public InputField getTextField()
	{
		return textField;
	}

	public JButton getUndoButton()
	{
		return undoButton;
	}

	public JWeatherCanvas getWeatherCanvas()
	{
		return this.weatherCanvas;
	}

	public boolean isDialogOpened()
	{
		return isDialogOpened;
	}

	public boolean isMousePressed()
	{
		return mousePressed;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

		//logger.info("mouse dragged:");
		/*
		 * if (!isSelectTile()) { CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), new Point(e.getX(), e.getY())); }
		 */
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			if (!isSelectTile())
			{
				CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
			}
		}

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (gridCanvas.isDragEnabled())
			{
				final TransferHandler transferHandler = gridCanvas.getTransferHandler();
				if (transferHandler != null)
				{
					// TODO here could be more "logic" to initiate the drag
					transferHandler.exportAsDrag(gridCanvas, e, DnDConstants.ACTION_MOVE);
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
		// logger.info("mouse entered: {}", e.getPoint());
		setMouseOutsideOfGrid(false);
		getGridCanvas().requestFocusInWindow();
		// CursorUtils.setCursor(Cursor.getDefaultCursor());
		CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// logger.info("mouse exit: {}", e.getPoint());
		// CursorUtils.setCursor(Cursor.getDefaultCursor());
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
		//logger.info("mouse move relative: {}, mouse move absolute: {}", e.getPoint(), MouseInfo.getPointerInfo().getLocation());
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
				if (tile.getInventory().isEmpty())
				{

				}
				else
				{
					handler.exportAsDrag(c, e, TransferHandler.MOVE);
				}
			}

			if (e.getButton() == MouseEvent.BUTTON3)
			{
				//we are not in cross hair mode, need to figure out a way to identify drag drop!
				if (!isSelectTile())
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
	 * functionality is zero https://stackoverflow.com/questions/44615276/thread-safe-find-and-remove-an-object-from-a-collection
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{		
		// we are in movement mode
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			logger.info("stop pressing mouse3");
			if (!isSelectTile())
			{
				if (pressedTimer != null) {
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
				throw new RuntimeException("no maptile found, mouse click must have been outside of grid");
			}
			if (this.getCurrentAction() != null)
			{
				switch (this.getCurrentAction().getType())
				{
					case GET:
						setSelectTile(false);
						CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
						getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
						runActions(getCurrentAction(), true);
						break;
					case DROP:
						setSelectTile(false);
						CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
						getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
						getCurrentAction().setAffectedItem(this.getCurrentItemInHand());
						runActions(getCurrentAction(), true);
						break;
					case TALK:
						boolean found = false;
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
								setSelectTile(false);
								CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
								getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
								if (isDialogOpened == true)
								{
									break;
								}
								else
								{
									this.setDialogOpened(true);
									AbstractDialog.createDialog(this.getFrame(), "Talk", false, getCurrentAction(), npc);
									logger.info("talk: {}", "");
								}
								runActions(getCurrentAction(), true);
							}
							else
							{
								logger.info("no NPC here!");
								setCurrentAction(new AbstractKeyboardAction());
								runActions(getCurrentAction(), true);
							}
						break;

					case MOVE:
						setSelectTile(false);
						CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
						getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
						runActions(getCurrentAction(), false);
						runQueue();
						break;

					case ATTACK:
						setSelectTile(false);
						CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
						getCurrentAction().setGetWhere(new Point(tile.getX(), tile.getY()));
						logger.info("targetPx: {}, targetPy: {}", e.getX(), e.getY());
						getCurrentAction().setTargetCoordinates(new Point(e.getX(), e.getY()));
						runActions(getCurrentAction(), false);
						break;
					default:
						throw new IllegalStateException("Unexpected value: " + this.getCurrentAction().getType());
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

	private void runActions(AbstractKeyboardAction action, boolean hasNPCAction)
	{
		//logger.info("Current action: {}", action);
		Game.getCurrent().getCurrentPlayer().doAction(new PlayerAction(action, Game.getCurrent().getCurrentPlayer()));
		Game.getCurrent().getIdleTimer().stop();
		textArea.append(action.getType().name());
		textArea.append("\n");
		textField.setText(action.getType().name());
		// move to next player
		if (Game.getCurrent().getCurrentPlayer().getNumber() < (Game.getCurrent().getPlayers().size() - 1))
		{
			Game.getCurrent().setCurrentPlayer(Game.getCurrent().getPlayers().get(Game.getCurrent().getCurrentPlayer().getNumber() + 1));

		}
		// all players have moved - en is no player.
		// NPCs are handled in game because the npcs on the current map
		// are loaded into game
		else
		{
			getUndoButton().setEnabled(true);
			Game.getCurrent().setCurrentPlayer(Game.getCurrent().getPlayers().get(0));
			Game.getCurrent().advanceTurn(hasNPCAction);
		}
		CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
	}

	@Subscribe
	public void onMessageEvent(AbstractKeyboardAction action)
	{
		//logger.info("Event in MainWindow: {}", action.getType());
		boolean haveNPCAction = true;
		switch (action.getType())
		{
			case INVENTORY :
			{
				if (isDialogOpened == true)
				{
					break;
				}
				else
				{
					haveNPCAction = false;
					logger.info("inventory as separate event type, lets not add this to the action queue");
					Game.getCurrent().getIdleTimer().stop();
					this.setDialogOpened(true);
					AbstractDialog.createDialog(this.getFrame(), "Inventory", false, action);

					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					break;
				}
			}

			case ZSTATS :
			{
				if (isDialogOpened == true)
				{
					break;
				}
				else
				{
					haveNPCAction = false;
					logger.info("zstats as separate event type, lets not add this to the action queue");
					Game.getCurrent().getIdleTimer().stop();
					this.setDialogOpened(true);
					AbstractDialog.createDialog(this.getFrame(), "Z-Stats", false, action);
					logger.info("stats: {}", Game.getCurrent().getCurrentPlayer().getAttributes());
					break;
				}
			}

			case DROP :
			{
				if (isDialogOpened == true)
				{
					break;
				}
				else
				{
					haveNPCAction = false;
					Game.getCurrent().getIdleTimer().stop();
					setSelectTile(true);
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					setCurrentAction(action);
					AbstractDialog.createDialog(this.getFrame(), "Inventory", false, action);
					break;
				}
			}

			case ENTER :
			{
				haveNPCAction = false;
				break;
			}
			/*
			 * Game.getCurrent().switchMap(); Game.getCurrent().getCurrentPlayer().doAction(new PlayerAction(action, Game.getCurrent().getCurrentPlayer())); Game.getCurrent().getIdleTimer().stop();
			 * textArea.append(action.getType().name()); textArea.append("\n"); textField.setText(action.getType().name()); // move to next player if (getGame().getCurrentPlayer().getNumber() <
			 * (getGame().getPlayers().size() - 1)) { getGame().setCurrentPlayer(getGame().getPlayers().get(getGame().getCurrentPlayer().getNumber() + 1));
			 * 
			 * } // all players have moved - en is no player. // NPCs are handled in game because the npcs on the current map // are loaded into game else { getUndoButton().setEnabled(true);
			 * getGame().setCurrentPlayer(getGame().getPlayers().get(0)); } break; }
			 */
			case ESC :
			{
				logger.info("ESC Pressed");
				if (isSelectTile() == true)
				{
					logger.info("selection is true");
					setSelectTile(false);
					Game.getCurrent().getIdleTimer().start();
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					break;
				}
				else
				{
					logger.info("stopping game");
					getFrame().dispatchEvent(new WindowEvent(getFrame(), WindowEvent.WINDOW_CLOSING));
					Game.getCurrent().stopGame();
					break;
				}
			}

			case GET :
			{
				if (isMouseOutsideOfGrid() == true)
				{
					int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);// + border;
					int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);// + border;
					Point relativePoint = getGridCanvas().getLocationOnScreen();
					moveMouse(new Point(Px + relativePoint.x, Py + relativePoint.y));
					setMouseOutsideOfGrid(false);
				}
				haveNPCAction = false;
				logger.info("get");
				Game.getCurrent().getIdleTimer().stop();
				setSelectTile(true);
				CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
				setCurrentAction(action);
				break;
			}

			case TALK :
			{
				if (isDialogOpened == true)
				{
					break;
				}
				else
				{
					if (isMouseOutsideOfGrid() == true)
					{
						int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);// + border;
						int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);// + border;
						Point relativePoint = getGridCanvas().getLocationOnScreen();
						moveMouse(new Point(Px + relativePoint.x, Py + relativePoint.y));
						setMouseOutsideOfGrid(false);
					}
					haveNPCAction = false;
					logger.info("talk");
					Game.getCurrent().getIdleTimer().stop();
					setSelectTile(true);
					CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
					setCurrentAction(action);
					break;
				}
			}

			case MOVE :
			{
				if (isMouseOutsideOfGrid() == true)
				{
					int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
					int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
					Point relativePoint = getGridCanvas().getLocationOnScreen();
					moveMouse(new Point(Px + relativePoint.x, Py + relativePoint.y));
					setMouseOutsideOfGrid(false);
				}
				haveNPCAction = false;
				logger.info("move");
				Game.getCurrent().getIdleTimer().stop();
				setSelectTile(true);
				CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
				setCurrentAction(action);
				break;
			}

			case ATTACK:
				if (isMouseOutsideOfGrid() == true)
				{
					int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
					int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
					Point relativePoint = getGridCanvas().getLocationOnScreen();
					moveMouse(new Point(Px + relativePoint.x, Py + relativePoint.y));
					setMouseOutsideOfGrid(false);
				}
				haveNPCAction = false;
				logger.info("attack");
				Game.getCurrent().getIdleTimer().stop();

				action.setOldMousePosition(MouseInfo.getPointerInfo().getLocation());
				int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
				int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * Game.getCurrent().getTileSize()) + (Game.getCurrent().getTileSize() / 2);
				Point relativePoint = getGridCanvas().getLocationOnScreen();
				moveMouse(new Point(Px + relativePoint.x, Py + relativePoint.y));
				action.setSourceCoordinates(new Point(Px , Py ));
				moveMouse(action.getOldMousePosition());

				setSelectTile(true);
				CursorUtils.calculateCursorFromGridPosition(Game.getCurrent().getCurrentPlayer(), MouseInfo.getPointerInfo().getLocation());
				setCurrentAction(action);
				break;

			case SEARCH:
				setCurrentAction(action);
				break;

			// default is movement only
			default :
			{
				break;
			}
		}

		if (action.isActionimmediately() == true)
		{
			runActions(action, haveNPCAction);
		}
	}

	public void setDialogOpened(boolean isDialogOpened)
	{
		logger.info("new value: {}", isDialogOpened);
		this.isDialogOpened = isDialogOpened;
	}

	public void setFrame(JFrame frame)
	{
		this.frame = frame;
	}

	public void setGridCanvas(JGridCanvas gridCanvas)
	{
		this.gridCanvas = gridCanvas;
	}

	public void setInventoryDialog(InventoryDialog inventoryDialog)
	{
		this.inventoryDialog = inventoryDialog;
	}

	public void setMousePressed(boolean mousePressed)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logger.info("calling setMousePressed from: {} or: {}", stackTraceElements[1].getMethodName(), stackTraceElements[2].getMethodName());
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

	public void setTextArea(TextList textArea)
	{
		this.textArea = textArea;
	}

	public void setTextField(InputField textField)
	{
		this.textField = textField;
	}

	public void setUndoButton(JButton undo)
	{
		this.undoButton = undo;
	}

	public void setWeatherCanvas(JWeatherCanvas weatherCanvas)
	{
		this.weatherCanvas = weatherCanvas;
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		logger.info("activated");
		if (Game.getCurrent().isPlayMusic())
		{
			Game.getCurrent().getSoundSystem().startMusic();
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
		if (isDialogOpened() == false)
		{
			if (Game.getCurrent().isPlayMusic())
			{
				Game.getCurrent().getSoundSystem().stopMusic();
			}
		}
		// }
		Game.getCurrent().getIdleTimer().stop();
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		logger.info("deiconified");
		if (Game.getCurrent().isPlayMusic())
		{
			Game.getCurrent().getSoundSystem().startMusic();
		}
		Game.getCurrent().getIdleTimer().start();
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		logger.info("iconified");
		if (Game.getCurrent().isPlayMusic())
		{
			Game.getCurrent().getSoundSystem().stopMusic();
		}
		Game.getCurrent().getIdleTimer().stop();
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		getGridCanvas().requestFocus();
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		logger.info(e.getComponent().getName());
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		logger.info(e.getComponent().getName());
	}

	public void moveMouse(Point p)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		// Search the devices for the one that draws the specified point.
		for (GraphicsDevice device : gs)
		{
			GraphicsConfiguration[] configurations = device.getConfigurations();
			for (GraphicsConfiguration config : configurations)
			{
				Rectangle bounds = config.getBounds();
				if (bounds.contains(p))
				{
					// Set point to screen coordinates.
					Point b = bounds.getLocation();
					Point s = new Point(p.x - b.x, p.y - b.y);

					try
					{
						Robot r = new Robot(device);
						r.mouseMove(s.x, s.y);
					}
					catch (AWTException e)
					{
						e.printStackTrace();
					}

					return;
				}
			}
		}
		// Couldn't move to the point, it may be off screen.
	}

	public AbstractKeyboardAction getCurrentAction()
	{
		return currentAction;
	}

	public void setCurrentAction(AbstractKeyboardAction currentAction)
	{
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

}

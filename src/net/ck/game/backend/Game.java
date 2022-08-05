package net.ck.game.backend;

import net.ck.game.animation.*;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.*;
import net.ck.game.graphics.GraphicsSystem;
import net.ck.game.items.*;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;
import net.ck.game.old.PressedTimer;
import net.ck.game.old.PressedTimerTask;
import net.ck.game.old.TurnTimer;
import net.ck.game.sound.SoundPlayer;
import net.ck.game.ui.IdleActionListener;
import net.ck.game.ui.IdleTimer;
import net.ck.game.ui.MainWindow;
import net.ck.game.ui.QuequeTimer;
import net.ck.game.weather.*;
import net.ck.util.GameUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.keyboard.AttackAction;
import net.ck.util.communication.keyboard.GetAction;
import net.ck.util.communication.sound.GameStateChanged;
import net.ck.util.security.SecurityManagerExtension;
import net.ck.util.xml.RunXMLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;

/**
 * Game Main class also Y6MU+=A7B=NpmQSs
 *
 * @author Claus
 */
public class Game
{

	/**
	 * Singleton
	 */
	private static final Game game = new Game();
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	/**
	 * sync object for animation
	 */
	final private Lock lock = new Lock();
	/**
	 * how long shall the game wait until sending a pass message in ms
	 */
	public long turnwait = 10000;
	/**
	 * how many frames per second
	 */
	private int FFPS = 60;

	private GameState gameState;

	/**
	 * list that contains all the NPC prototypes
	 */
	private Hashtable<Integer, NPC> npcList;

	/**
	 * list that contains all utility items
	 */
	private Hashtable<Integer, Utility> utilityList;

	/**
	 * list that contains all furniture items
	 */
	private Hashtable<Integer, FurnitureItem> furnitureList;

	/**
	 * so weaponList has two entries now, but as these are referenced, I need to make an item factory which gets a prototype from the list and creates a new instance with the values instead.
	 */
	private Hashtable<Integer, Weapon> weaponList;
	/**
	 * this is the list of all armor items that exist, ids will need to match the npc equipment. how much will this be used? not really sure
	 */
	private Hashtable<Integer, Armor> armorList;

	/**
	 * the arraylist holds all the maps
	 * not sure if i do need to do that or can load from disk on demand,
	 * but the average map wont be that big
	 */
	private ArrayList<Map> maps;
	/**
	 * holds the current map, might be game map, might be any map
	 */
	private Map currentMap;
	/**
	 * show how many rows and columns of tiles in the UI
	 */
	private int numberOfTiles = 15;
	/**
	 * shall music be played?
	 */
	private boolean playMusic;
	/**
	 * how many milliseconds until the turn is passed?
	 */
	private IdleTimer idleTimer;
	/**
	 * thread group for all of the threads of the game. Currently there should be three: Main Weather Animation. Mouse pressed timer IdleTimer are handled with swing timers.
	 */
	private ThreadGroup threadGroup;
	/**
	 * how big are the images, check this somewhere.
	 */
	private Point imageSize;
	/**
	 * the player has moved
	 */
	private boolean moved = true;
	/**
	 * is the game still running or shutting down?
	 */
	private boolean running;
	/**
	 * which is the currently active player, PC, NPCs are treated differently, but same :D
	 */
	private Player currentPlayer;
	/**
	 * Tile Size
	 */
	private int tileSize;
	/**
	 * are there animations?
	 */
	private boolean animated;
	/**
	 * how many animation cycles are there?
	 */
	private int animationCycles;
	/**
	 * which turn is the current turn, filled up with each doAction(), rolled over in advanceTurn() or retractTurn()
	 */
	private Turn currentTurn;
	/**
	 * world is doing the random events
	 */
	private World en;
	/**
	 * NPC Number, running ID for NPCs, not sure whether this can be kept game-wide, or needs to go down to GameMap
	 */
	private int npcNumber;
	/**
	 * list of players
	 */
	private ArrayList<Player> players = new ArrayList<>();
	/**
	 * threadController
	 */
	private ThreadController threadController;
	/**
	 * turn number
	 */
	private int turnNumber;
	/**
	 * the list of turn objects
	 */
	private ArrayList<Turn> turns = new ArrayList<>();
	/**
	 * this is the weather system
	 */
	private AbstractWeatherSystem weatherSystem;
	/**
	 * graphicsSystem for initializing Java FX
	 */
	private GraphicsSystem graphicsSystem = null;
	/**
	 * animated entities, contains everything that has changing images, i.e. NPCs, PCs, also other items. will probably also contain all inanimated objects. Perhaps these will go on a separate thread
	 */
	private ArrayList<LifeForm> animatedEntities = new ArrayList<>();
	/**
	 * controller as interaction between MainWindow and Game and controller here is the WindowBuilder and the Controller class in one. This actually needs to be treated differently.
	 */
	private MainWindow controller;
	/**
	 * soundSystem is the class dealing with the music. currently only taking files from a directory and trying to play one random song at a time
	 */
	private SoundPlayer soundSystem;

	/**
	 * this holds the actual game time which is increasing with time
	 */
	private GameTime gameTime;

	/**
	 * so if we have a go to command, this needs to go into a command queue
	 */
	private CommandQueue commandQueue;

	public QuequeTimer getQuequeTimer()
	{
		return quequeTimer;
	}

	public void setQuequeTimer(QuequeTimer quequeTimer)
	{
		this.quequeTimer = quequeTimer;
	}

	/**
	 * so how long is the time between movements being run from the command queue?
	 */
	private QuequeTimer quequeTimer;


	public MissileTimer getMissileTimer()
	{
		return missileTimer;
	}

	public void setMissileTimer(MissileTimer missileTimer)
	{
		this.missileTimer = missileTimer;
	}

	/**
	 * how long is the time period between missiles being drawn on the map?
	 */
	private MissileTimer missileTimer;


	private int baseHealth;

	/**
	 * standard constructor: initializes turns, game map, weather system, players weathersystem synchonized is handled by gamemap animation by game itself probably needs a rewrite in the future
	 * depends on how far i wanna go
	 */
	private Game()
	{
		// do this here, the access denied error for the jaxp.properties gets on
		// my nerves
		logger.info("setting up security manager");
		System.setProperty("java.security.policy", "policy.txt");
		SecurityManagerExtension secMan = new SecurityManagerExtension();
		java.lang.System.setSecurityManager(secMan);

		// thread handling
		logger.info("setting up thread system");
		setRunning(true);
		threadController = new ThreadController();
		threadController.add(Thread.currentThread());

		setPlayMusic(true);

		setTileSize(32);
		setTurnNumber(0);
		Turn turn = new Turn(getTurnNumber());
		setAnimated(true);
		setAnimationCycles(7);
		setBaseHealth(100);
		setCurrentTurn(turn);
		getTurns().add(turn);
		en = new World();

		setCommandQueue(new CommandQueue());
		setGameTime(new GameTime());
		getGameTime().setCurrentHour(9);

		setGameState(GameState.WORLD);
		// Toolkit.getDefaultToolkit().getSystemEventQueue().
		// java FX - perhaps in 2025
		// graphicsSystem = new GraphicsSystem();
		// graphicsSystem.startUp();
		logger.info("game start with default settings finished");

	}

	/**
	 * Singleton access - now I can take away game in a lot of things :D
	 */
	public static Game getCurrent()
	{
		return game;
	}

	public Hashtable<Integer, Utility> getUtilityList()
	{
		return utilityList;
	}

	public void setUtilityList(Hashtable<Integer, Utility> utilityList)
	{
		this.utilityList = utilityList;
	}

	public int getFFPS()
	{
		return FFPS;
	}

	public void setFFPS(int fFPS)
	{
		FFPS = fFPS;
	}

	public IdleTimer getIdleTimer()
	{
		return idleTimer;
	}

	public void setIdleTimer(IdleTimer idleTimer)
	{
		this.idleTimer = idleTimer;
	}

	public boolean isPlayMusic()
	{
		return playMusic;
	}

	public void setPlayMusic(boolean playMusic)
	{
		this.playMusic = playMusic;
	}

	public long getTurnwait()
	{
		return turnwait;
	}

	public void setTurnwait(long turnwait)
	{
		this.turnwait = turnwait;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public boolean isRunning()
	{
		return this.running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public Lock getLock()
	{
		return lock;
	}

	public GraphicsSystem getGraphicsSystem()
	{
		return this.graphicsSystem;
	}

	public void setGraphicsSystem(GraphicsSystem graphicsSystem)
	{
		this.graphicsSystem = graphicsSystem;
	}

	public SoundPlayer getSoundSystem()
	{
		return soundSystem;
	}

	public void setSoundSystem(SoundPlayer soundSystem)
	{
		this.soundSystem = soundSystem;
	}

	/**
	 * initialize the maps, cleanup later
	 */
	public void initializeMaps()
	{
		logger.info("start: initialize maps");
		maps = new ArrayList<>();
		String mapRootPath = "maps";

		File folder = new File(mapRootPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : Objects.requireNonNull(listOfFiles))
		{
			if (file.isFile())
			{
				//logger.info("file name: {}", file.getName());
				if (file.getName().contains("xml"))
				{
					Map map;
					logger.info("parsing map: {}", mapRootPath + File.separator + file.getName());
					map = RunXMLParser.parseMap(mapRootPath + File.separator + file.getName());

					String gameMapName = "testname";
					if (Objects.requireNonNull(map).getName().equalsIgnoreCase(gameMapName))
					{
						map.initialize();
						map.setVisibilityRange(2);
						// not sure whether I actually want this or even need this, or even need
						// the north/east/south/west
						// MapUtils.calculateTileDirections(map.getTiles());
						setCurrentMap(map);
						// addManyNPCs(map);
						addItemsToFloor();
					}
					else
					{
						map.setVisibilityRange(1);
					}
					if (map.getWeather() == null)
					{
						Weather weather = new Weather();
						weather.setType(WeatherTypes.SUN);
						map.setWeather(weather);
					}
					getMaps().add(map);
				}

			}
		}

		//getMaps().add(MapUtils.importUltima4MapFromCSV());
		//logger.info("maps: {}", getMaps());
		logger.info("end: initialize maps");

	}

	private void addItemsToFloor()
	{
		Weapon club = getWeaponList().get(1);
		Weapon magicClub = getWeaponList().get(2);
		Weapon sling = getWeaponList().get(3);

		//club.setMapPosition(new Point(3, 0));
		//magicClub.setMapPosition(new Point(3, 1));
		//map.getItems().add(magicClub);
		//map.getItems().add(club);
		Objects.requireNonNull(MapUtils.getTileByCoordinates(new Point(3, 0))).getInventory().add(club);
		Objects.requireNonNull(MapUtils.getTileByCoordinates(new Point(9, 3))).getInventory().add(magicClub);
		logger.info("furniture: {}", getFurnitureList().get(0));
		Objects.requireNonNull(MapUtils.getTileByCoordinates(new Point(9, 4))).setFurniture(getFurnitureList().get(1));
	}

	public void initializeAllItems()
	{
		logger.info("start: initialize items");
		String mapRootPath = "items";

		File folder = new File(mapRootPath);
		File[] listOfFiles = folder.listFiles();

		assert listOfFiles != null;
		for (File file : listOfFiles)
		{
			if (file.isFile())
			{
				if (file.getName().equalsIgnoreCase("armor.xml"))
				{
					logger.info("parsing armor: {}", mapRootPath + File.separator + file.getName());
					setArmorList(RunXMLParser.parseArmor(mapRootPath + File.separator + file.getName()));
				}

				else if (file.getName().equalsIgnoreCase("weapons.xml"))
				{
					logger.info("parsing weapons: {}", mapRootPath + File.separator + file.getName());
					setWeaponList(RunXMLParser.parseWeapons(mapRootPath + File.separator + file.getName()));
				}

				else if (file.getName().equalsIgnoreCase("utilities.xml"))
				{
					logger.info("parsing utilities: {}", mapRootPath + File.separator + file.getName());
					setUtilityList(RunXMLParser.parseUtilities(mapRootPath + File.separator + file.getName()));
				}

				else if (file.getName().equalsIgnoreCase("furniture.xml"))
				{
					logger.info("parsing furniture: {}", mapRootPath + File.separator + file.getName());
					setFurnitureList(RunXMLParser.parseFurniture(mapRootPath + File.separator + file.getName()));
				}

			}
		}
		logger.info("end: initialize items");

		//listWeapons();
		//listUtilities();
		//listArmor();
		listFurniture();
	}

	private void listFurniture()
	{
		for (FurnitureItem t : getFurnitureList().values())
		{
			logger.info("furniture item: {} ", t.toString());
		}
	}

	public void initializeNPCs()
	{
		logger.info("start: initialize npcs");
		String mapRootPath = "npcs";

		File folder = new File(mapRootPath);
		File[] listOfFiles = folder.listFiles();

		assert listOfFiles != null;
		for (File file : listOfFiles)
		{
			if (file.isFile())
			{
				if (file.getName().equalsIgnoreCase("npc.xml"))
				{
					logger.info("parsing npcs: {}", mapRootPath + File.separator + file.getName());
					setNpcList(RunXMLParser.parseNPCs(mapRootPath + File.separator + file.getName()));
				}
			}
		}
		logger.info("end: initialize items");
	}

		@SuppressWarnings("unused")
		private void addManyNPCs (Map map)
		{
			int max = 100;
			for (int i = 0; i <= max; i++)
			{
				NPC np = new NPC();
				np.setMapPosition(new Point(i, 1));
				np.setNumber(i + 5);
				np.setType(NPCTypes.WARRIOR);
				np.initialize();
				map.getNpcs().add(np);
			}

		}

		/**
		 * So we have found an exit point - switch to the target map - map is identified by name for now NPCs stay on the map on the places in the state that they are in. If there is a time system in the
		 * future, perhaps there will be npc routines that are running even if the player is not on the map and only drawn once the player is on the map? Player needs to be removed from the map once the
		 * map is being switched. needs to be added to the new map.
		 */
		public void switchMap ()
		{
			logger.info("start: switching map");

			MapTile exit = MapUtils.getTileByCoordinates(getCurrentPlayer().getMapPosition());
			String mapName = exit.getTargetMap();
			int targetTileID = exit.getTargetID();

			for (Map m : getMaps())
			{
				if (m.getName().equalsIgnoreCase(mapName))
				{
					MapTile targetTile = MapUtils.getMapTileByID(m, targetTileID);
					setCurrentMap(m);
					m.initialize();
					assert targetTile != null;
					getCurrentPlayer().setMapPosition(new Point(targetTile.x, targetTile.y));
					setAnimatedEntities(animatedEntities = new ArrayList<>());
					addAnimatedEntities();
				}
			}
			//these two are ugly and need to be done better somehow
			//but they make the switch faster, way faster
			getWeatherSystem().checkWeather();
			getController().getGridCanvas().repaint();
			// logger.info("current map: {}", Game.getCurrent().getCurrentMap());
			if (Game.getCurrent().getCurrentMap().getName().equalsIgnoreCase("INDOORS"))
			{
				EventBus.getDefault().post(new GameStateChanged(GameState.DUNGEON));
			}
			if (Game.getCurrent().getCurrentMap().getName().equalsIgnoreCase("testname"))
			{
				EventBus.getDefault().post(new GameStateChanged(GameState.WORLD));
			}
			getIdleTimer().start();
			logger.info("end: switching map");
		}

		/**
		 * https://stackoverflow.com/questions/9317461/get-the-application-closing-event
		 *
		 * it appears you can only do it this way not really necessary, but I guess can be used for later on but its interesting that in order to get the shutdown event, you need yet another thread also,
		 * when the fuck is this running? ThreadController is initialized way before
		 */
		/*
		 * public void addShutdownHook() { Runtime.getRuntime().addShutdownHook(new Thread() { public void run() { logger.error("Shutdown Hook is running !"); } });
		 * logger.error("Application Terminating ..."); setRunning(false); if (threadController != null) { for (Thread t : threadController.getThreads()) { // how the heck is this supposed to work?
		 * logger.error("shutdown: " + t.getName()); t.interrupt(); } } }
		 */

		@Subscribe
		public void onMessageEvent(GameStateChanged gameState)
		{
			this.setGameState(gameState.getGameState());
		}

		public void initializeWeatherSystem ()
		{
			logger.info("BEGIN: initializing weather system");
			weatherSystem = WeatherSystemFactory.createWeatherSystem(Game.getCurrent().getCurrentMap());

			switch (weatherSystem.getRealClass().getSimpleName())
			{
				case "SyncWeatherSystem":
					logger.info("initializing sync weather system");
					break;
				case "AsyncWeatherSystem":
					logger.info("initializing  async weather system");
					Thread weatherSystemThread = new Thread((AsyncWeatherSystem) weatherSystem);
					weatherSystemThread.setName(String.valueOf(ThreadNames.WEATHER_ANIMATION));
					threadController.add(weatherSystemThread);
					//weatherSystemThread.start();
					break;
				case "FixedWeatherSystem":
					logger.info("initializing fixed weather system");
					break;
				case "NoWeatherSystem":
					logger.info("initializing no weather system");
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + weatherSystem.getClass().getName());
			}

			if (Game.getCurrent().getCurrentMap().getWeather() == null)
			{
				Weather weather = new Weather();
				weather.setType(WeatherTypes.SUN);
				Game.getCurrent().getCurrentMap().setWeather(weather);
			}

			logger.info("END: initializing weather system");
		}

		public ThreadGroup getThreadGroup ()
		{
			return this.threadGroup;
		}

		public void setThreadGroup (ThreadGroup threadGroup)
		{
			this.threadGroup = threadGroup;
		}

		/**
		 * advance one turn - the end of the rollover in civ for instance. all npcs act, environment acts, idle timer for passing the turn starts.
		 *
		 * https://stackoverflow.com/questions/30989558/java-8-retry-a-method-until-a-condition-is-fulfilled-in-intervals
		 *
		 * @param haveNPCAction is a npc action allowed or not
		 */
		public void advanceTurn ( boolean haveNPCAction)
		{

			/*if (Game.getCurrent().getCurrentMap().getMissiles() != null)
			{
				while (Game.getCurrent().getCurrentMap().getMissiles().size() > 0)
				{

				}
			}*/

			/*if (Game.getCurrent().getMissileTimer() != null)
			{
				while (Game.getCurrent().getMissileTimer().isRunning())
				{
					try
					{
						Thread.sleep(5);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}*/

			// logger.info("current turn number 1: {}", Game.getCurrent().getCurrentTurn().getTurnNumber());
			//
			// logger.info("npc actions");
			if (haveNPCAction)
			{
				for (NPC e : Game.getCurrent().getCurrentMap().getNpcs())
				{
					//npc is aggressive
					if (e.isAgressive())
					{
						logger.info("trying to attack");
						//attack with melee
						if (MapUtils.isAdjacent(e.getMapPosition(), e.getVictim().getMapPosition()))
						{
							logger.info("attacking");
							e.doAction(new PlayerAction(new AttackAction()));
							//return;
						}
						//victim is not adjacent
						else
						{
							logger.info("out of melee range, what to do");
							//Weapon sling = getWeaponList().get(3);
							//e.getItem(sling);
							//npc has ranged weapon wielded or has one in inventory
							if (e.isRanged())
							{
								logger.info("NPC has ranged capabilities");
								//wielded attack
								if (e.getWeapon().getType().equals(WeaponTypes.RANGED))
								{
									logger.info("npc already wields ranged weapon, attack!");
									PlayerAction action = new PlayerAction(new AttackAction());
									e.doAction(action);
									//return;
								}
								//in inventory, wield
								else
								{
									logger.info("ranged weapon in inventory");
									e.switchWeapon(WeaponTypes.RANGED);
									//return;
								}
							}
							else
							{
								logger.info("out of range move towards victim");
								e.doAction((NPCUtils.calculateVictimDirection(e)));
								//return;
							}
						}
					}
					else
					{
						GetAction action = e.lookAroundForItems();
						//GetAction action = null;
						if (action != null)
						{
							logger.info("trying to get");
							PlayerAction ac = new PlayerAction(action);
							e.doAction(ac);
						}
						else
						{
							e.doAction(NPCUtils.calculateAction(e));
						}
					}
				}
				// logger.info("environment action");
				Game.getCurrent().getEn().doAction(Game.getCurrent().getEn().createRandomEvent());
				getIdleTimer().start();
			}
			// logger.info("advance turn!");
			setTurnNumber(getTurnNumber() + 1);

			Turn turn = new Turn(getTurnNumber());
			getTurns().add(turn);
			this.setCurrentTurn(turn);
			getGameTime().advanceTime(getCurrentMap().getMinutesPerTurn());
			MapUtils.calculateDayOrNight();
			logger.info("=======================================================================================");
			// logger.info("current turn number 2: {}", Game.getCurrent().getCurrentTurn().getTurnNumber());
			// Game.getCurrent().initializeTurnTimer();
		}

		public Logger getLogger ()
		{
			return logger;
		}

		public void decrementTurnNumber ()
		{
			this.setTurnNumber(getTurnNumber() - 1);
		}

		public int getAnimationCycles ()
		{
			return animationCycles;
		}

		public Turn getCurrentTurn ()
		{
			return currentTurn;
		}

		public World getEn ()
		{
			return en;
		}


		public ArrayList<Player> getPlayers ()
		{
			return players;
		}

		public ThreadController getThreadController ()
		{
			return threadController;
		}

		public int getTurnNumber ()
		{
			return turnNumber;
		}

		public ArrayList<Turn> getTurns ()
		{
			return turns;
		}

		public AbstractWeatherSystem getWeatherSystem ()
		{
			return weatherSystem;
		}

		public void incrementTurnNumber ()
		{
			this.setTurnNumber(getTurnNumber() + 1);
		}

		public boolean isAnimated ()
		{
			return animated;
		}

		/**
		 * go back one turn in history
		 *
		 * three ideas how to implement this:
		 *
		 * 1. get the last turn, just add it in front 2. get the last turn, delete it from the list, then replay it. 3. get the last turn, play it, just overwrite.
		 *
		 * I went for option 2, which gets the last turn and then replays it hoping the implementation is correct
		 */
		public int retractTurn ()
		{
			// logger.info("current turn info: {}, action sizes: {}",
			// getCurrentTurn().getTurnNumber() ,
			// getCurrentTurn().getActions().size());
			logger.info("retracting turn, current turn: {}", getCurrentTurn().getTurnNumber());

			if (getTurnNumber() == 0)
			{
				logger.info("current turn is the first one");
			}
			else
			{
				// C Stupidity of 0-indexed lists, I will never ever understand it
				// Start turn is 0, index position is zero,
				// first rollover means, turn is 1, index position is also 1.
				// remove last turn, as it is blank anyhow.
				// we are at i - 1 again.
				getTurns().remove(getTurnNumber());
				// remove turnNumber
				setTurnNumber(getTurnNumber() - 1);

				// get last Turn
				// C Stupidity of 0-indexed lists, I will never ever understand it
				Turn turn = getTurns().get(getTurnNumber());
				GameUtils.invertActions(turn);
				for (AbstractAction e : turn.getActions())
				{
					//e.getEntity().doAction(e);
					logger.error("this does not work anymore");
				}

				getController().getTextField().retractTurn();

				turn.getActions().clear();
				this.setCurrentTurn(turn);
			}
			return getTurnNumber();
		}

		/**
		 * run the turn: this is a legacy method which might be used to test automation without the ui - or with it?
		 *
		 *
		 * then it is the next turn
		 *
		 *
		 */
		/*public void runTurn ()
		{
			for (NPC p : Game.getCurrent().getCurrentMap().getNpcs())
			{
				p.doAction(NPCUtils.calculateAction(p));
				getEn().createRandomEvent();
			}
			advanceTurn(false);
			for (Thread t : this.getThreadController().getThreads())
			{
				logger.info("thread: {}", t.getName());
				logger.info(t.getState().toString());
			}
		}*/

		public void setAnimated ( boolean animated)
		{
			this.animated = animated;
		}

		private void setAnimationCycles ( int animationCycles)
		{
			this.animationCycles = animationCycles;
		}

		public void setCurrentTurn (Turn currentTurn)
		{
			this.currentTurn = currentTurn;
		}

		public void setEn (World en)
		{
			this.en = en;
		}

		public void setPlayers (ArrayList <Player> players)
		{
			this.players = players;
		}

		public void setThreadController (ThreadController threadController)
		{
			this.threadController = threadController;
		}

		public void setTurnNumber ( int turnNumber)
		{
			this.turnNumber = turnNumber;
		}

		public void setTurns (ArrayList < Turn > turns)
		{
			this.turns = turns;
		}

		public void setWeatherSystem (AbstractWeatherSystem weatherSystem)
		{
			this.weatherSystem = weatherSystem;
		}

		public void stopGame ()
		{
			setRunning(false);
			System.exit(0);
		}

		public int getTileSize ()
		{
			return tileSize;
		}

		public void setTileSize ( int tileSize)
		{
			this.tileSize = tileSize;
		}

		public Player getCurrentPlayer ()
		{
			return currentPlayer;
		}

		public void setCurrentPlayer (Player abstractEntity)
		{
			this.currentPlayer = abstractEntity;
		}

		public Point getImageSize ()
		{
			return imageSize;
		}

		public void setImageSize (Point imageSize)
		{
			this.imageSize = imageSize;
		}

		public int getNpcNumber ()
		{
			return npcNumber;
		}

		public void setNpcNumber ( int npcNumber)
		{
			this.npcNumber = npcNumber;
		}

		public int getNextNPCNumber ()
		{
			setNpcNumber(getNpcNumber() + 1);
			return getNpcNumber();
		}

	/**
	 * so the game starts so the first player has the first movement action. Then it goes to the second player ... when all players have moved, roll over turn there only will be one player without
	 * heavy extension, interesting, lets see whether i can make this work
	 */
		public void addPlayers ()
		{
			logger.info("adding players");
			Player p1 = new Player(0);
			Weapon sling = getWeaponList().get(3);
			p1.setWeapon(sling);

			// Player p2 = new Player(this, 1);
			// NPC npc1 = new NPC();
			getPlayers().add(p1);
			// getPlayers().add(p2);
			// getPlayers().add(npc1);

			// gameMap.getPlayers().add(p2);
			// getCurrentMap().getNpcs().add(npc1);
			//p1.setMapPosition(new Point(42, 42));
			p1.setMapPosition(new Point(2, 2));
			// p2.setPosition(new Point(1, 0));
			// npc1.setMapPosition(new Point(2, 3));
			setCurrentPlayer(getPlayers().get(0));

			Set<ArmorPositions> positions = Game.getCurrent().getCurrentPlayer().getWearEquipment().keySet();
			for (ArmorPositions pos : positions)
			{
				//logger.info("equipment position:{}  item: {} ", pos, Game.getCurrent().getCurrentPlayer().getWearEquipment().get(pos));
			}

		}

		public void initializeAnimationSystem ()
		{
			AnimationSystem animationSystem = AnimationSystemFactory.createAnymationSystem();
			if (isAnimated() == true)
			{
				logger.info("initializing animation system");
				Thread animationSystemThread = new Thread(animationSystem);
				animationSystemThread.setName(String.valueOf(ThreadNames.LIFEFORM_ANIMATION));
				threadController.add(animationSystemThread);
				//animationSystemThread.start();
			}
		}

		public void initializeBackgroundAnimationSystem ()
		{
			if (isAnimated() == true)
			{
				logger.info("initializing BackgroundAnimationSystem animation system");
				BackgroundAnimationSystem backgroundAnimationSystem = new BackgroundAnimationSystem();

				Thread backgroundAnimationSystemThread = new Thread(backgroundAnimationSystem);
				backgroundAnimationSystemThread.setName(String.valueOf(ThreadNames.BACKGROUND_ANIMATION));

				threadController.add(backgroundAnimationSystemThread);
				//backgroundAnimationSystemThread.start();
			}
		}

		public void initializeForegroundAnimationSystem ()
		{
			if (isAnimated() == true)
			{
				logger.info("initializing ForegroundAnimationSystem animation system");
				ForegroundAnimationSystem foregroundAnimationSystem = new ForegroundAnimationSystem();

				Thread foregroundAnimationSystemThread = new Thread(foregroundAnimationSystem);
				foregroundAnimationSystemThread.setName(String.valueOf(ThreadNames.FOREGROUND_ANIMATION));

				threadController.add(foregroundAnimationSystemThread);
				//foregroundAnimationSystemThread.start();
			}
		}

		public ArrayList<LifeForm> getAnimatedEntities ()
		{
			return animatedEntities;
		}

		public void setAnimatedEntities (ArrayList <LifeForm> animatedEntities)
		{
			this.animatedEntities = animatedEntities;
		}

		public void addAnimatedEntities ()
		{
			//logger.info("number of players: {}", Game.getCurrent().getCurrentMap().getPlayers().size());
			//logger.info("number of npcs: {}", Game.getCurrent().getCurrentMap().getNpcs().size());

				getAnimatedEntities().add(Game.getCurrent().getCurrentPlayer());

			for (NPC e : Game.getCurrent().getCurrentMap().getNpcs())
			{
				// logger.info("adding npc: {}", e);
				getAnimatedEntities().add(e);
			}

		}

		public MainWindow getController ()
		{
			return controller;
		}

		public void setController (MainWindow controller)
		{
			this.controller = controller;
		}

		@Deprecated
		public void initializeTurnTimerThread ()
		{
			logger.info("initializing Turn Timer Thread");
			TurnTimer turnTimer = new TurnTimer();
			Thread turnTimerThread = new Thread(turnTimer);
			turnTimerThread.setName("Turn Timer Thread");
			threadController.add(turnTimerThread);
			turnTimerThread.start();
		}

		@Deprecated
		public void initializeTurnTimer ()
		{
			logger.info("initializing Turn Timer as Timer");
			PressedTimerTask timerTask = new PressedTimerTask();
			PressedTimer timer = new PressedTimer("Turn Timer Task");
			timer.schedule(timerTask, getTurnwait(), getTurnwait());
		}

		public void initializeTurnTimerTimer ()
		{
			logger.info("initializing Turn Timer as Swing Timer");
			// Game.getCurrent().setMoved(false);
			IdleActionListener idleActionListener = new IdleActionListener();
			idleTimer = new IdleTimer((int) getTurnwait(), idleActionListener);
			idleTimer.setRepeats(true);
			idleTimer.start();
		}

		public void initializeQuequeTimer()
		{
			logger.info("initializing Movement Timer as Swing Timer");
			QuequeTimerActionListener quequeTimerActionListener = new QuequeTimerActionListener();
			quequeTimer = new QuequeTimer(300, quequeTimerActionListener);
			quequeTimer.setRepeats(true);
			//quequeTimer.start();
		}

	public void initializeMissileTimer()
	{
		logger.info("initializing Missile Timer as Swing Timer");
		MissileTimerActionListener missileTimerActionListener = new MissileTimerActionListener();
		missileTimer = new MissileTimer(30, missileTimerActionListener);
		missileTimer.setRepeats(true);
	}


		public boolean isMoved ()
		{
			return moved;
		}

		public void setMoved ( boolean moved)
		{
			logger.info("set moved set to: {}", moved);
			this.moved = moved;
		}

		/**
		 * https://www.youtube.com/watch?v=VpH33Uw-_0E
		 */
		public void run ()
		{
			double drawInterval = 1000000000 / 60;
			double nextDrawTime = System.nanoTime() + drawInterval;

			update();
			repaint();

			this.getController().getFrame().repaint();

			double remainingTime = nextDrawTime - System.nanoTime();
			try
			{
				logger.info("sleep");
				Thread.sleep((long) remainingTime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}

		private void update ()
		{

		}

		private void repaint ()
		{

		}

		public void initializeMusic ()
		{
			if (isPlayMusic() == true)
			{
				logger.info("initializing sound system");
				soundSystem = new SoundPlayer();
				soundSystem.setMusicIsRunning(true);
				Thread soundSystemThread = new Thread(soundSystem);
				soundSystemThread.setName(String.valueOf(ThreadNames.SOUND_SYSTEM));
				threadController.add(soundSystemThread);
				//soundSystemThread.start();
			}
		}

		public void listThreads ()
		{
			for (Thread t : getThreadController().getThreads())
			{
				logger.info("Thread running: {}, priority: {}, state: {}", t.getName(), t.getPriority(), t.getState());
			}
		}

		public void listArmor ()
		{
			for (Armor t : getArmorList().values())
			{
				logger.info("armor item: {} ", t.toString());
			}
		}

		public void listWeapons ()
		{
			for (Weapon t : getWeaponList().values())
			{
				logger.info("weapon item: {} ", t.toString());
			}
		}


		public void listUtilities ()
		{
			for (Utility t : getUtilityList().values())
			{
				logger.info("utility item: {} ", t.toString());
			}
		}

		public int getNumberOfTiles ()
		{
			return numberOfTiles;
		}

		public void setNumberOfTiles ( int numberOfTiles)
		{
			this.numberOfTiles = numberOfTiles;
		}

		public Map getCurrentMap ()
		{
			return currentMap;
		}

		public void setCurrentMap (Map currentMap)
		{
			this.currentMap = currentMap;
		}

		public ArrayList<Map> getMaps ()
		{
			return maps;
		}

		public void setMaps (ArrayList < Map > maps)
		{
			this.maps = maps;
		}

		public Hashtable<Integer, Armor> getArmorList ()
		{
			return armorList;
		}

		public void setArmorList (Hashtable < Integer, Armor > armorList)
		{
			this.armorList = armorList;
		}

		public Hashtable<Integer, Weapon> getWeaponList ()
		{
			return weaponList;
		}

		public void setWeaponList (Hashtable < Integer, Weapon > weaponList)
		{
			this.weaponList = weaponList;
		}
		public Hashtable<Integer, NPC> getNpcList ()
		{
			return npcList;
		}

		public void setNpcList (Hashtable < Integer, NPC > npcList)
		{
			this.npcList = npcList;
		}

	public void listNPCs()
	{
		for (NPC i : getNpcList().values())
		{
			logger.info("npc: {}", i);
		}
	}

	public Hashtable<Integer, FurnitureItem> getFurnitureList()
	{
		return furnitureList;
	}

	public void setFurnitureList(Hashtable<Integer, FurnitureItem> furnitureList)
	{
		logger.info("setting Furniture list");
		this.furnitureList = furnitureList;
	}

	public GameTime getGameTime()
	{
		return gameTime;
	}

	public void setGameTime(GameTime gameTime)
	{
		this.gameTime = gameTime;
	}

	public CommandQueue getCommandQueue()
	{
		return commandQueue;
	}

	public void setCommandQueue(CommandQueue commandQueue)
	{
		this.commandQueue = commandQueue;
	}

	public int getBaseHealth()
	{
		return baseHealth;
	}

	public void setBaseHealth(int baseHealth)
	{
		this.baseHealth = baseHealth;
	}

	public GameState getGameState()
	{
		return gameState;
	}

	public void setGameState(GameState gameState)
	{
		this.gameState = gameState;
	}

	public void startThreads()
	{
		getThreadController().startThreads();
	}
}



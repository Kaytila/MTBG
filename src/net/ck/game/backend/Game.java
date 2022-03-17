package net.ck.game.backend;

import net.ck.game.animation.AnimationSystem;
import net.ck.game.animation.AnimationSystemFactory;
import net.ck.game.animation.BackgroundAnimationSystem;
import net.ck.game.animation.ForegroundAnimationSystem;
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
import net.ck.game.weather.*;
import net.ck.util.GameUtils;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.keyboard.GetAction;
import net.ck.util.security.SecurityManagerExtension;
import net.ck.util.xml.RunXMLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
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
	private final String gameMapName = "testname";
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
	public int FFPS = 60;

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
	private AbstractEntity currentPlayer;
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
	private ArrayList<AbstractEntity> players = new ArrayList<AbstractEntity>();
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
	private ArrayList<Turn> turns = new ArrayList<Turn>();
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
	private ArrayList<AbstractEntity> animatedEntities = new ArrayList<AbstractEntity>();
	/**
	 * controller as interaction between MainWindow and Game and controller here is the WindowBuilder and the Controller class in one. This actually needs to be treated differently.
	 */
	private MainWindow controller;
	/**
	 * soundSystem is the class dealing with the music. currently only taking files from a directory and trying to play one random song at a time
	 */
	private SoundPlayer soundSystem;

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
		threadController = new ThreadController(this);
		threadController.add(Thread.currentThread());

		setPlayMusic(false);

		setTileSize(32);
		setTurnNumber(0);
		Turn turn = new Turn(getTurnNumber());
		setAnimated(true);
		setAnimationCycles(7);
		setCurrentTurn(turn);
		getTurns().add(turn);
		en = new World();

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
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
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
		maps = new ArrayList<Map>();
		String mapRootPath = "maps";

		File folder = new File(mapRootPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles)
		{

			if (file.isFile())
			{
				//logger.info("file name: {}", file.getName());
				if (file.getName().contains("xml"))
				{
					Map map = null;
					logger.info("parsing map: {}", mapRootPath + File.separator + file.getName());
					map = RunXMLParser.parseMap(mapRootPath + File.separator + file.getName());

					if (map.getName().equalsIgnoreCase(gameMapName))
					{
						map.initialize();
						map.setVisibilityRange(2);
						// not sure whether I actually want this or even need this, or even need
						// the north/east/south/west
						// MapUtils.calculateTileDirections(map.getTiles());
						setCurrentMap(map);
						// addManyNPCs(map);
						addItemsToFloor(map);
					}
					else
					{
						map.setVisibilityRange(1);
					}
					if (map.getCurrentWeather() == null)
					{
						Weather weather = new Weather();
						weather.setType(WeatherTypes.SUN);
						map.setCurrentWeather(weather);
					}
					getMaps().add(map);
				}

			}
		}

		//getMaps().add(MapUtils.importUltima4MapFromCSV());
		//logger.info("maps: {}", getMaps());
		logger.info("end: initialize maps");

	}

	private void addItemsToFloor(Map map)
	{
		Weapon club = getWeaponList().get(1);
		Weapon magicClub = getWeaponList().get(2);
		//club.setMapPosition(new Point(3, 0));
		//magicClub.setMapPosition(new Point(3, 1));
		//map.getItems().add(magicClub);
		//map.getItems().add(club);
		MapUtils.getTileByCoordinates(new Point(3, 0)).getInventory().add(club);
		MapUtils.getTileByCoordinates(new Point(9, 3)).getInventory().add(magicClub);
		logger.info("furniture: {}", getFurnitureList().get(0));
		MapUtils.getTileByCoordinates(new Point(9, 4)).setFurniture(getFurnitureList().get(1));
	}

	public void initializeAllItems()
	{
		logger.info("start: initialize items");
		String mapRootPath = "items";

		File folder = new File(mapRootPath);
		File[] listOfFiles = folder.listFiles();

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

			Game.getCurrent().getCurrentMap().getPlayers().remove(Game.getCurrent().getCurrentPlayer());
			for (Map m : getMaps())
			{
				if (m.getName().equalsIgnoreCase(mapName))
				{
					MapTile targetTile = MapUtils.getMapTileByID(m, targetTileID);
					setCurrentMap(m);
					m.initialize();
					if (m.getPlayers().size() == 0)
					{
						m.getPlayers().add(getCurrentPlayer());
					}
					getCurrentPlayer().setMapPosition(new Point(targetTile.x, targetTile.y));
					setAnimatedEntities(animatedEntities = new ArrayList<AbstractEntity>());
					addAnimatedEntities();
				}
			}
			//these two are ugly and need to be done better somehow
			//but they make the switch faster, way faster
			getWeatherSystem().checkWeather();
			getController().getGridCanvas().repaint();
			// logger.info("current map: {}", Game.getCurrent().getCurrentMap());
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

		public void initializeWeatherSystem ()
		{
			weatherSystem = WeatherSystemFactory.createWeatherSystem(Game.getCurrent().getCurrentMap());
			// is ThreadGroup an idea here?
			// dont really know the stuff here
			if (weatherSystem.isSynchronized() == false)
			{
				logger.info("initializing async weather system");
				Thread weatherSystemThread = new Thread((AsyncWeatherSystem) weatherSystem);
				weatherSystemThread.setName("Weather System Thread");
				threadController.add(weatherSystemThread);
				weatherSystemThread.start();
			}

			// addManyNPCs(map);
			if (Game.getCurrent().getCurrentMap().getCurrentWeather() == null)
			{
				Weather weather = new Weather();
				weather.setType(WeatherTypes.SUN);
				Game.getCurrent().getCurrentMap().setCurrentWeather(weather);
			}

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
		 * @param haveNPCAction
		 */
		public void advanceTurn ( boolean haveNPCAction)
		{
			// logger.info("current turn number 1: {}", Game.getCurrent().getCurrentTurn().getTurnNumber());
			//
			// logger.info("npc actions");
			if (haveNPCAction == true)
			{
				for (AbstractEntity e : Game.getCurrent().getCurrentMap().getNpcs())
				{
					GetAction action = e.lookAroundForItems();
					//GetAction action = null;
					if (action != null)
					{
						logger.info("trying to get");
						PlayerAction ac = new PlayerAction(action, e);
						e.doAction(ac);
					}
					else
					{
						e.doAction(NPCUtils.calculateAction(e));
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


		public ArrayList<AbstractEntity> getPlayers ()
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
					e.getEntity().doAction((PlayerAction) e);
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
		public void runTurn ()
		{
			for (AbstractEntity p : getPlayers())
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
		}

		public void setAnimated ( boolean animated)
		{
			this.animated = animated;
		}

		public void setAnimationCycles ( int animationCycles)
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

		public void setPlayers (ArrayList < AbstractEntity > players)
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

		public AbstractEntity getCurrentPlayer ()
		{
			return currentPlayer;
		}

		public void setCurrentPlayer (AbstractEntity abstractEntity)
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

		public void addPlayers ()
		{
			logger.info("adding players");
			Player p1 = new Player(0);
			// Player p2 = new Player(this, 1);
			// NPC npc1 = new NPC();
			getPlayers().add(p1);
			// getPlayers().add(p2);
			// getPlayers().add(npc1);
			getCurrentMap().getPlayers().add(p1);
			// gameMap.getPlayers().add(p2);
			// getCurrentMap().getNpcs().add(npc1);
			//p1.setMapPosition(new Point(42, 42));
			p1.setMapPosition(new Point(2, 2));
			// p2.setPosition(new Point(1, 0));
			// npc1.setMapPosition(new Point(2, 3));

			/**
			 * so the game starts so the first player has the first movement action. Then it goes to the second player ... when all players have moved, roll over turn there only will be one player without
			 * heavy extension, interesting, lets see whether i can make this work
			 */
			setCurrentPlayer(getPlayers().get(0));

			Set<ArmorPositions> positions = Game.getCurrent().getCurrentPlayer().getWearEquipment().keySet();
			for (ArmorPositions pos : positions)
			{
				//logger.info("equipment position:{}  item: {} ", pos, Game.getCurrent().getCurrentPlayer().getWearEquipment().get(pos));
			}
		}

		public void initializeAnimationSystem ()
		{
			AnimationSystem animationSystem = AnimationSystemFactory.createAnymationSystem(this);
			if (isAnimated() == true)
			{
				logger.info("initializing animation system");
				Thread animationSystemThread = new Thread(animationSystem);
				animationSystemThread.setName("Animation System Thread");
				threadController.add(animationSystemThread);
				animationSystemThread.start();
			}
		}

		public void initializeBackgroundAnimationSystem ()
		{
			if (isAnimated() == true)
			{
				logger.info("initializing BackgroundAnimationSystem animation system");
				BackgroundAnimationSystem backgroundAnimationSystem = new BackgroundAnimationSystem(this);

				Thread backgroundAnimationSystemThread = new Thread(backgroundAnimationSystem);
				backgroundAnimationSystemThread.setName("Background Animation System Thread");

				threadController.add(backgroundAnimationSystemThread);
				backgroundAnimationSystemThread.start();
			}
		}

		public void initializeForegroundAnimationSystem ()
		{
			if (isAnimated() == true)
			{
				logger.info("initializing ForegroundAnimationSystem animation system");
				ForegroundAnimationSystem foregroundAnimationSystem = new ForegroundAnimationSystem(this);

				Thread foregroundAnimationSystemThread = new Thread(foregroundAnimationSystem);
				foregroundAnimationSystemThread.setName("Foreground Animation System Thread");

				threadController.add(foregroundAnimationSystemThread);
				foregroundAnimationSystemThread.start();
			}
		}

		public ArrayList<AbstractEntity> getAnimatedEntities ()
		{
			return animatedEntities;
		}

		public void setAnimatedEntities (ArrayList < AbstractEntity > animatedEntities)
		{
			this.animatedEntities = animatedEntities;
		}

		public void addAnimatedEntities ()
		{
			//logger.info("number of players: {}", Game.getCurrent().getCurrentMap().getPlayers().size());
			//logger.info("number of npcs: {}", Game.getCurrent().getCurrentMap().getNpcs().size());
			for (AbstractEntity e : Game.getCurrent().getCurrentMap().getPlayers())
			{
				// logger.info("adding player: {}", e);
				getAnimatedEntities().add(e);
			}

			for (AbstractEntity e : Game.getCurrent().getCurrentMap().getNpcs())
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
				try
				{
					soundSystem = new SoundPlayer();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					stopGame();
				}
				Thread soundSystemThread = new Thread(soundSystem);
				soundSystemThread.setName("Sound System Thread");
				threadController.add(soundSystemThread);
				soundSystemThread.start();
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
}



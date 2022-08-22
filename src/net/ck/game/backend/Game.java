package net.ck.game.backend;

import net.ck.game.animation.*;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.entities.*;
import net.ck.game.backend.threading.ThreadController;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.backend.time.GameTime;
import net.ck.game.backend.time.QuequeTimerActionListener;
import net.ck.game.items.Armor;
import net.ck.game.items.FurnitureItem;
import net.ck.game.items.Utility;
import net.ck.game.items.Weapon;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;
import net.ck.game.sound.MusicTimer;
import net.ck.game.sound.MusicTimerActionListener;
import net.ck.game.sound.SoundPlayer;
import net.ck.game.backend.time.IdleActionListener;
import net.ck.game.backend.time.IdleTimer;
import net.ck.game.ui.MainWindow;
import net.ck.game.backend.time.QuequeTimer;
import net.ck.game.weather.*;
import net.ck.util.GameUtils;
import net.ck.util.MapUtils;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
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

/**
 * Game Main class also Y6MU+=A7B=NpmQSs
 *
 * @author Claus
 */
public class Game implements Runnable
{

    /**
     * Singleton
     */
    private static final Game game = new Game();
    private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

    /**
     * contains the current game state,
     * is also used by soundplayer, is also persisted with each turn
     * and used for checking for music change
     */
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
     * weapon list get returns ID, not position.
     */
    private Hashtable<Integer, Weapon> weaponList;
    /**
     * this is the list of all armor items that exist, ids will need to match the npc equipment. how much will this be used? not really sure
     */
    private Hashtable<Integer, Armor> armorList;

    /**
     * the arraylist holds all the maps
     * not sure if I do need to do that or can load from disk on demand,
     * but the average map won't be that big
     */
    private ArrayList<Map> maps;
    /**
     * holds the current map, might be game map, might be any map
     */
    private Map currentMap;


    /**
     * how many milliseconds until the turn is passed?
     */
    private IdleTimer idleTimer;

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
     * so if we have a goto command, this needs to go into a command queue
     */
    private CommandQueue commandQueue;

    public MusicTimer getMusicTimer()
    {
        return musicTimer;
    }

    public void setMusicTimer(MusicTimer musicTimer)
    {
        this.musicTimer = musicTimer;
    }

    private MusicTimer musicTimer;

    public QuequeTimer getQuequeTimer()
    {
        return quequeTimer;
    }

    private GameState previousGameState;


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

    /**
     * base health is the basic health points number, i.e. does a NPC start with 10, 100, 1000 - depending on how far you want to go
     */
    private int baseHealth;

    /**
     * ready for next turn?
     */
    private boolean nextTurn;

    /**
     * npc action?
     */
    private boolean npcAction;

    /**
     * standard constructor: initializes turns, game map, weather system, players weathersystem synchonized is handled by gamemap animation by game itself probably needs a rewrite in the future
     * depends on how far I want to go
     */
    private Game()
    {
        // do this here, the access denied error for the jaxp.properties gets on
        // my nerves
        getLogger().info("setting up security manager");
        System.setProperty("java.security.policy", "policy.txt");
        SecurityManagerExtension secMan = new SecurityManagerExtension();
        java.lang.System.setSecurityManager(secMan);

        // thread handling
        getLogger().info("setting up thread system");
        setRunning(true);
        setThreadController(new ThreadController());
        getThreadController().add(Thread.currentThread());


        setTurnNumber(0);
        Turn turn = new Turn(getTurnNumber());

        setBaseHealth(10);
        setCurrentTurn(turn);
        getTurns().add(turn);
        setEn(new World());

        setCommandQueue(new CommandQueue());
        setGameTime(new GameTime());
        getGameTime().setCurrentHour(9);

        setGameState(GameState.DUSK);

        EventBus.getDefault().register(this);
        getLogger().info("game start with default settings finished");

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


    public IdleTimer getIdleTimer()
    {
        return idleTimer;
    }

    public void setIdleTimer(IdleTimer idleTimer)
    {
        this.idleTimer = idleTimer;
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
     * not sure whether I actually want this or even need this, or even need
     * the north/east/south/west
     * MapUtils.calculateTileDirections(map.getTiles());
     * To be identified later
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
        Objects.requireNonNull(MapUtils.getTileByCoordinates(new Point(3, 0))).getInventory().add(club);
        Objects.requireNonNull(MapUtils.getTileByCoordinates(new Point(9, 3))).getInventory().add(magicClub);
        Objects.requireNonNull(MapUtils.getTileByCoordinates(new Point(6, 6))).getInventory().add(sling);
        //logger.info("furniture: {}", getFurnitureList().get(0));
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

        listWeapons();
        listUtilities();
        listArmor();
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
    private void addManyNPCs(Map map)
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
    public void switchMap()
    {
        logger.info("start: switching map");

        MapTile exit = MapUtils.getTileByCoordinates(getCurrentPlayer().getMapPosition());
        assert exit != null;
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
        //these two are ugly and need to be done better somehow,
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
     * <a href="https://stackoverflow.com/questions/9317461/get-the-application-closing-event">...</a>
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
        //logger.info("caught game state change in game: {}", gameState.getGameState());
        if (gameState.getGameState() == GameState.COMBAT)
        {
            logger.info("setting previous game state to: {}", Game.getCurrent().getGameState());
            setPreviousGameState(Game.getCurrent().getGameState());
        }

        if (gameState.getGameState() != Game.getCurrent().getGameState())
        {
            Game.getCurrent().setGameState(gameState.getGameState());
        }


    }

    @Subscribe
    public void onMessageEvent(AdvanceTurnEvent event)
    {
        //logger.info("advance turn");
        setNpcAction(event.isNpcAction());
        setNextTurn(true);
    }


    public void initializeWeatherSystem()
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

    /**
     * advance one turn - the end of the rollover in civ for instance. all npcs act, environment acts, idle timer for passing the turn starts.
     * <p>
     * <a href="https://stackoverflow.com/questions/30989558/java-8-retry-a-method-until-a-condition-is-fulfilled-in-intervals">...</a>
     *
     * @param haveNPCAction is a npc action allowed or not
     */
    public synchronized void advanceTurn(boolean haveNPCAction)
    {
        Game.getCurrent().getCurrentTurn().setGameState(Game.getCurrent().getGameState());
        Game.getCurrent().getIdleTimer().stop();
        if (Game.getCurrent().getMissileTimer() != null)
        {
            //noinspection StatementWithEmptyBody
            while (Game.getCurrent().getMissileTimer().isRunning())
            {

            }
        }

        if (haveNPCAction)
        {
            for (NPC e : Game.getCurrent().getCurrentMap().getNpcs())
            {
                //npc is aggressive
                if (e.isHostile())
                {
                    AIBehaviour.determineCombat(e);

                }
                else
                {
                    AIBehaviour.determineRandom(e);

                }
            }
            // logger.info("environment action");
            Game.getCurrent().getEn().doAction(Game.getCurrent().getEn().createRandomEvent());
        }
        //logger.info("advance turn!");
        setTurnNumber(getTurnNumber() + 1);

        //logger.info("game game state: {}", Game.getCurrent().getGameState());
        if (Game.getCurrent().getGameState() == GameState.COMBAT)
        {
            boolean stillaggro = false;
            for (NPC e : Game.getCurrent().getCurrentMap().getNpcs())
            {
                if (e.isHostile())
                {
                    stillaggro = true;
                    break;
                }
            }

            //logger.info("still aggro: {}", stillaggro);

            if (stillaggro == false)
            {
                EventBus.getDefault().post(new GameStateChanged(GameState.VICTORY));
                getMusicTimer().start();
            }
        }

        if (Game.getCurrent().getGameState() == GameState.VICTORY)
        {
            if (GameUtils.checkVictoryGameStateDuration())
            {
                EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getPreviousGameState()));
                if (getMusicTimer().isRunning() == false)
                {
                    getMusicTimer().start();
                }
            }
        }

        Turn turn = new Turn(getTurnNumber());
        getTurns().add(turn);
        Game.getCurrent().setCurrentTurn(turn);
        getGameTime().advanceTime(getCurrentMap().getMinutesPerTurn());
        MapUtils.calculateDayOrNight();
        getLogger().info("TURN ENDS");
        getLogger().info("=======================================================================================");
        //TODO idletimer fails to stop in certain conditions
        getIdleTimer().start();
        // logger.info("current turn number 2: {}", Game.getCurrent().getCurrentTurn().getTurnNumber());
        // Game.getCurrent().initializeTurnTimer();
    }

    public Logger getLogger()
    {
        return logger;
    }

    @SuppressWarnings("unused")
    public void decrementTurnNumber()
    {
        Game.getCurrent().setTurnNumber(getTurnNumber() - 1);
    }

    public Turn getCurrentTurn()
    {
        return currentTurn;
    }

    public World getEn()
    {
        return en;
    }


    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public ThreadController getThreadController()
    {
        return threadController;
    }

    public int getTurnNumber()
    {
        return turnNumber;
    }

    public ArrayList<Turn> getTurns()
    {
        return turns;
    }

    public AbstractWeatherSystem getWeatherSystem()
    {
        return weatherSystem;
    }

    /**
     * go back one turn in history
     * <p>
     * three ideas how to implement this:
     * <p>
     * 1. get the last turn, just add it in front 2. get the last turn, delete it from the list, then replay it. 3. get the last turn, play it, just overwrite.
     * <p>
     * I went for option 2, which gets the last turn and then replays it hoping the implementation is correct
     */
    public int retractTurn()
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
                logger.error("this does not work anymore: {}", e);
            }

            getController().getTextField().retractTurn();

            turn.getActions().clear();
            Game.getCurrent().setCurrentTurn(turn);
        }
        return getTurnNumber();
    }

    public void setCurrentTurn(Turn currentTurn)
    {
        this.currentTurn = currentTurn;
    }

    public void setEn(World en)
    {
        this.en = en;
    }

    public void setPlayers(ArrayList<Player> players)
    {
        this.players = players;
    }

    public void setThreadController(ThreadController threadController)
    {
        this.threadController = threadController;
    }

    public void setTurnNumber(int turnNumber)
    {
        this.turnNumber = turnNumber;
    }

    @SuppressWarnings("unused")
    public void setTurns(ArrayList<Turn> turns)
    {
        this.turns = turns;
    }

    @SuppressWarnings("unused")
    public void setWeatherSystem(AbstractWeatherSystem weatherSystem)
    {
        this.weatherSystem = weatherSystem;
    }

    public void stopGame()
    {
        logger.info("stopping game");
        setRunning(false);
        getThreadController().listThreads();
        System.exit(0);
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player abstractEntity)
    {
        this.currentPlayer = abstractEntity;
    }

    public int getNpcNumber()
    {
        return npcNumber;
    }

    public void setNpcNumber(int npcNumber)
    {
        this.npcNumber = npcNumber;
    }

    @SuppressWarnings("unused")
    public int getNextNPCNumber()
    {
        setNpcNumber(getNpcNumber() + 1);
        return getNpcNumber();
    }

    /**
     * so the game starts so the first player has the first movement action. Then it goes to the second player ... when all players have moved, roll over turn there only will be one player without
     * heavy extension, interesting, lets see whether I can make this work
     */
    public void addPlayers()
    {
        logger.info("adding players");
        Player p1 = new Player(0);
        Weapon sling = getWeaponList().get(3);
        Weapon club = getWeaponList().get(1);
        p1.getInventory().add(sling);
        p1.setWeapon(club);
        getPlayers().add(p1);
        p1.setMapPosition(new Point(2, 2));
        setCurrentPlayer(getPlayers().get(0));

			/*Set<ArmorPositions> positions = Game.getCurrent().getCurrentPlayer().getWearEquipment().keySet();
			for (ArmorPositions pos : positions)
			{
				logger.info("equipment position:{}  item: {} ", pos, Game.getCurrent().getCurrentPlayer().getWearEquipment().get(pos));
			}*/

    }

    public void initializeAnimationSystem()
    {
        AnimationSystem animationSystem = AnimationSystemFactory.createAnymationSystem();
        if (GameConfiguration.animated == true)
        {
            logger.info("initializing animation system");
            Thread animationSystemThread = new Thread(animationSystem);
            animationSystemThread.setName(String.valueOf(ThreadNames.LIFEFORM_ANIMATION));
            threadController.add(animationSystemThread);
        }
    }

    public void initializeBackgroundAnimationSystem()
    {
        if (GameConfiguration.animated == true)
        {
            logger.info("initializing BackgroundAnimationSystem animation system");
            BackgroundAnimationSystem backgroundAnimationSystem = new BackgroundAnimationSystem();

            Thread backgroundAnimationSystemThread = new Thread(backgroundAnimationSystem);
            backgroundAnimationSystemThread.setName(String.valueOf(ThreadNames.BACKGROUND_ANIMATION));

            threadController.add(backgroundAnimationSystemThread);
        }
    }

    public void initializeForegroundAnimationSystem()
    {
        if (GameConfiguration.animated == true)
        {
            logger.info("initializing ForegroundAnimationSystem animation system");
            ForegroundAnimationSystem foregroundAnimationSystem = new ForegroundAnimationSystem();

            Thread foregroundAnimationSystemThread = new Thread(foregroundAnimationSystem);
            foregroundAnimationSystemThread.setName(String.valueOf(ThreadNames.FOREGROUND_ANIMATION));

            threadController.add(foregroundAnimationSystemThread);
        }
    }

    public ArrayList<LifeForm> getAnimatedEntities()
    {
        return animatedEntities;
    }

    public void setAnimatedEntities(ArrayList<LifeForm> animatedEntities)
    {
        this.animatedEntities = animatedEntities;
    }

    public void addAnimatedEntities()
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

    public MainWindow getController()
    {
        return controller;
    }

    public void setController(MainWindow controller)
    {
        this.controller = controller;
    }

    public void initializeIdleTimer()
    {
        getLogger().info("initializing Turn Timer as Swing Timer");
        IdleActionListener idleActionListener = new IdleActionListener();
        setIdleTimer(new IdleTimer((int) GameConfiguration.turnwait, idleActionListener));
        getIdleTimer().setRepeats(true);
        getIdleTimer().start();
    }

    public void initializeMusicTimer()
    {
        getLogger().info("initializing Music Timer as Swing Timer");
        MusicTimerActionListener actionListener = new MusicTimerActionListener();
        setMusicTimer(new MusicTimer(GameConfiguration.victoryWait, actionListener));
        getMusicTimer().setRepeats(false);
    }


    public void initializeQuequeTimer()
    {
        getLogger().info("initializing Movement Timer as Swing Timer");
        QuequeTimerActionListener quequeTimerActionListener = new QuequeTimerActionListener();
        setQuequeTimer(new QuequeTimer(GameConfiguration.quequeWait, quequeTimerActionListener));
        getQuequeTimer().setRepeats(true);
    }


    public void initializeMissileTimer()
    {
        getLogger().info("initializing Missile Timer as Thread");
        setMissileTimer(new MissileTimer(GameConfiguration.missileWait));
        Thread missileTimerThread = new Thread(getMissileTimer());
        missileTimerThread.setName(String.valueOf(ThreadNames.MISSILE));
        getThreadController().add(missileTimerThread);
    }

    public boolean isMoved()
    {
        return moved;
    }

    public void setMoved(boolean moved)
    {
        logger.info("set moved set to: {}", moved);
        this.moved = moved;
    }

    /**
     * <a href="https://www.youtube.com/watch?v=VpH33Uw-_0E">https://www.youtube.com/watch?v=VpH33Uw-_0E</a>
     * <p>
     * public void runOLD ()
     * {
     * double drawInterval = Math.floorDiv(1000000000, 60);
     * double nextDrawTime = System.nanoTime() + drawInterval;
     * <p>
     * update();
     * repaint();
     * <p>
     * Game.getCurrent().getController().getFrame().repaint();
     * <p>
     * double remainingTime = nextDrawTime - System.nanoTime();
     * try
     * {
     * logger.info("sleep");
     * Thread.sleep((long) remainingTime);
     * }
     * catch (InterruptedException e)
     * {
     * e.printStackTrace();
     * }
     * }
     * <p>
     * This is the run method, i.e. the big cheese or the main game loop.
     */
    public void run()
    {
        while (Game.getCurrent().isRunning() == true)
        {
            if (isNextTurn() == true)
            {
                //logger.info("running advance turn");
                advanceTurn(isNpcAction());
                setNextTurn(false);
                setNpcAction(false);
            }

        }
    }

    public void initializeSoundSystem()
    {
        if (GameConfiguration.playMusic == true)
        {
            getLogger().info("initializing sound system");
            setSoundSystem(new SoundPlayer());
            getSoundSystem().setMusicIsRunning(true);
            Thread soundSystemThread = new Thread(getSoundSystem());
            soundSystemThread.setName(String.valueOf(ThreadNames.SOUND_SYSTEM));
            getThreadController().add(soundSystemThread);
        }
    }



    public void listArmor()
    {
        for (Armor t : getArmorList().values())
        {
            logger.info("armor item: {} ", t.toString());
        }
    }


    public void listWeapons()
    {
        for (Weapon t : getWeaponList().values())
        {
            logger.info("weapon item: {} ", t.toString());
        }
    }


    public void listUtilities()
    {
        for (Utility t : getUtilityList().values())
        {
            logger.info("utility item: {} ", t.toString());
        }
    }

    public Map getCurrentMap()
    {
        return currentMap;
    }

    public void setCurrentMap(Map currentMap)
    {
        this.currentMap = currentMap;
    }

    public ArrayList<Map> getMaps()
    {
        return maps;
    }

    @SuppressWarnings("unused")
    public void setMaps(ArrayList<Map> maps)
    {
        this.maps = maps;
    }

    public Hashtable<Integer, Armor> getArmorList()
    {
        return armorList;
    }

    public void setArmorList(Hashtable<Integer, Armor> armorList)
    {
        this.armorList = armorList;
    }

    public Hashtable<Integer, Weapon> getWeaponList()
    {
        return weaponList;
    }

    public void setWeaponList(Hashtable<Integer, Weapon> weaponList)
    {
        this.weaponList = weaponList;
    }

    public Hashtable<Integer, NPC> getNpcList()
    {
        return npcList;
    }

    public void setNpcList(Hashtable<Integer, NPC> npcList)
    {
        this.npcList = npcList;
    }

    @SuppressWarnings("unused")
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
        //logger.info("setting Furniture list");
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
        logger.info("initializing game thread");
        Thread gameThread = new Thread(this);
        gameThread.setName(String.valueOf(ThreadNames.GAME_THREAD));
        threadController.add(gameThread);
        getThreadController().startThreads();
    }

    public GameState getPreviousGameState()
    {
        return previousGameState;
    }

    public void setPreviousGameState(GameState previousGameState)
    {
        logger.info("setting previous game state: {}", previousGameState);
        this.previousGameState = previousGameState;
    }

    public synchronized boolean isNextTurn()
    {
        return nextTurn;
    }

    public synchronized void setNextTurn(boolean nextTurn)
    {
        //logger.info("setting next turn to: {}", nextTurn);
        this.nextTurn = nextTurn;
    }

    public synchronized boolean isNpcAction()
    {
        return npcAction;
    }

    public synchronized void setNpcAction(boolean npcAction)
    {
        //logger.info("setting npcAction to: {}", npcAction);
        this.npcAction = npcAction;
    }
}



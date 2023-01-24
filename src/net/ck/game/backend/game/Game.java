package net.ck.game.backend.game;

import net.ck.game.animation.MissileTimer;
import net.ck.game.backend.actions.AbstractAction;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.*;
import net.ck.game.backend.queuing.CommandQueue;
import net.ck.game.backend.state.GameState;
import net.ck.game.backend.state.GameStateMachine;
import net.ck.game.backend.state.UIStateMachine;
import net.ck.game.backend.threading.ThreadController;
import net.ck.game.backend.threading.ThreadNames;
import net.ck.game.backend.time.GameTime;
import net.ck.game.backend.time.IdleTimer;
import net.ck.game.backend.time.QuequeTimer;
import net.ck.game.items.*;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;
import net.ck.game.music.MusicPlayerNoThread;
import net.ck.game.music.MusicTimer;
import net.ck.game.soundeffects.SoundPlayerNoThread;
import net.ck.game.ui.listeners.Controller;
import net.ck.game.ui.timers.HighlightTimer;
import net.ck.game.weather.AbstractWeatherSystem;
import net.ck.util.CodeUtils;
import net.ck.util.GameUtils;
import net.ck.util.MapUtils;
import net.ck.util.communication.graphics.AdvanceTurnEvent;
import net.ck.util.communication.graphics.HighlightEvent;
import net.ck.util.communication.sound.GameStateChanged;
import net.ck.util.security.SecurityManagerExtension;
import net.ck.util.ui.WindowBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;

/**
 * Game Main class also Y6MU+=A7B=NpmQSs
 *
 * @author Claus
 */
public class Game implements Runnable
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    /**
     * Singleton
     */
    private static final Game game = new Game();


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
    ArrayList<Map> maps = new ArrayList<>();
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
     * controller as interaction between MainWindow and Game and controller here is the WindowBuilder and the Controller class in one. This actually needs to be treated differently.
     */
    private Controller controller;


    /**
     * soundSystem is the class dealing with the music. currently only taking files from a directory and trying to play one random song at a time
     */
    private MusicPlayerNoThread musicSystemNoThread;


    private SoundPlayerNoThread soundPlayerNoThread;

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
     * does the player action cause npc action? i.e. does the turn roll over?
     */
    private boolean npcAction;

    private HighlightTimer highlightTimer;


    /**
     * standard constructor: initializes turns, game map, weather system, players weathersystem synchonized is handled by gamemap animation by game itself probably needs a rewrite in the future
     * depends on how far I want to go
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

        GameStateMachine.getCurrent();

        EventBus.getDefault().register(this);
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


    public IdleTimer getIdleTimer()
    {
        return idleTimer;
    }

    public void setIdleTimer(IdleTimer idleTimer)
    {
        this.idleTimer = idleTimer;
    }


    public boolean isRunning()
    {
        return this.running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }


    public void addItemsToFloor()
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
        String mapName = null;
        if (exit != null)

        {
            mapName = exit.getTargetMap();
        }
        int targetTileID = -1;
        if (exit != null)
        {
            targetTileID = exit.getTargetID();
        }
        logger.info("mapname: {}, targetTileID: {}", mapName, targetTileID);
        for (Map m : getMaps())
        {
            if (m.getName().equalsIgnoreCase(mapName))
            {
                MapTile targetTile = MapUtils.getMapTileByID(m, targetTileID);
                setCurrentMap(m);
                m.initialize();
                assert targetTile != null;
                getCurrentPlayer().setMapPosition(new Point(targetTile.x, targetTile.y));
                logger.debug("new player position: {}", getCurrentPlayer().getMapPosition());
                //setAnimatedEntities(animatedEntities = new ArrayList<>());
                //addAnimatedEntities();
            }
        }
        //these two are ugly and need to be done better somehow,
        //but they make the switch faster, way faster
        getWeatherSystem().checkWeather();
        WindowBuilder.getGridCanvas().paint();

        //update the Game to switch to the current state of the new map - might be different after all
        EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));

        getIdleTimer().start();
        //TODO clear running schedules? how? currently they are on NPC.
        logger.info("end: switching map");
    }

    /**
     * <a href="https://stackoverflow.com/questions/9317461/get-the-application-closing-event">https://stackoverflow.com/questions/9317461/get-the-application-closing-event</a>
     * it appears you can only do it this way not really necessary, but I guess can be used for later on but its interesting that in order to get the shutdown event, you need yet another thread also,
     * when the fuck is this running? ThreadController is initialized way before
     */
    /*
     * public void addShutdownHook() { Runtime.getRuntime().addShutdownHook(new Thread() { public void run() { logger.error("Shutdown Hook is running !"); } });
     * logger.error("Application Terminating ..."); setRunning(false); if (threadController != null) { for (Thread t : threadController.getThreads()) { // how the heck is this supposed to work?
     * logger.error("shutdown: " + t.getName()); t.interrupt(); } } }
     */
    @Subscribe
    public void onMessageEvent(AdvanceTurnEvent event)
    {
        //logger.info("advance turn");
        setNpcAction(event.isNpcAction());
        setNextTurn(true);
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
        Game.getCurrent().getCurrentTurn().setGameState(GameStateMachine.getCurrent().getCurrentState());
        Game.getCurrent().getIdleTimer().stop();
        Game.getCurrent().getHighlightTimer().stop();
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

                //EventBus.getDefault().post(new HighlightEvent(e.getMapPosition()));
                //getThreadController().sleep(100, ThreadNames.GAME_THREAD);
                if (e.isHostile())
                {
                    AIBehaviour.determineCombat(e);

                }
                else
                {
                    AIBehaviour.determineRandom(e);

                }
                //logger.info("setting UI position: {}", e.getMapPosition());
                e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
            }
            // logger.info("environment action");
            Game.getCurrent().getEn().doAction(Game.getCurrent().getEn().createRandomEvent());
        }
        //logger.info("advance turn!");
        setTurnNumber(getTurnNumber() + 1);

        logger.info("game game state: {}", GameStateMachine.getCurrent().getCurrentState());
        if (GameStateMachine.getCurrent().getCurrentState() == GameState.COMBAT)
        {
            boolean stillaggro = false;
            for (LifeForm e : Game.getCurrent().getCurrentMap().getLifeForms())
            {
                if (e.isHostile())
                {
                    stillaggro = true;
                    break;
                }
            }

            logger.info("still aggro: {}", stillaggro);

            if (stillaggro == false)
            {
                EventBus.getDefault().post(new GameStateChanged(GameState.VICTORY));
                getMusicTimer().start();
            }
        }

        if (GameStateMachine.getCurrent().getCurrentState() == GameState.VICTORY)
        {
            if (GameUtils.checkVictoryGameStateDuration())
            {
                EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));
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
        logger.info("TURN ENDS");
        logger.info("=======================================================================================");
        getIdleTimer().start();
        getHighlightTimer().start();
        EventBus.getDefault().post(new HighlightEvent(Game.getCurrent().getCurrentPlayer().getMapPosition()));
        // logger.info("current turn number 2: {}", Game.getCurrent().getCurrentTurn().getTurnNumber());
        // Game.getCurrent().initializeTurnTimer();
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

            WindowBuilder.getTextField().retractTurn();

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

    public synchronized void stopGame()
    {
        getThreadController().listThreads();
        logger.info("stopping game");
        setRunning(false);
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
        p1.setWeapon(sling);

        getPlayers().add(p1);
        setCurrentPlayer(getPlayers().get(0));
        //for ultima IV map
        //getCurrentPlayer().setMapPosition(new Point(38, 38));
        getCurrentPlayer().setMapPosition(new Point(100, 2));
        Set<ArmorPositions> positions = Game.getCurrent().getCurrentPlayer().getWearEquipment().keySet();
        for (ArmorPositions pos : positions)
        {
            logger.info("equipment position:{}  item: {} ", pos, Game.getCurrent().getCurrentPlayer().getWearEquipment().get(pos));
        }
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
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
            long startTime = System.nanoTime();

            if (isNextTurn() == true)
            {
                //logger.info("running advance turn");
                advanceTurn(isNpcAction());
                setNextTurn(false);
                setNpcAction(false);
            }

            if (GameConfiguration.useEvents == false)
            {
                if (UIStateMachine.isUiOpen())
                {
                    WindowBuilder.getGridCanvas().paint();
                    long timeTaken = System.nanoTime() - startTime;

                    if (timeTaken < GameConfiguration.targetTime)
                    {
                        try
                        {
                            Thread.sleep((GameConfiguration.targetTime - timeTaken) / 1000000);
                        }
                        catch (InterruptedException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
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
        //logger.info("setting previous game state: {}", previousGameState);
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

    public MusicPlayerNoThread getMusicSystemNoThread()
    {
        return musicSystemNoThread;
    }

    public void setMusicSystemNoThread(MusicPlayerNoThread soundSystemNoThread)
    {
        this.musicSystemNoThread = soundSystemNoThread;
    }

    public HighlightTimer getHighlightTimer()
    {
        return highlightTimer;
    }

    public void setHighlightTimer(HighlightTimer highlightTimer)
    {
        this.highlightTimer = highlightTimer;
    }

    public SoundPlayerNoThread getSoundPlayerNoThread()
    {
        return soundPlayerNoThread;
    }

    public void setSoundPlayerNoThread(SoundPlayerNoThread soundPlayerNoThread)
    {
        this.soundPlayerNoThread = soundPlayerNoThread;
    }
}



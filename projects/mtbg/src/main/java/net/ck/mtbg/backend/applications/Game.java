package net.ck.mtbg.backend.applications;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.ai.AIBehaviour;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.Player;
import net.ck.mtbg.backend.entities.entities.World;
import net.ck.mtbg.backend.game.Turn;
import net.ck.mtbg.backend.state.*;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.items.ArmorPositions;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.map.AutoMap;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;
import net.ck.mtbg.util.communication.graphics.AdvanceTurnEvent;
import net.ck.mtbg.util.communication.graphics.HighlightEvent;
import net.ck.mtbg.util.communication.graphics.PlayerPositionChanged;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.utils.GameUtils;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.utils.UILense;
import net.ck.mtbg.weather.WeatherManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Game Main class also Y6MU+=A7B=NpmQSs
 *
 * @author Claus
 */
@Log4j2
@Getter
@Setter
@ToString
public class Game implements Runnable, Serializable
{
    /**
     * Singleton
     */
    private static final Game game = new Game();

    /**
     * the arraylist holds all the maps
     * not sure if I do need to do that or can load from disk on demand,
     * but the average map won't be that big
     */
    ArrayList<Map> maps = new ArrayList<>();

    /*
     * holds the current map, might be game map, might be any map
     */
    private Map currentMap;


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
     * turn number
     */
    private int turnNumber;

    /**
     * the list of turn objects
     */
    private ArrayList<Turn> turns = new ArrayList<>();

    /**
     * this holds the actual game time which is increasing with time
     */
    private GameTime gameTime;

    /**
     * previous game state - where are we once we enter cutscene/combat and so on
     */
    private GameState previousGameState;


    /**
     * ready for next turn?
     */
    private boolean nextTurn;

    /**
     * does the player action cause npc action? i.e. does the turn roll over?
     */
    private boolean npcAction;

    /**
     * has the player enough dex to move twice per turn?
     */
    private boolean playerMovedTwice = true;

    /**
     * what is the player action for this round
     */
    private PlayerAction playerAction;

    private long startTime;

    /**
     * List of automaps - currently the whole map is generated. tomorrow, only the explored part will be visible.
     */
    private ArrayList<AutoMap> automaps = new ArrayList<>();

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
        // thread handling
        logger.info("setting up thread system");
        setRunning(true);
        ThreadController.add(Thread.currentThread());

        setTurnNumber(0);
        Turn turn = new Turn(getTurnNumber());

        setCurrentTurn(turn);
        getTurns().add(turn);
        setEn(new World());
        //GameStateMachine.getCurrent().setCurrentState(GameState.WORLD);

        setGameTime(new GameTime());
        getGameTime().setCurrentHour(GameConfiguration.startTime.x);
        getGameTime().setCurrentMinute(GameConfiguration.startTime.y);
        UIStateMachine.setUiState(UIState.CLOSED);

        EventBus.getDefault().register(this);
        logger.info("game start with default settings finished");

        startTime = System.currentTimeMillis();
    }

    /**
     * Singleton access - now I can take away game in a lot of things :D
     */
    public static Game getCurrent()
    {
        return game;
    }


    public void addItemsToFloor()
    {

    }

    /**
     * so all can run on the EDT. I wonder where this will fuck things up, according to this:
     * <a href="https://stackoverflow.com/questions/11825281/java-swing-running-on-edt">https://stackoverflow.com/questions/11825281/java-swing-running-on-edt</a>
     * this could kind of even work. But should it?
     *
     * @param event the advancedTurn event - contains whether NPCs have a movement or not. contains the player action.
     */
    @Subscribe
    public synchronized void onMessageEvent(AdvanceTurnEvent event)
    {
        setNpcAction(event.getAction().getEvent().isHaveNPCAction());
        setPlayerAction(event.getAction());

        if (GameConfiguration.useGameThread == false)
        {
            if (GameConfiguration.debugTurn == true)
            {
                logger.debug("running advance turn 1");
            }

            setNextTurn(true);

            if (Game.getCurrent().isRunning() == true)
            {
                if (GameConfiguration.debugTurn == true)
                {
                    logger.debug("running advance turn 2");
                }
                if (isNextTurn() == true)
                {

                    if (GameConfiguration.debugTurn == true)
                    {
                        logger.debug("running advance turn 3");
                    }
                    advanceTurn(event.getAction());
                    setNextTurn(false);
                    setNpcAction(false);
                }
            }
        }
        else
        {
            //TODO it fixes the issue but not quite I think
            //fixed it in Controller movement right away.
            //moving means mouse will center on cursor again.
            //CursorUtils.calulateCursorPosition();
            //logger.info("advance turn");
            setNextTurn(true);
        }
    }


    /**
     * advance one turn - the end of the rollover in civ for instance. all npcs act, environment acts, idle timer for passing the turn starts.
     * need to think about implementing double speed, either due to DEX or due to transport
     * <p>
     * <a href="https://stackoverflow.com/questions/30989558/java-8-retry-a-method-until-a-condition-is-fulfilled-in-intervals">...</a>
     *
     * @param action to determine what to do for player and to get this out of controller
     */
    public synchronized void advanceTurn(PlayerAction action)
    {
        Game.getCurrent().getCurrentPlayer().doAction(action);
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire highlighting event in advanceTurn");
        }
        EventBus.getDefault().post(new HighlightEvent(Game.getCurrent().getCurrentPlayer().getMapPosition()));
        Game.getCurrent().getCurrentTurn().setGameState(GameStateMachine.getCurrent().getCurrentState());
        TimerManager.getIdleTimer().stop();
        TimerManager.getHighlightTimer().stop();

        //logger.info("waiting for missile to finish");
        if (GameConfiguration.useTimerForMissiles == true)
        {
            if (TimerManager.getMissileUtilTimer() != null)
            {
                while (TimerManager.getMissileUtilTimer().getMissileTimerTask().isRunning())
                {
                    //logger.debug("missile is on its way");
                    ThreadController.sleep(50, ThreadNames.GAME_THREAD);
                }
            }
        }
        else
        {
            if (TimerManager.getMissileTimer() != null)
            {
                while (TimerManager.getMissileTimer().isRunning())
                {
                    //logger.debug("missile is on its way");
                    ThreadController.sleep(50, ThreadNames.GAME_THREAD);
                }
            }
        }
        //logger.info("waiting for hit animation to run");
        if (TimerManager.getHitMissImageTimer().getHitMissImageTimerTask() != null)
        {
            while (TimerManager.getHitMissImageTimer().getHitMissImageTimerTask().isRunning() == true)
            {
                //logger.debug("waiting for animation to finish");
                ThreadController.sleep(50, ThreadNames.GAME_THREAD);
            }
        }
        //logger.info("hit animation has finished");
        TimerManager.getHitMissImageTimer().purge();
        WindowBuilder.getGridCanvas().paint();

        Game.getCurrent().getEn().doAction(Game.getCurrent().getEn().createRandomEvent(action));


        if (Game.getCurrent().getCurrentPlayer().hasTwoActions())
        {
            if (playerMovedTwice == false)
            {
                if (GameConfiguration.debugTurn == true)
                {
                    logger.debug("player has moved twice, now world is allowed to");
                }
                if (action.isHaveNPCAction())
                {
                    for (LifeForm e : Game.getCurrent().getCurrentMap().getLifeForms())
                    {
                        if (e instanceof Player)
                        {
                            //logger.info("found player, continue");
                            continue;
                        }
                        if (e.hasTwoActions())
                        {
                            if (GameConfiguration.debugTurn == true)
                            {
                                logger.debug("two actions");
                            }
                            AIBehaviour.determineAction(e);
                            if (GameConfiguration.debugTurn == true)
                            {
                                logger.debug("first done");
                            }
                            e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
                            AIBehaviour.determineAction(e);
                            if (GameConfiguration.debugTurn == true)
                            {
                                logger.debug("second done");
                            }
                            e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
                        }
                        else
                        {
                            AIBehaviour.determineAction(e);
                            //logger.info("setting UI position: {}", e.getMapPosition());
                            e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
                        }
                    }
                    // logger.info("environment action");
                    playerMovedTwice = true;
                }
            }
            else
            {
                if (GameConfiguration.debugTurn == true)
                {
                    logger.debug("player moving twice now");
                }
                playerMovedTwice = false;
            }
        }
        else
        {
            if (action.isHaveNPCAction())
            {
                for (LifeForm e : Game.getCurrent().getCurrentMap().getLifeForms())
                {
                    if (e instanceof Player)
                    {
                        //logger.info("found player, continue");
                        continue;
                    }
                    // logger.info("npc: {}", e);
                    //EventBus.getDefault().post(new HighlightEvent(e.getMapPosition()));
                    //getThreadController().sleep(100, ThreadNames.GAME_THREAD);
                    if (e.hasTwoActions())
                    {
                        if (GameConfiguration.debugTurn == true)
                        {
                            logger.debug("two actions");
                        }
                        AIBehaviour.determineAction(e);
                        if (GameConfiguration.debugTurn == true)
                        {
                            logger.debug("first done");
                        }
                        e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
                        WindowBuilder.getGridCanvas().paint();
                        ThreadController.sleep(300, ThreadNames.GAME_THREAD);
                        AIBehaviour.determineAction(e);
                        if (GameConfiguration.debugTurn == true)
                        {
                            logger.debug("second done");
                        }
                        e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
                    }
                    else
                    {
                        AIBehaviour.determineAction(e);
                        //NPCs can shoot missiles as well!
                        if (GameConfiguration.useTimerForMissiles == true)
                        {
                            if (TimerManager.getMissileUtilTimer() != null)
                            {
                                while (TimerManager.getMissileUtilTimer().getMissileTimerTask().isRunning())
                                {
                                    if (GameConfiguration.debugTurn == true)
                                    {
                                        logger.debug("missile is on its way");
                                    }
                                    ThreadController.sleep(50, ThreadNames.GAME_THREAD);
                                }
                            }
                        }
                        else
                        {
                            if (TimerManager.getMissileTimer() != null)
                            {
                                while (TimerManager.getMissileTimer().isRunning())
                                {
                                    logger.debug("missile is on its way");
                                    ThreadController.sleep(50, ThreadNames.GAME_THREAD);
                                }
                            }
                        }
                        // logger.info("waiting for hit animation to run");
                        if (TimerManager.getHitMissImageTimer().getHitMissImageTimerTask() != null)
                        {
                            while (TimerManager.getHitMissImageTimer().getHitMissImageTimerTask().isRunning() == true)
                            {
                                //logger.debug("waiting for animation to finish");
                                ThreadController.sleep(50, ThreadNames.GAME_THREAD);
                            }
                        }
                        //logger.info("hit animation has finished");
                        TimerManager.getHitMissImageTimer().purge();
                        WindowBuilder.getGridCanvas().paint();
                        //logger.info("setting UI position: {}", e.getMapPosition());
                        e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
                    }
                }
            }
        }


        //logger.info("advance turn!");
        setTurnNumber(getTurnNumber() + 1);

        //logger.info("game game state: {}", GameStateMachine.getCurrent().getCurrentState());

        NoiseManager.calculateMusictoRun();

        Turn turn = new Turn(getTurnNumber());
        getTurns().add(turn);
        Game.getCurrent().setCurrentTurn(turn);
        //logger.debug("minutes per turn: {}", getCurrentMap().getMinutesPerTurn());
        getGameTime().advanceTime(getCurrentMap().getMinutesPerTurn());
        MapUtils.setVisibility(MapUtils.calculateDayOrNight());
        logger.info("TURN ENDS");
        logger.info("Current Game Time: {}", getGameTime().toString());
        logger.info("=======================================================================================");
        if (UIStateMachine.isDialogOpened())
        {
            logger.info("do nothing, wait");
        }
        else if (UIStateMachine.getUiState().equals(UIState.OVERLAY))
        {
            logger.info("in overlay dont start idle timer");
        }
        else
        {
            TimerManager.getIdleTimer().start();
        }

        if (UIStateMachine.isUiOpen())
        {
            TimerManager.getHighlightTimer().start();
            if (GameConfiguration.debugEvents == true)
            {
                logger.debug("fire highlighting event in advanceTurn");
            }
            EventBus.getDefault().post(new HighlightEvent(Game.getCurrent().getCurrentPlayer().getMapPosition()));
            UILense.getCurrent().identifyVisibleTilesBest();
            UILense.getCurrent().identifyBufferedTiles();
            MapUtils.calculateTiles(WindowBuilder.getGridCanvas().getGraphics());
            MapUtils.calculateVisibleTileImages(WindowBuilder.getGridCanvas().getGraphics());
            if (GameConfiguration.calculateBrightenUpImageInPaint == false)
            {
                WindowBuilder.getGridCanvas().paint();
            }
        }
    }

    public synchronized void stopGame()
    {
        logger.info("number of turns: {}", getTurnNumber());
        long elapsedTime = System.currentTimeMillis() - startTime;

        logger.info("time spent in game: {}", String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(elapsedTime), TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))));
        ThreadController.listThreads();
        GameUtils.listThreadTimes();
        setRunning(false);
        System.exit(0);
    }


    /**
     * so the game starts so the first player has the first movement action. Then it goes to the second player ... when all players have moved, roll over turn there only will be one player without
     * heavy extension, interesting, lets see whether I can make this work
     */
    public void addPlayers(Point startPosition)
    {
        logger.info("adding player");
        Player p1 = new Player(0);
        Weapon sling = ItemManager.getWeaponList().get(3);
        Weapon club = ItemManager.getWeaponList().get(1);
        p1.getInventory().add(sling);
        p1.getInventory().add(club);
        p1.wieldWeapon(sling);


        setCurrentPlayer(p1);
        //for ultima IV map
        //getCurrentPlayer().setMapPosition(new Point(38, 38));
        if (startPosition != null)
        {
            getCurrentPlayer().setMapPosition(startPosition);
        }
        else
        {
            getCurrentPlayer().setMapPosition(new Point(2, 2));
        }

        Game.getCurrent().getCurrentMap().mapTiles[getCurrentPlayer().getMapPosition().x][getCurrentPlayer().getMapPosition().y].setLifeForm(getCurrentPlayer());


        Set<ArmorPositions> positions = Game.getCurrent().getCurrentPlayer().getWearEquipment().keySet();
        for (ArmorPositions pos : positions)
        {
            //logger.info("equipment position:{}  item: {} ", pos, Game.getCurrent().getCurrentPlayer().getWearEquipment().get(pos));
        }
        Game.getCurrent().getCurrentMap().getLifeForms().add(Game.getCurrent().getCurrentPlayer());
    }


    /**
     * <a href="https://www.youtube.com/watch?v=VpH33Uw-_0E">https://www.youtube.com/watch?v=VpH33Uw-_0E</a>
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
                advanceTurn(getPlayerAction());
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
                        ThreadController.sleep((int) ((GameConfiguration.targetTime - timeTaken) / 1000000), ThreadNames.GAME_THREAD);
                    }
                }
            }
        }
    }

    public void startThreads()
    {
        //noinspection StatementWithEmptyBody
        if (GameConfiguration.useGameThread == false)
        {

        }
        else
        {
            logger.info("initializing game thread");
            Thread gameThread = new Thread(this);
            gameThread.setName(String.valueOf(ThreadNames.GAME_THREAD));
            ThreadController.add(gameThread);
            ThreadController.startThreads();
        }
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

    /**
     * is called by AbstractEntity - I could also move it to player and NPC as well
     * and just call super() before doing the little bit different things.
     * In fact, I think I will do so
     * //TODO do standard work in AbstractEntity for map switch, then do the concrete stuff relevant to player and npc there
     */
    public void finishMapSwitch(Map oldMap)
    {
        //these two are ugly and need to be done better somehow,
        //but they make the switch faster, way faster
        WeatherManager.getWeatherSystem().checkWeather();
        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire player position change in finishMapSwitch");
        }
        EventBus.getDefault().post(new PlayerPositionChanged(Game.getCurrent().getCurrentPlayer()));
        //WindowBuilder.getGridCanvas().paint();
        //update the Game to switch to the current state of the new map - might be different after all

        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire game state changed in finishMapSwitch");
        }
        EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));
        TimerManager.getIdleTimer().start();
        //TODO clear running schedules? how? currently they are on NPC.
        for (LifeForm f : oldMap.getLifeForms())
        {
            if (f.getSchedule() != null)
            {
                f.getSchedule().setActive(false);
            }
        }
        //enable current schedules
        for (LifeForm f : Game.getCurrent().getCurrentMap().getLifeForms())
        {
            if (f.getSchedule() != null)
            {
                f.getSchedule().setActive(true);
            }


            //TODO teleport NPCs to positions based on schedule here
            //if (Game.getCurrent().getGameTime().greaterOrEqual(f.getSchedule()))
            //{
            //TODO
            //}
        }
        //TODO currently stupid, but as the calculation does not work properly do it this way instead
        MapUtils.calculateAllTileImages(Game.getCurrent().getCurrentMap());
    }

    public void saveGame()
    {
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
            objectOutputStream.writeObject(this);
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

    public void loadGame()
    {
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



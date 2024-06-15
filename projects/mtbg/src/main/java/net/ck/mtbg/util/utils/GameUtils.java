package net.ck.mtbg.util.utils;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.animation.background.BackgroundAnimationSystem;
import net.ck.mtbg.animation.background.BackgroundAnimationSystemActionListener;
import net.ck.mtbg.animation.background.BackgroundAnimationSystemTimer;
import net.ck.mtbg.animation.foreground.ForegroundAnimationSystem;
import net.ck.mtbg.animation.foreground.ForegroundAnimationSystemActionListener;
import net.ck.mtbg.animation.foreground.ForegroundAnimationSystemTimer;
import net.ck.mtbg.animation.lifeform.*;
import net.ck.mtbg.animation.missile.MissileTimer;
import net.ck.mtbg.animation.missile.MissileUtilTimer;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.game.GameLogs;
import net.ck.mtbg.backend.game.Turn;
import net.ck.mtbg.backend.state.*;
import net.ck.mtbg.backend.threading.ThreadController;
import net.ck.mtbg.backend.threading.ThreadNames;
import net.ck.mtbg.backend.time.IdleActionListener;
import net.ck.mtbg.backend.time.IdleTimer;
import net.ck.mtbg.backend.time.QuequeTimer;
import net.ck.mtbg.backend.time.QuequeTimerActionListener;
import net.ck.mtbg.items.Armor;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.items.Utility;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.music.MusicPlayerNoThread;
import net.ck.mtbg.music.MusicTimer;
import net.ck.mtbg.music.MusicTimerActionListener;
import net.ck.mtbg.soundeffects.SoundPlayerNoThread;
import net.ck.mtbg.ui.highlighting.HighlightTimer;
import net.ck.mtbg.ui.highlighting.HightlightTimerActionListener;
import net.ck.mtbg.util.communication.graphics.HighlightEvent;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import net.ck.mtbg.util.ui.WindowBuilder;
import net.ck.mtbg.util.xml.RunXMLParser;
import net.ck.mtbg.weather.*;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Log4j2
public class GameUtils
{
    public static void showStackTrace(String methodName)
    {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        logger.info("calling " + methodName + " from: {} or: {}", stackTraceElements[1].getMethodName(), stackTraceElements[2].getMethodName());
    }


    public static void invertActions(Turn turn)
    {
        /*for (PlayerAction p : turn.getActions())
        {
            // logger.debug("player action p: {}", p.getEntity().getClass().getName());
            // NORTH, SOUTH, WEST, EAST, ENTER, ESC, NULL
            switch (p.getType())
            {
                case NORTH:
                {
                    p.setEvent(new SouthAction());
                    break;
                }
                case SOUTH:
                {
                    p.setEvent(new NorthAction());
                    break;
                }
                case EAST:
                {
                    p.setEvent(new WestAction());
                    break;
                }
                case WEST:
                {
                    p.setEvent(new EastAction());
                    break;
                }
                case ENTER:
                {
                    logger.error("ENTER crept in here");
                    break;
                }
                case ESC:
                {
                    logger.error("ESC crept in here");
                    break;
                }
                case NULL:
                {
                    logger.error("Null crept in here: {}", p.toString());
                    break;
                }
                case SPACE:
                {
                    p.setEvent(new SpaceAction());
                    break;
                }
                default:
                {
                    throw new IllegalArgumentException("not expected value during invertActions: " + p.getType().toString());
                }
            }
        }*/

    }

    public static boolean checkVictoryGameStateDuration()
    {
        int i = 0;
        for (Turn t : Lists.reverse(Game.getCurrent().getTurns()))
        {
            if (t.getGameState() == GameState.VICTORY)
            {
                i++;
            }

            if (i == GameConfiguration.waitTurns)
            {
                return true;
            }
        }

        return false;
    }

    public static void initializeSoundSystemNoThread()
    {
        if (GameConfiguration.playSound == true)
        {
            logger.info("initializing sound system no thread");
            NoiseManager.setSoundPlayerNoThread(new SoundPlayerNoThread());
        }
    }

    public static void initializeMusicSystemNoThread()
    {
        if (GameConfiguration.playMusic == true)
        {
            logger.info("initializing music system no thread");
            NoiseManager.setMusicSystemNoThread(new MusicPlayerNoThread());
            NoiseManager.getMusicSystemNoThread().setMusicIsRunning(true);
        }
    }

    public static void initializeHighlightingTimer()
    {
        logger.info("initializing Highlighting Timer as Swing Timer");
        HightlightTimerActionListener actionListener = new HightlightTimerActionListener();
        TimerManager.setHighlightTimer(new HighlightTimer(GameConfiguration.highlightDelay, actionListener));
        TimerManager.getHighlightTimer().setRepeats(true);
    }

    public static void initializeMusicTimer()
    {
        logger.info("initializing Music Timer as Swing Timer");
        MusicTimerActionListener actionListener = new MusicTimerActionListener();
        TimerManager.setMusicTimer(new MusicTimer(GameConfiguration.victoryWait, actionListener));
        TimerManager.getMusicTimer().setRepeats(false);

    }

    public static void initializeMissileThread()
    {
        if (GameConfiguration.useTimerForMissiles == true)
        {
            logger.info("initializing missile timer as util timer");
            TimerManager.setMissileUtilTimer(new MissileUtilTimer());
        }
        else
        {
            logger.info("initializing Missile Timer as Thread");
            TimerManager.setMissileTimer(new MissileTimer(GameConfiguration.missileWait));
            Thread missileTimerThread = new Thread(TimerManager.getMissileTimer());
            missileTimerThread.setName(String.valueOf(ThreadNames.MISSILE));
            ThreadController.add(missileTimerThread);
        }
    }

    public static void initializeQuequeTimer()
    {
        logger.info("initializing Movement Timer as Swing Timer");
        QuequeTimerActionListener quequeTimerActionListener = new QuequeTimerActionListener();
        TimerManager.setQuequeTimer(new QuequeTimer(GameConfiguration.quequeWait, quequeTimerActionListener));
        TimerManager.getQuequeTimer().setRepeats(true);
    }

    public static void initializeIdleTimer()
    {
        logger.info("initializing Turn Timer as Swing Timer");
        IdleActionListener idleActionListener = new IdleActionListener();
        TimerManager.setIdleTimer(new IdleTimer((int) GameConfiguration.turnwait, idleActionListener));
        TimerManager.getIdleTimer().setRepeats(true);
        TimerManager.getIdleTimer().start();
    }

    public static void initializeWeatherSystem()
    {
        logger.info("BEGIN: initializing weather system");


        AbstractWeatherSystem weatherSystem = WeatherSystemFactory.createWeatherSystem(Game.getCurrent().getCurrentMap());

        switch ((CodeUtils.getRealClass(weatherSystem)).getSimpleName())
        {
            case "SyncWeatherSystem":
                logger.info("initializing sync weather system");
                break;
            case "AsyncWeatherSystem":
                logger.info("initializing  async weather system");
                Thread weatherSystemThread = new Thread((AsyncWeatherSystem) weatherSystem);
                weatherSystemThread.setName(String.valueOf(ThreadNames.WEATHER_ANIMATION));
                ThreadController.add(weatherSystemThread);
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

        WeatherManager.setWeatherSystem(weatherSystem);
        logger.info("END: initializing weather system");
    }

    public static void initializeForegroundAnimationSystem()
    {
        if (GameConfiguration.animated == true)
        {
            if (GameConfiguration.useTimersForAnimations == true)
            {
                logger.info("initializing Foregroundanimation System as Swing Timer");
                ForegroundAnimationSystemActionListener foregroundAnimationSystemActionListener = new ForegroundAnimationSystemActionListener();
                TimerManager.setForegroundAnimationSystemTimer(new ForegroundAnimationSystemTimer(GameConfiguration.animationForeGroundDelay, foregroundAnimationSystemActionListener));
                TimerManager.getForegroundAnimationSystemTimer().setRepeats(true);
                TimerManager.getForegroundAnimationSystemTimer().start();
            }
            else
            {
                logger.info("initializing ForegroundAnimationSystem animation system");
                ForegroundAnimationSystem foregroundAnimationSystem = new ForegroundAnimationSystem();

                Thread foregroundAnimationSystemThread = new Thread(foregroundAnimationSystem);
                foregroundAnimationSystemThread.setName(String.valueOf(ThreadNames.FOREGROUND_ANIMATION));

                ThreadController.add(foregroundAnimationSystemThread);
            }
        }
    }

    public static void initializeBackgroundAnimationSystem()
    {
        if (GameConfiguration.animated == true)
        {
            if (GameConfiguration.useTimersForAnimations == true)
            {
                logger.info("initializing Backgroundanimation System as Swing Timer");
                BackgroundAnimationSystemActionListener backgroundAnimationSystemActionListener = new BackgroundAnimationSystemActionListener();
                TimerManager.setBackgroundAnimationSystemTimer(new BackgroundAnimationSystemTimer(GameConfiguration.animationBackGroundDelay, backgroundAnimationSystemActionListener));
                TimerManager.getBackgroundAnimationSystemTimer().setRepeats(true);
                TimerManager.getBackgroundAnimationSystemTimer().start();
            }
            else
            {
                logger.info("start: initializing BackgroundAnimationSystem animation system");
                BackgroundAnimationSystem backgroundAnimationSystem = new BackgroundAnimationSystem();

                Thread backgroundAnimationSystemThread = new Thread(backgroundAnimationSystem);
                backgroundAnimationSystemThread.setName(String.valueOf(ThreadNames.BACKGROUND_ANIMATION));

                ThreadController.add(backgroundAnimationSystemThread);
                logger.info("finish: initializing BackgroundAnimationSystem animation system");
            }
        }
    }

    public static void initializeAnimationSystem()
    {



        /*    logger.info("initializing Turn Timer as Swing Timer");
            IdleActionListener idleActionListener = new IdleActionListener();
            Game.getCurrent().setIdleTimer(new IdleTimer((int) GameConfiguration.turnwait, idleActionListener));
            Game.getCurrent().getIdleTimer().setRepeats(true);
            Game.getCurrent().getIdleTimer().start();
        */

        if (GameConfiguration.useTimersForAnimations == true)
        {
            if (GameConfiguration.useUtilTimerForAnimation)
            {
                logger.info("initializing animation system timer as util timer");
                TimerManager.setAnimationSystemUtilTimer(new AnimationSystemUtilTimer());
                AnimationSystemTimerTask animationSystemTimerTask = new AnimationSystemTimerTask();
                TimerManager.getAnimationSystemUtilTimer().schedule(animationSystemTimerTask, 0, GameConfiguration.animationLifeformDelay);
            }
            else
            {
                logger.info("initializing Animation System as Swing Timer");
                AnimationSystemActionListener animationSystemActionListener = new AnimationSystemActionListener();
                TimerManager.setAnimationSystemTimer(new AnimationSystemTimer(GameConfiguration.animationLifeformDelay, animationSystemActionListener));
                TimerManager.getAnimationSystemTimer().setRepeats(true);
                TimerManager.getAnimationSystemTimer().start();
            }
        }
        else
        {
            AnimationSystem animationSystem = AnimationSystemFactory.createAnymationSystem();
            //if (GameConfiguration.animated == true)
            //{
            logger.info("start: initializing animation system as thread");
            Thread animationSystemThread = new Thread(animationSystem);
            animationSystemThread.setName(String.valueOf(ThreadNames.LIFEFORM_ANIMATION));
            ThreadController.add(animationSystemThread);
            logger.info("finish: initializing animation system");
            //}
        }
    }


    /**
     * initialize the maps, cleanup later
     * not sure whether I actually want this or even need this, or even need
     * the north/east/south/west
     * MapUtils.calculateTileDirections(map.getTiles());
     * To be identified later
     */
    public static void initializeMaps()
    {
        logger.info("start: initialize maps");

        File folder = new File(GameConfiguration.mapFileRootPath);
        File[] listOfFiles = folder.listFiles();

        for (File file : Objects.requireNonNull(listOfFiles))
        {
            if (file.isFile())
            {
                //logger.info("file name: {}", file.getName());
                if (file.getName().contains("xml"))
                {
                    logger.info("parsing map: {}", GameConfiguration.mapFileRootPath + File.separator + file.getName());
                    Map map = RunXMLParser.parseMap(GameConfiguration.mapFileRootPath + File.separator + file.getName());
                    logger.info("parsed map: {}", map);
                    Game.getCurrent().getMaps().add(map);
                }

            }
        }
        logger.info("end: initialize maps");
    }

    public static void initializeAllItems()
    {
        logger.info("start: initialize items");

        File folder = new File(GameConfiguration.itemFileRootPath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                if (file.getName().equalsIgnoreCase("armor.xml"))
                {
                    logger.info("parsing armor: {}", GameConfiguration.itemFileRootPath + File.separator + file.getName());
                    ItemManager.setArmorList(RunXMLParser.parseArmor(GameConfiguration.itemFileRootPath + File.separator + file.getName()));
                }

                else if (file.getName().equalsIgnoreCase("weapons.xml"))
                {
                    logger.info("parsing weapons: {}", GameConfiguration.itemFileRootPath + File.separator + file.getName());
                    ItemManager.setWeaponList(RunXMLParser.parseWeapons(GameConfiguration.itemFileRootPath + File.separator + file.getName()));
                }

                else if (file.getName().equalsIgnoreCase("utilities.xml"))
                {
                    logger.info("parsing utilities: {}", GameConfiguration.itemFileRootPath + File.separator + file.getName());
                    ItemManager.setUtilityList(RunXMLParser.parseUtilities(GameConfiguration.itemFileRootPath + File.separator + file.getName()));
                }

                else if (file.getName().equalsIgnoreCase("furniture.xml"))
                {
                    logger.info("parsing furniture: {}", GameConfiguration.itemFileRootPath + File.separator + file.getName());
                    ItemManager.setFurnitureList(RunXMLParser.parseFurniture(GameConfiguration.itemFileRootPath + File.separator + file.getName()));
                }

            }
        }
        logger.info("end: initialize items");

        listWeapons();
        listUtilities();
        listArmor();
        listFurniture();

    }


    public static void initializeSpells()
    {
        logger.info("start: initialize spells");

        File folder = new File(GameConfiguration.spellsFileRootPath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                if (file.getName().equalsIgnoreCase("spells.xml"))
                {
                    logger.info("parsing spells: {}", GameConfiguration.spellsFileRootPath + File.separator + file.getName());
                    SpellManager.setSpellList(RunXMLParser.parseSpells(GameConfiguration.spellsFileRootPath + File.separator + file.getName()));
                }
            }
        }
        logger.info("end: initialize spells");

        listSpells();
    }

    public static void initializeSkills()
    {
        logger.info("start: initialize skills");

        File folder = new File(GameConfiguration.skillsFileRootPath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                if (file.getName().equalsIgnoreCase("skills.xml"))
                {
                    logger.info("parsing skills: {}", GameConfiguration.skillsFileRootPath + File.separator + file.getName());
                    SkillManager.setSkillList(RunXMLParser.parseSkills(GameConfiguration.skillsFileRootPath + File.separator + file.getName()));
                }
            }
        }
        logger.info("end: initialize skills");

        listSkills();
    }


    public static void initializeNPCs()
    {
        logger.info("start: initialize NPCs");

        File folder = new File(GameConfiguration.npcFileRootPath);
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                if (file.getName().equalsIgnoreCase("npc.xml"))
                {
                    logger.info("parsing NPCs: {}", GameConfiguration.npcFileRootPath + File.separator + file.getName());
                    NPCManager.setNpcList(RunXMLParser.parseNPCs(GameConfiguration.npcFileRootPath + File.separator + file.getName()));
                }
            }
        }
        logger.info("end: initialize NPCs");

        listNPCs();
    }


    private static void listSpells()
    {
        for (AbstractSpell s : SpellManager.getSpellList().values())
        {
            logger.info("spellid: {}, spell name: {}, spell level:{}", s.getId(), s.getName(), s.getLevel());
        }
    }


    private static void listNPCs()
    {
        for (NPC s : NPCManager.getNpcList().values())
        {
            logger.info("npc id: {}, details: {}", s.getId(), s.toString());
        }
    }


    private static void listSkills()
    {
        for (AbstractSkill s : SkillManager.getSkillList().values())
        {
            logger.info("skillid: {}, skill name: {}, skill level:{}", s.getId(), s.getName(), s.getLevel());
        }
    }


    private static void listFurniture()
    {
        for (FurnitureItem t : ItemManager.getFurnitureList().values())
        {
            logger.info("furniture item: {} ", t.toString());
        }
    }


    public static void listArmor()
    {
        for (Armor t : ItemManager.getArmorList().values())
        {
            logger.info("armor item: {} ", t.toString());
        }
    }


    public static void listWeapons()
    {
        for (Weapon t : ItemManager.getWeaponList().values())
        {
            logger.info("weapon item: {} ", t.toString());
        }
    }


    public static void listUtilities()
    {
        for (Utility t : ItemManager.getUtilityList().values())
        {
            logger.info("utility item: {} ", t.toString());
        }
    }

    /**
     * in turn 0, highlighting does not yet work, nor does visibility.
     * I want to fix this by simply initializing the things at the end of game start.
     * it appears, UI is not yet open.
     */
    public static synchronized void initializeRest()
    {

        TimerManager.getHighlightTimer().start();
        EventBus.getDefault().post(new HighlightEvent(Game.getCurrent().getCurrentPlayer().getMapPosition()));
        MapUtils.setVisibility(MapUtils.calculateDayOrNight());
        EventBus.getDefault().post(new GameStateChanged(Game.getCurrent().getCurrentMap().getGameState()));
        //let us see whether this works:

        for (LifeForm e : Game.getCurrent().getCurrentMap().getLifeForms())
        {
            logger.info("setting UI position: {}", e.getMapPosition());
            e.setUIPosition(MapUtils.calculateUIPositionFromMapOffset(e.getMapPosition()));
            //enable current schedules
            if (e.getSchedule() != null)
            {
                e.getSchedule().setActive(true);
            }

        }
        UILense.getCurrent().identifyVisibleTilesBest();
        UILense.getCurrent().identifyBufferedTiles();
        MapUtils.calculateTiles(WindowBuilder.getGridCanvas().getGraphics());
        MapUtils.calculateVisibleTileImages(WindowBuilder.getGridCanvas().getGraphics());
        //MapUtils.calculateAllTileImages(WindowBuilder.getGridCanvas().getGraphics());
        Game.getCurrent().getCurrentPlayer().setMapPosition(Game.getCurrent().getCurrentPlayer().getMapPosition());
    }

    public static void listMaps()
    {
        for (Map e : Game.getCurrent().getMaps())
        {
            for (MapTile[] rowOfStrings : e.mapTiles)
            {
                for (MapTile s : rowOfStrings)
                {
                    System.out.println(s);
                }
            }
        }
    }


    public static void initializeHitOrMissTimer()
    {
        HitMissImageTimer hitMissImageTimer = new HitMissImageTimer();
        TimerManager.setHitMissImageTimer(hitMissImageTimer);
    }

    /**
     * start map now has logic in it that should not be there but thats good enough for now.
     */
    public static void setStartMap()
    {
        for (Map map : Game.getCurrent().getMaps())
        {

            map.setVisibilityRange(1);

            map.setWeatherSystem(true);
            map.setFixedWeather(WeatherTypes.SUN);
            if (map.getWeather() == null)
            {
                Weather weather = new Weather();
                weather.setType(WeatherTypes.SUN);
                map.setWeather(weather);
            }

            if (Objects.requireNonNull(map).getName().equalsIgnoreCase(GameConfiguration.startMap))
            {
                logger.info("map: {}", map);
                map.initialize();
                map.setVisibilityRange(2);
                map.setMinutesPerTurn(10);
                Game.getCurrent().setCurrentMap(map);
                Game.getCurrent().addItemsToFloor();
                //Game.getCurrent().addManyNPCs(map);
                return;
            }
        }
    }

    public static void listThreadTimes()
    {
        logger.info("Paint events {}, taking on average: {} miliseconds,", GameLogs.getPaintTimes().size(), TimeUnit.NANOSECONDS.toMillis(GameLogs.calculateTimeAverage(GameLogs.getPaintTimes())));
        logger.info("retrieve bright images on average: {} nanoseconds", TimeUnit.NANOSECONDS.toNanos(GameLogs.calculateTimeAverage(GameLogs.getRetrieveBrightImages())));
        logger.info("create bright images on average: {} nanoseconds", TimeUnit.NANOSECONDS.toNanos(GameLogs.calculateTimeAverage(GameLogs.getCreateBrightImages())));
        logger.info("Paint times on average: {} seconds", TimeUnit.NANOSECONDS.toSeconds(GameLogs.calculateTimeAverage(GameLogs.getPaintTimes())));
    }

}



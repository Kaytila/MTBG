package net.ck.mtbg.test;

import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.weather.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SyncWeatherSystemTest
{
    private Map originalCurrentMap;

    @BeforeAll
    public static void setUpBeforeClass()
    {
        System.setProperty("mtbg.testMode", "true");
        RunGame.startGame(false);
    }

    @AfterAll
    public static void tearDownAfterClass()
    {
        Game.getCurrent().setRunning(false);
    }

    private static Map createWeatherMap(boolean weatherEnabled, boolean synced, int randomness)
    {
        Map map = new Map();
        map.setWeatherSystem(weatherEnabled);
        map.setSyncedWeatherSystem(synced);
        map.setWeatherRandomness(randomness);
        map.setWeather(new Weather());
        map.initialize();
        return map;
    }

    @BeforeEach
    public void setUp()
    {
        originalCurrentMap = Game.getCurrent().getCurrentMap();
    }

    @AfterEach
    public void tearDown()
    {
        if (originalCurrentMap != null)
        {
            Game.getCurrent().setCurrentMap(originalCurrentMap);
        }
    }

    @Test
    public void factoryCreatesSynchronizedWeatherSystemForSyncedWeatherMaps()
    {
        Map map = createWeatherMap(true, true, 7);
        Game.getCurrent().setCurrentMap(map);

        AbstractWeatherSystem weatherSystem = WeatherSystemFactory.createWeatherSystem(map);

        assertAll(
                () -> assertInstanceOf(SyncWeatherSystem.class, weatherSystem),
                () -> assertTrue(weatherSystem.isSynchronized()),
                () -> assertEquals(7, weatherSystem.getRandomness())
        );
    }

    @Test
    public void switchWeatherSetsNoneWhenCurrentMapHasNoWeatherSystem()
    {
        Map map = createWeatherMap(false, false, 3);
        Game.getCurrent().setCurrentMap(map);
        SyncWeatherSystem weatherSystem = new SyncWeatherSystem(3);

        weatherSystem.switchWeather();

        assertEquals(WeatherTypes.NONE, map.getWeather().getType(),
                "Auf Karten ohne Wettersystem muss das Wetter auf NONE gesetzt werden");
    }

    @Test
    public void checkWeatherKeepsExistingWeatherObjectAccessibleOnWeatherMaps()
    {
        Map map = createWeatherMap(true, true, 5);
        map.getWeather().setType(WeatherTypes.FOG);
        Game.getCurrent().setCurrentMap(map);
        SyncWeatherSystem weatherSystem = new SyncWeatherSystem(5);

        weatherSystem.checkWeather();

        assertAll(
                () -> assertNotNull(map.getWeather()),
                () -> assertNotNull(weatherSystem.getCurrentWeather()),
                () -> assertEquals(map.getWeather(), weatherSystem.getCurrentWeather())
        );
    }
}

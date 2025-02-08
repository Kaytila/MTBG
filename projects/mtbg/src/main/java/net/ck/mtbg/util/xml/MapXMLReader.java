package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCFactory;
import net.ck.mtbg.backend.queuing.Schedule;
import net.ck.mtbg.backend.queuing.ScheduleActivity;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.items.Armor;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.items.ItemFactory;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.Message;
import net.ck.mtbg.map.MessageTypes;
import net.ck.mtbg.util.communication.keyboard.MoveAction;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.weather.WeatherTypes;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.util.ArrayList;


/**
 * /**
 * <tile>
 * <id>12544</id>
 * <type>GRASS</type>
 * <x>111</x>
 * <y>111</y>
 * <east></east>
 * <west>12543</west>
 * <south></south>
 * <north>12432</north>
 * </tile>
 * * <npc>
 * * <id>1</id>
 * * <type>WARRIOR</type>
 * * <x>3</x>
 * * <y>2</y>
 * * <mobasks>
 * * <mobask>
 * * <question>Hello</question>
 * * <answer>Hello, how do you do?</answer>
 * * </mobask>
 * * <mobask>
 * * <question>Name</question>
 * * <answer>My name is orc</answer>
 * * </mobask>
 * * <mobask>
 * * <question>job</question>
 * * <answer>I am an orc warrior!</answer>
 * * </mobask>
 * * <mobask>
 * * <question>bye</question>
 * * <answer>Ort!</answer>
 * * </mobask>
 * * </mobasks>
 * <targetPosition>
 * <x>10</x>
 * <y>3</y>
 * </targetPosition>
 * * </npc>
 */
@Getter
@Setter
@Log4j2
public class MapXMLReader extends DefaultHandler
{

    private ArrayList<MapTile> maptiles;
    private MapTile maptile;

    private NPC np;
    private Map gameMap;
    private boolean tile;
    private boolean npc;
    private StringBuilder data = null;
    private int x;
    private int y;

    private boolean mapPosition;
    private boolean targetPosition;

    private Point mapPos;
    private Point targetPos;

    private GameState gameState;

    private ArrayList<NPC> npcs;
    private boolean exit;
    private Point exitPos;

    private Message message;

    private boolean msg;
    private boolean furniture;
    private FurnitureItem furnitureItem;
    private boolean sched;
    private Schedule schedule;
    private ScheduleActivity scheduleActivity;
    private GameTime startTime;


    @Override
    public void startDocument()
    {
        maptiles = new ArrayList<>();
        gameMap = new Map();
    }

    @Override
    public void endDocument()
    {
        gameMap.setSize(MapUtils.calculateMapSize(getMaptiles()));
        gameMap.setMapTiles(new MapTile[gameMap.getSize().x][gameMap.getSize().y]);
        if (GameConfiguration.debugMapParser == true)
        {
            logger.debug("start: map {} adding maptiles to 2d array", gameMap.getName());
        }
        for (MapTile t : getMaptiles())
        {
            gameMap.mapTiles[t.x][t.y] = t;
        }

        if (GameConfiguration.debugMapParser == true)
        {
            logger.debug("end: map {} adding maptiles to 2d array", gameMap.getName());
        }
        if (getNpcs() != null && !(getNpcs().isEmpty()))
        {
            //this remains, the rest of the NPC code goes out to NPCreader
            for (NPC n : getNpcs())
            {
                if (GameConfiguration.debugMapParser == true)
                {
                    logger.debug(" map {} attach NPC {} instance to map", gameMap.getName(), n.getId());
                }
                NPC npc = NPCFactory.createNPC(n.getId());
                npc.setMapPosition(new Point(n.getMapPosition().x, n.getMapPosition().y));
                npc.setOriginalMapPosition(new Point(n.getMapPosition().x, n.getMapPosition().y));
                if (n.getSchedule() != null)
                {
                    npc.setSchedule(n.getSchedule());
                }

                if (n.getTargetMapPosition() != null)
                {
                    npc.setTargetMapPosition(new Point(n.getTargetMapPosition().x, n.getTargetMapPosition().y));
                    npc.setOriginalTargetMapPosition(new Point(n.getOriginalTargetMapPosition().x, n.getOriginalTargetMapPosition().y));
                    npc.setPatrolling(true);
                }
                gameMap.getLifeForms().add(npc);
                gameMap.mapTiles[npc.getMapPosition().x][npc.getMapPosition().y].setLifeForm(npc);
            }
        }
    }


    /**
     * <tile> <id>4</id> <type>GRASS</type> <x>0</x> <y>1</y> <east>5</east> <west></west> <south></south> <north>1</north> </tile>
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        switch (qName)
        {
            case "furniture":
                break;
            case "visibility":
                break;
            case "map":
                break;
            case "weather":
                break;
            case "weatherrandomness":
                break;
            case "wrapping":
                break;
            case "name":
                break;
            case "parent":
                break;
            case "tiles":
                break;
            case "meta":
                break;
            case "tile":
                maptile = new MapTile();
                if (maptiles == null)
                {
                    maptiles = new ArrayList<>();
                }
                tile = true;
                break;
            case "id":
                break;
            case "type":
                break;
            case "x":
                break;
            case "y":
                break;
            case "minutesperturn":
                break;
            case "synchronweather":
                break;
            case "fixedWeather":
                break;
            case "gamestate":
                break;
            case "npcs":
                if (npcs == null)
                {
                    npcs = new ArrayList<>();
                }
                break;
            case "npc":
                np = new NPC();
                npc = true;
                break;
            case "mapPosition":
                mapPosition = true;
                mapPos = new Point();
                break;
            case "targetPosition":
                targetPosition = true;
                targetPos = new Point();
                break;
            case "mobask":
                break;
            case "question":
                break;
            case "answer":
                break;
            case "exit":
                exit = true;
                exitPos = new Point();
                break;
            case "targetMap":
                break;
            case "targetCoordinates":
                break;
            case "message":
                msg = true;
                message = new Message();
                break;
            case "description":
                break;
            case "repeat":
                break;
            case "inventory":
                break;
            case "weapon":
                break;
            case "armor":
                break;
            case "utility":
                break;
            case "schedule":
                sched = true;
                schedule = new Schedule(np);
                break;
            case "scheduleActivity":
                scheduleActivity = new ScheduleActivity();
                break;
            case "scheduleActivityString":
                break;
            case "startTime":
                startTime = new GameTime();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + qName);
        }
        data = new StringBuilder();
    }

    /**
     * <tile> <id>4</id> <type>GRASS</type> <x>0</x> <y>1</y> <east>5</east> <west></west> <south></south> <north>1</north> </tile>
     */
    @Override
    public void endElement(String uri, String localName, String qName)
    {
        switch (qName)
        {
            case "npcs":
                break;
            case "id":
                if (tile)
                {
                    maptile.setId(Integer.parseInt(data.toString()));
                }
                if (npc)
                {
                    np.setId(Integer.parseInt(data.toString()));

                }
                break;
            case "type":
                String str = data.toString().replaceAll("(\\r|\\n|\\t|\\W)", "");
                if (tile)
                {
                    if (msg)
                    {
                        message.setMessageType(MessageTypes.valueOf(str));
                    }
                    else
                    {
                        maptile.setType(TileTypes.valueOf(str));
                    }
                }

                break;
            case "x":
                if (exit)
                {
                    exitPos.x = Integer.parseInt(data.toString());
                }
                else
                {
                    x = Integer.parseInt(data.toString());
                }
                break;
            case "y":
                if (exit)
                {
                    exitPos.y = Integer.parseInt(data.toString());
                }
                else
                {
                    y = Integer.parseInt(data.toString());
                }
                break;
            case "targetMap":
                maptile.setTargetMap(data.toString());
                break;
            case "targetID":
                maptile.setTargetID(Integer.parseInt(data.toString()));
                break;
            case "tile":
                maptile.setBlocked(false);
                maptile.setX(x);
                maptile.setY(y);
                maptile.setMapPosition(new Point(x, y));
                maptiles.add(maptile);
                tile = false;
                //logger.info("maptile: {}", maptile);
                break;
            case "npc":
                npcs.add(np);
                npc = false;
                break;
            case "weather":
                if (gameMap == null)
                {
                    logger.error("gamemap is null");
                }
                gameMap.setWeatherSystem(Boolean.parseBoolean(data.toString()));
                break;
            case "weatherrandomness":
                gameMap.setWeatherRandomness(Integer.parseInt(data.toString()));
                break;
            case "synchronweather":
                gameMap.setSyncedWeatherSystem(Boolean.parseBoolean(data.toString()));
                break;
            case "fixedWeather":
                //https://stackoverflow.com/questions/604424/how-to-get-an-enum-value-from-a-string-value-in-java
                gameMap.setFixedWeather(WeatherTypes.valueOf(data.toString()));
                break;
            case "wrapping":
                gameMap.setWrapping(Boolean.parseBoolean(data.toString()));
                break;
            case "name":
                gameMap.setName(data.toString());
                break;
            case "parent":
                gameMap.setParentMap(data.toString());
                break;
            case "visibility":
                //logger.info("data: {}", data.toString());
                gameMap.setVisibilityRange(Integer.parseInt(data.toString()));
                break;
            case "tiles":
                break;
            case "meta":
                break;
            case "map":
                break;
            case "minutesperturn":
                gameMap.setMinutesPerTurn(Integer.parseInt(data.toString()));
                break;
            case "gamestate":
                if (GameConfiguration.debugMapParser == true)
                {
                    logger.debug("map {} parsing game state map: {} ", gameMap.getName(), GameState.valueOf(data.toString()));
                }
                gameMap.setGameState(GameState.valueOf(data.toString()));
                break;
            case "exit":
                gameMap.setTargetCoordinates(exitPos);
                maptile.setTargetCoordinates(exitPos);
                exit = false;
            case "targetCoordinates":
                break;
            case "repeat":
                message.setRepeat(Boolean.parseBoolean(data.toString()));
                break;
            case "description":
                message.setDescription(data.toString());
                break;
            case "message":
                maptile.setMessage(message);
                msg = false;
                break;
            case "mapPosition":
                String ma = data.toString();
                String[] mas = ma.split(",");
                np.setMapPosition(new Point(Integer.parseInt(mas[0].replaceAll("\\s+", "")), Integer.parseInt(mas[1].replaceAll("\\s+", ""))));
                np.setOriginalMapPosition(new Point(np.getMapPosition().x, np.getMapPosition().y));
                mapPosition = false;
                break;
            case "furniture":
                furnitureItem = ItemFactory.createFurniture(Integer.parseInt(data.toString()));
                maptile.setFurniture(furnitureItem);
                maptile.setBlocked(true);
                break;
            case "inventory":
                break;
            case "weapon":
                Weapon weapon = ItemFactory.createWeapon(Integer.parseInt(data.toString()));
                maptile.getInventory().add(weapon);
                break;
            case "armor":
                Armor armor = ItemFactory.createArmor(Integer.parseInt(data.toString()));
                maptile.getInventory().add(armor);
            case "startTime":
                String[] strings = data.toString().split(":");
                if (GameConfiguration.debugMapParser == true)
                {
                    logger.debug("strings: {} - {}", strings[0], strings[1]);
                }
                startTime.setCurrentHour(Integer.parseInt(strings[0]));
                startTime.setCurrentMinute(Integer.parseInt(strings[1]));
                scheduleActivity.setStartTime(startTime);
                break;
            case "scheduleActivityString":
                scheduleActivity.setScheduleActivityString(data.toString());
                break;
            case "scheduleActivity":
                schedule.add(scheduleActivity);
                break;
            case "schedule":
                schedule.setActive(true);
                np.setSchedule(schedule);
                sched = false;
                break;
            case "targetPosition":
                ma = data.toString();
                mas = ma.split(",");
                if (sched)
                {
                    if (GameConfiguration.debugMapParser == true)
                    {
                        logger.debug("npc {} - setting schedule", np.getId());
                    }
                    scheduleActivity.setTargetLocation(new Point(Integer.parseInt(mas[0].replaceAll("\\s+", "")), Integer.parseInt(mas[1].replaceAll("\\s+", ""))));
                    MoveAction moveAction = new MoveAction();
                    moveAction.setGetWhere(new Point(Integer.parseInt(mas[0].replaceAll("\\s+", "")), Integer.parseInt(mas[1].replaceAll("\\s+", ""))));
                    scheduleActivity.setAction(moveAction);
                }
                else
                {
                    np.setTargetMapPosition(new Point(Integer.parseInt(mas[0].replaceAll("\\s+", "")), Integer.parseInt(mas[1].replaceAll("\\s+", ""))));
                    np.setOriginalTargetMapPosition(new Point(Integer.parseInt(mas[0].replaceAll("\\s+", "")), Integer.parseInt(mas[1].replaceAll("\\s+", ""))));
                    np.setPatrolling(true);
                    if (GameConfiguration.debugMapParser == true)
                    {
                        logger.debug("npc {} setting targetPosition and patrolling {}", np.getId());
                    }
                }
                targetPosition = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        data.append(new String(ch, start, length));
    }
}

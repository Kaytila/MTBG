package net.ck.mtbg.util.xml;

import net.ck.mtbg.backend.entities.entities.NPC;
import net.ck.mtbg.backend.entities.entities.NPCType;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.graphics.TileTypes;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.map.Message;
import net.ck.mtbg.map.MessageTypes;
import net.ck.mtbg.util.MapUtils;
import net.ck.mtbg.weather.WeatherTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;


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
public class MapXMLReader extends DefaultHandler
{
	private final Logger logger = LogManager.getLogger(getRealClass());

	private ArrayList<MapTile> maptiles;
	private MapTile maptile;

	private NPC np;
	private Map gameMap;
	private boolean tile;
	private boolean npc;
	private StringBuilder data = null;
	private int x;
	private int y;

	private Hashtable<String, String> mobasks;

	private String question;
	private String answer;
    private boolean targetPosition;

	private boolean mapPosition;
	private Point mapPos;

	private Point targetPos;

	private GameState gameState;

	private ArrayList<NPC> npcs;
	private boolean exit;
	private Point exitPos;

	private Message message;

	private boolean msg;

	public ArrayList<NPC> getNpcs()
	{
		return npcs;
	}

	public void setNpcs(ArrayList<NPC> npcs)
	{
		this.npcs = npcs;
	}

	public Map getGameMap()
	{
		return this.gameMap;
	}

	public void setGameMap(Map gameMap)
	{
		this.gameMap = gameMap;
	}

	public ArrayList<MapTile> getMaptiles()
	{
		return this.maptiles;
	}

	public void setMaptiles(ArrayList<MapTile> maptiles)
	{
		this.maptiles = maptiles;
	}

	private Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

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

		logger.debug("start: adding maptiles to 2d array");
		for (MapTile t : getMaptiles())
		{
			gameMap.mapTiles[t.x][t.y] = t;
		}

		if (getNpcs() != null && getNpcs().size() > 0)
		{
			for (NPC n : getNpcs())
			{
				logger.info("initialize properly");
				n.initialize();
				gameMap.getLifeForms().add(n);
				gameMap.mapTiles[n.getMapPosition().x][n.getMapPosition().y].setLifeForm(n);
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
					maptiles = new ArrayList<>();
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
				break;
			case "npc":
				np = new NPC();
				if (npcs == null)
				{
					npcs = new ArrayList<>();
				}
				npc = true;
				break;
			case "mobasks":
				mobasks = new Hashtable<>();
				break;
			case "targetPosition":
				targetPosition = true;
				targetPos = new Point();
                break;
            case "mapPosition":
                mapPosition = true;
                mapPos = new Point();
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
				String str = data.toString().replaceAll("(\\r|\\n|\\t)", "");
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
				if (npc)
				{
					np.setType(NPCType.valueOf(str));
				}
				break;
			case "x":
				if (npc)
                {
                    if (mapPosition)
                    {
                        mapPos.x = Integer.parseInt(data.toString());
                    }
                    if (targetPosition)
                    {
                        targetPos.x = Integer.parseInt(data.toString());
                    }
                }
                else if (exit)
                {
                    exitPos.x = Integer.parseInt(data.toString());
                }
                else
                {
                    x = Integer.parseInt(data.toString());
                }
				break;
			case "y":
				if (npc)
                {
                    if (mapPosition)
                    {
                        mapPos.y = Integer.parseInt(data.toString());
                    }
                    if (targetPosition)
                    {
                        targetPos.y = Integer.parseInt(data.toString());
                    }
                }
                else if (exit)
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
				gameMap.setGameState(GameState.valueOf(data.toString()));
				break;
			case "mapPosition":
				np.setMapPosition(mapPos);
				np.setOriginalMapPosition(new Point(mapPos.x, mapPos.y));
				mapPosition = false;
				break;
			case "targetPosition":
				np.setOriginalTargetMapPosition(targetPos);
				np.setTargetMapPosition(targetPos);
                np.setPatrolling(true);
                logger.info("targetPosition and patrolling set for id: {}", np.getId());
                targetPosition = false;
				break;
            case "question":
                question = (data.toString());
                break;
            case "answer":
                answer = (data.toString());
                break;
            case "mobask":
				mobasks.put(question, answer);
			case "mobasks":
				np.setMobasks(mobasks);
				break;
			case "exit":
				gameMap.setTargetCoordinates(exitPos);
				maptile.setTargetCoordinates(exitPos);
				exit = false;
			case "targetCoordinates":
				break;
			case "repeat":
				message.setRepeat(Boolean.valueOf(data.toString()));
				break;
			case "description":
				message.setDescription(data.toString());
				break;
			case "message":
				maptile.setMessage(message);
				msg = false;
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

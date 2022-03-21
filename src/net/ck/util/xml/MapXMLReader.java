package net.ck.util.xml;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;

import net.ck.game.backend.Game;
import net.ck.game.weather.WeatherTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.ck.game.backend.entities.NPC;
import net.ck.game.backend.entities.NPCTypes;
import net.ck.game.graphics.TileTypes;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;

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

	private ArrayList<NPC> npcs;

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
		npcs = new ArrayList<>();
		maptiles = new ArrayList<>();
		gameMap = new Map();
	}

	@Override
	public void endDocument()
	{
		for (MapTile t : getMaptiles())
		{
			// logger.info("adding tile: {} to map: {}", t, gameMap);
			gameMap.getTiles().add(t);
		}

		for (NPC n : getNpcs())
		{
			// logger.info("adding npc: {} to map: {}", n, gameMap);
			NPC realNPC = new NPC(n.getNumber(), n.getMapPosition());
			realNPC.initialize();
			gameMap.getNpcs().add(realNPC);
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
			case "npcs":
				break;
			case "east":
				break;
			case "north":
				break;
			case "south":
				break;
			case "west":
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
			case "targetMap":
				break;
			case "targetID":
				break;
			case "npc":
				np = new NPC();
				if (npcs == null)
				{
					npcs = new ArrayList<>();
				}
				npc = true;
				break;
			case "minutesperturn":
				break;
			case "synchronweather":
				break;
			case "fixedWeather":
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
			case "east":
				break;
			case "north":
				break;
			case "south":
				break;
			case "west":
				break;
			case "id":
				if (tile)
				{
					maptile.setId(Integer.parseInt(data.toString()));
					tile = false;
				}
				if (npc)
				{
					np.setNumber(Integer.parseInt(data.toString()));
					npc = false;
				}
				break;
			
			case "type":
					maptile.setType(TileTypes.valueOf(data.toString()));
				break;

			case "x":
				x = Integer.parseInt(data.toString());
				break;
			case "y":
				y = Integer.parseInt(data.toString());
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
				//logger.info("maptile: {}", maptile);
				break;
			case "npc":
				npcs.add(np);
				np.setMapPosition(new Point(x, y));
				//logger.info("npc: {}", np);
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
			default:
				throw new IllegalStateException("Unexpected value: " + qName);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		data.append(new String(ch, start, length));
	}
}

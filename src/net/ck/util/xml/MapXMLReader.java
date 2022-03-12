package net.ck.util.xml;

import java.awt.Point;
import java.util.ArrayList;

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
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private ArrayList<MapTile> maptiles;
	private MapTile maptile;
	private NPC np;
	private ArrayList<NPC> npcs;

	public ArrayList<NPC> getNpcs()
	{
		return npcs;
	}

	public void setNpcs(ArrayList<NPC> npcs)
	{
		this.npcs = npcs;
	}

	private Map gameMap;
	public Map getGameMap()
	{
		return this.gameMap;
	}

	public void setGameMap(Map gameMap)
	{
		this.gameMap = gameMap;
	}

	private boolean parent;
	private boolean tile;
	private boolean id;
	private boolean type;
	private boolean x;
	private boolean y;
	private boolean map;
	private boolean weather;
	private boolean weatherrandomness;
	private boolean wrapping;
	private boolean name;
	private boolean targetMap;
	private boolean targetID;
	private boolean npc;
	private boolean visibility;
	private boolean tiles;
	private boolean mobasks;
	private boolean mobask;
	private boolean question;
	private boolean answer;
	private String q;
	private String a;

	private StringBuilder data = null;

	private boolean meta;

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
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	@Override
	public void startDocument() throws SAXException
	{
		npcs = new ArrayList<NPC>();
		maptiles = new ArrayList<MapTile>();
		gameMap = new Map();
	}

	@Override
	public void endDocument() throws SAXException
	{
		for (MapTile t : getMaptiles())
		{
			// logger.info("adding tile: {} to map: {}", t, gameMap);
			gameMap.getTiles().add(t);
		}

		for (NPC n : getNpcs())
		{
			// logger.info("adding npc: {} to map: {}", n, gameMap);
			gameMap.getNpcs().add(n);
		}
	}

	@Override
	/**
	 * <tile> <id>4</id> <type>GRASS</type> <x>0</x> <y>1</y> <east>5</east> <west></west> <south></south> <north>1</north> </tile>
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{

		if (qName.equalsIgnoreCase("visibility"))
		{
			// logger.info("visiblity true");
			visibility = true;
		}

		else if (qName.equalsIgnoreCase("map"))
		{
			map = true;

		}

		else if (qName.equalsIgnoreCase("weather"))
		{
			weather = true;
		}

		else if (qName.equalsIgnoreCase("weatherrandomness"))
		{
			weatherrandomness = true;
		}

		else if (qName.equalsIgnoreCase("wrapping"))
		{
			wrapping = true;
		}

		else if (qName.equalsIgnoreCase("name"))
		{
			name = true;
		}

		else if (qName.equalsIgnoreCase("parent"))
		{
			parent = true;
		}

		else if (qName.equalsIgnoreCase("tiles"))
		{
			tiles = true;
		}

		else if (qName.equalsIgnoreCase("meta"))
		{
			meta = true;
		}

		else if (qName.equalsIgnoreCase("tile"))
		{
			// logger.error("new tile");
			maptile = new MapTile();
			if (maptiles == null)
				maptiles = new ArrayList<MapTile>();
			tile = true;
		}
		else if (qName.equalsIgnoreCase("id"))
		{
			id = true;
		}
		else if (qName.equalsIgnoreCase("type"))
		{
			type = true;
		}
		else if (qName.equalsIgnoreCase("x"))
		{
			x = true;
		}
		else if (qName.equalsIgnoreCase("y"))
		{
			y = true;
		}
		else if (qName.equalsIgnoreCase("targetMap"))
		{
			targetMap = true;
		}
		else if (qName.equalsIgnoreCase("targetID"))
		{
			targetID = true;
		}

		else if (qName.equalsIgnoreCase("npc"))
		{
			np = new NPC();
			if (npcs == null)
			{
				npcs = new ArrayList<NPC>();
			}
			npc = true;
		}
		else if (qName.equalsIgnoreCase("mobasks"))
		{
			mobasks = true;
		}
		else if (qName.equalsIgnoreCase("mobask"))
		{
			mobask = true;
		}
		else if (qName.equalsIgnoreCase("question"))
		{
			question = true;
		}
		else if (qName.equalsIgnoreCase("answer"))
		{
			answer = true;
		}

		/*
		 * else if (qName.equalsIgnoreCase("north")) { north = true; } else if (qName.equalsIgnoreCase("east")) { east = true;
		 * 
		 * } else if (qName.equalsIgnoreCase("south")) { south = true; } else if (qName.equalsIgnoreCase("west")) { west = true; }
		 */
		// create the data container
		data = new StringBuilder();
	}

	/**
	 * <tile> <id>4</id> <type>GRASS</type> <x>0</x> <y>1</y> <east>5</east> <west></west> <south></south> <north>1</north> </tile>
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		//logger.info("uri: {}, localName: {}, qName: {}", uri, localName, qName);
		if (id)
		{
			if (tile)
			{
				maptile.setId(Integer.parseInt(data.toString()));
			}
			if (npc)
			{
				np.setNumber(Integer.parseInt(data.toString()));
			}
			id = false;
		}
		else if (type)
		{
			if (tile)
			{
				maptile.setType(TileTypes.valueOf(data.toString()));
			}
			if (npc)
			{
				np.setType(NPCTypes.valueOf(data.toString()));
			}
			type = false;

		}
		else if (x)
		{
			if (tile)
			{
				maptile.setX(Integer.parseInt(data.toString()));
			}
			if (npc)
			{
				if (np.getMapPosition() == null)
				{
					Point pos = new Point();
					pos.x = Integer.parseInt(data.toString());
					np.setMapPosition(pos);
				}
			}
			x = false;
		}
		else if (y)
		{
			if (tile)
			{
				maptile.setY(Integer.parseInt(data.toString()));
			}
			if (npc)
			{
				if (np.getMapPosition() == null)
				{
					Point pos = new Point();
					pos.y = Integer.parseInt(data.toString());
					np.setMapPosition(pos);
				}
				else
				{
					np.setMapPosition(new Point(np.getMapPosition().x, Integer.parseInt(data.toString())));
				}
			}
			y = false;
		}
		else if (targetMap)
		{
			targetMap = false;
			maptile.setTargetMap(data.toString());
		}
		else if (targetID)
		{
			targetID = false;
			maptile.setTargetID(Integer.parseInt(data.toString()));

		}
		else if (tile)
		{
			tile = false;
			maptile.setBlocked(false);
			maptile.setMapPosition(new Point(maptile.x, maptile.y));
			maptiles.add(maptile);
		}

		else if (npc)
		{
			npc = false;
			np.initialize();
			npcs.add(np);
		}

		else if (weather)
		{
			weather = false;
			if (gameMap == null)
			{
				logger.error("gamemap is null");
			}
			gameMap.setWeatherSystem(Boolean.valueOf(data.toString()));
		}
		else if (weatherrandomness)
		{
			weatherrandomness = false;
			gameMap.setWeatherRandomness(Integer.parseInt(data.toString()));
		}

		else if (wrapping)
		{
			wrapping = false;
			gameMap.setWrapping(Boolean.valueOf(data.toString()));
		}

		else if (name)
		{
			name = false;
			gameMap.setName(data.toString());
		}

		else if (map)
		{
			map = false;
		}

		else if (parent)
		{
			parent = false;
			gameMap.setParentMap(data.toString());
		}

		else if (visibility)
		{
			visibility = false;
			logger.info("data: {}", data.toString());
			// gameMap.setVisibilityRange(Integer.parseInt(data.toString()));
			gameMap.setVisibilityRange(Integer.parseInt(data.toString()));
		}

		else if (tiles)
		{
			tiles = false;
		}

		else if (meta)
		{
			meta = false;
		}

		else if (mobasks)
		{
			mobasks = false;
		}

		else if (mobask)
		{
			logger.info("uri: {}, localName: {}, qName: {}", uri, localName, qName);
			logger.info("q: {}, a: {}", q, a);
			mobask = false;
			if (a != null && q != null)
			{
				np.getMobasks().put(q, a);
			}
		}

		else if (question)
		{
			question = false;
			q = (data.toString());
		}

		else if (answer)
		{
			answer = false;
			a = (data.toString());
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException
	{
		data.append(new String(ch, start, length));
	}
}

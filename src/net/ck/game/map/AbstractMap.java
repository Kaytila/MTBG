package net.ck.game.map;

import java.awt.Point;
import java.util.ArrayList;

import net.ck.game.weather.WeatherTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.entities.AbstractEntity;
import net.ck.game.backend.entities.NPC;
import net.ck.game.items.AbstractItem;
import net.ck.game.weather.Weather;

/**
 * Abstract Map, to define the most basic properties of a map extension parent
 * children
 * 
 * 
 * @author Claus
 *
 */


public class AbstractMap
{ 
	
	@Override
	public String toString()
	{
		return "AbstractMap [name=" + name + ", childMaps=" + childMaps + ", parentMap=" + parentMap + ", size=" + size + ", players=" + players + "]";
	}


	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}
	

	/**
	 * holds the weather on the map. it will either continue running, or it will be frozen
	 * but it will be peristed with the map
	 */
	private Weather currentWeather;
	
	private String name;
	
	/**
	 * hold the children
	 */
	private ArrayList<Map> childMaps;

	/**
	 * the parent map - the game map has no parent
	 */
	private String parentMap;
	
	/**
	 * Point is (x,y), you start top left and move left and then downwards
	 */
	private Point size;

	/**
	 * Tiles are currently just stored in a stupid array list.
	 */
	protected ArrayList<MapTile> tiles;

	
	/**
	 * the list of players who are on the current map
	 */
	private ArrayList<AbstractEntity> players;
	
	/**
	 * npc is close to player but not quite the same :D
	 */
	private ArrayList<NPC> npcs;
	
	/**
	 * the items littering the ground -
	 * corpses, food,
	 * takeable items, the map only knows about the items
	 * the item knows where it is on the map
	 */
	private ArrayList<AbstractItem> items = new ArrayList<AbstractItem>();
		
	/**
	 * defines standard visibility range in tile rings around the player
	 * 1 = 
	 */
	private int visibilityRange;

	/**
	 * how many minutes pass per turn
	 */
	private int minutesPerTurn;

	private WeatherTypes fixedWeather;


	public ArrayList<AbstractItem> getItems()
	{
		return items;
	}

	public void setItems(ArrayList<AbstractItem> items)
	{
		this.items = items;
	}

	public ArrayList<AbstractEntity> getPlayers()
	{
		return this.players;
	}

	public void setPlayers(ArrayList<AbstractEntity> players)
	{
		this.players = players;
	}

	/**
	 * 
	 */
	public AbstractMap()
	{
		
	}

	public ArrayList<Map> getChildMaps()
	{
		return childMaps;
	}

	public String getParentMap()
	{
		return parentMap;
	}

	public Point getSize()
	{
		return size;
	}

	public ArrayList<MapTile> getTiles()
	{
		return tiles;
	}

	public void setChildMaps(ArrayList<Map> childMaps)
	{
		this.childMaps = childMaps;
	}

	public void setParentMap(String parentMap)
	{
		this.parentMap = parentMap;
	}

	public void setSize(Point size)
	{
		this.size = size;
	}

	public void setTiles(ArrayList<MapTile> tiles)
	{
		this.tiles = tiles;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<NPC> getNpcs()
	{
		return npcs;
	}

	public void setNpcs(ArrayList<NPC> arrayList)
	{
		this.npcs = arrayList;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public Weather getCurrentWeather()
	{
		return currentWeather;
	}

	public void setCurrentWeather(Weather currentWeather)
	{
		this.currentWeather = currentWeather;
	}

	public int getVisibilityRange()
	{
		return visibilityRange;
	}

	public void setVisibilityRange(int visibilityRange)
	{
		this.visibilityRange = visibilityRange;
	}

	public int getMinutesPerTurn()
	{
		return minutesPerTurn;
	}

	public void setMinutesPerTurn(int minutesPerTurn)
	{
		this.minutesPerTurn = minutesPerTurn;
	}

	public WeatherTypes getFixedWeather()
	{
		return fixedWeather;
	}

	public void setFixedWeather(WeatherTypes fixedWeather)
	{
		this.fixedWeather = fixedWeather;
	}
}

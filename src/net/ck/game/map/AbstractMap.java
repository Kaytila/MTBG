package net.ck.game.map;

import net.ck.game.backend.entities.Missile;
import net.ck.game.items.AbstractItem;
import net.ck.game.weather.Weather;
import net.ck.game.weather.WeatherTypes;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract Map, to define the most basic properties of a map extension parent
 * children
 *
 * @author Claus
 */


public class AbstractMap implements Serializable
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	@Override
	public String toString()
	{
		return "AbstractMap [name=" + name + ", childMaps=" + childMaps + ", parentMap=" + parentMap + ", size=" + size + "]";
	}


	/**
	 * holds the weather on the map. it will either continue running, or it will be frozen
	 * but it will be peristed with the map
	 */
	private Weather weather;
	
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

	private Point targetCoordinates;

	public MapTile[][] getMapTiles()
	{
		return mapTiles;
	}

	public void setMapTiles(MapTile[][] mapTiles)
	{
		this.mapTiles = mapTiles;
	}

	/**
	 *
	 */
	public MapTile[][] mapTiles;

	/**
	 * the items littering the ground -
	 * corpses, food,
	 * takeable items, the map only knows about the items
	 * the item knows where it is on the map
	 */
	private ArrayList<AbstractItem> items = new ArrayList<>();
		
	/**
	 * defines standard visibility range in tile rings around the player
	 * 1 = 
	 */
	private int visibilityRange;

	/**
	 * how many minutes pass per turn
	 */
	private int minutesPerTurn;

	/**
	 * if the map has a fixed weather, then store it here.
	 */
	private WeatherTypes fixedWeather;

	public ArrayList<AbstractItem> getItems()
	{
		return items;
	}

	public void setItems(ArrayList<AbstractItem> items)
	{
		this.items = items;
	}

	private ArrayList<Missile> missiles;


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


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Weather getWeather()
	{
		return weather;
	}

	public void setWeather(Weather currentWeather)
	{
		this.weather = currentWeather;
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

    public ArrayList<Missile> getMissiles()
    {
        return missiles;
    }

    public void setMissiles(ArrayList<Missile> missiles)
    {
        this.missiles = missiles;
    }

    public Point getTargetCoordinates()
    {
        return targetCoordinates;
    }

    public void setTargetCoordinates(Point targetCoordinates)
    {
        this.targetCoordinates = targetCoordinates;
    }
}

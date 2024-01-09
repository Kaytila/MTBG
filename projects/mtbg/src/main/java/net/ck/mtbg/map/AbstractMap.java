package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.Missile;
import net.ck.mtbg.weather.Weather;
import net.ck.mtbg.weather.WeatherTypes;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract Map, to define the most basic properties of a map extension parent
 * children
 *
 * @author Claus
 */

@Log4j2
@Getter
@Setter
public class AbstractMap implements Serializable
{
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

	/**
	 *
	 */
	public MapTile[][] mapTiles;

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

	private ArrayList<Missile> missiles;


	/**
	 * 
	 */
	public AbstractMap()
	{
		
	}
}

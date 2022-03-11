package net.ck.util;

import java.awt.Point;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.game.map.MapTile;

/**
 * So the UI Lense is the Class which acts as "Lense" over the map to figure out which
 * tiles are actually in the visible area and therefore need to be drawn.
 * Also, this is what calculates which "UI Tiles" are empty tiles and therefore black.
 * Currently, this is used from the paintComponent() Method in the Canvas but actually why?
 * As long as nothing moves, nothing can change here.
 * Something to think about at a later date
 * @author Claus
 *
 */
public class UILense
{

	
	/**
	 * Singleton
	 */
	private static final UILense UILense = new UILense();

	/**
	 * Singleton access - now I can use Lense in a lot of things :D
	 */
	public static UILense getCurrent()
	{
		return UILense;
	}
	
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private ArrayList<Boolean> xCoordinateSystem;
	private ArrayList<Boolean> yCoordinateSystem;
	
	/**
	 * contains the visible map tiles, as the UI Tiles are calculated
	 */
	private ArrayList<MapTile> visibleMapTiles;
	
	public Class<?> getRealClass()
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

	public Logger getLogger()
	{
		return logger;
	}

	/**
	 * initialize the lens and of course stumble over add and set as always
	 */
	public UILense()
	{
		visibleMapTiles = new ArrayList<MapTile>();
		
		xCoordinateSystem = new ArrayList<Boolean>(Game.getCurrent().getNumberOfTiles());
		yCoordinateSystem = new ArrayList<Boolean>(Game.getCurrent().getNumberOfTiles());

		logger.info("initializing lense");
		for (int i = 0; i < Game.getCurrent().getNumberOfTiles(); i++)
		{
			xCoordinateSystem.add(i, false);
			yCoordinateSystem.add(i, false);
		}
		initialize();
	
	}

	/**
	 * something might have changed, reinitialize the Lense to defaults.
	 */
	public void initialize()
	{
		getVisibleMapTiles().clear();
		for (int i = 0; i < Game.getCurrent().getNumberOfTiles(); i++)
		{
			xCoordinateSystem.set(i, false);
			yCoordinateSystem.set(i, false);
		}
	}
	
	/**
	 * add a point hence maptile to both coordinate lists
	 * this says, yes, this tile is in the visible area.
	 * @param p
	 */
	public void add(Point p)
	{
		//logger.info("adding point: {}", p.toString());
		xCoordinateSystem.set(p.x, true);
		yCoordinateSystem.set(p.y, true);
	}

	/**
	 * Calculates the UI Tiles that are not on the map so to speak
	 * @return returns an arraylist of points
	 */
	public ArrayList<Point> identifyEmptyCoordinates()
	{
		ArrayList<Point> emptyTiles = new ArrayList<Point>();

		for (int row = 0; row < Game.getCurrent().getNumberOfTiles(); row++)
		{
			for (int column = 0; column < Game.getCurrent().getNumberOfTiles(); column++)
			{		
				if (xCoordinateSystem.get(column) == false)
				{					
					emptyTiles.add(new Point(column, row));
				}
				
				if (yCoordinateSystem.get(row) == false)
				{					
					emptyTiles.add(new Point(column, row));
				}
			}
		}		
		return emptyTiles;
	}
	
	/**
	 * This is a helper method that just lists all the visible Tiles the lense can currently see
	 * 
	 */
	public void listEntries()
	{
		for (int row = 0; row < Game.getCurrent().getNumberOfTiles(); row++)
		{
			for (int column = 0; column < Game.getCurrent().getNumberOfTiles(); column++)
			{
				logger.info("X: {}, value: {}, Y: {}, value: {}", column, xCoordinateSystem.get(column), row, yCoordinateSystem.get(row));
			}
		}		
	}
	
	
	public  ArrayList<Point> getEntries()
	{
		ArrayList<Point> list = new ArrayList<Point>();
	
		for (int row = 0; row < Game.getCurrent().getNumberOfTiles(); row++)
		{
			for (int column = 0; column < Game.getCurrent().getNumberOfTiles(); column++)
			{
				list.add(new Point(column, row));
			}
		}
		
		return list;
	}
	
	public boolean isPointOnMap(Point p)
	{		
		return Boolean.logicalAnd(xCoordinateSystem.get(p.x), yCoordinateSystem.get(p.y));
	}

	public ArrayList<MapTile> getVisibleMapTiles()
	{
		return visibleMapTiles;
	}

	public void setVisibleMapTiles(ArrayList<MapTile> visibleTiles)
	{
		this.visibleMapTiles = visibleTiles;
	}	
}

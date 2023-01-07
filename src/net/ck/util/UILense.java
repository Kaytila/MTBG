package net.ck.util;

import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.map.MapTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

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
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	
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


	private final ArrayList<Boolean> xCoordinateSystem;
	private final ArrayList<Boolean> yCoordinateSystem;
	
	/**
	 * contains the visible map tiles, as the UI Tiles are calculated
	 */
	private ArrayList<MapTile> visibleMapTiles;


	/**
	 * initialize the lens and of course stumble over add and set as always
	 */
	public UILense()
	{
		logger.info("initializing lense");

		visibleMapTiles = new ArrayList<>();
		
		xCoordinateSystem = new ArrayList<>(GameConfiguration.numberOfTiles);
		yCoordinateSystem = new ArrayList<>(GameConfiguration.numberOfTiles);

		for (int i = 0; i < GameConfiguration.numberOfTiles; i++)
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
		for (int i = 0; i < GameConfiguration.numberOfTiles; i++)
		{
			xCoordinateSystem.set(i, false);
			yCoordinateSystem.set(i, false);
		}
	}
	
	/**
	 * add a point hence maptile to both coordinate lists
	 * this says, yes, this tile is in the visible area.
	 * @param p adding a UI point
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
		ArrayList<Point> emptyTiles = new ArrayList<>();

		for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
		{
			for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
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
		for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
		{
			for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
			{
				logger.info("X: {}, value: {}, Y: {}, value: {}, type: {}", column, xCoordinateSystem.get(column), row, yCoordinateSystem.get(row));
			}
		}		
	}
	
	
	public  ArrayList<Point> getEntries()
	{
		ArrayList<Point> list;
		list = new ArrayList<>();

		for (int row = 0; row < GameConfiguration.numberOfTiles; row++)
		{
			for (int column = 0; column < GameConfiguration.numberOfTiles; column++)
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

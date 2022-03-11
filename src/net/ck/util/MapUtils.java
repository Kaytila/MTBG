package net.ck.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Game;
import net.ck.game.backend.entities.Player;
import net.ck.game.graphics.TileTypes;
import net.ck.game.map.AbstractMap;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;

public class MapUtils
{

	private static final Logger logger = (Logger) LogManager.getLogger(MapUtils.class);

	private static int middle = (int) Math.floor(Game.getCurrent().getNumberOfTiles() / 2);

	public static int getMiddle()
	{
		return middle;
	}

	public static void setMiddle(int middle)
	{
		MapUtils.middle = middle;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public static int getIDOfMapTileEast(MapTile tile)
	{
		return tile.getEast().getId();
	}

	public static int getIDOfMapTileNorth(MapTile tile)
	{
		return tile.getNorth().getId();
	}

	public static int getIDOfMapTileSouth(MapTile tile)
	{
		return tile.getSouth().getId();
	}

	public static int getIDOfMapTileWest(MapTile tile)
	{
		return tile.getWest().getId();
	}

	/**
	 * Helper Method cause I am lazy not actually sure how useful this in a real context is, but for testing purposes
	 * 
	 * @param map
	 *            the map to search
	 * @param ID
	 *            the id
	 * @return the MapTile the id matches
	 */
	public static MapTile getMapTileByID(AbstractMap map, int ID)
	{
		for (MapTile tile : map.getTiles())
		{
			if (tile.getId() == ID)
			{
				return tile;
			}
		}
		return null;
	}

	public static Point calculateMapSize(AbstractMap map)
	{
		int x = 0;
		int y = 0;
		for (MapTile tile : map.getTiles())
		{
			if (tile.getX() > x)
			{
				x = tile.getX();
			}

			if (tile.getY() > y)
			{
				y = tile.getY();
			}
		}

		return new Point(x, y);
	}

	public static void calculateTileDirections(ArrayList<MapTile> list)
	{
		logger.info("start: calculate tile directions");
		// iterate over all tiles
		for (MapTile tile : list)
		{
			int x = tile.getX();
			int y = tile.getY();
			tile.setMapPosition(new Point(x, y));
			// now for each tile try to find whether there is a tile whose x and y coordinates are in the +-1 range
			// do i really want to do this in one go? hmmm
			for (MapTile otherTile : list)
			{
				// check if the other tile is one tile north
				if (otherTile.getY() - y == -1)
				{
					// if X is the same coordinate, then its really north
					if (otherTile.getX() - x == 0)
					{
						tile.setNorth(otherTile);
						otherTile.setSouth(tile);
					}
				}

				// check if the other tile is one tile south
				if (otherTile.getY() - y == 1)
				{
					// if X is the same coordinate, then its really south
					if (otherTile.getX() - x == 0)
					{
						tile.setSouth(otherTile);
						otherTile.setNorth(tile);
					}
				}

				// check if the other tile is one tile east
				if (otherTile.getX() - x == 1)
				{
					// if Y is the same coordinate, then its really east
					if (otherTile.getY() - y == 0)
					{
						tile.setEast(otherTile);
						otherTile.setWest(tile);
					}
				}

				// check if the other tile is one tile west
				if (otherTile.getX() - x == -1)
				{
					// if Y is the same coordinate, then its really west
					if (otherTile.getY() - y == 0)
					{
						tile.setWest(otherTile);
						otherTile.setEast(tile);
					}
				}
			}
		}
		logger.info("end: calculate tile directions");
	}

	/**
	 * creates a map, all of type grassland or ocean in random with a little help from stackoverflow: https://stackoverflow.com/questions/7366266/best-way-to-write-string-to-file-using-java-nio
	 * 
	 * @param x
	 *            size (zero indexed, so size 12 is 11)
	 * @param y
	 *            size (zero indexed, so size 12 is 11)
	 * 
	 */
	public static void createMap(int x, int y, TileTypes type)
	{
		logger.info("begin creating Map with (zero-indexed) x: {} and y: {} and type: {}", x, y, type);
		int id = 1;
		String north = "";
		String south = "";
		String east = "";
		String west = "";

		String fileName = ("maps" + File.separator + "Testmap2.xml");
		String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<map>\r\n" + "\t<meta>\r\n" + "\t\t<weather>true</weather>\r\n" + "\t\t<weatherrandomness>10</weatherrandomness>\r\n"
			+ "\t\t<wrapping>true</wrapping>\r\n" + "\t\t<name>testname</name>\r\n" + "\t\t<parent></parent>\r\n" + "\t</meta>\r\n" + "\t<tiles>\n";

		for (int j = 0; j <= y; j++)
		{
			for (int i = 0; i <= x; i++)
			{
				id = (i + 1) + ((y + 1) * j);

				if (id % 2 == 0)
				{
					type = TileTypes.GRASS;
				}
				else
				{
					type = TileTypes.OCEAN;
				}

				north = String.valueOf(id - (x + 1));
				south = String.valueOf(id + (x + 1));
				east = String.valueOf(id + 1);
				west = String.valueOf(id - 1);

				if (j == 0)
				{
					north = "";
				}

				if (i == 0)
				{
					west = "";
				}

				if (i == x)
				{
					east = "";
				}

				if (j == y)
				{
					south = "";
				}

				contents = contents + "\t\t<tile>\r\n" + "\t\t\t<id>" + id + "</id>\r\n" + "\t\t\t<type>" + type + "</type>\r\n" + "\t\t\t<x>" + i + "</x>\r\n" + "\t\t\t<y>" + j + "</y>\r\n"
					+ "\t\t\t<east>" + east + "</east>\r\n" + "\t\t\t<west>" + west + "</west>\r\n" + "\t\t\t<south>" + south + "</south>\r\n" + "\t\t\t<north>" + north + "</north>\r\n"
					+ "\t\t</tile>\r\n";
			}
		}

		contents = contents + "	</tiles>\r\n" + "</map>";

		try
		{
			Files.writeString(Paths.get(fileName), contents, StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		logger.info("finished writing map");
	}

	/**
	 * 
	 * @param Point
	 *            p - position of the tile or entity
	 * 
	 */
	public static Point calculateUIPositionFromMapOffset(Point p)
	{
		Point offSet = MapUtils.calculateMapOffsetFromPlayerMapPosition(p);

		return new Point(Game.getCurrent().getCurrentPlayer().getUIPosition().x + offSet.x, Game.getCurrent().getCurrentPlayer().getUIPosition().y + offSet.y);
	}

	public static MapTile calculateMapTileUnderCursor(Point mousePosition)
	{
		int x = Math.floorDiv(mousePosition.x, Game.getCurrent().getTileSize());
		int y = Math.floorDiv(mousePosition.y, Game.getCurrent().getTileSize());
		Point offSet = calculateUIOffsetFromMapPoint();
		return getTileByCoordinates(new Point(x - offSet.x, y - offSet.y));
	}

	/*
	 * public static MapTile calculateMapTileUnderCursorOld(Point mousePosition) { int x = Math.floorDiv(mousePosition.x, Game.getCurrent().getTileSize()); int y = Math.floorDiv(mousePosition.y,
	 * Game.getCurrent().getTileSize()); Point uiTile = new Point(x,y); //logger.info("on map or not: {} ", UILense.getCurrent().isPointOnMap(uiTile)); Point mapPos =
	 * Game.getCurrent().getCurrentPlayer().getMapPosition(); Point uiPos = Game.getCurrent().getCurrentPlayer().getUIPosition(); Point offSet = calculateUIOffsetFromMapPoint(uiPos, mapPos);
	 * 
	 * logger.info("ui tile: {}", uiTile); logger.info("map position: {}", mapPos); logger.info("offset: {}", calculateUIOffsetFromMapPoint(mapPos, uiTile)); logger.info("player offset: {}",
	 * calculateUIOffsetFromMapPoint(uiPos, mapPos)); //Point offSet = MapUtils.calculateMapOffsetFromPlayer(new Point(x, y)); //logger.info("offset: {}", offSet);
	 * 
	 * return getTileByCoordinates(new Point(uiTile.x - offSet.x, uiTile.y - offSet.y)); }
	 */

	public static Rectangle calculateVisibleRectangle(Player p)
	{
		Point playerPosition = p.getMapPosition();

		return new Rectangle(playerPosition.x - MapUtils.getMiddle(), playerPosition.y - MapUtils.getMiddle(), MapUtils.getMiddle() + MapUtils.getMiddle(),
			MapUtils.getMiddle() + MapUtils.getMiddle());
	}

	public static ArrayList<MapTile> calculateVisibleTiles(Rectangle visibleRect, Map map)
	{
		ArrayList<MapTile> visibleTiles = new ArrayList<MapTile>();

		Range<Integer> rangeX = Range.between(visibleRect.x, visibleRect.x + (int) visibleRect.getWidth());
		Range<Integer> rangeY = Range.between(visibleRect.y, visibleRect.y + (int) visibleRect.getHeight());

		for (MapTile t : map.getTiles())
		{

			if ((rangeY.contains(t.getY()) && (rangeX.contains(t.getX()))))
			{
				visibleTiles.add(t);
			}
		}

		return visibleTiles;
	}

	/**
	 * there is a bug, but where????
	 * 
	 * @param position
	 * @return
	 */
	public static Point calculateOffsetFromPlayerBug(Point position)
	{
		Point pP = Game.getCurrent().getCurrentPlayer().getMapPosition();
		// logger.info("maptile x: {}, y: {}", tile.getX(), tile.getY());
		// logger.info("player position: {} {}", pP.x, pP.y);
		int x = 0;
		int y = 0;
		// the tile is to the left
		if (position.x < pP.x)
		{
			// logger.info("to the left");
			x = Math.abs(pP.x - position.x - middle);
		}
		// the tile is to the right
		else if (position.x > pP.x)
		{
			// logger.info("to the right");
			x = Math.abs(position.x - pP.x + middle);
		}
		else
		{
			// logger.info("same X");
			x = middle;
		}
		// the tile is to the top
		if (position.y < pP.y)
		{
			// logger.info("to the top");
			y = Math.abs(pP.y - position.y - middle);
		}
		// the tile is to the bottom
		else if (position.y > pP.y)
		{
			// logger.info("to the bottom");
			y = Math.abs(position.y - pP.y + middle);
		}
		else
		{
			// logger.info("same Y");
			y = middle;
		}

		return new Point(x, y);
	}

	/**
	 * 
	 * @param position
	 * @return Point with the offset as point from the player position. In case of player, this is 0,0 of course.
	 */
	public static Point calculateMapOffsetFromPlayerMapPosition(Point position)
	{
		Point pP = Game.getCurrent().getCurrentPlayer().getMapPosition();

		int xDiff = position.x - pP.x;
		int yDiff = position.y - pP.y;

		return new Point(xDiff, yDiff);
	}

	/**
	 * 
	 * @param position
	 * @return Point with the offset as point from the player position. In case of player, this is 0,0 of course.
	 */
	public static Point calculateUIOffsetFromMapPoint()
	{
		Point mapPos = Game.getCurrent().getCurrentPlayer().getMapPosition();
		Point uiPos = Game.getCurrent().getCurrentPlayer().getUIPosition();
		int xDiff = uiPos.x - mapPos.x;
		int yDiff = uiPos.y - mapPos.y;

		return new Point(xDiff, yDiff);
	}

	public static boolean lookAhead(int x, int y)
	{
		for (MapTile t : Game.getCurrent().getCurrentMap().getTiles())
		{
			// same tile
			if ((t.getX() == x) && (t.getY() == y))
			{
				// logger.info("found tile: {}, blocked: {}", t.toString(), t.isBlocked());
				return t.isBlocked();
			}
		}
		// return false;
		return null != null;
	}

	public static MapTile getTileByCoordinates(Point p)
	{
		for (MapTile t : Game.getCurrent().getCurrentMap().getTiles())
		{
			// same tile
			if ((t.x == p.x) && (t.y == p.y))
			{
				return t;
			}
		}
		return null;
	}

	public static void listMaps()
	{
		for (Map ma : Game.getCurrent().getMaps())
		{
			logger.info("map: {}", ma);
		}
	}

	/**
	 * Bresehhams Algorithm
	 * @param start a point, probably player position
	 * @param target an end point
	 * @return
	 */
	public static ArrayList<Point> getLine(Point start, Point target)
	{
		ArrayList<Point> ret = new ArrayList<Point>();
		int x0 = start.x;
		int y0 = start.y;

		int x1 = target.x;
		int y1 = target.y;

		int sx = 0;
		int sy = 0;

		int dx = Math.abs(x1 - x0);
		sx = x0 < x1 ? 1 : -1;
		int dy = -1 * Math.abs(y1 - y0);
		sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2; /* error value e_xy */

		for (;;)
		{ /* loop */
			ret.add(new Point(x0, y0));
			if (x0 == x1 && y0 == y1)
				break;
			e2 = 2 * err;
			if (e2 >= dy)
			{
				err += dy;
				x0 += sx;
			} /* e_xy+e_x > 0 */
			if (e2 <= dx)
			{
				err += dx;
				y0 += sy;
			} /* e_xy+e_y < 0 */
		}

		return ret;
	}

}

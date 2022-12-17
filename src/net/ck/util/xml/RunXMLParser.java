package net.ck.util.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.ck.game.backend.entities.NPC;
import net.ck.game.items.FurnitureItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.game.Game;
import net.ck.game.items.Armor;
import net.ck.game.items.Utility;
import net.ck.game.items.Weapon;
import net.ck.game.map.Map;
import net.ck.game.map.MapTile;

public class RunXMLParser
{
	private static final Logger logger = (Logger) LogManager.getLogger(RunXMLParser.class);
	private static File xmlMap;

	public RunXMLParser()
	{

	}

	public static void main(String[] args)
	{
		{
			xmlMap = new File("maps/TestMap1.xml");

			logger.info(xmlMap.toString());

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			try
			{
				SAXParser saxParser = saxParserFactory.newSAXParser();
				MapXMLReader handler = new MapXMLReader();
				saxParser.parse(xmlMap, handler);
				ArrayList<MapTile> maptiles = handler.getMaptiles();
				//MapUtils.calculateTileDirections(maptiles);
				for (MapTile ti : maptiles)
				{
					logger.error(ti.toString());
				}
			}
			catch (Exception e)
			{
				logger.error("hmmm");
				e.printStackTrace();
			}
		}
	}

	public static Map parseMap(String fileName)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			MapXMLReader handler = new MapXMLReader();
			saxParser.parse(new File(fileName), handler);
			
			//ArrayList<MapTile> maptiles = handler.getMaptiles();
			//MapUtils.calculateTileDirections(maptiles);
			//for (MapTile ti : maptiles)
			//{
				//logger.info(ti.toString());
			//}
			return handler.getGameMap();
		}
		catch (Exception e)
		{
			logger.error("hmmm");
			e.printStackTrace();
			Game.getCurrent().stopGame();
		}
		return null;

	}

	
	public static Hashtable<Integer, Armor> parseArmor(String fileName)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			ArmorXMLReader handler = new ArmorXMLReader();
			saxParser.parse(new File(fileName), handler);
			
			return handler.getArmorList();
		}
		catch (Exception e)
		{
			logger.error("hmmm");
			e.printStackTrace();
			Game.getCurrent().stopGame();
		}
		return null;

	}

	
	public static Hashtable<Integer,Weapon> parseWeapons(String fileName)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			WeaponXMLReader handler = new WeaponXMLReader();
			saxParser.parse(new File(fileName), handler);
			
			return handler.getWeaponList();
		}
		catch (Exception e)
		{
			logger.error("hmmm");
			e.printStackTrace();
			Game.getCurrent().stopGame();
		}
		return null;

	}

	public static Hashtable<Integer,Utility> parseUtilities(String fileName)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			UtilityXMLReader handler = new UtilityXMLReader();
			saxParser.parse(new File(fileName), handler);
			
			return handler.getUtilityList();
		}
		catch (Exception e)
		{
			logger.error("hmmm");
			e.printStackTrace();
			Game.getCurrent().stopGame();
		}
		return null;

	}

	public static Hashtable<Integer, NPC> parseNPCs(String fileName)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			NPCReader handler = new NPCReader();
			saxParser.parse(new File(fileName), handler);

			return handler.getNpcs();
		}
		catch (Exception e)
		{
			logger.error("hmmm");
			e.printStackTrace();
			Game.getCurrent().stopGame();
		}
		return null;
	}

	public static Hashtable<Integer, FurnitureItem> parseFurniture(String fileName)
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try
		{
			SAXParser saxParser = saxParserFactory.newSAXParser();
			FurnitureReader handler = new FurnitureReader();
			saxParser.parse(new File(fileName), handler);
			logger.info("fu: {}", handler.getFurnitureList());
			return handler.getFurnitureList();
		}
		catch (Exception e)
		{
			logger.error("hmmm");
			e.printStackTrace();
			Game.getCurrent().stopGame();
		}
		return null;
	}
}

package net.ck.mtbg.util.xml;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.skills.AbstractSkill;
import net.ck.mtbg.backend.entities.skills.AbstractSpell;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.items.Armor;
import net.ck.mtbg.items.FurnitureItem;
import net.ck.mtbg.items.Utility;
import net.ck.mtbg.items.Weapon;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.map.MapTile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

@Log4j2
@Getter
@Setter
public class RunXMLParser
{
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
            return handler.getGameMap();
        }
        catch (Exception e)
        {
            logger.error("error during parsing maps");
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
            logger.error("error during parsing armor");
            e.printStackTrace();
            Game.getCurrent().stopGame();
        }
        return null;

    }


    public static Hashtable<Integer, Weapon> parseWeapons(String fileName)
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
            logger.error("error during parsing weapons");
            e.printStackTrace();
            Game.getCurrent().stopGame();
        }
        return null;

    }

    public static Hashtable<Integer, Utility> parseUtilities(String fileName)
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
            logger.error("error during parsing utilities");
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
            FurnitureXMLReader handler = new FurnitureXMLReader();
            saxParser.parse(new File(fileName), handler);
            return handler.getFurnitureList();
        }
        catch (Exception e)
        {
            logger.error("error during parsing furniture");
            e.printStackTrace();
            Game.getCurrent().stopGame();
        }
        return null;
    }


    public static Hashtable<Integer, AbstractSpell> parseSpells(String fileName)
    {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SpellXMLReader handler = new SpellXMLReader();
            saxParser.parse(new File(fileName), handler);

            return handler.getSpellList();
        }
        catch (Exception e)
        {
            logger.error("issue during parsing spells");
            e.printStackTrace();
            Game.getCurrent().stopGame();
        }
        return null;
    }


    public static Hashtable<Integer, AbstractSkill> parseSkills(String fileName)
    {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SkillXMLReader handler = new SkillXMLReader();
            saxParser.parse(new File(fileName), handler);
            return handler.getSkillList();
        }
        catch (Exception e)
        {
            logger.error("issue during parsing spells");
            e.printStackTrace();
            Game.getCurrent().stopGame();
        }
        return null;
    }

}

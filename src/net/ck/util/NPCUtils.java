package net.ck.util;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.configuration.GameConfiguration;
import net.ck.game.backend.entities.AttributeTypes;
import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.game.Game;
import net.ck.game.items.AbstractItem;
import net.ck.game.items.Armor;
import net.ck.game.items.ArmorPositions;
import net.ck.game.map.MapTile;
import net.ck.util.astar.AStar;
import net.ck.util.communication.keyboard.*;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class NPCUtils
{

    private static final Logger logger = LogManager.getLogger(NPCUtils.class);





    public static boolean calculateHit(LifeForm attacker, LifeForm defender)
    {
        int baseHitChance = 50;
        int attackDex = attacker.getAttributes().get(AttributeTypes.DEXTERITY).getValue();
        int defendDex = defender.getAttributes().get(AttributeTypes.DEXTERITY).getValue();

        logger.info("attack dex: {}", attackDex);
        logger.info("defendDex dex: {}", defendDex);

        int diff = attackDex - defendDex;

        if (diff > 0)
        {
            //more nimble attacker
            baseHitChance = baseHitChance + (diff * 4);
        }
        else if (diff == 0)
        {
            //equal
        }
        else
        {
            //defender more nimble
            baseHitChance = baseHitChance - (diff * 4);
        }

        Random rand = new Random();
        int random = rand.nextInt(100);
        logger.info("random: {}", random);
        logger.info("hit chance: {}", baseHitChance);
        if (baseHitChance > random)
        {
            return true;
        }

        return false;
    }

    /**
     * damage calculation:
     * take strength (direct value) + random weapon damage (high - low) + low)
     * in club 1d4 => rand(4 - 1) + 1. This should be correct. I hope.
     * - AC
     * equals damage. this makes strength too important, but it is good enough for now.
     */
    public static int calculcateDamage(LifeForm attacker, LifeForm defender)
    {
        int attackStr = attacker.getAttributes().get(AttributeTypes.STRENGTH).getValue();
        Range<Integer> attackWeapon = attacker.getWeapon().getWeaponDamage();
        int low = attackWeapon.getMinimum();
        int high = attackWeapon.getMaximum();
        Random rand = new Random();

        int defendAC = defender.getArmorClass();

        return (attackStr + (rand.nextInt(high - low ) + low)) - defendAC;
    }

    public static Armor calculateArmorToWear(ArmorPositions armorPosition)
    {

        for (AbstractItem item : Game.getCurrent().getCurrentPlayer().getInventory().getInventory())
        {
            if (item instanceof Armor)
            {
                if (((Armor) item).getPosition().equals(armorPosition))
                {
                    return (Armor) item;
                }
            }
        }
        return null;
    }



    /**
     *  Helper method, dumb calculation - move towards the player for attacking in melee
     *  need to write the opposite for fleeing
     */
    public static PlayerAction calculateVictimDirection(LifeForm n)
    {
        Point sourcePoint = n.getMapPosition();
        Point targetPoint = n.getVictim().getMapPosition();
        logger.info("source Point: {}", sourcePoint);
        logger.info("target Point: {}", targetPoint);
        // NPC is more to the east
        if (sourcePoint.x > targetPoint.x)
        {
            logger.info("NPC is more to the east, move west.");
            if (!(MapUtils.lookAhead(sourcePoint.x - 1, sourcePoint.y)))
            {
                logger.info("tile to the west is free, move");
                return new PlayerAction(new WestAction());
            }
        }

        // NPC is more to the west, move east
        if (sourcePoint.x < targetPoint.x)
        {
            logger.info("NPC is more to the west, move east");
            if (!(MapUtils.lookAhead(sourcePoint.x + 1 , sourcePoint.y)))
            {
                logger.info("Tile to the east is free, move east");
                return new PlayerAction(new EastAction());
            }
        }

        // NPC is more to the south, move north
        if (sourcePoint.y > targetPoint.y)
        {
            logger.info("npc is more to the south, move north");
            if (!(MapUtils.lookAhead(sourcePoint.x, sourcePoint.y - 1)))
            {
                logger.info("tile to the north is free, move north");
                return new PlayerAction(new NorthAction());
            }
        }

        // NPC is more to the north, move south
        if (sourcePoint.y < targetPoint.y)
        {
            logger.info("npc is more to the north, move south");
            if (!(MapUtils.lookAhead(sourcePoint.x, sourcePoint.y + 1)))
            {
                logger.info("tile to the south is free, move south");
                return new PlayerAction(new SouthAction());
            }
        }
        return new PlayerAction(new SpaceAction());
    }

    public static PlayerAction calculateVictimDirectionAStar(LifeForm n)
    {
        //TODO
        Point sourcePoint = n.getMapPosition();
        Point targetPoint = n.getVictim().getMapPosition();

        logger.info("source: {}", sourcePoint);
        logger.info("target: {}", targetPoint);

        AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getMapTileByCoordinatesAsPoint(sourcePoint), MapUtils.getMapTileByCoordinatesAsPoint(targetPoint), Game.getCurrent().getCurrentMap());
        ArrayList<MapTile> path = (ArrayList<MapTile>) AStar.findPath();

        for (MapTile node : path)
        {
            logger.info(node);
            if (node.getMapPosition().equals(n.getMapPosition()))
            {
                logger.info("start node");
            }
            else
            {
                //logger.info(node);
                if (node.x > n.getMapPosition().x)
                {
                    return (new PlayerAction(new EastAction()));
                }

                else if (node.x < n.getMapPosition().x)
                {
                    return (new PlayerAction(new WestAction()));
                }

                else if (node.y > n.getMapPosition().y)
                {
                    return (new PlayerAction(new SouthAction()));
                }

                else if (node.y < n.getMapPosition().y)
                {
                    return (new PlayerAction(new NorthAction()));
                }
            }
        }
        return  new PlayerAction(new SpaceAction());
    }


    public static AbstractKeyboardAction calculateCoordinatesFromActionAndTile(AbstractKeyboardAction action, MapTile tile, LifeForm form)
    {
        Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(tile.getMapPosition());
        action.setTargetCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
        logger.info("taget coordinates: {}", action.getTargetCoordinates());

        //source
        screenPosition = MapUtils.calculateUIPositionFromMapOffset(Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(form.getMapPosition())).getMapPosition());
        action.setSourceCoordinates(new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
        logger.info("source coordinates: {}", action.getSourceCoordinates());
        return action;
    }

    public static Point calculateUIPointFromMapPosition(Point position)
    {
        Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(position);
        return (new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
    }

    public static Point calculatePlayerPosition()
    {
        int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 2);
        int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 2);
        return new Point(Px, Py);
    }
}

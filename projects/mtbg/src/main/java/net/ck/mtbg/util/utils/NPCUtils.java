package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.NPCAction;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.attributes.AttributeTypes;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.entities.entities.LifeFormState;
import net.ck.mtbg.items.AbstractItem;
import net.ck.mtbg.items.Armor;
import net.ck.mtbg.items.ArmorPositions;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.astar.AStar;
import net.ck.mtbg.util.communication.keyboard.*;
import org.apache.commons.lang3.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
@Log4j2
public class NPCUtils
{
    public static boolean calculateHit(LifeForm attacker, LifeForm defender)
    {
        int baseHitChance = 50;
        int attackDex = attacker.getAttributes().get(AttributeTypes.DEXTERITY).getValue();
        int defendDex = defender.getAttributes().get(AttributeTypes.DEXTERITY).getValue();
        if (GameConfiguration.debugNPC == true)
        {
            logger.debug("attack dex: {}", attackDex);
            logger.debug("defendDex dex: {}", defendDex);
        }
        int diff = attackDex - defendDex;

        if (diff > 0)
        {
            //more nimble attacker
            baseHitChance = baseHitChance + (diff * 4);
        }
        else if (diff == 0)
        {
            //equal - favor attack slightly
            baseHitChance = baseHitChance + 5;
        }
        else
        {
            //defender more nimble
            baseHitChance = baseHitChance - (diff * 4);
        }

        Random rand = new Random();
        int random = rand.nextInt(100);
        if (GameConfiguration.debugNPC == true)
        {
            logger.debug("random: {}", random);
            logger.debug("hit chance: {}", baseHitChance);
        }
        return baseHitChance > random;
    }

    /**
     * damage calculation:
     * take strength (direct value) + random weapon damage ((high - low) + low)
     * in club 1d4 => rand(4 - 1) + 1. This should be correct. I hope.
     * - AC
     * equals damage. this makes strength too important, but it is good enough for now.
     */
    public static int calculateDamage(LifeForm attacker, LifeForm defender)
    {
        int attackStr = attacker.getAttributes().get(AttributeTypes.STRENGTH).getValue();
        Range<Integer> attackWeapon = attacker.getWeapon().getWeaponDamage();
        int low = attackWeapon.getMinimum();
        int high = attackWeapon.getMaximum();
        Random rand = new Random();

        int defendAC = defender.getArmorClass();

        return (attackStr + (rand.nextInt(high - low) + low)) - defendAC;
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
     * Helper method, dumb calculation - move towards the player for attacking in melee
     * need to write the opposite for fleeing
     */
    public static NPCAction calculateVictimDirection(LifeForm n)
    {
        Point sourcePoint = n.getMapPosition();
        Point targetPoint = n.getVictim().getMapPosition();
        if (GameConfiguration.debugNPC == true)
        {
            logger.info("source Point: {}", sourcePoint);
            logger.info("target Point: {}", targetPoint);
        }
        // NPC is more to the east
        if (sourcePoint.x > targetPoint.x)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.info("NPC is more to the east, move west.");
            }
            if (!(MapUtils.lookAhead(sourcePoint.x - 1, sourcePoint.y)))
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("tile to the west is free, move");
                }
                return new NPCAction(new WestAction());
            }
        }

        // NPC is more to the west, move east
        if (sourcePoint.x < targetPoint.x)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.info("NPC is more to the west, move east");
            }
            if (!(MapUtils.lookAhead(sourcePoint.x + 1, sourcePoint.y)))
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("Tile to the east is free, move east");
                }
                return new NPCAction(new EastAction());
            }
        }

        // NPC is more to the south, move north
        if (sourcePoint.y > targetPoint.y)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.info("npc is more to the south, move north");
            }
            if (!(MapUtils.lookAhead(sourcePoint.x, sourcePoint.y - 1)))
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("tile to the north is free, move north");
                }
                return new NPCAction(new NorthAction());
            }
        }

        // NPC is more to the north, move south
        if (sourcePoint.y < targetPoint.y)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.info("npc is more to the north, move south");
            }
            if (!(MapUtils.lookAhead(sourcePoint.x, sourcePoint.y + 1)))
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.info("tile to the south is free, move south");
                }
                return new NPCAction(new SouthAction());
            }
        }
        return new NPCAction(new SpaceAction());
    }

    public static NPCAction calculateVictimDirectionAStar(LifeForm n)
    {
        //TODO
        Point sourcePoint = n.getMapPosition();
        Point targetPoint = n.getVictim().getMapPosition();
        if (GameConfiguration.debugNPC == true)
        {
            logger.debug("source: {}", sourcePoint);
            logger.debug("target: {}", targetPoint);
        }
        AStar.initialize(Game.getCurrent().getCurrentMap().getSize().y, Game.getCurrent().getCurrentMap().getSize().x, MapUtils.getMapTileByCoordinatesAsPoint(sourcePoint), MapUtils.getMapTileByCoordinatesAsPoint(targetPoint), Game.getCurrent().getCurrentMap());
        ArrayList<MapTile> path = (ArrayList<MapTile>) AStar.findPath();

        for (MapTile node : path)
        {
            logger.info(node);
            if (node.getMapPosition().equals(n.getMapPosition()))
            {
                logger.debug("start node");
            }
            else
            {
                //logger.info(node);
                if (node.x > n.getMapPosition().x)
                {
                    return (new NPCAction(new EastAction()));
                }

                else if (node.x < n.getMapPosition().x)
                {
                    return (new NPCAction(new WestAction()));
                }

                else if (node.y > n.getMapPosition().y)
                {
                    return (new NPCAction(new SouthAction()));
                }

                else if (node.y < n.getMapPosition().y)
                {
                    return (new NPCAction(new NorthAction()));
                }
            }
        }
        return new NPCAction(new SpaceAction());
    }

    /**
     * calculate the on-screen pixel position of the provided Map Point.
     *
     * @param mapPoint - the map point
     * @return a point with screen position (NOT UI Position)
     */
    public static Point calculateScreenPositionFromMapPosition(Point mapPoint)
    {
        Point screenPosition = MapUtils.calculateUIPositionFromMapOffset(mapPoint);
        return (new Point(screenPosition.x * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2), screenPosition.y * GameConfiguration.tileSize + (GameConfiguration.tileSize / 2)));
    }

    public static Point calculatePlayerPosition()
    {
        int Px = (Game.getCurrent().getCurrentPlayer().getUIPosition().x * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 2);
        int Py = (Game.getCurrent().getCurrentPlayer().getUIPosition().y * GameConfiguration.tileSize) + (GameConfiguration.tileSize / 2);
        return new Point(Px, Py);
    }

    /**
     * checks if the life form is not dead or unconscious or asleep
     *
     * @param e life form
     * @return true if is active, false if not
     */
    public static boolean isActive(LifeForm e)
    {
        if ((e.getState().equals(LifeFormState.DEAD)) || (e.getState().equals(LifeFormState.UNCONSCIOUS)) || (e.getState().equals(LifeFormState.ASLEEP)))
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.info("NPC {} is {}", e.getId(), e.getState());
            }
            return false;
        }
        return true;
    }
}

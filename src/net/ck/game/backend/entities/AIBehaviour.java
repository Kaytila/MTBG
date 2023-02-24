package net.ck.game.backend.entities;

import net.ck.game.backend.actions.NPCAction;
import net.ck.game.backend.game.Game;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.keyboard.*;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Random;

public class AIBehaviour
{

    private static final Logger logger = LogManager.getLogger(AIBehaviour.class);


    public static void determineCombat(LifeForm e)
    {
        logger.info(" {} trying to attack", e);
        //attack with melee
        if (MapUtils.isAdjacent(e.getMapPosition(), e.getVictim().getMapPosition()))
        {
            if (e.isRanged())
            {
                e.switchWeapon(WeaponTypes.MELEE);
            }
            else
            {
                logger.info("attacking");
                AttackAction action = new AttackAction();
                action.setGetWhere(e.getVictim().getMapPosition());
                e.doAction(new NPCAction(action));
            }
        }
        //victim is not adjacent
        else
        {
            logger.info("npc {} is out of melee range, what to do", e.getId());
            //Weapon sling = getWeaponList().get(3);
            //e.getItem(sling);
            //npc has ranged weapon wielded or has one in inventory
            if (e.isRanged())
            {
                logger.info("NPC {} has ranged capabilities", e.getId());
                //wielded attack
                if (e.getWeapon() != null)
                {
                    if (e.getWeapon().getType().equals(WeaponTypes.RANGED))
                    {
                        logger.info("npc already wields ranged weapon, attack!");
                        AttackAction ac = new AttackAction();
                        ac.setGetWhere(e.getVictim().getMapPosition());
                        NPCAction action = new NPCAction(ac);
                        e.doAction(action);
                        //return;
                    }
                    //in inventory, wield
                    else
                    {
                        logger.info("ranged weapon in inventory");
                        e.switchWeapon(WeaponTypes.RANGED);
                        //return;
                    }
                }
                else
                {
                    logger.info("npc {} weapon is null", e.getId());
                    Weapon club = Game.getCurrent().getWeaponList().get(1);
                    e.wieldWeapon(club);
                }
            }
            else
            {
                logger.info("out of range move towards victim");
                //logger.info("what do do: {}", (NPCUtils.calculateVictimDirectionAStar(e)));
                e.doAction((NPCUtils.calculateVictimDirection(e)));
                //e.moveTo(MapUtils.getTileByCoordinates(e.getVictim().getMapPosition()));
                //return;
            }
        }
    }

    public static void determineRandom(LifeForm e)
    {
        GetAction action = e.lookAroundForItems();
        if (action != null)
        {
            logger.info("trying to get");
            NPCAction ac = new NPCAction(action);
            e.doAction(ac);
        }
        else
        {
            e.doAction(calculateAction(e));
        }
    }

    public static void determinePatrol(LifeForm e)
    {
        /*if (e.getQueuedActions().size() > 0)
        {
            //logger.info("action in queue: {}", npc.getQueuedActions().peek());
            e.doAction(new PlayerAction((AbstractKeyboardAction) e.getQueuedActions().poll()));
        }
        else
        {*/
        //we have not reached the target
        if (!(e.getMapPosition().equals(e.getTargetMapPosition())))
        {
            MoveAction action = new MoveAction();
            action.setGetWhere(new Point(e.getTargetMapPosition().x, e.getTargetMapPosition().y));
            //logger.info("move towards target map position: {},{}", e.getTargetMapPosition().x, e.getTargetMapPosition().y);
            e.doAction(new NPCAction(action));
            //e.moveTo(Game.getCurrent().getCurrentMap().mapTiles[e.getTargetMapPosition().x][e.getTargetMapPosition().y]);
        }
        //we have reached the target
        else
        {
            //we have reached the original position, switch to original target
            if (e.getMapPosition().equals(e.getOriginalMapPosition()))
            {
                e.setTargetMapPosition(e.getOriginalTargetMapPosition());
            }
            //we have reached the original target, switch to original position
            else
            {
                e.setTargetMapPosition(e.getOriginalMapPosition());
            }
        }
        //}
    }

    public static void determineMove(LifeForm e)
    {
        if (!(e.getMapPosition().equals(e.getRunningAction().getGetWhere())))
        {
            //logger.info("npc {} move towards target map position: {}", e.getId(), e.getRunningAction().getGetWhere());
            e.doAction(new NPCAction(e.getRunningAction()));
            //e.moveTo(Game.getCurrent().getCurrentMap().mapTiles[e.getTargetMapPosition().x][e.getTargetMapPosition().y]);
        }
        else
        {
            logger.info("ncp {} has reached target", e.getId());
            e.setRunningAction(null);
        }
    }

    /**
     * this is the global method that is called in game in advance turn
     *
     * @param e - the lifeform in question
     */
    public static void determineAction(LifeForm e)
    {
        if (e.isHostile())
        {
            //logger.info("npc {} is hostile", e.getId());
            AIBehaviour.determineCombat(e);
        }
        else if (e.isPatrolling())
        {
            //logger.info("npc {} is patrolling", e.getId());
            AIBehaviour.determinePatrol(e);
        }
        else if (e.getRunningAction() != null)
        {
            if (e.getRunningAction().getType().equals(KeyboardActionType.MOVE))
            {
                AIBehaviour.determineMove(e);
            }
        }
        else if (e.getQueuedActions().size() > 0)
        {
            logger.info("npc {}, action in queue: {}", e.getId(), e.getQueuedActions().peek());
            e.doAction(new NPCAction((AbstractKeyboardAction) e.getQueuedActions().poll()));
        }
        else
        {
            logger.info("NPC {} is random", e.getId());
            AIBehaviour.determineRandom(e);
        }
    }

    /**
     * this is the pseudo AI which calculates actions done by NPCs. There probably needs to be a difference between hostile and friendly NPCs meaning that there will need to be a separate class for
     * that instead of having this in a Utils class.
     *
     * @param e the abstract entity, npc probably
     * @return an action that is done once the npc is doing action
     */
    public static NPCAction calculateAction(LifeForm e)
    {
        if (!(e.isStatic()))
        {
            return wanderAround(e, -1);
        }
        return new NPCAction(new SpaceAction());
    }


    /**
     * @param e AbstractEntity - the npc
     * @return PlayerAction which direction the NPC will move to
     * only move 2 squares in any direction
     */
    public static NPCAction wanderAround(LifeForm e, int i)
    {
        logger.info("npc: {} is wanderer, move {}", e, i);

        final Range<Integer> range = Range.between(-1, 3);

        if (range.contains(i))
        {
            //logger.info("lifeform {} current map position: {}", e.getId(), e.getMapPosition());
            //logger.info("lifeform {} original map position {}", e.getId(), e.getOriginalMapPosition());

            if (e.getOriginalMapPosition() == null)
            {
                e.setOriginalTargetMapPosition(new Point(e.getMapPosition()));
            }

            final Range<Integer> rangeX = Range.between(e.getOriginalMapPosition().x - 2, e.getOriginalMapPosition().x + 2);
            final Range<Integer> rangeY = Range.between(e.getOriginalMapPosition().y - 2, e.getOriginalMapPosition().y + 2);

            if (i == -1)
            {
                Random rand = new Random();
                i = rand.nextInt(4);
            }
            //logger.info("npc {}, original position {}, map position: {}",e,  e.getOriginalMapPosition(), e.getMapPosition());
            switch (i)
            {
                // north
                case 0:
                    if (rangeY.contains(e.getMapPosition().y - 1))
                    {
                        return new NPCAction(new NorthAction());
                    }
                    else
                    {
                        logger.info("npc {} at border of box {}, mapposition: {}", e, "north", e.getMapPosition());
                        return new NPCAction(new SouthAction());
                    }
                    // east
                case 1:
                    if (rangeX.contains(e.getMapPosition().x + 1))
                    {
                        logger.info("wander east");
                        return new NPCAction(new EastAction());
                    }
                    else
                    {
                        logger.info("npc {} at border of box {}, mapposition: {}", e, "east", e.getMapPosition());
                        logger.info("wander west");
                        return new NPCAction(new WestAction());
                    }
                    // south
                case 2:
                    if (rangeY.contains(e.getMapPosition().y + 1))
                    {
                        return new NPCAction(new SouthAction());
                    }
                    else
                    {
                        logger.info("npc {} at border of box {}, mapposition: {}", e, "south", e.getMapPosition());
                        return new NPCAction(new NorthAction());
                    }
                    // west
                case 3:
                    if (rangeX.contains(e.getMapPosition().x - 1))
                    {
                        return new NPCAction(new WestAction());
                    }
                    else
                    {
                        logger.info("npc {} at border of box {}, mapposition: {}", e, "west", e.getMapPosition());
                        return new NPCAction(new EastAction());
                    }
                default:
                    logger.info("npc {} spaces out", e);
                    return new NPCAction(new SpaceAction());
            }
        }
        else
        {
            logger.error("issue in calling wanderer!");
            Game.getCurrent().stopGame();
        }
        return null;
    }

}

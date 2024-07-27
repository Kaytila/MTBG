package net.ck.mtbg.backend.entities.ai;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.actions.NPCAction;
import net.ck.mtbg.backend.actions.PlayerAction;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.backend.queuing.ScheduleActivity;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.items.WeaponTypes;
import net.ck.mtbg.map.MapTile;
import net.ck.mtbg.util.communication.keyboard.*;
import net.ck.mtbg.util.utils.MapUtils;
import net.ck.mtbg.util.utils.NPCUtils;
import net.ck.mtbg.weather.DayNight;
import org.apache.commons.lang3.Range;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

@Log4j2
@Setter
@Getter
public class AIBehaviour
{
    public static void determineCombat(LifeForm e)
    {
        if (Objects.requireNonNull(MapUtils.getMapTileByCoordinatesAsPoint(e.getMapPosition())).isHidden())
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} at position: {} moving towards player", e.getId(), e.getMapPosition());
            }
            e.doAction((NPCUtils.calculateVictimDirection(e)));
        }
        else
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} trying to attack", e.getId());
            }
            //attack with melee
            if (MapUtils.isAdjacent(e.getMapPosition(), e.getVictim().getMapPosition()))
            {
                if (e.isRanged())
                {
                    e.switchWeapon(WeaponTypes.MELEE);
                }
                else
                {
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("npc {} attacking", e.getId());
                    }
                    AttackAction action = new AttackAction();
                    action.setGetWhere(e.getVictim().getMapPosition());
                    e.doAction(new NPCAction(action));
                }
            }
            //victim is not adjacent
            else
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("npc {} is out of melee range, what to do", e.getId());
                }
                //Weapon sling = getWeaponList().get(3);
                //e.getItem(sling);
                //npc has ranged weapon wielded or has one in inventory
                if (e.isRanged())
                {
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("NPC {} has ranged capabilities", e.getId());
                    }
                    //wielded attack
                    if (e.getWeapon() != null)
                    {
                        if (e.getWeapon().getType().equals(WeaponTypes.RANGED))
                        {
                            if (e.getWeapon().getRange() < MapUtils.calculateMaxDistance(e.getMapPosition(), e.getVictim().getMapPosition()))
                            {
                                if (GameConfiguration.debugNPC == true)
                                {
                                    logger.debug("npc {} is out of range move towards victim", e.getId());
                                }
                                //logger.info("what do do: {}", (NPCUtils.calculateVictimDirectionAStar(e)));
                                e.doAction((NPCUtils.calculateVictimDirection(e)));
                            }
                            else
                            {
                                //todo check weapon range here
                                //todo implement range check
                                //todo implement rage on weapon
                                if (GameConfiguration.debugNPC == true)
                                {
                                    logger.debug("npc {} already wields ranged weapon, attack!", e.getId());
                                }
                                AttackAction ac = new AttackAction();
                                ac.setGetWhere(e.getVictim().getMapPosition());
                                NPCAction action = new NPCAction(ac);
                                e.doAction(action);
                            }
                            //return;
                        }
                        //in inventory, wield
                        else
                        {
                            if (GameConfiguration.debugNPC == true)
                            {
                                logger.debug("npc {} has ranged weapon in inventory, switch", e.getId());
                            }
                            e.switchWeapon(WeaponTypes.RANGED);
                            //return;
                        }
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} weapon is null", e.getId());
                        }
                        e.switchWeapon(WeaponTypes.RANGED);
                    }
                }
                else
                {
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("npc {} - victim is out of range move towards victim", e.getId());
                    }
                    e.doAction((NPCUtils.calculateVictimDirection(e)));
                }
            }
        }
    }

    public static void determineRandom(LifeForm e)
    {
        AbstractKeyboardAction action;
        action = e.lookAroundForItems();
        //TODO - this is what leads to the bug
        /*if (action == null)
        {
            action = e.lookForExit();
        }*/
        if (action != null)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} is trying to get", e.getId());
            }
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
        if (GameConfiguration.debugNPC == true)
        {
            logger.debug("NPC {} map position: {}", e.getId(), e.getMapPosition());
            logger.debug("NPC {} original map position: {}", e.getId(), e.getOriginalMapPosition());
            logger.debug("NPC {} target position: {}", e.getId(), e.getTargetMapPosition());
            logger.debug("NPC {} original target position: {}", e.getId(), e.getOriginalTargetMapPosition());
        }
        if (e.getQueuedActions().size() > 0)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} action in queue: {}", e.getId(), e.getQueuedActions().peek());
            }
            e.doAction(new PlayerAction(e.getQueuedActions().poll()));
        }
        else
        {
            if (!(e.getMapPosition().equals(e.getTargetMapPosition())))
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("npc {} we have not reached the target", e.getId());
                }
                MoveAction action = new MoveAction();

                action.setGetWhere(new Point(e.getTargetMapPosition().x, e.getTargetMapPosition().y));
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("npc {} move towards target map position: {},{}", e.getId(), e.getTargetMapPosition().x, e.getTargetMapPosition().y);
                }
                e.doAction(new NPCAction(action));
                //e.moveTo(Game.getCurrent().getCurrentMap().mapTiles[e.getTargetMapPosition().x][e.getTargetMapPosition().y]);
            }
            //we have reached the target
            else
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("npc {} we have reached the original position, switch to original map position: {}", e.getId(), e.getOriginalMapPosition());
                }
                //we have reached the original position, switch to original target
                if (e.getMapPosition().equals(e.getOriginalMapPosition()))
                {
                    e.setTargetMapPosition(e.getOriginalTargetMapPosition());
                }

                //we have reached the original target, switch to original position
                else
                {
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("npc {} we have reached the original target, switch to original position", e.getId());
                    }
                    e.setTargetMapPosition(e.getOriginalMapPosition());
                }
            }
        }
    }

    public static void determineMove(LifeForm e)
    {
        if (!(e.getMapPosition().equals(e.getRunningAction().getGetWhere())))
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} move towards target map position: {}", e.getId(), e.getRunningAction().getGetWhere());
            }
            e.doAction(new NPCAction(e.getRunningAction()));
            //e.moveTo(Game.getCurrent().getCurrentMap().mapTiles[e.getTargetMapPosition().x][e.getTargetMapPosition().y]);
        }
        else
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("ncp {} has reached target", e.getId());
            }

            if (e.getSchedule() != null)
            {
                if (e.getSchedule().getActivities().size() > 0)
                {
                    if (e.getSchedule().getCurrentlyActiveActivity().isActive())
                    {
                        if (GameConfiguration.debugSchedule)
                        {
                            logger.debug("npc: {} current activity reached, set inactive, move to next one", e.getId());
                        }
                        e.getSchedule().getCurrentlyActiveActivity().setActive(false);
                        e.getSchedule().moveToNextScheduleActivity();
                    }
                }
            }
            e.setRunningAction(null);
        }
    }

    /**
     * this is the global method that is called in game in advance turn
     * to determine NPC behaviour generally
     *
     * @param e - the life form in question
     */
    public static void determineAction(LifeForm e)
    {
        if (NPCUtils.isActive(e) == false)
        {
            return;
        }

        if (e.isHostile())
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} is hostile", e.getId());
            }
            AIBehaviour.determineCombat(e);
        }
        else if (e.isPatrolling())
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} is patrolling", e.getId());
            }
            AIBehaviour.determinePatrol(e);
        }
        else if (e.getRunningAction() != null)
        {
            if (e.getRunningAction().getType().equals(KeyboardActionType.MOVE))
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("npc {} has a running action and it is move", e.getId());
                }
                AIBehaviour.determineMove(e);
            }
        }
        else if (e.getQueuedActions().size() > 0)
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {}, action in queue: {}", e.getId(), e.getQueuedActions().peek());
            }
            e.doAction(new NPCAction(e.getQueuedActions().poll()));
        }
        //TODO this is already too naive
        //if i only check for schedule existing, npc will never to anything else
        //i need to determine here whether there is a schedule activity that needs to be run
        //now instead of doing that within the call
        else if ((e.getSchedule() != null) && (e.getSchedule().getCurrentlyActiveActivity().isActive()))
        {
            if (GameConfiguration.debugSchedule == true)
            {
                logger.debug("npc {} has a schedule", e.getId());
            }
            AIBehaviour.runSchedule(e);
        }
        else
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("NPC {} is random", e.getId());
            }
            AIBehaviour.determineRandom(e);
        }
    }

    private static void runSchedule(LifeForm e)
    {
        if (e.getSchedule() != null)
        {
            if (GameConfiguration.debugSchedule == true)
            {
                logger.debug("npc  {} has schedule", e.getId());
            }
            //easy for now
            if (e.getSchedule().isActive() == true)
            {
                if (GameConfiguration.debugSchedule == true)
                {
                    logger.debug("npc {} schedule is active", e.getId());
                }
                if (e.getSchedule().getActivities() != null)
                {
                    if (GameConfiguration.debugSchedule == true)
                    {
                        logger.debug("npc {} schedule has activities", e.getId());
                    }

                    ScheduleActivity activity = e.getSchedule().getActivities().get(e.getSchedule().getCurrentScheduleActivityIndex());

                    GameTime startTime = activity.getStartTime();
                    if (GameConfiguration.debugSchedule == true)
                    {
                        logger.debug("npc {} game time: {}", e.getId(), Game.getCurrent().getGameTime());
                        logger.debug("npc {} activity start time: {}", e.getId(), startTime);
                    }
                    if (Game.getCurrent().getGameTime().getCurrentHour() >= startTime.getCurrentHour())
                    {
                        if (Game.getCurrent().getGameTime().getCurrentMinute() >= startTime.getCurrentMinute())
                        {
                            if (e.getSchedule().getCurrentScheduleActivityIndex() == 0)
                            {
                                //first one is already set active
                            }
                            else
                            {
                                activity.setActive(true);
                            }
                            if (GameConfiguration.debugSchedule == true)
                            {
                                logger.debug("npc {} activating activity: {}", e.getId(), activity);
                            }

                            e.setRunningAction(activity.getAction());
                            e.doAction(new PlayerAction(activity.getAction()));
                        }
                    }

                }
            }
        }
    }

    /**
     * this is the pseudo AI which calculates actions done by NPCs.
     * There probably needs to be a difference between hostile and friendly NPCs meaning
     * that there will need to be a separate class for
     * that instead of having this in a Utils class.
     *
     * @param e the abstract entity, npc probably
     * @return an action that is done once the npc is doing action
     */
    public static NPCAction calculateAction(LifeForm e)
    {
        if (MapUtils.calculateDayOrNight() == DayNight.NIGHT)
        {
            MapTile tile = MapUtils.getClosestLightSourceInVicinity(MapUtils.getMapTileByCoordinates(e.getMapPosition()), 17, false);
            if (tile != null)
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("npc {} light source to ignite", e.getId());
                }
                if (MapUtils.isAdjacent(e.getMapPosition(), tile.getMapPosition()))
                {
                    //e.look(tile);
                    LookAction action = new LookAction();
                    action.setGetWhere(new Point(tile.x, tile.y));
                    e.doAction(new NPCAction(action));
                    e.say("ITS DARK HERE - switch on");
                }
                else
                {
                    MoveAction action = new MoveAction();
                    action.setGetWhere(new Point(tile.x, tile.y));
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("npc {} move towards target map position: {},{}", e.getId(), e.getTargetMapPosition().x, e.getTargetMapPosition().y);
                    }
                    e.doAction(new NPCAction(action));
                }
            }
        }
        else if (MapUtils.calculateDayOrNight() == DayNight.DAY)
        {
            MapTile tile = MapUtils.getClosestLightSourceInVicinity(MapUtils.getMapTileByCoordinates(e.getMapPosition()), 17, true);
            if (tile != null)
            {
                if (GameConfiguration.debugNPC == true)
                {
                    logger.debug("light source to douse");
                }
                if (MapUtils.isAdjacent(e.getMapPosition(), tile.getMapPosition()))
                {
                    //e.look(tile);
                    LookAction action = new LookAction();
                    action.setGetWhere(new Point(tile.x, tile.y));
                    e.doAction(new NPCAction(action));
                    e.say("TURN IT OFF; WASTE");
                }
                else
                {
                    MoveAction action = new MoveAction();
                    action.setGetWhere(new Point(tile.x, tile.y));
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("npc {} move towards target map position: {},{}", e.getId(), e.getTargetMapPosition().x, e.getTargetMapPosition().y);
                    }
                    e.doAction(new NPCAction(action));
                }
            }
        }
        else
        {
            logger.debug("shrug what to do here");
        }

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
        if (GameConfiguration.debugNPC == true)
        {
            logger.debug("npc: {} is wanderer, move {}", e, i);
        }
        final Range<Integer> range = Range.of(-1, 3);

        if (range.contains(i))
        {
            if (GameConfiguration.debugNPC == true)
            {
                logger.debug("npc {} current map position: {}", e.getId(), e.getMapPosition());
                logger.debug("npc {} original map position {}", e.getId(), e.getOriginalMapPosition());
            }
            if (e.getOriginalMapPosition() == null)
            {
                e.setOriginalTargetMapPosition(new Point(e.getMapPosition()));
            }

            final Range<Integer> rangeX = Range.of(e.getOriginalMapPosition().x - 2, e.getOriginalMapPosition().x + 2);
            final Range<Integer> rangeY = Range.of(e.getOriginalMapPosition().y - 2, e.getOriginalMapPosition().y + 2);

            if (i == -1)
            {
                Random rand = new Random();
                i = rand.nextInt(4);
            }

            switch (i)
            {
                // north
                case 0:
                    if (rangeY.contains(e.getMapPosition().y - 1))
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} wander north", e.getId());
                        }
                        return new NPCAction(new NorthAction());
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} at border of box {}, map position: {}", e.getId(), "north", e.getMapPosition());
                            logger.debug("npc {} wander south", e.getId());
                        }
                        return new NPCAction(new SouthAction());
                    }
                    // east
                case 1:
                    if (rangeX.contains(e.getMapPosition().x + 1))
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} wander east", e.getId());
                        }
                        return new NPCAction(new EastAction());
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} at border of box {}, map position: {}", e.getId(), "east", e.getMapPosition());
                            logger.debug("npc {} wander west", e.getId());
                        }
                        return new NPCAction(new WestAction());
                    }
                    // south
                case 2:
                    if (rangeY.contains(e.getMapPosition().y + 1))
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} wander south", e.getId());
                        }
                        return new NPCAction(new SouthAction());
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} at border of box {}, map position: {}", e.getId(), "south", e.getMapPosition());
                            logger.debug("npc {} wander north", e.getId());
                        }
                        return new NPCAction(new NorthAction());
                    }
                    // west
                case 3:
                    if (rangeX.contains(e.getMapPosition().x - 1))
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} wander west", e.getId());
                        }
                        return new NPCAction(new WestAction());
                    }
                    else
                    {
                        if (GameConfiguration.debugNPC == true)
                        {
                            logger.debug("npc {} at border of box {}, map position: {}", e.getId(), "west", e.getMapPosition());
                            logger.debug("npc {} wander east", e.getId());
                        }
                        return new NPCAction(new EastAction());
                    }
                default:
                    if (GameConfiguration.debugNPC == true)
                    {
                        logger.debug("npc {} spaces out", e.getId());
                    }
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

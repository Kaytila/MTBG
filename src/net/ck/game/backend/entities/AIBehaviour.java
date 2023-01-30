package net.ck.game.backend.entities;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.game.Game;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.AttackAction;
import net.ck.util.communication.keyboard.GetAction;
import net.ck.util.communication.keyboard.MoveAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class AIBehaviour
{

    private static final Logger logger = LogManager.getLogger(AIBehaviour.class);


    public static void determineCombat(LifeForm e)
    {
        logger.info("trying to attack");
        //attack with melee
        if (MapUtils.isAdjacent(e.getMapPosition(), e.getVictim().getMapPosition()))
        {
            logger.info("attacking");
            e.doAction(new PlayerAction(new AttackAction()));
            //return;
        }
        //victim is not adjacent
        else
        {
            logger.info("out of melee range, what to do");
            //Weapon sling = getWeaponList().get(3);
            //e.getItem(sling);
            //npc has ranged weapon wielded or has one in inventory
            if (e.isRanged())
            {
                logger.info("NPC has ranged capabilities");
                //wielded attack
                if (e.getWeapon() != null)
                {
                    if (e.getWeapon().getType().equals(WeaponTypes.RANGED))
                    {
                        logger.info("npc already wields ranged weapon, attack!");
                        PlayerAction action = new PlayerAction(new AttackAction());
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
            PlayerAction ac = new PlayerAction(action);
            e.doAction(ac);
        }
        else
        {
            e.doAction(NPCUtils.calculateAction(e));
        }
    }

    public static void determinePatrol(LifeForm e)
    {
        if (e.getQueuedActions().size() > 0)
        {
            //logger.info("action in queue: {}", npc.getQueuedActions().peek());
            e.doAction(new PlayerAction((AbstractKeyboardAction) e.getQueuedActions().poll()));
        }
        else
        {
            //we have not reached the target
            if (!(e.getMapPosition().equals(e.getTargetMapPosition())))
            {
                MoveAction action = new MoveAction();
                action.setGetWhere(new Point(e.getTargetMapPosition().x, e.getTargetMapPosition().y));
                logger.info("move towards target map position: {},{}", e.getTargetMapPosition().x, e.getTargetMapPosition().y);
                e.doAction(new PlayerAction(action));
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
        }
    }
}

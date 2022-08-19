package net.ck.game.backend.entities;

import net.ck.game.backend.Game;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.items.Weapon;
import net.ck.game.items.WeaponTypes;
import net.ck.util.MapUtils;
import net.ck.util.NPCUtils;
import net.ck.util.communication.keyboard.AttackAction;
import net.ck.util.communication.keyboard.GetAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                e.doAction((NPCUtils.calculateVictimDirection(e)));
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
}

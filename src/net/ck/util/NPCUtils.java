package net.ck.util;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.NPC;
import net.ck.util.communication.keyboard.*;
import org.apache.commons.lang3.Range;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Random;

public class NPCUtils
{

    private static final Logger logger = LogManager.getLogger(NPCUtils.class);

    public NPCUtils()
    {
        logger.info("creating npc");
    }

    /**
     * this is the pseudo AI which calculates actions done by NPCs. There probably needs to be a difference between hostile and friendly NPCs meaning that there will need to be a separate class for
     * that instead of having this in a Utils class.
     *
     * @param e the abstract entity, npc probably
     * @return an action that is done once the npc is doing action
     */
    public static PlayerAction calculateAction(NPC e)
    {
        //logger.info("calculate action");

        if (e.getQueuedActions().size() > 0)
        {
            //logger.info("action in queue: {}", npc.getQueuedActions().peek());
            return new PlayerAction((AbstractKeyboardAction) e.getQueuedActions().poll(),e);
        }
        else
        {
            if (!(e.isStatic()))
            {
                return initializeWanderer(e);
            }
            return new PlayerAction(new SpaceAction(), e);
        }
    }

    /**
     *
     * @param e AbstractEntity - the npc
     * @return PlayerAction which direction the NPC will move to
     * only move 2 squares in any direction
     */
    private static PlayerAction initializeWanderer(NPC e)
    {

        final Range<Integer> rangeX = Range.between(e.getOriginalMapPosition().x - 2, e.getOriginalMapPosition().x + 2);
        final Range<Integer> rangeY = Range.between(e.getOriginalMapPosition().y - 2, e.getOriginalMapPosition().y + 2);

        Random rand = new Random();
        logger.info("npc {}, original position {}, map position: {}",e,  e.getOriginalMapPosition(), e.getMapPosition());

        switch (rand.nextInt(4))
        {
            // north
            case 0:
                if (rangeY.contains(e.getMapPosition().y))
                {
                    return new PlayerAction(new NorthAction(), e);
                }
                else
                {
                    logger.info("npc {} at border of box {}, mapposition: {}", e, "north", e.getMapPosition());
                    return new PlayerAction(new SouthAction(), e);
                }
            // east
            case 1:
                if (rangeX.contains(e.getMapPosition().x))
                {
                    return new PlayerAction(new EastAction(), e);
                }
                else
                {
                    logger.info("npc {} at border of box {}, mapposition: {}", e, "east", e.getMapPosition());
                    return new PlayerAction(new WestAction(), e);
                }
            // south
            case 2:
                if (rangeY.contains(e.getMapPosition().y))
                {
                    return new PlayerAction(new SouthAction(), e);
                }
                else
                {
                    logger.info("npc {} at border of box {}, mapposition: {}", e, "south", e.getMapPosition());
                    return new PlayerAction(new NorthAction(), e);
                }
            // west
            case 3:
                if (rangeX.contains(e.getMapPosition().x))
                {
                    return new PlayerAction(new WestAction(), e);
                }
                else
                {
                    logger.info("npc {} at border of box {}, mapposition: {}", e, "west", e.getMapPosition());
                    return new PlayerAction(new EastAction(), e);
                }
            default:
                return new PlayerAction(new SpaceAction(), e);
        }

    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }
}

package net.ck.util;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.AbstractEntity;
import net.ck.game.backend.entities.NPC;
import net.ck.util.communication.keyboard.*;
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
    public static PlayerAction calculateAction(AbstractEntity e)
    {
        //logger.info("calculate action");
        NPC npc = (NPC) e;
        if (npc.getQueuedActions().size() > 0)
        {
            //logger.info("action in queue: {}", npc.getQueuedActions().peek());
            return new PlayerAction((AbstractKeyboardAction) npc.getQueuedActions().poll(),e);
        }
        else
        {
            Random rand = new Random();

            switch (rand.nextInt(4))
            {
                // north
                case 0:
                    return new PlayerAction(new NorthAction(), e);

                // east
                case 1:
                    return new PlayerAction(new EastAction(), e);

                // south
                case 2:
                    return new PlayerAction(new SouthAction(), e);

                // west
                case 3:
                    return new PlayerAction(new WestAction(), e);

                default:
                    return new PlayerAction(new SpaceAction(), e);
            }
        }
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }
}

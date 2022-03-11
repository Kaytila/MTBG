package net.ck.util;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.entities.AbstractEntity;
import net.ck.util.communication.keyboard.EastAction;
import net.ck.util.communication.keyboard.NorthAction;
import net.ck.util.communication.keyboard.SouthAction;
import net.ck.util.communication.keyboard.SpaceAction;
import net.ck.util.communication.keyboard.WestAction;

public class NPCUtils
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public NPCUtils()
	{
		logger.info("creating npc");
	}

	/**
	 * this is the pseudo AI which calculates actions done by NPCs. There probably needs to be a difference between hostile and friendly NPCs meaning that there will need to be a separate class for
	 * that instead of having this in a Utils class.
	 * 
	 * @param e
	 * @return
	 */
	public static PlayerAction calculateAction(AbstractEntity e)
	{
		Random rand = new Random();

		switch (rand.nextInt(4))
		{
			// north
			case 0 :
				return new PlayerAction(new NorthAction(), e);

			// east
			case 1 :
				return new PlayerAction(new EastAction(), e);

			// south
			case 2 :
				return new PlayerAction(new SouthAction(), e);

			// west
			case 3 :
				return new PlayerAction(new WestAction(), e);

			default :
				return new PlayerAction(new SpaceAction(), e);

		}
	}

}

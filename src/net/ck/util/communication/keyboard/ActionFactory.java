package net.ck.util.communication.keyboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ActionFactory
{

	private static final Logger logger = LogManager.getLogger(ActionFactory.class);

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public static AbstractKeyboardAction createAction(KeyboardActionType type)
	{
		switch (type)
		{
			case EAST :
				return new EastAction();
			case ENTER :
				return new EnterAction();
			case ESC :
				return new ESCAction();
			case NORTH :
				return new NorthAction();
			case NULL :
				return new AbstractKeyboardAction();
			case SOUTH :
				return new SouthAction();
			case SPACE :
				return new SpaceAction();
			case WEST :
				return new WestAction();		
			case INVENTORY:
				return new InventoryAction();
			case ZSTATS:
				return new ZStatsAction();
			default :
				logger.error("how did we end up here ?????");
				return new AbstractKeyboardAction();
		}
	}

}

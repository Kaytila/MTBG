package net.ck.util;

import net.ck.game.backend.Turn;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.util.communication.keyboard.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class GameUtils
{

	private static final Logger logger = (Logger) LogManager.getLogger(GameUtils.class);
	private static final Font font = new Font("Helvetica Neue", Font.PLAIN, 20);
	private static final int padding = 5;

	public static void showStackTrace(String methodName)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		logger.info("calling " + methodName + " from: {} or: {}", stackTraceElements[1].getMethodName(), stackTraceElements[2].getMethodName());
	}


	public static void invertActions(Turn turn)
	{
		for (PlayerAction p : turn.getActions())
		{
			// logger.debug("playeraction p: {}", p.getEntity().getClass().getName());
			// NORTH, SOUTH, WEST, EAST, ENTER, ESC, NULL
			switch (p.getType())
			{
				case NORTH :
				{
					p.setEvent(new SouthAction());
					break;
				}
				case SOUTH :
				{
					p.setEvent(new NorthAction());
					break;
				}
				case EAST :
				{
					p.setEvent(new WestAction());
					break;
				}
				case WEST :
				{
					p.setEvent(new EastAction());
					break;
				}
				case ENTER :
				{
					logger.error("ENTER crept in here");
					break;
				}
				case ESC :
				{
					logger.error("ESC crept in here");
					break;
				}
				case NULL :
				{
					logger.error("Null crept in here: {}", p.toString());
					break;
				}
				case SPACE :
				{
					p.setEvent(new SpaceAction());
					break;
				}
				default :
				{
					throw new IllegalArgumentException("not expected value during invertActions: " + p.getType().toString());
				}
			}
		}

	}

	public static Font getFont()
	{
		//logger.info("font attributes: {}", font.getSize());
		return font;
	}

	public static int getLineHeight()
	{
		return font.getSize() + getPadding();
	}

	public static int getPadding()
	{
		return padding;
	}

}

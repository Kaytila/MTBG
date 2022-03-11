package net.ck.util;

import java.awt.Font;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.backend.Turn;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.util.communication.keyboard.KeyboardActionType;

public class GameUtils
{

	private static final Logger logger = (Logger) LogManager.getLogger(GameUtils.class);
	private static final Font font = new Font("Helvetica Neue", Font.PLAIN, 20);
	private static final int padding = 5;

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
					p.setType(KeyboardActionType.SOUTH);
					break;
				}
				case SOUTH :
				{
					p.setType(KeyboardActionType.NORTH);
					break;
				}
				case EAST :
				{
					p.setType(KeyboardActionType.WEST);
					break;
				}
				case WEST :
				{
					p.setType(KeyboardActionType.EAST);
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
					p.setType(KeyboardActionType.SPACE);
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

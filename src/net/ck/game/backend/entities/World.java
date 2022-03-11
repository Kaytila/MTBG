package net.ck.game.backend.entities;

import java.awt.Point;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.Game;
import net.ck.game.backend.Turn;
import net.ck.game.backend.actions.RandomAction;
import net.ck.game.graphics.AnimatedRepresentation;

/**
 * environment is the counter part to player, the AI so to speak
 * 
 * @author Claus
 *
 */
public class World extends AbstractEntity
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


	public World()
	{
		super();
	}

	public RandomAction createRandomEvent()
	{
		RandomAction event = new RandomAction();
		Random rand = new Random();
		int result = rand.nextInt(100);

		switch (result)
		{
			case 0 :
				event.setTitle("BINGO test" + result);
			default :
				event.setTitle("test " + result);
		}
		getCurrentTurn().getEvents().add(event);
		// logger.info("random event: " + event.getTitle());
		event.setEntity(this);
		return event;
	}

	public Turn getCurrentTurn()
	{
		return Game.getCurrent().getCurrentTurn();
	}

	@Override
	public Point getMapPosition()
	{
		// logger.error("world does not have a position, return empty point");
		return new Point(-1, -1);
	}

	@Override
	public void setMapPosition(Point p)
	{
		logger.error("world does not have a position, do not set");
	}

	@Override
	public AnimatedRepresentation getAppearance()
	{
		logger.error("world does not have a graphical appearance");
		return null;
	}

	@Override
	public int getNumber()
	{
		logger.error("world does not have a number");
		return 0;
	}

	@Override
	public void setNumber(int i)
	{
		logger.error("world does not have a number");
	}

	public String toString()
	{
		return "World TBD";
	}

	@Override
	public Point getUIPosition()
	{
		return new Point(-1, -1);
	}

}

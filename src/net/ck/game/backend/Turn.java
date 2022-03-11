package net.ck.game.backend;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.backend.actions.RandomAction;

/**
 * so  a turn is a turn.
 * what scope does a turn have?
 * is the scope of a turn on the map?
 * is it game?
 * currently, it is game.
 * but that would mean, for each movement inside a map, the rest would move too?
 * or can retract turn also go back down a ladder?
 * @author Claus
 *
 */
public class Turn
{
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}
	
	private ArrayList<PlayerAction> actions = new ArrayList<PlayerAction>();
	private ArrayList<RandomAction> events = new ArrayList<RandomAction>();
	private int turnNumber;

	public Turn(int turnNumber)
	{
		setTurnNumber(turnNumber);
	}

	public ArrayList<PlayerAction> getActions()
	{
		return actions;
	}

	public ArrayList<RandomAction> getEvents()
	{
		return events;
	}

	public int getTurnNumber()
	{
		return turnNumber;
	}

	public void setActions(ArrayList<PlayerAction> actions)
	{
		this.actions = actions;
	}

	public void setEvents(ArrayList<RandomAction> events)
	{
		this.events = events;
	}

	public void setTurnNumber(int turnNumber)
	{
		this.turnNumber = turnNumber;
	}

	public Logger getLogger()
	{
		return logger;
	}
}

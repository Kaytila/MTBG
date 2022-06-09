package net.ck.game.backend.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.Result;
import net.ck.game.backend.entities.AbstractEntity;
import net.ck.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.util.communication.keyboard.KeyboardActionType;

import java.util.Objects;

public class AbstractAction
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	private Result result;
	private String title;
	
	/**
	 * who does the action?
	 */
	private AbstractEntity entity;
	
	/**
	 * what type of event is it?
	 * this probably needs to go somewhere else. 
	 */
	private AbstractKeyboardAction event;
	
	
	public Result getResult()
	{
		return result;
	}

	public String getTitle()
	{
		return title;
	}

	public void setResult(Result result)
	{
		this.result = result;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public AbstractEntity getEntity()
	{
		return entity;
	}

	public void setEntity(AbstractEntity ent)
	{
		this.entity = ent;
	}

	public KeyboardActionType getType()
	{
		return KeyboardActionType.NULL;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public AbstractKeyboardAction getEvent()
	{
		return event;
	}

	public void setEvent(AbstractKeyboardAction event)
	{
		this.event = event;
	}
}

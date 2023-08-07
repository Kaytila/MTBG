package net.ck.mtbg.backend.actions;

import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Result;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class AbstractAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private Result result;
	private String title;


	private boolean success;

	/**
	 * who does the action?
	 */
	private LifeForm entity;
	
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

	public LifeForm getEntity()
	{
		return entity;
	}

	public void setEntity(LifeForm ent)
	{
		this.entity = ent;
	}

	public abstract KeyboardActionType getType();


	public AbstractKeyboardAction getEvent()
	{
		return event;
	}

	public void setEvent(AbstractKeyboardAction event)
	{
		this.event = event;
	}


	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}
}

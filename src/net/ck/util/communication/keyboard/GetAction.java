package net.ck.util.communication.keyboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Objects;

public class GetAction extends AbstractKeyboardAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger(getRealClass());
		
	public GetAction(Point getWhere)
	{
		super();
		this.setGetWhere(getWhere);
	}

	
	public GetAction()
	{
		super();
		this.setGetWhere(new Point (-1, -1));
	}
	
	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public  KeyboardActionType getType()
	{
		return KeyboardActionType.GET;
	}
	
	
	
	public Logger getLogger()
	{
		return logger;
	}

	public boolean isActionimmediately()
	{
		return false;
	}

	
}

package net.ck.util.communication.keyboard;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class GetAction extends AbstractKeyboardAction
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
		
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

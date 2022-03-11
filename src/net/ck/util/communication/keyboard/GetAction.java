package net.ck.util.communication.keyboard;
import java.awt.Point;
import java.awt.event.ActionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

public class GetAction extends AbstractKeyboardAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
		
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
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		logger.info(getType() + " pressed");
		EventBus.getDefault().post(this);
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

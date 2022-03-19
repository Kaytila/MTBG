package net.ck.util.communication.keyboard;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import net.ck.game.items.AbstractItem;

public class AbstractKeyboardAction extends AbstractAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean actionimmediately = false;
	private boolean done = false;
	private final Logger logger = LogManager.getLogger(getRealClass());
	private Point getWhere;
	private AbstractItem affectedItem;
	
	public AbstractItem getAffectedItem()
	{
		return affectedItem;
	}

	public Point getGetWhere()
	{
		return getWhere;
	}

	public void setGetWhere(Point getWhere)
	{
		this.getWhere = getWhere;
	}

	public AbstractKeyboardAction()
	{
		setActionimmediately(false);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		logger.info(getType() + " pressed");
		EventBus.getDefault().post(this);
	}

	public Logger getLogger()
	{
		return logger;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}
	
	public  KeyboardActionType getType()
	{
		return KeyboardActionType.NULL;
	}

	public boolean isActionimmediately()
	{
		return actionimmediately;
	}

	public boolean isDone()
	{
		return done;
	}

	public void setActionimmediately(boolean actionimmediately)
	{
		this.actionimmediately = actionimmediately;
	}

	public void setDone(boolean done)
	{
		this.done = done;
	}

	public void setAffectedItem(AbstractItem currentItemInHand)
	{
		this.affectedItem = currentItemInHand;
		
	}	
}
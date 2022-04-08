package net.ck.util.communication.keyboard;

import net.ck.game.items.AbstractItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class AbstractKeyboardAction extends AbstractAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean actionimmediately = false;
	private boolean done = false;
	private final Logger logger = LogManager.getLogger(getRealClass());
	/**
	 * this is the tile where the crosshair clicked on
	 */
	private Point getWhere;
	private AbstractItem affectedItem;

	private Point sourceCoordinates;

	private Point targetCoordinates;

	public Point getOldMousePosition()
	{
		return oldMousePosition;
	}

	public void setOldMousePosition(Point oldMousePosition)
	{
		this.oldMousePosition = oldMousePosition;
	}

	private Point oldMousePosition;


	public Point getSourceCoordinates()
	{
		return sourceCoordinates;
	}

	public void setSourceCoordinates(Point sourceCoordinates)
	{
		this.sourceCoordinates = sourceCoordinates;
	}

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

	public Point getTargetCoordinates()
	{
		return targetCoordinates;
	}

	public void setTargetCoordinates(Point targetCoordinates)
	{
		this.targetCoordinates = targetCoordinates;
	}
}
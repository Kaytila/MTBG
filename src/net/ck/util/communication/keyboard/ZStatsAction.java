package net.ck.util.communication.keyboard;

import java.awt.event.ActionEvent;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

public class ZStatsAction extends AbstractKeyboardAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8861304812548371264L;
	private final Logger logger = LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public Logger getLogger()
	{
		return logger;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		logger.info(getType() + " pressed");
		EventBus.getDefault().post(this);
	}

	public KeyboardActionType getType()
	{
		return KeyboardActionType.ZSTATS;
	}
	
	public boolean isActionimmediately()
	{
		return false;
	}

}
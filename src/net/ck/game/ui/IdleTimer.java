package net.ck.game.ui;

import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.Timer;

public class IdleTimer extends Timer
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void start()
	{
		//StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();		
		//logger.info("calling start: {} or: {}", stackTraceElements[1].getMethodName(), stackTraceElements[2].getMethodName());
		//logger.info("IdleTimer: start");
		super.start();
	}

	@Override
	public void stop()
	{
		//logger.info("IdleTimer: stop");
		super.stop();
	}
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

	public Logger getLogger()
	{
		return logger;
	}
	public IdleTimer(int delay, ActionListener listener)
	{
		super(delay, listener);
	}
}

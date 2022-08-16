package net.ck.game.backend.time;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * idletime does exactly this - after PC has done a turn, idle timer starts
 * for N milliseconds, until a space, i.e. pass is being sent.
 */
public class IdleTimer extends Timer
{

	@Override
	public void start()
	{
		//logger.info("starting idle timer");
		super.start();
	}

	@Override
	public void stop()
	{
		//logger.info("stopping idle timer");
		super.stop();
	}

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
	public IdleTimer(int delay, ActionListener listener)
	{
		super(delay, listener);
	}
}

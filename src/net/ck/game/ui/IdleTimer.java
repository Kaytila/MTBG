package net.ck.game.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class IdleTimer extends Timer
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void start()
	{
		super.start();
	}

	@Override
	public void stop()
	{
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

package net.ck.game.backend.time;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * idletime does exactly this - after PC has done a turn, idle timer starts
 * for N milliseconds, until a space, i.e. pass is being sent.
 */
public class IdleTimer extends Timer
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
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

	public IdleTimer(int delay, ActionListener listener)
	{
		super(delay, listener);
	}
}

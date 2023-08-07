package net.ck.mtbg.backend.queuing;

import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.util.CodeUtils;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class ScheduleActivity
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private LifeForm npc;
	private GameTime startTime;
	private Point startLocation;
	private String actionString;

	private AbstractKeyboardAction action;


	public Point getStartLocation()
	{
		return startLocation;
	}

	public void setStartLocation(Point startLocation)
	{
		this.startLocation = startLocation;
	}

	public GameTime getStartTime()
	{
		return startTime;
	}

	public void setStartTime(GameTime startTime)
	{
		this.startTime = startTime;
	}

	public LifeForm getNpc()
	{
		return npc;
	}

	public void setNpc(LifeForm npc)
	{
		this.npc = npc;
	}

	public String getActionString()
	{
		return actionString;
	}

	public void setActionString(String actionString)
	{
		this.actionString = actionString;
	}

	public AbstractKeyboardAction getAction()
	{
		return action;
	}

	public void setAction(AbstractKeyboardAction action)
	{
		this.action = action;
	}
}

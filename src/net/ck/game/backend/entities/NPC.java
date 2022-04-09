package net.ck.game.backend.entities;

import net.ck.game.backend.CommandQueue;
import net.ck.game.backend.Game;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.ImageUtils;
import net.ck.util.communication.keyboard.MoveAction;
import net.ck.util.communication.time.GameTimeChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class NPC extends AbstractEntity
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	private AbstractRepresentation appearance;
	
	private NPCTypes type;
	private Hashtable<String, String> mobasks;

	/**
	 * describes whether a npc is moving or not (outside of schedules)
	 * @return true - meaning static, or false, meaning moving
	 */
	public boolean isStatic()
	{
		return isStatic;
	}

	public void setStatic(boolean aStatic)
	{
		isStatic = aStatic;
	}

	private boolean isStatic;

	private ArrayList<NPCSchedule> npcSchedules;
	private CommandQueue queuedActions;

	public Point getOriginalMapPosition()
	{
		return originalMapPosition;
	}

	public void setOriginalMapPosition(Point originalMapPosition)
	{
		this.originalMapPosition = originalMapPosition;
	}

	/**
	 * original position on the map - remember the placement that the npc does not wander off too much
	 */
	private Point originalMapPosition;

	public NPC(Integer i, Point p)
	{
		setStatic(false);
		setOriginalMapPosition(new Point (p.x, p.y));
		NPC master = Game.getCurrent().getNpcList().get(i);
		setMapPosition(new Point(p.x, p.y));
		setType(master.getType());
		setMobasks(master.getMobasks());
		setNpcSchedules(new ArrayList<>());
		setQueuedActions(new CommandQueue());
		EventBus.getDefault().register(this);
		//logger.info("npc: {}", this);
	}

	public void setType(NPCTypes type)
	{
		this.type = type;
	}

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	public NPC()
	{
		super();
		mobasks = new Hashtable<>();
	}

	@Override
	public String toString()
	{
		return "NPC [type=" + type + ", mapposition=" + mapPosition + ", mobasks=" + (mobasks != null ? toString(mobasks.entrySet()) : null) + "]";
	}

	private String toString(Collection<?> collection)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < 2; i++)
		{
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public void initialize()
	{
		ArrayList<BufferedImage> images = new ArrayList<>();

		BufferedImage standardImage;
		ArrayList<BufferedImage> movingImages;

		standardImage = ImageUtils.loadStandardPlayerImage(this);
		movingImages = ImageUtils.loadMovingPlayerImages(this);

		images.add(standardImage);
		images.addAll(movingImages);
		setAppearance(new AnimatedRepresentation(standardImage, images));
	}
	
	public Point getMapPosition()
	{
		return mapPosition;
	}

	public void setMapPosition(Point position)
	{
		this.mapPosition = position;
	}

	@Override
	public AbstractRepresentation getAppearance()
	{
		return appearance;
	}



	public void setAppearance(AbstractRepresentation appearance)
	{
		this.appearance = appearance;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public NPCTypes getType()
	{
		return type;
	}

	public Hashtable<String, String> getMobasks()
	{
		return mobasks;
	}

	public void setMobasks(Hashtable<String, String> mobasks)
	{
		this.mobasks = mobasks;
	}

	public ArrayList<NPCSchedule> getNpcSchedules()
	{
		return npcSchedules;
	}

	public void setNpcSchedules(ArrayList<NPCSchedule> npcSchedules)
	{
		this.npcSchedules = npcSchedules;
	}

	public CommandQueue getQueuedActions()
	{
		return queuedActions;
	}

	public void setQueuedActions(CommandQueue queuedActions)
	{
		this.queuedActions = queuedActions;
	}

	/**
	 *
	 * @param event
	 *            an animatedRepresentation has changed, repaint the canvas this triggers the whole repaint - do this more often, then there is more fluidity
	 */
	@Subscribe
	public void onMessageEvent(GameTimeChanged event)
	{
		this.checkSchedules(event);
	}

	private void checkSchedules(GameTimeChanged event)
	{
		if (Game.getCurrent().getGameTime().getCurrentHour() == 9 && Game.getCurrent().getGameTime().getCurrentMinute() == 10)
		{
			logger.info("check schedule");
			if (getMobasks().size() > 0)
			{
				logger.info("running");
				MoveAction action = new MoveAction();
				action.setGetWhere(new Point (1, 0));
				doAction(new PlayerAction(action, this));
			}
		}

		if (Game.getCurrent().getGameTime().getCurrentHour() == 9 && Game.getCurrent().getGameTime().getCurrentMinute() == 30)
		{
			logger.info("check schedule");
			if (getMobasks().size() > 0)
			{
				logger.info("running");
				MoveAction action = new MoveAction();
				action.setGetWhere(getOriginalMapPosition());
				doAction(new PlayerAction(action, this));
			}
		}

	}
}

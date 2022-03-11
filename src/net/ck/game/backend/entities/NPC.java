package net.ck.game.backend.entities;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.ImageUtils;

public class NPC extends AbstractEntity
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private AbstractRepresentation appearance;
	
	private NPCTypes type;
	private Hashtable<String, String> mobasks;
	
	public void setType(NPCTypes type)
	{
		this.type = type;
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

	public NPC()
	{
		super();
		mobasks = new Hashtable<String, String>();
		//setType(NPCTypes.WARRIOR);
		//setNumber(Game.getCurrent().getNextNPCNumber());
	}

	@Override
	public String toString()
	{
		final int maxLen = 2;
		return "NPC [type=" + type + ", mobasks=" + (mobasks != null ? toString(mobasks.entrySet(), maxLen) : null) + "]";
	}

	private String toString(Collection<?> collection, int maxLen)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++)
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
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

		BufferedImage standardImage = null;
		ArrayList<BufferedImage> movingImages = new ArrayList<BufferedImage>();

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
		//MapUtils.getTileByCoordinates(position).setBlocked(true);
		//logger.info("NPC number {}, position {}", getNumber(), mapPosition.toString());
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

}

package net.ck.game.backend.entities;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.*;

import net.ck.game.backend.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.ImageUtils;

public class NPC extends AbstractEntity
{

	private final Logger logger = LogManager.getLogger(getRealClass());
	private AbstractRepresentation appearance;
	
	private NPCTypes type;
	private Hashtable<String, String> mobasks;

	public NPC(Integer i, Point p)
	{
		NPC master = Game.getCurrent().getNpcList().get(i);
		setMapPosition(new Point(p.x, p.y));
		setType(master.getType());
		setMobasks(master.getMobasks());
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

}

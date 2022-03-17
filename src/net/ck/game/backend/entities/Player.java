package net.ck.game.backend.entities;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import net.ck.game.backend.Game;
import net.ck.game.backend.Turn;
import net.ck.game.backend.actions.PlayerAction;
import net.ck.game.graphics.AbstractRepresentation;
import net.ck.game.graphics.AnimatedRepresentation;
import net.ck.util.ImageUtils;
import net.ck.util.MapUtils;

public class Player extends AbstractEntity
{

	/**
	 * appearance is something that both player and npc have, en does not, so separate parent class?
	 */
	private AbstractRepresentation appearance;

	private int animationCycles = 0;


	public void setMapPosition(Point position)
	{
		this.mapPosition = position;
		MapUtils.getTileByCoordinates(position).setBlocked(true);		
		//logger.info("player number {}, map position {}", getNumber(), mapPosition.toString());
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

	/**
	 * constructor for the player player has two images types defined now.
	 * standard image is what is used if animation is turned off moving image is
	 * for whatever images is used to catch all images for the animation cycles.
	 * there needs to be an image per animation cycle per player.
	 * 
	 * to check for caller of method:
	 * https://stackoverflow.com/questions/421280/how-do-i-find-the-caller-of-a-method-using-stacktrace-or-reflection
	 * 
	 *
	 * parameters: player number
	 */
	public Player(int number)
	{
		super();
		
		//StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();		
		//logger.info("calling player constructor from: {} or: {}", stackTraceElements[1].getMethodName(), stackTraceElements[2].getMethodName());
		setLightSource(true);
		setLightRange(4);
		setNumber(number);

		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

		BufferedImage standardImage = null;
		ArrayList<BufferedImage> movingImages = new ArrayList<BufferedImage>();

		standardImage = ImageUtils.loadStandardPlayerImage(this);
		movingImages = ImageUtils.loadMovingPlayerImages(this);

		images.add(standardImage);
		images.addAll(movingImages);
		setAppearance(new AnimatedRepresentation(standardImage, images));
			
		this.getItem(Game.getCurrent().getArmorList().get(1));
		this.getItem(Game.getCurrent().getArmorList().get(2));
		this.getItem(Game.getCurrent().getArmorList().get(3));
		this.getItem(Game.getCurrent().getArmorList().get(4));
		this.getItem(Game.getCurrent().getArmorList().get(5));
		this.getItem(Game.getCurrent().getArmorList().get(6));
		this.getItem(Game.getCurrent().getArmorList().get(7));
		
		wearItem(Game.getCurrent().getArmorList().get(1));
		wearItem(Game.getCurrent().getArmorList().get(2));
		wearItem(Game.getCurrent().getArmorList().get(3));
		wearItem(Game.getCurrent().getArmorList().get(4));
		wearItem(Game.getCurrent().getArmorList().get(5));
		wearItem(Game.getCurrent().getArmorList().get(6));
		wearItem(Game.getCurrent().getArmorList().get(7));
	
		
		this.getItem(Game.getCurrent().getWeaponList().get(1));
		this.getItem(Game.getCurrent().getWeaponList().get(2));
		this.getItem(Game.getCurrent().getUtilityList().get(1));
		
		getAttributes().get(AttributeTypes.STRENGTH).setValue(10);
		getAttributes().get(AttributeTypes.DEXTERITY).setValue(10);
		getAttributes().get(AttributeTypes.INTELLIGENCE).setValue(10);
		getAttributes().get(AttributeTypes.CONSTITUTION).setValue(10);		
	}

	@SuppressWarnings("unused")
	/**
	 * old way of loading images - unused
	 */
	private void oldImageLoading()
	{
		logger.error("called by accident");
		System.exit(-1);
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		BufferedImage standardImage = null;
		BufferedImage movingImage = null;
		String imageRootPath = "graphics/players/player";
		try
		{
			standardImage = ImageIO.read(new File(imageRootPath + getNumber() + "/image1.png"));
		}
		catch (IOException e1)
		{
			logger.error("problem loading standard image {}", imageRootPath + getNumber() + "/image1.png");
			e1.printStackTrace();
		}
		catch (java.security.AccessControlException e2)
		{
			logger.error("caught it");
		}

		for (int i = 1; i < Game.getCurrent().getAnimationCycles(); i++)
		{
			try
			{
				logger.info("images: {} ", imageRootPath + getNumber() + "/image" + i + ".png");
				movingImage = ImageIO.read(new File(imageRootPath + getNumber() + "/image" + i + ".png"));
				animationCycles++;

			}
			catch (IOException e)
			{
				logger.error("problem loading image {}", imageRootPath + getNumber() + "/image" + i + ".png");

			}
			catch (java.security.AccessControlException e2)
			{
				logger.error("caught it");
			}

			images.add(movingImage);
		}
		images.add(standardImage);

	}



	public int getAnimationCycles()
	{
		return this.animationCycles;
	}

	public void setAnimationCycles(int animationCycles)
	{
		this.animationCycles = animationCycles;
	}

	public void doAction(PlayerAction action)
	{		
		super.doAction(action);		
	}

	public AbstractRepresentation getAppearance()
	{
		return appearance;
	}

	public Turn getCurrentTurn()
	{
		return Game.getCurrent().getCurrentTurn();
	}

	public void setAppearance(AbstractRepresentation appearance)
	{
		this.appearance = appearance;
	}

	@Override
	public Point getUIPosition()
	{
		return new Point(MapUtils.getMiddle(), MapUtils.getMiddle());
	}
}

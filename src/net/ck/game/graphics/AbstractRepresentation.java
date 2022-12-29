package net.ck.game.graphics;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;


/**
 * Abstract Representation is a class that can represent basically anything that has an appearance.
 * This can be the player,
 * part of the environment, i.e. living, but also not living things like items, places, terrain
 * Which reminds me, need to rewrite TileType in order to make this also fit this case
 * Tile has also an abstract representation.
 * @author Claus
 *
 */
public class AbstractRepresentation
{
	private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(AbstractRepresentation.class));

	/**
	 * this the currently visible image.
	 * in case of animation this is where the currently visible image is stored
	 * in unanimated cases, standard image and current are basically the same
	 * perhaps this will be changed
	 */
	protected BufferedImage currentImage;

	/**
	 * fallback image not really sure I need that at all
	 */
	private BufferedImage standardImage;

	/**
	 * AbstractRepresentation does not have any graphics of course :-)
	 */
	public AbstractRepresentation()
	{
		//logger.error("begin abstractrepresentation"); 
	}

	public BufferedImage getCurrentImage()
	{
		return currentImage;
	}


	public BufferedImage getStandardImage()
	{
		return standardImage;
	}

	public void setCurrentImage(BufferedImage currentImage)
	{
		this.currentImage = currentImage;
	
	}

	public void setStandardImage(BufferedImage standardImage)
	{
		this.standardImage = standardImage;
	}
	
}

package net.ck.game.graphics;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class UnanimatedRepresentation extends AbstractRepresentation
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	/**
	 * needs to have an image Image hmmm, needs to come from somewhere, need to
	 * implement an image loading function somwhere somewhen
	 * 
	 * @param img
	 */
	public UnanimatedRepresentation(BufferedImage img)
	{
		super();
		//logger.info("begin UnanimatedRepresentation");
		setStandardImage(img);
	}

	public UnanimatedRepresentation()
	{
		
	}
}

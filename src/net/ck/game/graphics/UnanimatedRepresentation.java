package net.ck.game.graphics;

import java.awt.image.BufferedImage;

public class UnanimatedRepresentation extends AbstractRepresentation
{

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

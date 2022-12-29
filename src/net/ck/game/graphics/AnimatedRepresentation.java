package net.ck.game.graphics;


import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class AnimatedRepresentation extends UnanimatedRepresentation
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private ArrayList<BufferedImage> animationImageList;
	
	public AnimatedRepresentation(BufferedImage img, ArrayList<BufferedImage> animations)
	{
		super(img);
		//logger.info("begin AnimatedRepresentation");
		setAnimationImageList(animations);
	}
	
	public ArrayList<BufferedImage> getAnimationImageList()
	{
		return animationImageList;
	}
	
	public void setAnimationImageList(ArrayList<BufferedImage> animationImageList)
	{
		this.animationImageList = animationImageList;
	}
}

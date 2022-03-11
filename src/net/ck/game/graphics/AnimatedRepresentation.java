package net.ck.game.graphics;


import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class AnimatedRepresentation extends UnanimatedRepresentation
{
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

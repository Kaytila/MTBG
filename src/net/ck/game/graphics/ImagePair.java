package net.ck.game.graphics;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class ImagePair
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	public ImagePair(float percentage, BufferedImage sourceImage, BufferedImage resultImage)
	{
		this.percentage = percentage;
		this.sourceImage = sourceImage;
		this.resultImage = resultImage;
	}

	private float percentage;
	private BufferedImage sourceImage;
	private BufferedImage resultImage;

	public BufferedImage getResultImage()
	{
		return resultImage;
	}

	public void setResultImage(BufferedImage resultImage)
	{
		this.resultImage = resultImage;
	}

	public BufferedImage getSourceImage()
	{
		return sourceImage;
	}

	public void setSourceImage(BufferedImage sourceImage)
	{
		this.sourceImage = sourceImage;
	}

	public float getPercentage()
	{
		return percentage;
	}

	public void setPercentage(float percentage)
	{
		this.percentage = percentage;
	}
}

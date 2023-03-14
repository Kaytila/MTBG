package net.ck.game.backend.game;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GameLogs
{
	private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(GameLogs.class));


	private static ArrayList<Long> paintTimes = new ArrayList<Long>();

	private static ArrayList<Long> retrieveBrightImages = new ArrayList();

	private static ArrayList<Long> createBrightImages = new ArrayList();

	public static ArrayList<Long> getPaintTimes()
	{
		return paintTimes;
	}

	public static void setPaintTimes(ArrayList<Long> pT)
	{
		paintTimes = pT;
	}

	public static Long calculateTimeAverage(ArrayList<Long> list)
	{
		long sum = 0;
		int counter = 0;
		for (long re : list)
		{
			counter++;
			sum = sum + re;
		}
		if (counter > 0)
		{
			return Math.floorDiv(sum, counter);
		}
		else
		{
			return (long) 0;
		}
	}

	public static ArrayList<Long> getRetrieveBrightImages()
	{
		return retrieveBrightImages;
	}

	public static void setRetrieveBrightImages(ArrayList<Long> retrieveBrightImages)
	{
		GameLogs.retrieveBrightImages = retrieveBrightImages;
	}

	public static ArrayList<Long> getCreateBrightImages()
	{
		return createBrightImages;
	}

	public static void setCreateBrightImages(ArrayList<Long> createBrightImages)
	{
		GameLogs.createBrightImages = createBrightImages;
	}

	public static void dumpStatistics()
	{
		for (Field field : GameLogs.class.getDeclaredFields())
		{
			logger.info("field: {}", field.toString());
		}
	}

}

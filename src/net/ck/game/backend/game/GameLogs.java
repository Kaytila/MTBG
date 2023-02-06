package net.ck.game.backend.game;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class GameLogs
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(GameLogs.class));


	private static ArrayList<Long> paintTimes = new ArrayList();

	public static ArrayList<Long> getPaintTimes()
	{
		return paintTimes;
	}

	public static void setPaintTimes(ArrayList<Long> pT)
	{
		paintTimes = pT;
	}

	public static Long calculatePaintTimeAverage()
	{
		long sum = 0;
		int counter = 0;
		for (long re : getPaintTimes())
		{
			counter++;
			sum = sum + re;
		}
		return Math.floorDiv(sum, counter);
	}
}

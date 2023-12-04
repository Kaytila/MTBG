package net.ck.mtbg.backend.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class GameLogs
{
	@Getter
	@Setter
	private static ArrayList<Long> paintTimes = new ArrayList<>();

	@Getter
	@Setter
	private static ArrayList<Long> retrieveBrightImages = new ArrayList();

	@Getter
	@Setter
	private static ArrayList<Long> createBrightImages = new ArrayList();


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

	public static void dumpStatistics()
	{
		for (Field field : GameLogs.class.getDeclaredFields())
		{
			logger.info("field: {}", field.toString());
		}
	}

}

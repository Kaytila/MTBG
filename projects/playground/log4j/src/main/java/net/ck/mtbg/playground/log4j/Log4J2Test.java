package net.ck.mtbg.playground.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4J2Test
{

	private static final Logger logger = LogManager.getLogger(Log4J2Test.class);

	public static void main(final String... args)
	{
		//System.setProperty("log4j2.configurationFile", "out/log4j2.xml");
		// Set up a simple configuration that logs on the console.

		logger.trace("Entering application.");
		Bar bar = new Bar();
		if (!bar.doIt())
		{
			logger.error("Didn't do it.");
		}
		logger.trace("Exiting application.");
	}

	public Log4J2Test()
	{
	}
}

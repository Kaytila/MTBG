package net.ck.game.test;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SyncWeatherSystemTest
{
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}
	@BeforeClass
	public static void setUpBeforeClass()
	{
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void test()
	{
		logger.error("Not yet implemented");
	}

}

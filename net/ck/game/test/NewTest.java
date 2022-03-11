package net.ck.game.test;

import org.testng.annotations.Test;

import net.ck.util.TestGameSetup;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;

import static org.junit.Assert.fail;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class NewTest
{
	@Test(dataProvider = "dp")
	public void f(Integer n, String s)
	{
	}
	@BeforeMethod
	public void beforeMethod()
	{
	}

	@AfterMethod
	public void afterMethod()
	{
	}

	@DataProvider
	public Object[][] dp()
	{
		return new Object[][]
		{new Object[]
			{1, "a"}, new Object[]
			{2, "b"},};
	}
	@BeforeClass
	public void beforeClass()
	{
	public static void setUpBeforeClass() throws Exception
	{
		System.out.println("GameTest: setupBeforeClass begin");
		TestGameSetup.SetupGameForTest();
		game = TestGameSetup.getGame();
		gameMap = TestGameSetup.getGameMap();
		System.out.println("GameTest: setupBeforeClass end");
	}
	}

	@AfterClass
	public void afterClass()
	{
		System.out.println("GameTest - shutting down everything hopefully");
		game = null;
		gameMap = null;

		System.out.println("GameTest - finished shutting down");
	}

	@BeforeTest
	public void beforeTest()
	{
	}

	@AfterTest
	public void afterTest()
	{
	}

	@BeforeSuite
	public void beforeSuite()
	{
	}

	@AfterSuite
	public void afterSuite()
	{
	}

	@Test
	public void testMainLoopTenTimes()
	{
		logger.error("testMainLoopTenTimes start");
		try
		{
			for (int i = 0; i < 10; i++)
			{
				game.runTurn();

				for (Thread t : game.getThreadController().getThreads())
				{
					if (t.getName().equalsIgnoreCase("main"))
					{
						t.sleep(1000);
					}
				}
			}
		} catch (Exception e)
		{
			fail("game loop does not work for ten turns!");
		}
		logger.error("testMainLoopTenTimes end");
	}
}

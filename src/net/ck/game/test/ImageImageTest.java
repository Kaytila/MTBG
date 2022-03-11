package net.ck.game.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javafx.stage.Stage;
import net.ck.game.graphics.GraphicsSystem;

public class ImageImageTest
{
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private ImageTester tester;
	private Stage stage;

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
	public static void setUpBeforeClass() throws Exception
	{
		GraphicsSystem system = new GraphicsSystem();
		system.startUp();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{

		logger.error("setup");
		stage = new Stage();
	}

	@After
	public void tearDown() throws Exception
	{
		logger.error("tear down");
	}

	@Test
	public void testDefault()
	{
		logger.error("testing default");
		tester = new ImageTester("https://docs.oracle.com/javafx/javafx/images/javafx-documentation.png");
		tester.run();
		try
		{
			tester.start(stage);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testImage1()
	{
		logger.error("testing 1");
		tester = new ImageTester("file:graphics/image1.png");
		tester.run();
		try
		{
			tester.start(stage);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testImage2()
	{
		logger.error("testing 2");
		tester = new ImageTester("file:graphics/image2.png");
		tester.run();
		try
		{
			tester.start(stage);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

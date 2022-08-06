package net.ck.game.graphics;
/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GraphicsSystem extends Application implements Runnable
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		}
		else
		{
			return getClass();
		}
	}

	public static void main(String[] args)
	{

	}


	public void startUp()
	{
		logger.error("starting graphics system now");
		Platform.startup(this);
		logger.error("Thread {} executing method startup", Thread.currentThread().getName());
	}

	@Override
	public void run()
	{
		logger.error("Thread {} executing method run", Thread.currentThread().getName());

	}

	@Override
	public void start(@SuppressWarnings("exports") Stage arg0)
	{
		logger.error("Thread {} executing method start", Thread.currentThread().getName());

	}
}
*/
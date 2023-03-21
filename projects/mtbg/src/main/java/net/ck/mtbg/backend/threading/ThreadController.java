package net.ck.mtbg.backend.threading;

import net.ck.mtbg.backend.game.Game;
import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * So I want to do proper thread handling:
 * <a href="https://stackoverflow.com/questions/28922040/alternative-to-thread-suspend-and-resume/45786529#45786529">...</a>
 * and
 * <a href="https://stackoverflow.com/questions/28922040/alternative-to-thread-suspend-and-resume">...</a>
 *
 * @author Claus
 */
public class ThreadController
{
	private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(ThreadController.class));
	private static final List<Thread> threads = Collections.synchronizedList(new ArrayList<>(10));
	private static boolean wakeupNeeded;

	public static synchronized void add(Thread thread)
	{
		getThreads().add(thread);
	}

	public static List<Thread> getThreads()
	{
		return threads;
	}

	public static void reanimateAnimation()
	{

		for (Thread t : List.copyOf(getThreads()))
		{
			// logger.info("thread: {}", t.getName());
			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.LIFEFORM_ANIMATION)))
			{
				//t.notify();
				synchronized (Game.getCurrent())
				{
					logger.info("restart animation - animations");
					makeWakeupNeeded();
					Game.getCurrent().notifyAll();
				}

			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.WEATHER_ANIMATION)))
			{
				//t.notify();
				synchronized (Game.getCurrent())
				{
					logger.info("restart animation - weather");
					makeWakeupNeeded();
					Game.getCurrent().notifyAll();
				}

			}


		}
	}
	/*
	 * if (t.getName().equalsIgnoreCase("Weather System Thread")) { try {
	 * logger.info("restart weather"); t.notify(); } catch
	 * (IllegalMonitorStateException e2) {
	 * logger.error("caught IllegalMonitorStateException"); } }
	 */

	private static void makeWakeupNeeded()
	{
		wakeupNeeded = true;
	}

	public static void suspendAnimation()
	{

		// logger.error("iconified");

		/*
		 * try { game.getLock().wait(); } catch (InterruptedException e1) { //
		 *
		 */
		for (Thread t : List.copyOf(getThreads()))
		{
			// logger.info("thread: {}", t.getName());
			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.LIFEFORM_ANIMATION)))
			{
				synchronized (Game.getCurrent())
				{
					while (!isWakeupNeeded())
					{
						try
						{
							Game.getCurrent().wait();
							logger.info("suspend animation");
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}

					}

					// t.wait();

				}
				/*
				 * if (t.getName().equalsIgnoreCase("Weather System Thread")) {
				 * try { logger.info("suspend weather thread"); t.wait(); }
				 * catch (InterruptedException e1) { catch block
				 * e1.printStackTrace(); } catch (IllegalMonitorStateException
				 * e2) { // e2.printStackTrace();
				 * logger.error("caught IllegalMonitorStateException"); } }
				 */
			}
			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.WEATHER_ANIMATION)))
			{
				synchronized (Game.getCurrent())
				{
					while (!isWakeupNeeded())
					{
						try
						{
							Game.getCurrent().wait();
							logger.info("suspend weather");
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}

					}

					// t.wait();
				}
			}

		}
	}

	private static boolean isWakeupNeeded()
	{
		return wakeupNeeded;
	}


	public static void sleep(int ms, ThreadNames threadName)
	{
		for (Thread t : List.copyOf(getThreads()))
		{
			if (t.getName().equalsIgnoreCase(String.valueOf(threadName)))
			{
				try
				{
					t.sleep(ms);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void startThreads()
	{
		for (Thread t : List.copyOf(getThreads()))
		{
			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.LIFEFORM_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
				continue;
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.FOREGROUND_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
				continue;
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.BACKGROUND_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
				continue;
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.SOUND_SYSTEM)))
			{
				logger.info("starting thread: {}", t);
				t.start();
				continue;
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.WEATHER_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
				continue;
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.GAME_THREAD)))
			{
				logger.info("starting thread: {}", t);
				t.start();
				continue;
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.MISSILE)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}
		}
	}

	public static void listThreads()
	{
		for (Thread t : getThreads())
		{
			logger.info("Thread running: {}, priority: {}, state: {}", t.getName(), t.getPriority(), t.getState());
		}
	}
}

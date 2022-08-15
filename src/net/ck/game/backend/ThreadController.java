package net.ck.game.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.*;

/**
 *  So I want to do proper thread handling:
 *  https://stackoverflow.com/questions/28922040/alternative-to-thread-suspend-and-resume/45786529#45786529
 *  and
 *  https://stackoverflow.com/questions/28922040/alternative-to-thread-suspend-and-resume
 * @author Claus
 *
 */
public class ThreadController implements Runnable
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
	}

	private volatile boolean exit = false;
	private boolean wakeupNeeded;
	private List<Thread> threads;

	public ThreadController()
	{
		threads = Collections.synchronizedList(new ArrayList<>());
	}


	public synchronized void add(Thread thread)
	{
		getThreads().add(thread);
	}

	public List<Thread> getThreads()
	{
		return threads;
	}

	@Override
	public void run()
	{
		while (!exit)
		{
			logger.debug("running running");
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		for (Thread t : getThreads())
		{
			// Todo how the heck is this supposed to work?
			logger.debug("shutdown: " + t.getName());
			t.interrupt();

		}
	}

	public void reanimateAnimation()
	{

		for (Thread t : List.copyOf(getThreads()))
		{
			// logger.info("thread: {}", t.getName());
			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.LIFEFORM_ANIMATION)))
			{
				//t.notify();
				synchronized (Game.getCurrent().getLock())
				{
					logger.info("restart animation - animations");
					makeWakeupNeeded();
					Game.getCurrent().getLock().notifyAll();
				}

			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.WEATHER_ANIMATION)))
			{
				//t.notify();
				synchronized (Game.getCurrent().getLock())
				{
					logger.info("restart animation - weather");
					makeWakeupNeeded();
					Game.getCurrent().getLock().notifyAll();
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

	private void makeWakeupNeeded()
	{
		wakeupNeeded = true;
	}

	public void suspendAnimation()
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
				synchronized (Game.getCurrent().getLock())
				{
					while (!isWakeupNeeded())
					{
						try
						{
							Game.getCurrent().getLock().wait();
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
				synchronized (Game.getCurrent().getLock())
				{
					while (!isWakeupNeeded())
					{
						try
						{
							Game.getCurrent().getLock().wait();
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

	private boolean isWakeupNeeded()
	{
		return wakeupNeeded;
	}

	@SuppressWarnings("static-access")
	public void sleep(int ms, ThreadNames threadName)
	{
		for (Thread t : List.copyOf(getThreads()))
		{
			if (t.getName().equalsIgnoreCase(String.valueOf(threadName)))
			{
				try
				{
					t.sleep(ms);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void startThreads()
	{
		for (Thread t : List.copyOf(getThreads()))
		{
			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.LIFEFORM_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.FOREGROUND_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.BACKGROUND_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.SOUND_SYSTEM)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.WEATHER_ANIMATION)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.GAME_THREAD)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}

			if (t.getName().equalsIgnoreCase(String.valueOf(ThreadNames.MISSILE)))
			{
				logger.info("starting thread: {}", t);
				t.start();
			}
		}
	}

	public void listThreads ()
	{
		for (Thread t : getThreads())
		{
			logger.info("Thread running: {}, priority: {}, state: {}", t.getName(), t.getPriority(), t.getState());
		}
	}

}

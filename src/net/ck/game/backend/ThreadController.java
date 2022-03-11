package net.ck.game.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

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
	private Game game;

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

	private volatile boolean exit = false;
	private boolean wakeupNeeded;
	private List<Thread> threads;

	public ThreadController(Game game)
	{
		setGame(game);
		threads = Collections.synchronizedList(new ArrayList<Thread>());
	}

	public Game getGame()
	{
		return this.game;
	}

	public void setGame(Game game)
	{
		this.game = game;
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

	public void setThreads(ArrayList<Thread> threads)
	{
		this.threads = threads;
	}

	public void reanimateAnimation()
	{

		for (Iterator<Thread> threadIterator = getThreads().iterator(); threadIterator.hasNext();)
		{
			Thread t = threadIterator.next();
			// logger.info("thread: {}", t.getName());
			if (t.getName().equalsIgnoreCase("Animation System Thread"))
			{		
				//t.notify();
				synchronized (getGame().getLock())
				{
					logger.info("restart animation - animations");
					makeWakeupNeeded();
					getGame().getLock().notifyAll();
				}

			}
			
			if (t.getName().equalsIgnoreCase("Weather System Thread"))
			{		
				//t.notify();
				synchronized (getGame().getLock())
				{
					logger.info("restart animation - weather");
					makeWakeupNeeded();
					getGame().getLock().notifyAll();
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
		for (Iterator<Thread> threadIterator = getThreads().iterator(); threadIterator.hasNext();)
		{
			Thread t = threadIterator.next();
			// logger.info("thread: {}", t.getName());
			if (t.getName().equalsIgnoreCase("Animation System Thread"))
			{
				synchronized (getGame().getLock())
				{
					while (!isWakeupNeeded())
					{
						try
						{
							getGame().getLock().wait();
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
			if (t.getName().equalsIgnoreCase("Weather System Thread"))
			{
				synchronized (getGame().getLock())
				{
					while (!isWakeupNeeded())
					{
						try
						{
							getGame().getLock().wait();
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
	public void sleep(int ms, String threadName) throws Exception
	{
		for (Iterator<Thread> threadIterator = getThreads().iterator(); threadIterator.hasNext();)
		{
			Thread t = threadIterator.next();
			if (t.getName().equalsIgnoreCase(threadName))
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
}

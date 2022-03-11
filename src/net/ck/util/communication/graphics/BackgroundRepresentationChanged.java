package net.ck.util.communication.graphics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackgroundRepresentationChanged extends ChangedEvent
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	private int currentNumber;
	
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

	public Logger getLogger()
	{
		return logger;
	}
	
	public BackgroundRepresentationChanged(int i)
	{
		setCurrentNumber(i);
	}

	public int getCurrentNumber()
	{
		return currentNumber;
	}

	public void setCurrentNumber(int currentNumber)
	{
		this.currentNumber = currentNumber;
	}
}

package net.ck.util.communication.graphics;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackgroundRepresentationChanged extends ChangedEvent
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private int currentNumber;

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

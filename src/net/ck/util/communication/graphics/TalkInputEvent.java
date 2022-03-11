package net.ck.util.communication.graphics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TalkInputEvent extends ChangedEvent
{

	private static final long serialVersionUID = 1L;
	private String contents;
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public TalkInputEvent(String contents)
	{
		super();
		this.setContents(contents);
	}

	public String getContents()
	{
		return contents;
	}

	public Logger getLogger()
	{
		return logger;
	}

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

	public void setContents(String contents)
	{
		this.contents = contents;
	}

}

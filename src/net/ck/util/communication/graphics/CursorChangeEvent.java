package net.ck.util.communication.graphics;

import java.awt.Cursor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CursorChangeEvent extends ChangedEvent
{
	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());
	private Cursor cursor; 
	
	public CursorChangeEvent(Cursor cursor)
	{
		super();
		setCursor(cursor);
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

	public Logger getLogger()
	{
		return logger;
	}

	public Cursor getCursor()
	{
		return cursor;
	}

	public void setCursor(Cursor cursor)
	{
		this.cursor = cursor;
	}
}

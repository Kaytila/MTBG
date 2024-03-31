package net.ck.mtbg.util.communication.graphics;

import net.ck.mtbg.util.utils.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class CursorChangeEvent extends ChangedEvent
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private Cursor cursor; 
	
	public CursorChangeEvent(Cursor cursor)
	{
		super();
		setCursor(cursor);
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

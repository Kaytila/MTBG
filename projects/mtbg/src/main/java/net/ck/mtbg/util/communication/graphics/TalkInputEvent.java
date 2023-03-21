package net.ck.mtbg.util.communication.graphics;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TalkInputEvent extends ChangedEvent
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
	private String contents;


	public TalkInputEvent(String contents)
	{
		super();
		this.setContents(contents);
	}

	public String getContents()
	{
		return contents;
	}


	public void setContents(String contents)
	{
		this.contents = contents;
	}

}

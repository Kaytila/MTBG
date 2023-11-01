package net.ck.mtbg.map;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Message
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private String description;
	private boolean repeat;
	private MessageTypes messageType;

	public Logger getLogger()
	{
		return logger;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isRepeat()
	{
		return repeat;
	}

	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
	}

	public MessageTypes getMessageType()
	{
		return messageType;
	}

	public void setMessageType(MessageTypes messageType)
	{
		this.messageType = messageType;
	}
}

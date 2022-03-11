package net.ck.game.backend.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constitution extends AbstractAttribute
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

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

	public Constitution()
	{
		
	}

	public AttributeTypes getType()
	{
		return AttributeTypes.CONSTITUTION;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(getValue());
	}

	@Override
	public int getValue()
	{
		return value;
	}
}

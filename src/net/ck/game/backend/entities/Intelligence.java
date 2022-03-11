package net.ck.game.backend.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Intelligence extends AbstractAttribute
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

	public Intelligence()
	{

	}

	public AttributeTypes getType()
	{
		return AttributeTypes.INTELLIGENCE;
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

package net.ck.game.backend.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Constitution extends AbstractAttribute
{

	private final Logger logger = LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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

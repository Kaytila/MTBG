package net.ck.mtbg.backend.entities.attributes;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Intelligence extends AbstractAttribute
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

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

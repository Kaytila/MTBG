package net.ck.mtbg.backend.entities.attributes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Setter
@Getter
public class Intelligence extends AbstractAttribute
{
	public Intelligence()
	{
		value = 0;
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
}

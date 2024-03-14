package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;

/**
 * Abstract Map, to define the most basic properties of a map extension parent
 * children
 *
 * @author Claus
 */

@Log4j2
@Getter
@Setter
public class AbstractMap implements Serializable
{
	public AbstractMap()
	{
		
	}
}

package net.ck.mtbg.backend.actions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;

/**
 * random action is something world is supposed to do.
 * @author Claus
 *
 */
@Log4j2
@Getter
@Setter
public class RandomAction extends AbstractAction
{
	public RandomAction()
	{
		super();

	}

	public KeyboardActionType getType()
	{
		return KeyboardActionType.NULL;
	}
}

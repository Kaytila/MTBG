package net.ck.mtbg.backend.actions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.game.Result;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;
import net.ck.mtbg.util.communication.keyboard.KeyboardActionType;


@Log4j2
@Getter
@Setter
@ToString
public abstract class AbstractAction
{
	private Result result;
	private String title;


	private boolean success;

	/**
	 * who does the action?
	 */
	private LifeForm entity;
	
	/**
	 * what type of event is it?
	 * this probably needs to go somewhere else. 
	 */
	private AbstractKeyboardAction event;


	public abstract KeyboardActionType getType();

}

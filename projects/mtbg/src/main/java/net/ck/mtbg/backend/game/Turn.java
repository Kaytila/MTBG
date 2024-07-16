package net.ck.mtbg.backend.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.GameState;

import java.io.Serializable;

/**
 * so  a turn is a turn.
 * A turn is downgraded to just storing a number (turn counter) and the game state of that turn in order to make sure
 * we are able to react to things after n turns of a certain game state.
 * First case: when three turns have gone and victory music is still running, switch music.
 *
 * @author Claus
 */
@Log4j2
@Getter
@Setter
@ToString
public class Turn implements Serializable
{
	private int turnNumber;

	private GameState gameState;

	public Turn(int turnNumber)
	{
		setTurnNumber(turnNumber);
	}
}

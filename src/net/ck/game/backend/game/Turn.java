package net.ck.game.backend.game;

import net.ck.game.backend.state.GameState;
import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * so  a turn is a turn.
 * A turn is downgraded to just storing a number (turn counter) and the game state of that turn in order to make sure
 * we are able to react to things after n turns of a certain game state.
 * First case: when three turns have gone and victory music is still running, switch music.
 *
 * @author Claus
 */
public class Turn
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private int turnNumber;

	private GameState gameState;


	public Turn(int turnNumber)
	{
		setTurnNumber(turnNumber);
	}



	public int getTurnNumber()
	{
		return turnNumber;
	}


	public void setTurnNumber(int turnNumber)
	{
		this.turnNumber = turnNumber;
	}

	public GameState getGameState()
	{
		return gameState;
	}

	public void setGameState(GameState gameState)
	{
		this.gameState = gameState;
	}
}

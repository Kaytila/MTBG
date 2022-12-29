package net.ck.util.communication.sound;

import net.ck.game.backend.state.GameState;
import net.ck.util.CodeUtils;
import net.ck.util.communication.graphics.ChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameStateChanged extends ChangedEvent
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    private GameState gameState;


    public GameStateChanged(GameState gameState)
    {
        logger.info("Gamestate changes: {}", gameState);
        setGameState(gameState);
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

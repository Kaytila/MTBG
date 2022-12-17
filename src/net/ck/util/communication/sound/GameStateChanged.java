package net.ck.util.communication.sound;

import net.ck.game.backend.state.GameState;
import net.ck.util.communication.graphics.ChangedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class GameStateChanged extends ChangedEvent
{

    private GameState gameState;

    private final Logger logger = LogManager.getLogger(getRealClass());

    public GameStateChanged(GameState gameState)
    {
        logger.info("Gamestate changes: {}", gameState);
        setGameState(gameState);
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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

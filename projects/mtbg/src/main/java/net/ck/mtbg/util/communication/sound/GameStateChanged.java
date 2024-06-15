package net.ck.mtbg.util.communication.sound;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.GameState;
import net.ck.mtbg.util.communication.graphics.ChangedEvent;

@Getter
@Setter
@Log4j2
public class GameStateChanged extends ChangedEvent
{
    private GameState gameState;


    public GameStateChanged(GameState gameState)
    {
        logger.info("Gamestate changes: {}", gameState);
        setGameState(gameState);
    }
}

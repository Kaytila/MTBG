package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.communication.sound.GameStateChanged;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * static StateMachine for game state
 */
@Log4j2
@Getter
@Setter
@ToString
public class GameStateMachine
{
    private static final GameStateMachine gameStateMachine = new GameStateMachine();
    private  GameState currentState;
    private  GameState previousState;

    private  boolean registered;

    public GameStateMachine()
    {
        if (!registered)
        {
            EventBus.getDefault().register(this);
            registered = true;
        }
    }

    @Subscribe
    public void onMessageEvent(GameStateChanged gameStateChanged)
    {
        if (GameConfiguration.debugGameState == true)
        {
            logger.debug("setting game state to: {}", gameStateChanged.getGameState());
        }
        setCurrentState(gameStateChanged.getGameState());
    }

    public static GameStateMachine getCurrent()
    {
        return gameStateMachine;
    }
}

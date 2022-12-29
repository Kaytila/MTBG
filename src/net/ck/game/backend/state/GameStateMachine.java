package net.ck.game.backend.state;

import net.ck.util.CodeUtils;
import net.ck.util.communication.sound.GameStateChanged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * static StateMachine for game state
 */
public class GameStateMachine
{
    private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(GameStateMachine.class));
    private static final GameStateMachine gameStateMachine = new GameStateMachine();
    private  GameState currentState;
    private  GameState previousState;

    private  boolean registered;

    public  GameState getPreviousState()
    {
        return previousState;
    }

    public  void setPreviousState(GameState previousState)
    {
        this.previousState = previousState;
    }


    public GameStateMachine()
    {
        if (!registered)
        {
            EventBus.getDefault().register(this);
            registered = true;
        }
    }

    public  GameState getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(GameState state)
    {
        this.currentState = state;
    }

    @Subscribe
    public void onMessageEvent(GameStateChanged gameStateChanged)
    {
        logger.info("setting game state to: {}", gameStateChanged.getGameState());
        setCurrentState(gameStateChanged.getGameState());
//        if (gameState.getGameState() == GameState.COMBAT)
//        {
//            logger.info("setting previous game state to: {}", Game.getCurrent().getGameState());
//            setPreviousGameState(Game.getCurrent().getGameState());
//        }
//        else if (gameState.getGameState() == GameState.VICTORY)
//        {
//            logger.info("victory, previous game state stays at: {}", Game.getCurrent().getPreviousGameState());
//        }
//        else
//        {
//            if (gameState.getGameState() != Game.getCurrent().getGameState())
//            {
//                Game.getCurrent().setGameState(gameState.getGameState());
//            }
//        }
    }

    public static GameStateMachine getCurrent()
    {
        return gameStateMachine;
    }

}

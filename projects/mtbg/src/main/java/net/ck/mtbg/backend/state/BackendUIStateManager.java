package net.ck.mtbg.backend.state;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.state.UIState;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Backend UI State Manager
 * Manages whether UI is open/active from backend perspective
 * This is used to decouple backend logic (animations, events) from UIStateMachine
 * allowing the backend to run without UI and be testable independently
 */
@Log4j2
public class BackendUIStateManager
{
    /**
     * Is the UI currently open/active?
     * This is a backend counterpart to UIStateMachine.isUiOpen()
     * but without the actual UI dependency
     */
    private static final AtomicBoolean uiActive = new AtomicBoolean(false);
    private static final AtomicReference<GameMode> gameMode = new AtomicReference<>(GameMode.UI_PAUSED);

    /**
     * Check if UI is active/open from backend perspective
     */
    public static boolean isUIActive()
    {
        return uiActive.get();
    }

    /**
     * Set UI active state from backend perspective
     */
    public static void setUIActive(boolean active)
    {
        uiActive.set(active);
    }

    public static GameMode getGameMode()
    {
        return gameMode.get();
    }

    public static void setGameMode(GameMode mode)
    {
        if (mode != null)
        {
            gameMode.set(mode);
        }
    }

    public static boolean isIdleTimerAllowed()
    {
        return gameMode.get() == GameMode.GAMEPLAY;
    }

    public static void applyUiState(UIState uiState)
    {
        if (uiState == null)
        {
            return;
        }

        switch (uiState)
        {
            case OPENED:
            case ACTIVATED:
            case DEICONIFIED:
                setGameMode(GameMode.GAMEPLAY);
                break;
            case OVERLAY:
                setGameMode(GameMode.OVERLAY);
                break;
            case CUTSCENE:
                setGameMode(GameMode.CUTSCENE);
                break;
            case DEACTIVATED:
            case ICONIFIED:
                setGameMode(GameMode.UI_PAUSED);
                break;
            case TITLE:
            case CHARACTEREDITOR:
            case CREDITS:
            case OPTIONS:
                setGameMode(GameMode.TITLE);
                break;
            case CLOSED:
                setGameMode(GameMode.CLOSED);
                break;
            default:
                break;
        }
    }

    /**
     * Initialize backend UI state (usually called when UI is created)
     */
    public static void initialize()
    {
        uiActive.set(false);
        gameMode.set(GameMode.UI_PAUSED);
    }
}


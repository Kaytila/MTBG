package net.ck.mtbg.backend.state;

import net.ck.mtbg.ui.state.UIState;

/**
 * Backend state-machine facade for game mode handling.
 * Delegates to {@link BackendUIStateManager} so there is one canonical state source.
 */
public final class GameModeStateMachine
{
    private GameModeStateMachine()
    {
    }

    public static GameMode getCurrentMode()
    {
        return BackendUIStateManager.getGameMode();
    }

    public static void setCurrentMode(GameMode mode)
    {
        BackendUIStateManager.setGameMode(mode);
    }

    public static boolean isIdleTimerAllowed()
    {
        return BackendUIStateManager.isIdleTimerAllowed();
    }

    public static void applyUiState(UIState uiState)
    {
        BackendUIStateManager.applyUiState(uiState);
    }
}


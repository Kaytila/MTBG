package net.ck.mtbg.backend.state;

import net.ck.mtbg.ui.state.UIState;

/**
 * Compatibility manager for game mode transitions.
 * Delegates to {@link BackendUIStateManager}.
 */
public final class GameModeManager
{
    private GameModeManager()
    {
    }

    public static GameMode getMode()
    {
        return BackendUIStateManager.getGameMode();
    }

    public static void setMode(GameMode mode)
    {
        BackendUIStateManager.setGameMode(mode);
    }

    public static void applyUiState(UIState uiState)
    {
        BackendUIStateManager.applyUiState(uiState);
    }

    public static boolean isGameplayMode()
    {
        return BackendUIStateManager.getGameMode() == GameMode.GAMEPLAY;
    }
}


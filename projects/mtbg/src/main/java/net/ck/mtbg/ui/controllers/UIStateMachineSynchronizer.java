package net.ck.mtbg.ui.controllers;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.BackendUIStateManager;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;

/**
 * UIStateMachine Synchronizer
 * Ensures that BackendUIStateManager stays synchronized with UIStateMachine
 * This bridge allows backend code to remain independent of UI while still
 * responding to UI state changes
 * <p>
 * Should be called by UI controller when UI state changes
 */
@Log4j2
public class UIStateMachineSynchronizer
{
    /**
     * Synchronize backend UI state from UIStateMachine state
     * Call this whenever UIStateMachine state changes
     */
    public static void synchronizeBackendUIState()
    {
        boolean isActive = false;

        UIState currentState = UIStateMachine.getUiState();
        if (currentState != null)
        {
            isActive = (currentState.equals(UIState.OPENED) || currentState.equals(UIState.ACTIVATED));
        }

        BackendUIStateManager.setUIActive(isActive);
    }

    /**
     * Initialize synchronization (usually called at application startup)
     */
    public static void initialize()
    {
        BackendUIStateManager.initialize();
        synchronizeBackendUIState();
    }
}


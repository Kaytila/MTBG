package net.ck.mtbg.ui.adapters;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.application.port.UIStatePort;
import net.ck.mtbg.backend.state.GameMode;
import net.ck.mtbg.ui.state.UIState;
import net.ck.mtbg.ui.state.UIStateMachine;

@Getter
@Setter
@Log4j2
public final class UIStateMachineAdapter implements UIStatePort
{
    @Override
    public GameMode getMode()
    {
        UIState uiState = UIStateMachine.getUiState();
        if (uiState == null)
        {
            return GameMode.UI_PAUSED;
        }

        switch (uiState)
        {
            case OPENED:
            case ACTIVATED:
            case DEICONIFIED:
                return GameMode.GAMEPLAY;
            case OVERLAY:
                return GameMode.OVERLAY;
            case CUTSCENE:
                return GameMode.CUTSCENE;
            case DEACTIVATED:
            case ICONIFIED:
                return GameMode.UI_PAUSED;
            case TITLE:
            case CHARACTEREDITOR:
            case CREDITS:
            case OPTIONS:
                return GameMode.TITLE;
            case CLOSED:
                return GameMode.CLOSED;
            default:
                return GameMode.UI_PAUSED;
        }
    }

    @Override
    public void setMode(GameMode mode)
    {
        if (mode == null)
        {
            return;
        }

        switch (mode)
        {
            case GAMEPLAY:
                UIStateMachine.setUiState(UIState.OPENED);
                break;
            case OVERLAY:
                UIStateMachine.setUiState(UIState.OVERLAY);
                break;
            case CUTSCENE:
                UIStateMachine.setUiState(UIState.CUTSCENE);
                break;
            case UI_PAUSED:
                UIStateMachine.setUiState(UIState.ICONIFIED);
                break;
            case TITLE:
                UIStateMachine.setUiState(UIState.TITLE);
                break;
            case CLOSED:
                UIStateMachine.setUiState(UIState.CLOSED);
                break;
            case DIALOG:
                UIStateMachine.setUiState(UIState.OPENED);
                break;
        }
    }

    @Override
    public boolean isUiActive()
    {
        UIState uiState = UIStateMachine.getUiState();
        return uiState == UIState.OPENED
                || uiState == UIState.ACTIVATED
                || uiState == UIState.DEICONIFIED;
    }
}
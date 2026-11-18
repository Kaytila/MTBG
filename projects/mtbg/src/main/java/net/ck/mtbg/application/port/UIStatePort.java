package net.ck.mtbg.application.port;

import net.ck.mtbg.backend.state.GameMode;

public interface UIStatePort
{
    GameMode getMode();

    void setMode(GameMode mode);

    boolean isUiActive();
}

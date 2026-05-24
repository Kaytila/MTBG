package net.ck.mtbg.backend.state;

/**
 * Backend game mode used for timer/input gating independent of UI internals.
 */
public enum GameMode
{
    GAMEPLAY,
    DIALOG,
    OVERLAY,
    CUTSCENE,
    UI_PAUSED,
    TITLE,
    CLOSED
}


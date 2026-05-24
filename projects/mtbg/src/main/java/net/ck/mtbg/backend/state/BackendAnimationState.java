package net.ck.mtbg.backend.state;

/**
 * Backend Animation State - tracks animation states independent of UI
 * This is used to decouple animation logic from UIStateMachine
 */
public enum BackendAnimationState
{
    /**
     * No animation is running
     */
    IDLE,
    /**
     * Hit/Miss animation is running after combat action
     */
    HIT_MISS_ANIMATION,
    /**
     * General animation is running
     */
    ANIMATING
}


package net.ck.mtbg.backend.state;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Backend Animation State Manager
 * Manages animation states independent of UI (UIStateMachine)
 * Replaces UIStateMachine usage in battle/animation logic
 */
@Log4j2
public class AnimationStateManager
{
    /**
     * Current backend animation state
     */
    private static final AtomicReference<BackendAnimationState> currentAnimationState =
            new AtomicReference<>(BackendAnimationState.IDLE);

    /**
     * Get current backend animation state
     */
    public static BackendAnimationState getAnimationState()
    {
        return currentAnimationState.get();
    }

    /**
     * Set new backend animation state
     */
    public static void setAnimationState(BackendAnimationState state)
    {
        if (state != null)
        {
            currentAnimationState.set(state);
        }
    }

    /**
     * Check if any animation is running (not IDLE)
     */
    public static boolean isAnimating()
    {
        return !currentAnimationState.get().equals(BackendAnimationState.IDLE);
    }

    /**
     * Check if hit/miss animation is running
     */
    public static boolean isHitMissAnimationRunning()
    {
        return currentAnimationState.get().equals(BackendAnimationState.HIT_MISS_ANIMATION);
    }

    /**
     * Set hit/miss animation running state
     */
    public static void setHitMissAnimationRunning(boolean running)
    {
        if (running)
        {
            setAnimationState(BackendAnimationState.HIT_MISS_ANIMATION);
        }
        else
        {
            setAnimationState(BackendAnimationState.IDLE);
        }
    }

    /**
     * Reset animation state to IDLE
     */
    public static void resetAnimationState()
    {
        currentAnimationState.set(BackendAnimationState.IDLE);
    }
}


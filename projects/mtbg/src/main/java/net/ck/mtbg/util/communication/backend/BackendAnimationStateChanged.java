package net.ck.mtbg.util.communication.backend;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.state.BackendAnimationState;
import net.ck.mtbg.util.communication.graphics.ChangedEvent;

/**
 * Backend Animation State Changed Event
 * Posted when backend animation states change, independent of UI state
 * This allows decoupling of animation logic from UIStateMachine
 */
@Getter
@Setter
@Log4j2
@ToString
public class BackendAnimationStateChanged extends ChangedEvent
{
    private BackendAnimationState animationState;

    public BackendAnimationStateChanged(BackendAnimationState animationState)
    {
        this.animationState = animationState;
    }
}


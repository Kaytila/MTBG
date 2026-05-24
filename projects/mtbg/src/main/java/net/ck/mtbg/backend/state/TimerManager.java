package net.ck.mtbg.backend.state;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.animation.background.BackgroundAnimationSystemTimer;
import net.ck.mtbg.animation.foreground.ForegroundAnimationSystemTimer;
import net.ck.mtbg.animation.lifeform.AnimationSystemTimer;
import net.ck.mtbg.animation.lifeform.AnimationSystemUtilTimer;
import net.ck.mtbg.animation.lifeform.HitMissImageTimer;
import net.ck.mtbg.animation.missile.MissileTimer;
import net.ck.mtbg.animation.missile.MissileUtilTimer;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.time.IdleTimer;
import net.ck.mtbg.backend.time.QuequeTimer;
import net.ck.mtbg.music.MusicTimer;
import net.ck.mtbg.ui.highlighting.HighlightTimer;
import net.ck.mtbg.util.ui.RenderClock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * as ObjectOutputStream so eloquently tells me, the Timers need to go out of game and into a separate class where they
 * are statically referenced
 */
@Log4j2
public class TimerManager
{
    @Getter
    private static final AtomicBoolean missileInFlight = new AtomicBoolean(false);

    private static final AtomicBoolean hitMissInFlight = new AtomicBoolean(false);

    /**
     * how many milliseconds until the turn is passed?
     */
    @Getter
    @Setter
    private static IdleTimer idleTimer;
    /**
     * how many milliseconds until the turn is passed?
     */
    @Getter
    @Setter
    private static AnimationSystemTimer animationSystemTimer;
    /**
     * how much time to switch from victory back to world music
     */
    @Getter
    @Setter
    private static MusicTimer musicTimer;
    /**
     * how long until the highlighting frame ticks
     */
    @Getter
    @Setter
    private static HighlightTimer highlightTimer;
    /**
     *
     */
    @Getter
    @Setter
    private static ForegroundAnimationSystemTimer foregroundAnimationSystemTimer;
    /**
     *
     */
    @Getter
    @Setter
    private static BackgroundAnimationSystemTimer backgroundAnimationSystemTimer;
    /**
     * make missile timer use an util timer that does not run on
     * Event Dispatch Thread for not blocking the UI
     */
    @Getter
    @Setter
    private static MissileUtilTimer missileUtilTimer;
    /**
     * so how long is the time between movements being run from the command queue?
     */
    @Getter
    @Setter
    private static QuequeTimer quequeTimer;
    /**
     * how long is the time period between missiles being drawn on the map?
     */
    @Getter
    @Setter
    private static MissileTimer missileTimer;
    /**
     * make animation system timer use an util timer that does not run on
     * Event Dispatch Thread for not blocking the UI
     */
    @Getter
    @Setter
    private static AnimationSystemUtilTimer animationSystemUtilTimer;
    /**
     * make an UTIL timer for hit or miss to make sure its visible a bit before overwritten by
     * the next animation image
     */
    @Getter
    @Setter
    private static HitMissImageTimer hitMissImageTimer;
    @Getter
    @Setter
    private static RenderClock renderClock;

    public static boolean isMissileInFlight()
    {
        return missileInFlight.get();
    }

    public static void setMissileInFlight(boolean inFlight)
    {
        missileInFlight.set(inFlight);
    }

    public static boolean isHitMissInFlight()
    {
        return hitMissInFlight.get();
    }

    public static void setHitMissInFlight(boolean inFlight)
    {
        hitMissInFlight.set(inFlight);
    }

    /**
     * Zentraler Guard fuer Keyboard/Maus
     */
    public static boolean isInputBlocked()
    {
        return missileInFlight.get() || hitMissInFlight.get();
    }

    public static boolean isPlayerMovementInProgress()
    {
        if (Game.getCurrent() == null || Game.getCurrent().getCurrentPlayer() == null)
        {
            return false;
        }

        boolean queueTimerRunning = getQuequeTimer() != null && getQuequeTimer().isRunning();
        boolean queuedPlayerSteps = Game.getCurrent().getCurrentPlayer().getQueuedActions() != null
                && !Game.getCurrent().getCurrentPlayer().getQueuedActions().isEmpty();

        return queueTimerRunning || queuedPlayerSteps;
    }
}

package net.ck.game.backend.state;

import net.ck.game.animation.background.BackgroundAnimationSystemTimer;
import net.ck.game.animation.foreground.ForegroundAnimationSystemTimer;
import net.ck.game.animation.lifeform.AnimationSystemTimer;
import net.ck.game.animation.lifeform.AnimationSystemUtilTimer;
import net.ck.game.animation.lifeform.HitMissImageTimer;
import net.ck.game.animation.missile.MissileTimer;
import net.ck.game.animation.missile.MissileUtilTimer;
import net.ck.game.backend.time.IdleTimer;
import net.ck.game.backend.time.QuequeTimer;
import net.ck.game.music.MusicTimer;
import net.ck.game.ui.highlighting.HighlightTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * as ObjectOutputStream so eloquently tells me, the Timers need to go out of game and into a separate class where they
 * are statically referenced
 */
public class TimerManager
{


	private static final Logger logger = LogManager.getLogger(TimerManager.class);
	/**
	 * how many milliseconds until the turn is passed?
	 */
	private static IdleTimer idleTimer;
	/**
	 * how many milliseconds until the turn is passed?
	 */
	private static AnimationSystemTimer animationSystemTimer;
	/**
	 * how much time to switch from victory back to world music
	 */
	private static MusicTimer musicTimer;
	/**
	 * how long until the highlighting frame ticks
	 */
	private static HighlightTimer highlightTimer;
	/**
	 *
	 */
	private static ForegroundAnimationSystemTimer foregroundAnimationSystemTimer;
	/**
	 *
	 */
	private static BackgroundAnimationSystemTimer backgroundAnimationSystemTimer;
	/**
	 * make missile timer use an util timer that does not run on
	 * Event Dispatch Thread for not blocking the UI
	 */
	private static MissileUtilTimer missileUtilTimer;
	/**
	 * so how long is the time between movements being run from the command queue?
	 */
	private static QuequeTimer quequeTimer;
	/**
	 * how long is the time period between missiles being drawn on the map?
	 */
	private static MissileTimer missileTimer;
	/**
	 * make animation system timer use an util timer that does not run on
	 * Event Dispatch Thread for not blocking the UI
	 */
	private static AnimationSystemUtilTimer animationSystemUtilTimer;
	/**
	 * make an UTIL timer for hit or miss to make sure its visible a bit before overwritten by
	 * the next animation image
	 */
	private static HitMissImageTimer hitMissImageTimer;

	public static IdleTimer getIdleTimer()
	{
		return idleTimer;
	}

	public static void setIdleTimer(IdleTimer idleTimer)
	{
		TimerManager.idleTimer = idleTimer;
	}

	public static AnimationSystemTimer getAnimationSystemTimer()
	{
		return animationSystemTimer;
	}

	public static void setAnimationSystemTimer(AnimationSystemTimer animationSystemTimer)
	{
		TimerManager.animationSystemTimer = animationSystemTimer;
	}

	public static MusicTimer getMusicTimer()
	{
		return musicTimer;
	}

	public static void setMusicTimer(MusicTimer musicTimer)
	{
		TimerManager.musicTimer = musicTimer;
	}

	public static HighlightTimer getHighlightTimer()
	{
		return highlightTimer;
	}

	public static void setHighlightTimer(HighlightTimer highlightTimer)
	{
		TimerManager.highlightTimer = highlightTimer;
	}

	public static ForegroundAnimationSystemTimer getForegroundAnimationSystemTimer()
	{
		return foregroundAnimationSystemTimer;
	}

	public static void setForegroundAnimationSystemTimer(ForegroundAnimationSystemTimer foregroundAnimationSystemTimer)
	{
		TimerManager.foregroundAnimationSystemTimer = foregroundAnimationSystemTimer;
	}

	public static BackgroundAnimationSystemTimer getBackgroundAnimationSystemTimer()
	{
		return backgroundAnimationSystemTimer;
	}

	public static void setBackgroundAnimationSystemTimer(BackgroundAnimationSystemTimer backgroundAnimationSystemTimer)
	{
		TimerManager.backgroundAnimationSystemTimer = backgroundAnimationSystemTimer;
	}

	public static MissileUtilTimer getMissileUtilTimer()
	{
		return missileUtilTimer;
	}

	public static void setMissileUtilTimer(MissileUtilTimer missileUtilTimer)
	{
		TimerManager.missileUtilTimer = missileUtilTimer;
	}

	public static QuequeTimer getQuequeTimer()
	{
		return quequeTimer;
	}

	public static void setQuequeTimer(QuequeTimer quequeTimer)
	{
		TimerManager.quequeTimer = quequeTimer;
	}

	public static MissileTimer getMissileTimer()
	{
		return missileTimer;
	}

	public static void setMissileTimer(MissileTimer missileTimer)
	{
		TimerManager.missileTimer = missileTimer;
	}

	public static AnimationSystemUtilTimer getAnimationSystemUtilTimer()
	{
		return animationSystemUtilTimer;
	}

	public static void setAnimationSystemUtilTimer(AnimationSystemUtilTimer animationSystemUtilTimer)
	{
		TimerManager.animationSystemUtilTimer = animationSystemUtilTimer;
	}

	public static HitMissImageTimer getHitMissImageTimer()
	{
		return hitMissImageTimer;
	}

	public static void setHitMissImageTimer(HitMissImageTimer hitMissImageTimer)
	{
		TimerManager.hitMissImageTimer = hitMissImageTimer;
	}
}

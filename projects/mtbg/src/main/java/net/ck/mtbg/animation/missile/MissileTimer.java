package net.ck.mtbg.animation.missile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Missile;
import net.ck.mtbg.backend.state.TimerManager;
import net.ck.mtbg.util.communication.graphics.MissilePositionChanged;
import net.ck.mtbg.util.utils.ImageUtils;
import net.ck.mtbg.util.utils.MapUtils;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;

@Log4j2
public class MissileTimer implements Runnable
{
    private boolean running;

    @Getter
    @Setter
    private int delay;

    /**
     * Creates a {@code Timer} and initializes both the initial delay and
     * between-event delay to {@code delay} milliseconds. If {@code delay}
     * is less than or equal to zero, the timer fires as soon as it
     * is started. If <code>listener</code> is not <code>null</code>,
     * it's registered as an action listener on the timer.
     *
     * @param delay milliseconds for the initial and between-event delay
     */
    public MissileTimer(int delay)
    {
        setDelay(delay);
    }


    public synchronized boolean isRunning()
    {
        return running;
    }

    public synchronized void setRunning(boolean running)
    {
        this.running = running;
    }

    @Override
    public void run()
    {
        while (Game.getCurrent().isRunning())
        {
            if (Game.getCurrent().getCurrentMap().getActiveMissile() != null)
            {
                //logger.info("posting message");
                TimerManager.setMissileInFlight(true);
                setRunning(true);
                //EventBus.getDefault().post(new MissilePositionChanged());
                //TODO do calculation for missiles here actually instead of in Paint method
                //Paint method will need to do only the drawing of missile at its correct place
                //missile will need to know about everything
                calculateMissile();
            }
            else
            {
                setRunning(false);
                TimerManager.setMissileInFlight(false);
                continue;
            }
            try
            {
                Thread.sleep(getDelay());
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        TimerManager.setMissileInFlight(false);
    }

    //TODO properly handle this - nice that we paint 60 frames, but missile will need to appear at least a little bit :D
    private void calculateMissile()
    {
        Missile m = Game.getCurrent().getCurrentMap().getActiveMissile();
        if (m == null)
        {
            return;
        }

        if (m.getSourceCoordinates() == null)
        {
            logger.error("missile has no source");
            Game.getCurrent().stopGame();
        }

        if (m.getLine() == null)
        {
            if (m.getCurrentPosition() == null)
            {
                m.setCurrentPosition(new Point(m.getSourceCoordinates().x, m.getSourceCoordinates().y));
            }
            m.setLine(MapUtils.getLine(m.getCurrentPosition(), m.getTargetCoordinates()));
        }

        if (m.getLine().size() == 0)
        {
            if (m.isSuccess())
            {
                m.setStandardImage(ImageUtils.loadImage("combat", "explosion"));
            }
            m.setFinished(true);
            Game.getCurrent().getCurrentMap().setActiveMissile(null);
            EventBus.getDefault().post(new MissilePositionChanged());
            return;
        }

        Point p = m.getLine().get(0);
        m.setCurrentPosition(p);

        if (m.getCurrentPosition().equals(m.getTargetCoordinates()))
        {
            if (m.isSuccess())
            {
                m.setStandardImage(ImageUtils.loadImage("combat", "explosion"));
            }
            m.setFinished(true);
            Game.getCurrent().getCurrentMap().setActiveMissile(null);
        }

        //only paint missile every configured pixel
        for (int i = 0; i <= (GameConfiguration.skippedPixelsForDrawingMissiles - 1); i++)
        {
            if (m.getLine().size() > 0)
            {
                m.getLine().remove(0);
            }
        }

        if (GameConfiguration.debugEvents == true)
        {
            logger.debug("fire new missile position");
        }
        EventBus.getDefault().post(new MissilePositionChanged());
    }

    public void stop()
    {

    }
}
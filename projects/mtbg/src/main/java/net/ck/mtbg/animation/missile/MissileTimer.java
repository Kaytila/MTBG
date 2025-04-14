package net.ck.mtbg.animation.missile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.applications.Game;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.Missile;
import net.ck.mtbg.util.communication.graphics.MissilePositionChanged;
import net.ck.mtbg.util.utils.ImageUtils;
import net.ck.mtbg.util.utils.MapUtils;
import org.greenrobot.eventbus.EventBus;

import java.awt.*;
import java.util.ArrayList;

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
            if (Game.getCurrent().getCurrentMap().getMissiles() != null)
            {
                if (Game.getCurrent().getCurrentMap().getMissiles().size() > 0)
                {
                    //logger.info("posting message");
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
                    continue;
                }
            }
            else
            {
                setRunning(false);
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
    }

    //TODO properly handle this - nice that we paint 60 frames, but missile will need to appear at least a little bit :D
    private void calculateMissile()
    {
        ArrayList<Missile> finishedMissiles = new ArrayList<>();
        if ((Game.getCurrent().getCurrentMap().getMissiles() == null) || (Game.getCurrent().getCurrentMap().getMissiles().size() == 0))
        {
            return;
        }
        for (Missile m : Game.getCurrent().getCurrentMap().getMissiles())
        {
            if (m.getSourceCoordinates() == null)
            {
                logger.error("missile has no source");
                Game.getCurrent().stopGame();
            }
            //logger.info("m: {}", m);
            //logger.info("m image: {}", m.getAppearance().getStandardImage());
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
                //logger.info("finished missile");
                m.setFinished(true);
                finishedMissiles.add(m);
            }
            else
            {
                Point p = m.getLine().get(0);
                m.setCurrentPosition(p);

                if (m.getCurrentPosition().equals(m.getTargetCoordinates()))
                {
                    if (m.isSuccess())
                    {
                        m.setStandardImage(ImageUtils.loadImage("combat", "explosion"));
                    }
                    m.setFinished(true);
                    finishedMissiles.add(m);
                }

                //only paint missile every configured pixel
                for (int i = 0; i <= (GameConfiguration.skippedPixelsForDrawingMissiles - 1); i++)
                {
                    if (m.getLine().size() > 0)
                    {
                        m.getLine().remove(0);
                    }
                }

            }
        }

        if (finishedMissiles.size() > 0)
        {
            //logger.info("finished missiles: {}", finishedMissiles);
            Game.getCurrent().getCurrentMap().getMissiles().removeAll(finishedMissiles);
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
package net.ck.mtbg.animation.missile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
import java.util.TimerTask;

@Log4j2
@Getter
@Setter
@ToString
public class MissileTimerTask extends TimerTask
{
    @Getter//(onMethod_ = {@Synchronized})
    @Setter//(onMethod_ = {@Synchronized})
    private boolean running;


    @Override
    public void run()
    {
        if (!Game.getCurrent().isRunning())
        {
            setRunning(false);
            TimerManager.setMissileInFlight(false);
            return;
        }

        if (TimerManager.isPlayerMovementInProgress())
        {
            // Missile rendering is deferred until movement queue/step execution is complete.
            setRunning(false);
            TimerManager.setMissileInFlight(false);
            return;
        }

        boolean hasMissilesAtStart = hasActiveMissiles();
        if (!hasMissilesAtStart)
        {
            setRunning(false);
            TimerManager.setMissileInFlight(false);
            return;
        }

        TimerManager.setMissileInFlight(true);
        setRunning(true);

        try
        {
            calculateMissile();
            if (GameConfiguration.useRenderClock)
            {
                Game.getCurrent().getRenderClock().markDirty();
            }
        }
        catch (Exception e)
        {
            setRunning(false);
            TimerManager.setMissileInFlight(false);
            throw new RuntimeException(e);
        }
        finally
        {
            boolean stillHasMissiles = hasActiveMissiles();
            setRunning(stillHasMissiles);
            // Wichtig: nur freigeben, wenn wirklich keine Missile mehr aktiv ist
            TimerManager.setMissileInFlight(stillHasMissiles);
        }
    }

    private boolean hasActiveMissiles()
    {
        return Game.getCurrent().getCurrentMap() != null
                && Game.getCurrent().getCurrentMap().getActiveMissile() != null;
    }



    private void calculateMissile()
    {
        Missile m = Game.getCurrent().getCurrentMap().getActiveMissile();
        if (m == null)
        {
            return;
        }

        boolean justInitialized = false;
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
            justInitialized = true;
        }

        // Keep the initial source position visible for one render tick.
        if (justInitialized)
        {
            EventBus.getDefault().post(new MissilePositionChanged());
            return;
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

}

package net.ck.mtbg.util.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.ck.mtbg.backend.configuration.GameConfiguration.fps;

@Log4j2
@Getter
@Setter
public final class RenderClock
{
    private final Timer timer;
    private final AtomicBoolean dirty = new AtomicBoolean(true);
    private final javax.swing.JComponent canvas;

    public RenderClock(javax.swing.JComponent canvas)
    {
        this.canvas = canvas;
        int delay = Math.max(1, 1000 / fps);
        this.timer = new Timer(delay, e ->
        {
            if (dirty.getAndSet(false))
            {
                canvas.repaint();
            }
        });
        this.timer.setCoalesce(true);
    }

    public void start()
    {
        timer.start();
    }

    public void stop()
    {
        timer.stop();
    }

    public void markDirty()
    {
        dirty.set(true);
    }
}
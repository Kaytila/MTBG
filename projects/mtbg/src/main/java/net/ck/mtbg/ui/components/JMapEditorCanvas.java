package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class JMapEditorCanvas extends JComponent
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }


    public void paintComponent(Graphics g)
    {
        paintGridLines(g);
    }


    private void paintGridLines(Graphics g)
    {
        int rows = this.getHeight() / GameConfiguration.tileSize;
        int cols = this.getWidth() / GameConfiguration.tileSize;
        int i;
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * GameConfiguration.tileSize, this.getWidth(), i * GameConfiguration.tileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * GameConfiguration.tileSize, 0, i * GameConfiguration.tileSize, this.getHeight());
        }
    }
}

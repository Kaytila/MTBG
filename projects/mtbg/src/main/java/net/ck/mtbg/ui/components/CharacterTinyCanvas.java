package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class CharacterTinyCanvas extends JComponent
{


    public void paint()
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            this.repaint();
        });
    }


    public void paintComponent(Graphics g)
    {
        //super.paintComponent(g);
        g.setColor(Color.CYAN);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());
        int rows = this.getHeight() / GameConfiguration.characterEditorTinyTileSize;
        int cols = this.getWidth() / GameConfiguration.characterEditorTinyTileSize;
        int i;
        g.setColor(Color.GRAY);
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * GameConfiguration.characterEditorTinyTileSize, this.getWidth(), i * GameConfiguration.characterEditorTinyTileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * GameConfiguration.characterEditorTinyTileSize, 0, i * GameConfiguration.characterEditorTinyTileSize, this.getHeight());
        }
    }
}
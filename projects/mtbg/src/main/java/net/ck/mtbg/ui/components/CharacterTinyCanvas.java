package net.ck.mtbg.ui.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.models.CharacterPortraitModel;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
public class CharacterTinyCanvas extends JComponent
{
    CharacterPortraitModel characterPortraitModel;

    public CharacterTinyCanvas(CharacterPortraitModel characterPortraitModel)
    {
        this.characterPortraitModel = characterPortraitModel;
    }

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


        //TODO
        //TODO add skincolor
        g.setColor(Color.ORANGE);
        for (int x = 6; x <= 9; x++)
        {
            for (int y = 2; y <= 5; y++)
            {
                g.fillRect(x * GameConfiguration.characterEditorTinyTileSize, y * GameConfiguration.characterEditorTinyTileSize, 1 * GameConfiguration.characterEditorTinyTileSize, 1 * GameConfiguration.characterEditorTinyTileSize);
                g.fillRect(x * GameConfiguration.characterEditorTinyTileSize, y * GameConfiguration.characterEditorTinyTileSize, 1 * GameConfiguration.characterEditorTinyTileSize, 1 * GameConfiguration.characterEditorTinyTileSize);
                g.fillRect(x * GameConfiguration.characterEditorTinyTileSize, y * GameConfiguration.characterEditorTinyTileSize, 1 * GameConfiguration.characterEditorTinyTileSize, 1 * GameConfiguration.characterEditorTinyTileSize);
            }
        }
        

    }
}
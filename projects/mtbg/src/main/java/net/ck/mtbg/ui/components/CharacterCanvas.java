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
public class CharacterCanvas extends JComponent
{
    CharacterPortraitModel characterPortraitModel;

    public CharacterCanvas(CharacterPortraitModel characterPortraitModel)
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
        //super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());
        int rows = this.getHeight() / GameConfiguration.characterEditorTileSize;
        int cols = this.getWidth() / GameConfiguration.characterEditorTileSize;
        int i;
        g.setColor(Color.GRAY);
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * GameConfiguration.characterEditorTileSize, this.getWidth(), i * GameConfiguration.characterEditorTileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * GameConfiguration.characterEditorTileSize, 0, i * GameConfiguration.characterEditorTileSize, this.getHeight());
        }

        //TODO add skincolor
        g.setColor(Color.ORANGE);


        for (int x = 1; x <= 3; x++)
        {
            for (int y = 1; y <= 3; y++)
            {
                g.fillRect(x * GameConfiguration.characterEditorTileSize, y * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
                g.fillRect(x * GameConfiguration.characterEditorTileSize, y * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
                g.fillRect(x * GameConfiguration.characterEditorTileSize, y * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            }
        }

        g.fillRect(2 * GameConfiguration.characterEditorTileSize, 4 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);

        //eye Color
        g.setColor(characterPortraitModel.getEyeColor().getColor());
        g.fillRect(1 * GameConfiguration.characterEditorTileSize, 2 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
        g.fillRect(3 * GameConfiguration.characterEditorTileSize, 2 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);

        if (characterPortraitModel.getGender().equalsIgnoreCase("male"))
        {
            //hair color
            g.setColor(characterPortraitModel.getHairColor().getColor());
            g.fillRect(1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(2 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(3 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
        }
        else
        {
            //hair color
            g.setColor(characterPortraitModel.getHairColor().getColor());
            g.fillRect(1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(2 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(3 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(0 * GameConfiguration.characterEditorTileSize, 2 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(0 * GameConfiguration.characterEditorTileSize, 3 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(0 * GameConfiguration.characterEditorTileSize, 4 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(4 * GameConfiguration.characterEditorTileSize, 2 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(4 * GameConfiguration.characterEditorTileSize, 3 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
            g.fillRect(4 * GameConfiguration.characterEditorTileSize, 4 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize, 1 * GameConfiguration.characterEditorTileSize);
        }

    }
}

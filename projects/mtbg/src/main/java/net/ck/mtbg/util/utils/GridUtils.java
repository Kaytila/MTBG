package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@Log4j2
public class GridUtils
{
    public static void paintLines(JComponent characterTinyCanvas, Graphics g, int characterEditorTinyTileSize)
    {
        int rows = characterTinyCanvas.getHeight() / characterEditorTinyTileSize;
        int cols = characterTinyCanvas.getWidth() / characterEditorTinyTileSize;
        int i;
        g.setColor(Color.GRAY);
        for (i = 0; i < rows; i++)
        {
            g.drawLine(0, i * characterEditorTinyTileSize, characterTinyCanvas.getWidth(), i * characterEditorTinyTileSize);
        }

        for (i = 0; i < cols; i++)
        {
            g.drawLine(i * characterEditorTinyTileSize, 0, i * characterEditorTinyTileSize, characterTinyCanvas.getHeight());
        }
    }
}

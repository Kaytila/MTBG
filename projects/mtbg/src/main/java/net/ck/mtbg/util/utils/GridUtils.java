package net.ck.mtbg.util.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.components.CharacterTinyCanvas;

import java.awt.*;

@Getter
@Setter
@Log4j2
public class GridUtils {
    public static void paintLines(CharacterTinyCanvas characterTinyCanvas) {
        Graphics g = characterTinyCanvas.getGraphics();
        int rows = characterTinyCanvas.getHeight() / GameConfiguration.characterEditorTinyTileSize;
        int cols = characterTinyCanvas.getWidth() / GameConfiguration.characterEditorTinyTileSize;
        int i;
        g.setColor(Color.GRAY);
        for (i = 0; i < rows; i++) {
            g.drawLine(0, i * GameConfiguration.characterEditorTinyTileSize, characterTinyCanvas.getWidth(), i * GameConfiguration.characterEditorTinyTileSize);
        }

        for (i = 0; i < cols; i++) {
            g.drawLine(i * GameConfiguration.characterEditorTinyTileSize, 0, i * GameConfiguration.characterEditorTinyTileSize, characterTinyCanvas.getHeight());
        }
    }
}

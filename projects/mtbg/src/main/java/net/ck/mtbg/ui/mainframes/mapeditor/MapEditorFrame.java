package net.ck.mtbg.ui.mainframes.mapeditor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.controllers.MapEditorController;

import javax.swing.*;
import java.awt.*;


/**
 * so how to do it
 * we have a map
 * how to fill it
 * click
 * drag and drop
 * <p>
 * how to fill the list of map type prototypes
 * JLabel with image of tile type
 * Tab list? Scroll list?
 * lets try tab and then jlist per type
 * <p>
 * same for furniture and npcs
 */
@Getter
@Setter
@Log4j2
public class MapEditorFrame extends JFrame
{
    public MapEditorFrame() throws HeadlessException
    {
        this.setTitle("MTBG - Map Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setBounds(500, 500, GameConfiguration.UIwidth, GameConfiguration.UIheight);
        this.setPreferredSize(new Dimension(1500, 1000));
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setUndecorated(false);
        this.addWindowListener(MapEditorController.getCurrent());
        this.pack();
        this.setVisible(true);
        setLocationRelativeTo(null);
    }

}

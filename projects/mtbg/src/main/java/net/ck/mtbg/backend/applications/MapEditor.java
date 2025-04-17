package net.ck.mtbg.backend.applications;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.map.Map;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;

@Log4j2
@Getter
@Setter
public class MapEditor
{

    /**
     * Singleton
     */
    private static final MapEditor mapEditor = new MapEditor();
    private Map map;

    /**
     * Singleton access - now I can take away game in a lot of things :D
     */
    public static MapEditor getCurrent()
    {
        return mapEditor;
    }

    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MapEditorFrame mapEditorFrame = new MapEditorFrame();
                mapEditorFrame.pack();
                mapEditorFrame.setVisible(true);
            }
        });
    }
}

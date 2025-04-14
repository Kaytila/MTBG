package net.ck.mtbg.backend.applications;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.ui.mainframes.MapEditorFrame;

@Log4j2
@Getter
@Setter
public class MapEditor
{
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

package net.ck.util.ui;

import net.ck.game.ui.components.JMapEditorCanvas;
import net.ck.util.ImageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MapEditor extends JFrame
{

    private static final org.apache.logging.log4j.core.Logger logger = (Logger) LogManager.getLogger(MapEditor.class);

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
    }


    public MapEditor() throws HeadlessException
    {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try
        {
            this.setIconImage(ImageIO.read(new File(ImageUtils.getAdditionalimagespath() + File.separator + File.separator + "sun.jpg")));
        }
        catch (IOException e)
        {
            logger.error("issue loading icon: {}", e.toString());
        }

        this.setBounds(0, 0, 2000, 1000);
        logger.info("bound: {}", this.getBounds());
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        this.setTitle("MapEditor");
        this.setResizable(false);
        this.toFront();
        this.setLayout(null);

        JMapEditorCanvas canvas = new JMapEditorCanvas();
        this.add(canvas);

        JFileChooser fileChooser = new JFileChooser();

        JButton newMapButton = new JButton();
        JButton saveMapButton = new JButton();
        JTable tileTable = new JTable();
        //tileTable.add


        //this.add(fileChooser);


        initialize();



    }

   private void initialize()
   {
       ImageUtils.initializeBackgroundImages();
   }

    public static void main(String[] args)
    {
        logger.info("starting map editor");
        javax.swing.SwingUtilities.invokeLater(() -> new MapEditor());
    }
}

package net.ck.mtbg.playground;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

/**
 * https://stackoverflow.com/questions/1786886/remove-arrows-from-swing-scrollbar-in-jscrollpane https://stackoverflow.com/questions/12265740/make-a-custom-jscrollbar-using-an-image
 *
 * @author Claus
 */
public class CustomScrollbarUIExample
{
    public static void main(String[] args)
    {
        JScrollPane before = makeExamplePane();
        JScrollPane after = makeExamplePane();

        JScrollBar sb = after.getVerticalScrollBar();
        sb.setPreferredSize(new Dimension(50, Integer.MAX_VALUE));
        sb.setUI(new MyScrollbarUI());

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = f.getContentPane();
        c.setLayout(new GridLayout(2, 1, 0, 1));
        c.add(before);
        c.add(after);
        f.setSize(450, 400);
        f.setVisible(true);
    }

    private static JScrollPane makeExamplePane()
    {
        String exampleText = "Lorem ipsum dolor sit amet,\n consetetur sadipscing elitr,\n sed diam nonumy eirmod \ntempor invidunt ut labore et dolore \nmagna aliquyam erat,\n sed diam voluptua. At vero eos et accusam et \njusto duo dolores et ea rebum. Stet clita\n kasd gubergren, no sea\n takimata sanctus est Lorem ipsum dolor sit amet.\n Lorem ipsum dolor sit amet,\n consetetur sadipscing elitr, sed diam\n nonumy eirmod tempor invidunt \nut labore et dolore\n magna aliquyam erat, sed diam voluptua.\n At vero eos et accusam et justo \nduo\n dolores et ea rebum. Stet clita kasd gubergren, no sea\n takimata sanctus est Lorem\n ipsum dolor sit amet. Lorem ipsum dolor\n sit amet, consetetur sadipscing elitr,\n sed diam nonumy\n eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos\n et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est \nLorem ipsum dolor sit amet.Duis\n autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel\n illum dolore eu feugiat nulla facilisis at vero eros et \naccumsan et iusto odio \ndignissim qui blandit praesent luptatum zzril delenit augue\n duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer\n adipiscing elit, sed diam nonummy nibh euismod \ntincidunt ut laoreet\n dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam,\n quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea\n commodo consequat. Duis autem vel eum iriure \ndolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla \nfacilisis at vero eros et accumsan et iusto odio dignissim qui blandit\n praesent luptatum zzril delenit augue duis dolore \nte feugait nulla facilisi.";
        JTextArea text = new JTextArea(exampleText);
        return new JScrollPane(text);
    }

    static class MyScrollbarUI extends MetalScrollBarUI
    {

        private Image imageThumb, imageTrack;

        MyScrollbarUI()
        {
            try
            {
                imageThumb = ImageIO.read(new File("graphics/misc/CURSORS/0.png"));
                imageTrack = ImageIO.read(new File("graphics/misc/CURSORS/90.png"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        protected JButton createDecreaseButton(int orientation)
        {
            return createZeroButton();
        }

        protected JButton createIncreaseButton(int orientation)
        {
            return createZeroButton();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
        {
            g.translate(thumbBounds.x, thumbBounds.y);
            g.setColor(Color.red);
            g.drawRect(0, 0, thumbBounds.width - 2, thumbBounds.height - 1);
            AffineTransform transform = AffineTransform.getScaleInstance((double) thumbBounds.width / imageThumb.getWidth(null), (double) thumbBounds.height / imageThumb.getHeight(null));
            ((Graphics2D) g).drawImage(imageThumb, transform, null);
            g.translate(-thumbBounds.x, -thumbBounds.y);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
        {
            g.translate(trackBounds.x, trackBounds.y);
            ((Graphics2D) g).drawImage(imageTrack, AffineTransform.getScaleInstance(1, (double) trackBounds.height / imageTrack.getHeight(null)), null);
            g.translate(-trackBounds.x, -trackBounds.y);
        }

        protected JButton createZeroButton()
        {
            JButton button = new JButton("zero button");
            Dimension zeroDim = new Dimension(0, 0);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }

    }

}
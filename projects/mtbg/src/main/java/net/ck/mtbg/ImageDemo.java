package net.ck.mtbg;

import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Log4j2
@SuppressWarnings("removal")
public class ImageDemo implements ChangeListener, ActionListener
{

    private final String bildPfad = GameConfiguration.out + File.separator + "graphics" + File.separator + "cutscenes" + File.separator + "dischord.png";
    JSlider rotateSlider;
    private BufferedImage[] img;
    private JRadioButton scaleRB, rotateRB, transRB, scherRB;
    private JLabel origLabel, transLabel;
    private JTextField xScaleField, yScaleField,
            xPosRotateField, yPosRotateField, xTransField, yTransField,
            xScherField, yScherField;
    private JButton button;
    private CardLayout cl;

    private JPanel cardPanel;

    private Box scalePanel, rotatePanel, transPanel, scherPanel;

    public ImageDemo()
    {
        initGUI();
    }

    public static void main(String args[])
    {
        SwingUtilities.invokeLater(() -> new ImageDemo());
    }

    private void initGUI()
    {
        JPanel rechtsPanel = initRB();

        img = getImages();

        if (img == null)
        {
            System.out.println("Image-Import fehlgeschlagen!");
            System.exit(1);
        }

        ImageIcon origIcon = new ImageIcon(img[0]);
        origLabel = new JLabel(origIcon);
        origLabel.setBorder(createBorder("Original"));

        ImageIcon transIcon = new ImageIcon();
        transLabel = new JLabel(transIcon);
        transLabel.setBorder(createBorder("Transformation"));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.add(origLabel);
        centerPanel.add(transLabel);

        JFrame frame = new JFrame("Affine Transformation");
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(rechtsPanel, BorderLayout.EAST);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Border createBorder(String title)
    {
        return new TitledBorder(new LineBorder(Color.WHITE), title,
                TitledBorder.ABOVE_BOTTOM, TitledBorder.LEADING, new Font(
                "Verdana", Font.PLAIN, 12), Color.WHITE);
    }

    private JPanel initRB()
    {
        scaleRB = new JRadioButton("Skalierung", true);
        scaleRB.addChangeListener(this);
        rotateRB = new JRadioButton("Rotation");
        rotateRB.addChangeListener(this);
        transRB = new JRadioButton("Translation");
        transRB.addChangeListener(this);
        scherRB = new JRadioButton("Scherung");
        scherRB.addChangeListener(this);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(scaleRB);
        buttonGroup.add(rotateRB);
        buttonGroup.add(transRB);
        buttonGroup.add(scherRB);
        JPanel rBPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        rBPanel.add(scaleRB);
        rBPanel.add(rotateRB);
        rBPanel.add(transRB);
        rBPanel.add(scherRB);

        JLabel xScaleLabel, yScaleLabel, faktorRotateLabel, xPosRotateLabel, yPosRotateLabel, xTransLabel, yTransLabel, xScherLabel, yScherLabel;
        xScaleLabel = new JLabel("X-Wert:");
        yScaleLabel = new JLabel("Y-Wert:");
        faktorRotateLabel = new JLabel("Winkel");
        xPosRotateLabel = new JLabel("X-Wert:");
        yPosRotateLabel = new JLabel("Y-Wert:");
        xTransLabel = new JLabel("X-Wert:");
        yTransLabel = new JLabel("Y-Wert:");
        xScherLabel = new JLabel("X-Wert:");
        yScherLabel = new JLabel("Y-Wert:");

        xScaleField = new JTextField(4);
        xScaleField.setPreferredSize(new Dimension(100, 20));
        yScaleField = new JTextField(4);
        yScaleField.setPreferredSize(new Dimension(100, 20));
        // Bereich 0 - 2*PI
        // wird im Listener durch 100 dividiert
        rotateSlider = new JSlider(0, 628, 0);
        rotateSlider.setMajorTickSpacing(150);
        rotateSlider.setPaintTicks(true);
        xPosRotateField = new JTextField(4);
        yPosRotateField = new JTextField(4);
        xTransField = new JTextField(4);
        yTransField = new JTextField(4);
        xScherField = new JTextField(4);
        yScherField = new JTextField(4);

        Dimension fillDim = new Dimension(10, Short.MAX_VALUE);

        scalePanel = new Box(BoxLayout.Y_AXIS);
        scalePanel.add(xScaleLabel);
        scalePanel.add(xScaleField);
        scalePanel.add(yScaleLabel);
        scalePanel.add(yScaleField);
        scalePanel.add(new Box.Filler(fillDim, fillDim, fillDim));

        rotatePanel = new Box(BoxLayout.Y_AXIS);
        rotatePanel.add(faktorRotateLabel);
        rotatePanel.add(rotateSlider);
        rotatePanel.add(xPosRotateLabel);
        rotatePanel.add(xPosRotateField);
        rotatePanel.add(yPosRotateLabel);
        rotatePanel.add(yPosRotateField);
        rotatePanel.add(new Box.Filler(fillDim, fillDim, fillDim));

        transPanel = new Box(BoxLayout.Y_AXIS);
        transPanel.add(xTransLabel);
        transPanel.add(xTransField);
        transPanel.add(yTransLabel);
        transPanel.add(yTransField);
        transPanel.add(new Box.Filler(fillDim, fillDim, fillDim));

        scherPanel = new Box(BoxLayout.Y_AXIS);
        scherPanel.add(xScherLabel);
        scherPanel.add(xScherField);
        scherPanel.add(yScherLabel);
        scherPanel.add(yScherField);
        scherPanel.add(new Box.Filler(fillDim, fillDim, fillDim));

        cl = new CardLayout(0, 20);
        cardPanel = new JPanel(cl);
        cardPanel.add(scalePanel, "Skalierung");
        cardPanel.add(rotatePanel, "Rotation");
        cardPanel.add(transPanel, "Translation (Verschieben)");
        cardPanel.add(scherPanel, "Scherung");

        button = new JButton("Transformieren");
        button.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 10));
        mainPanel.add(rBPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(button, BorderLayout.SOUTH);
        return mainPanel;
    }

    private BufferedImage[] scaleImage(String xStr, String yStr)
    {
        BufferedImage[] iArr = getImages();
        if (iArr == null)
        {
            return null;
        }

        // 1.0 ist Originalgroesse
        double x = str2double(xStr);
        double y = str2double(yStr);
        if (x == 0 || y == 0)
        {
            return null;
        }
        AffineTransform trans = AffineTransform.getScaleInstance(x, y);
        AffineTransformOp op = new AffineTransformOp(trans,
                AffineTransformOp.TYPE_BILINEAR);
        op.filter(iArr[0], iArr[1]);
        return iArr;
    }

    private BufferedImage[] rotateImage(String fStr, String xStr, String yStr)
    {
        BufferedImage[] iArr = getImages();
        if (iArr == null)
        {
            return null;
        }
        double x = str2double(xStr);
        double y = str2double(yStr);
        if (xStr.equals("-1"))
        {
            x = iArr[0].getWidth() / 2;
        }
        if (yStr.equals("-1"))
        {
            y = iArr[0].getHeight() / 2;
        }

        // um die Mitte rotieren
        // f = 6.284 - volle Drehung
        double f = str2double(fStr);
        AffineTransform trans = AffineTransform.getRotateInstance(f, x, y);
        AffineTransformOp op = new AffineTransformOp(trans,
                AffineTransformOp.TYPE_BILINEAR);
        op.filter(iArr[0], iArr[1]);
        return iArr;
    }

    private BufferedImage[] transImage(String xStr, String yStr)
    {
        BufferedImage[] iArr = getImages();
        if (iArr == null)
        {
            return null;
        }

        // Translation
        double x = str2double(xStr);
        double y = str2double(yStr);
        AffineTransform trans = AffineTransform.getTranslateInstance(x, y);
        AffineTransformOp op = new AffineTransformOp(trans,
                AffineTransformOp.TYPE_BILINEAR);
        op.filter(iArr[0], iArr[1]);
        return iArr;
    }

    private BufferedImage[] scherImage(String xStr, String yStr)
    {
        BufferedImage[] iArr = getImages();
        if (iArr == null)
        {
            return null;
        }

        // scheren
        double x = str2double(xStr);
        double y = str2double(yStr);
        AffineTransform trans = AffineTransform.getShearInstance(x, y);
        AffineTransformOp op = new AffineTransformOp(trans,
                AffineTransformOp.TYPE_BILINEAR);
        op.filter(iArr[0], iArr[1]);
        return iArr;
    }

    private BufferedImage[] getImages()
    {
        logger.debug("bild pfad: {}", bildPfad);
        BufferedImage origImg = null;
        try
        {
            origImg = ImageIO.read(new File(bildPfad));
        }
        catch (IOException ex)
        {
            System.err.println("Image-Datei kann nicht gelesen werden!");
            System.exit(1);
        }
        if (origImg != null)
        {
            BufferedImage destImg = new BufferedImage(origImg.getWidth(),
                    origImg.getHeight(), origImg.getType());
            BufferedImage[] i = {origImg, destImg};
            return i;
        }
        return null;
    }

    private void setImg(BufferedImage[] images)
    {
        if (images == null)
        {
            System.out.println("fehlgeschlaagen");
            return;
        }
        ImageIcon origIcon = new ImageIcon(images[0]);
        ImageIcon transIcon = new ImageIcon(images[1]);
        origLabel.setIcon(origIcon);
        transLabel.setIcon(transIcon);
    }

    private double str2double(String dStr)
    {
        if (dStr == null)
        {
            return 1;
        }
        dStr = dStr.replace(',', '.');
        try
        {
            return new Double(dStr).doubleValue();
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("falsches Zahlformat");
        }
        return 0;
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == scaleRB)
        {
            cl.show(cardPanel, "Skalierung");
        }
        if (e.getSource() == rotateRB)
        {
            cl.show(cardPanel, "Rotation");
        }
        if (e.getSource() == transRB)
        {
            cl.show(cardPanel, "Translation (Verschieben)");
        }
        if (e.getSource() == scherRB)
        {
            cl.show(cardPanel, "Scherung");
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (scaleRB.isSelected())
        {
            if (xScaleField.getText().equals("") || yScaleField.getText().equals(""))
            {
                return;
            }
            img = scaleImage(xScaleField.getText(), yScaleField.getText());
            setImg(img);
        }
        if (rotateRB.isSelected())
        {
            String xFaktor = "-1", yFaktor = "-1";
            if (!xPosRotateField.getText().equals("") && !yPosRotateField.getText().equals(""))
            {
                xFaktor = xPosRotateField.getText();
                yFaktor = yPosRotateField.getText();
            }
            double faktor = (double) rotateSlider.getValue() / 100;
            img = rotateImage(new Double(faktor).toString(), xFaktor, yFaktor);
            setImg(img);
        }
        if (transRB.isSelected())
        {
            if (xTransField.getText().equals("") || yTransField.getText().equals(""))
            {
                return;
            }
            img = transImage(xTransField.getText(), yTransField.getText());
            setImg(img);
        }
        if (scherRB.isSelected())
        {
            if (xScherField.getText().equals("") || yScherField.getText().equals(""))
            {
                return;
            }
            img = scherImage(xScherField.getText(), yTransField.getText());
            setImg(img);
        }
    }
}
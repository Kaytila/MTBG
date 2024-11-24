package net.ck.mtbg.ui.buttons;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.run.RunGame;
import net.ck.mtbg.util.ui.WindowBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

@Getter
@Setter
@Log4j2
public class TitleScreenButton extends JButton implements MouseListener
{
    private boolean hovered;
    private String label;

    public TitleScreenButton(int x, int y, String label)
    {
        this.setFont(GameConfiguration.font);
        this.label = label;
        setText(label);
        setBounds(x, y, 150, 30);
        this.setActionCommand(label);
        this.addActionListener(WindowBuilder.getController());
        hovered = false;
        this.addMouseListener(this);
        this.setVisible(true);
        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getActionCommand().equalsIgnoreCase("New Game"))
                {
                    logger.info("start game");
                    WindowBuilder.getTitleFrame().dispose();
                    RunGame.openGameUI();
                }

                if (e.getActionCommand().equalsIgnoreCase("Character Editor"))
                {
                    logger.info("start character editor");
                    WindowBuilder.getTitleFrame().dispose();
                    RunGame.openCharacterEditor();
                }

                if (e.getActionCommand().equalsIgnoreCase("Map Editor"))
                {
                    logger.info("start map editor");
                    WindowBuilder.getTitleFrame().dispose();
                    RunGame.openMapEditor();
                }

            }
        });
    }

    /**
     * with a little help from stackoverflow again
     * <p>
     * https://stackoverflow.com/questions/14284754/java-center-text-in-rectangle/14287270#14287270
     */
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (hovered)
        {
            g.setColor(Color.white);
        }
        else
        {
            g.setColor(Color.black);
        }

        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(label, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(label, x, y);
    }


    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        hovered = true;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        hovered = false;
    }
}

package net.ck.mtbg.ui.buttons;

import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.util.ui.MTBGMouseAdapter;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * Simple button with a background-image.
 */
public class MTBGButton
    extends JButton
{
    private Image backgroundImage;
    
    {
        setBackgroundImage(initDefaultBackground());
        addMouseListener(createRolloverVisualChangeListener());
    }
    
    public MTBGButton () { }
    
    public MTBGButton (
        final String text )
    {
        super(text);
    }
    
    public MTBGButton (
        final Action action )
    {
        super(action);
    }
    
    public Image getBackgroundImage ()
    {
        return backgroundImage;
    }
    
    public void setBackgroundImage (
        final Image backgroundImage )
    {
        if ( Objects.equals(this.backgroundImage, backgroundImage) ) {
            return;
        }
        final var old = this.backgroundImage;
        this.backgroundImage = backgroundImage;
        firePropertyChange("backgroundImage", old, backgroundImage);
        repaint();
    }
    
    @Override
    public void updateUI ()
    {
        setUI(new UI());
    }
    
    private static MouseListener createRolloverVisualChangeListener ()
    {
        return new MTBGMouseAdapter()
        {
            @Override
            public void handle (
                final MouseEvent event )
            {
                final var button = (MTBGButton) event.getSource();
                
                switch ( event.getID() ) {
                    case MouseEvent.MOUSE_ENTERED -> {
                        button.setBackground(Color.BLACK);
                        button.setForeground(Color.WHITE);
                    }
                    case MouseEvent.MOUSE_EXITED -> {
                        button.setBackground(null);
                        button.setForeground(null);
                    }
                }
            }
        };
    }
    
    private static Image initDefaultBackground ()
    {
        try {
            final var path = GameConfiguration.MISC_IMAGES_BUTTONS
                .resolve("cleanButton.png")
                .toFile();
            return ImageIO.read(path);
        }
        catch ( final Throwable throwable ) {
            throw new RuntimeException(throwable);
        }
    }
    
    private static final class UI
        extends MetalButtonUI
    {
        @Override
        public void update (
            final Graphics graphics,
            final JComponent component )
        {
            final var button          = (MTBGButton) component;
            final var backgroundImage = button.backgroundImage;
            
            if ( backgroundImage != null && !button.getModel().isRollover() ) {
                paintBackground(graphics, button, backgroundImage);
                paint(graphics, component);
            }
            else {
                super.update(graphics, component);
            }
        }
        
        private void paintBackground (
            final Graphics graphics,
            final MTBGButton button,
            final Image backgroundImage )
        {
            final var image = backgroundImage.getScaledInstance(
                button.getWidth(),
                button.getHeight(),
                Image.SCALE_SMOOTH
            );
            
            graphics.drawImage(image, 0, 0, null);
        }
    }
}

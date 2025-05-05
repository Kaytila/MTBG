package net.ck.mtbg.ui.components.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.ui.listeners.game.MapLabelListener;

import javax.swing.*;
import java.awt.*;

@Log4j2
@Getter
@Setter
@ToString
public class MapLabel extends JLabel
{

    public MapLabel(String text, AutoMapCanvas autoMapCanvas)
    {
        super(text);
        this.setForeground(Color.WHITE);
        this.setFont(GameConfiguration.font);
        this.setVisible(true);
        MapLabelListener mapLabelMouseListener = new MapLabelListener(autoMapCanvas, this);
        this.addMouseListener(mapLabelMouseListener);
        this.addMouseMotionListener(mapLabelMouseListener);
        setHorizontalAlignment(JLabel.CENTER);
    }
}

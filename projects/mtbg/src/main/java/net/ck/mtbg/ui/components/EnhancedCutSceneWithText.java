package net.ck.mtbg.ui.components;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class EnhancedCutSceneWithText extends SimpleCutScene
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));


    public EnhancedCutSceneWithText(BufferedImage img)
    {
        super(img);
    }
}

package net.ck.mtbg.backend.entities.skills;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class AbstractSpell
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));
    protected String name;
    protected boolean adjecient;
    protected boolean immediately;
    protected BufferedImage menuImage;
    protected int costs;
    protected BufferedImage actionImage;
}

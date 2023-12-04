package net.ck.mtbg.backend.entities.skills;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;

@Log4j2
@Getter
@Setter
public class AbstractSkill
{
    protected String name;
    protected boolean adjecient;
    protected boolean immediately;
    protected BufferedImage menuImage;
    protected int costs;
    protected BufferedImage actionImage;
}

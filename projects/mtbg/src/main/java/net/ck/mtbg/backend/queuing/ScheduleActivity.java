package net.ck.mtbg.backend.queuing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.util.communication.keyboard.AbstractKeyboardAction;

import java.awt.*;

@Log4j2
@Getter
@Setter
public class ScheduleActivity
{
    private LifeForm npc;
    private GameTime startTime;
    private Point targetLocation;
    private String scheduleActivityString;
    private AbstractKeyboardAction action;
    private boolean active;
}

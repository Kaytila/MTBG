package net.ck.mtbg.backend.queuing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.time.GameTime;
import net.ck.mtbg.util.communication.keyboard.MoveAction;

import java.awt.*;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class Schedule
{
    private ArrayList<ScheduleActivity> activities;

    public Schedule(LifeForm e)
    {
        activities = new ArrayList<>();

        ScheduleActivity activity = new ScheduleActivity();
        activity.setNpc(e);
        activity.setStartLocation(new Point(9, 1));
        GameTime time = new GameTime();
        time.setCurrentMinute(15);
        time.setCurrentHour(8);
        activity.setStartTime(time);
        activity.setActionString("TADATADATADATADATADATADATADATADATADATADATADATADATADATADATADATADATADATADA");
        MoveAction action = new MoveAction();
        action.setGetWhere(new Point(0, 0));
        //logger.info("move towards target map position: {},{}", 0, 0);
        activity.setAction(action);
        activities.add(activity);
    }
}

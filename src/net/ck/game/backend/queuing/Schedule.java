package net.ck.game.backend.queuing;

import net.ck.game.backend.entities.LifeForm;
import net.ck.game.backend.time.GameTime;
import net.ck.util.CodeUtils;
import net.ck.util.communication.keyboard.MoveAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public class Schedule
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    private ArrayList<ScheduleActivity> activities;

    public ArrayList<ScheduleActivity> getActivities()
    {
        return activities;
    }

    public void setActivities(ArrayList<ScheduleActivity> activities)
    {
        this.activities = activities;
    }

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
        logger.info("move towards target map position: {},{}", 0, 0);
        activity.setAction(action);
        activities.add(activity);
    }
}

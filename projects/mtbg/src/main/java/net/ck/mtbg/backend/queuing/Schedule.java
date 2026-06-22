package net.ck.mtbg.backend.queuing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;
import net.ck.mtbg.backend.time.GameTime;

import java.awt.*;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
@ToString
public class Schedule
{
    private ArrayList<ScheduleActivity> activities;
    private boolean active;
    private int currentScheduleActivityIndex = 0;

    public Schedule(LifeForm e)
    {
        activities = new ArrayList<>();
    }

    public Schedule(Schedule that)
    {
        this.activities = new ArrayList<>();
        for (ScheduleActivity source : that.getActivities())
        {
            ScheduleActivity copy = new ScheduleActivity();
            copy.setNpc(source.getNpc());
            copy.setScheduleActivityString(source.getScheduleActivityString());
            copy.setAction(source.getAction());
            copy.setActive(source.isActive());
            copy.setTargetLocation(source.getTargetLocation() != null ? new Point(source.getTargetLocation()) : null);
            copy.setStartTime(copyGameTime(source.getStartTime()));
            this.activities.add(copy);
        }
        this.active = that.active;
        this.currentScheduleActivityIndex = that.currentScheduleActivityIndex;
    }

    private GameTime copyGameTime(GameTime source)
    {
        if (source == null)
        {
            return null;
        }
        GameTime copy = new GameTime();
        copy.setCurrentHour(source.getCurrentHour());
        copy.setCurrentMinute(source.getCurrentMinute());
        copy.setCurrentDay(source.getCurrentDay());
        copy.setCurrentMonth(source.getCurrentMonth());
        copy.setCurrentYear(source.getCurrentYear());
        copy.setOldHour(source.getOldHour());
        copy.setOldMinute(source.getOldMinute());
        copy.setOldDay(source.getOldDay());
        copy.setOldMonth(source.getOldMonth());
        copy.setOldYear(source.getOldYear());
        return copy;
    }

    /**
     * either set the index to the next activity,
     * or if reached the final one, set to the
     * first one again
     */
    public void moveToNextScheduleActivity()
    {
        if (GameConfiguration.debugSchedule == true)
        {
            logger.debug("activities index: {}", currentScheduleActivityIndex);
        }
        if (currentScheduleActivityIndex < activities.size() - 1)
        {
            currentScheduleActivityIndex++;
        }
        else
        {
            currentScheduleActivityIndex = 0;
        }
    }

    public void add(ScheduleActivity scheduleActivity)
    {
        getActivities().add(scheduleActivity);
    }

    public ScheduleActivity getCurrentlyActiveActivity()
    {
        return getActivities().get(currentScheduleActivityIndex);
    }
}

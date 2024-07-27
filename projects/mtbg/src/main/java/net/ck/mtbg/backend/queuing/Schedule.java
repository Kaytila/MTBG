package net.ck.mtbg.backend.queuing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.configuration.GameConfiguration;
import net.ck.mtbg.backend.entities.entities.LifeForm;

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

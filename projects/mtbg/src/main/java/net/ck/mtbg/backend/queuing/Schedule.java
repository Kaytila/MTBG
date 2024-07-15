package net.ck.mtbg.backend.queuing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.backend.entities.entities.LifeForm;

import java.util.ArrayList;

@Log4j2
@Getter
@Setter
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
        if (currentScheduleActivityIndex < activities.size())
        {
            currentScheduleActivityIndex++;
        }
        else
        {
            currentScheduleActivityIndex = 0;
        }
    }
}

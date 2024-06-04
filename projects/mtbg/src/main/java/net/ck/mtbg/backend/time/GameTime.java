package net.ck.mtbg.backend.time;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.ck.mtbg.util.communication.time.GameTimeChangeType;
import net.ck.mtbg.util.communication.time.GameTimeChanged;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * So Game Time starts with 0:0:0 at day 1, month 1, year 1.
 * An hour has 60 minutes,
 * a day has 24 hours,
 * a month has 30 days.
 * a year has 12 months.
 * No need for more, this is good enough anyhow.
 */
@Log4j2
@Getter
@Setter
public class GameTime implements Serializable
{
    private int currentHour;
    private int currentMinute;
    private int currentDay;
    private int currentMonth;
    private int currentYear;

    private int oldHour;
    private int oldMinute;
    private int oldDay;
    private int oldMonth;

    private int oldYear;


    public GameTime()
    {
        setCurrentMinute(0);
        setCurrentHour(0);
        setCurrentDay(1);
        setCurrentMonth(1);
        setCurrentYear(0);

        setOldMinute(0);
        setOldHour(0);
        setOldDay(1);
        setOldMonth(1);
        setOldYear(0);
    }

    @Override
    public String toString()
    {
        return "GameTime{" +
                "currentDay=" + currentDay +
                ", currentHour=" + currentHour +
                ", currentMinute=" + currentMinute +
                ", currentMonth=" + currentMonth +
                ", currentYear=" + currentYear +
                '}';
    }

    /**
     * advance the game time by amount minutes
     *
     * @param minutes - the minutes that have passed
     * @return the current game time
     */
    public GameTime advanceTime(int minutes)
    {
        int remainingMinutes = 0;
        int remainingHours = 0;
        int remainingDays = 0;
        int remainingMonths = 0;
        int hours = 0;
        int days = 0;
        int months = 0;
        int years = 0;

        if (getCurrentMinute() + minutes < 60)
        {
            setCurrentMinute(getCurrentMinute() + minutes);
        }
        else
        {
            hours = Math.floorDiv((getCurrentMinute() + minutes), 60);
            remainingMinutes = (getCurrentMinute() + minutes) % 60;

            if (getCurrentHour() + hours < 24)
            {
                setCurrentHour(getCurrentHour() + hours);
                setCurrentMinute(remainingMinutes);
            }
            else
            {
                days = Math.floorDiv((getCurrentHour() + hours), 24);
                remainingHours = (getCurrentHour() + hours) % 24;

                //we have 12 * 30 days here in this world :P
                if (getCurrentDay() + days < 30)
                {
                    setCurrentDay(getCurrentDay() + days);
                    setCurrentHour(remainingHours);
                }
                else
                {
                    months = Math.floorDiv((getCurrentDay() + days), 30);
                    remainingDays = (getCurrentDay() + days) % 30;

                    if (getCurrentMonth() + months < 12)
                    {
                        setCurrentMonth(getCurrentMonth() + months);
                        setCurrentDay(remainingDays);
                    }
                    // we are at years
                    else
                    {
                        years = Math.floorDiv((getCurrentMonth() + months), 12);
                        setCurrentYear(getCurrentYear() + years);
                        setCurrentMonth(remainingMonths);
                        setCurrentDay(remainingDays);
                        setCurrentHour(remainingHours);
                        setCurrentMinute(remainingMinutes);
                    }
                }
            }
        }
        //logger.info("minutes: {}, hours: {}, days: {}, months: {}, years: {}, remainingMinutes: {}, remaining hours: {}, remaining days: {}, remaining months: {},", minutes, hours, days, months, years, remainingMinutes, remainingHours, remainingDays, remainingMonths);
        logger.info("current time: {}", this);

        GameTimeChangeType type = null;
        if (currentMinute != oldMinute)
        {
            type = GameTimeChangeType.MINUTE;
            //logger.info("minute changed");
        }

        if (currentHour != oldHour)
        {
            type = GameTimeChangeType.HOUR;
            //logger.info("hour changed");
        }

        if (currentDay != oldDay)
        {
            type = GameTimeChangeType.DAY;
            //logger.info("day changed");
        }

        if (currentMonth != oldMonth)
        {
            type = GameTimeChangeType.MONTH;
            //logger.info("month changed");
        }

        if (currentYear != oldYear)
        {
            type = GameTimeChangeType.YEAR;
            //logger.info("year changed");
        }
        EventBus.getDefault().post(new GameTimeChanged(type));

        return this;
    }

    public boolean greaterOrEqual(GameTime time)
    {
        if (this.getCurrentHour() == time.getCurrentHour())
        {
            if (this.getCurrentMinute() == time.getCurrentMinute())
            {
                return true;
            } else if (this.getCurrentMinute() < time.getCurrentMinute())
            {
                return false;
            } else
            {
                return true;
            }
        } else if (this.getCurrentHour() < time.getCurrentHour())
        {
            return false;
        } else
        {
            return true;
        }
    }
}

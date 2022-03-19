package net.ck.game.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * So Game Time starts with 0:0:0 at day 1, month 1, year 1.
 * An hour has 60 minutes,
 * a day has 24 hours,
 * a month has 30 days.
 * a year has 12 months.
 * No need for more, this is good enough anyhow.
 */
public class GameTime
{

    private final Logger logger = LogManager.getLogger(getRealClass());

    private int currentHour;

    private int currentMinute;

    private int currentDay;

    private int currentMonth;

    private int currentYear;

    public GameTime()
    {
        setCurrentMinute(0);
        setCurrentHour(0);
        setCurrentDay(1);
        setCurrentMonth(1);
        setCurrentYear(0);
    }

    public Class<?> getRealClass()
    {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, this::getClass);
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
     * @param minutes
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
        return this;
    }

    public int getCurrentMonth()
    {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth)
    {
        this.currentMonth = currentMonth;
    }

    public int getCurrentDay()
    {
        return currentDay;
    }

    public void setCurrentDay(int currentDay)
    {
        this.currentDay = currentDay;
    }

    public int getCurrentYear()
    {
        return currentYear;
    }

    public void setCurrentYear(int currentYear)
    {
        this.currentYear = currentYear;
    }

    public int getCurrentMinute()
    {
        return currentMinute;
    }

    public void setCurrentMinute(int currentMinute)
    {
        this.currentMinute = currentMinute;
    }

    public int getCurrentHour()
    {
        return currentHour;
    }

    public void setCurrentHour(int currentHour)
    {
        this.currentHour = currentHour;
    }
}

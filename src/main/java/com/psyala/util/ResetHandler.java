package com.psyala.util;

import com.psyala.Beltip;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ResetHandler {
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEE MMM dd, hh:mm zzz");
    private static ResetHandler instance = null;
    private static ZonedDateTime nextResetDay;

    private ResetHandler() {
        nextResetDay = calculateNextReset();
    }

    public static ResetHandler getInstance() {
        if (instance == null) instance = new ResetHandler();
        return instance;
    }

    public ZonedDateTime getNextResetDay() {
        return nextResetDay;
    }

    public boolean hasResetHappened() {
        if (ZonedDateTime.now().isAfter(nextResetDay) || ZonedDateTime.now().isEqual(nextResetDay)) {
            nextResetDay = calculateNextReset();
            return true;
        }

        return false;
    }

    protected ZonedDateTime calculateNextReset() {
        return calculateNextReset(ZonedDateTime.now(), Beltip.configuration.resetHourUtc, Beltip.configuration.resetDay.toUpperCase());
    }

    protected static ZonedDateTime calculateNextReset(ZonedDateTime now, int resetHourUtc, String resetDayOfWeek) {
        ZonedDateTime resetDateTime = now
                .withZoneSameInstant(ZoneId.of("UTC"))
                .withHour(resetHourUtc)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        DayOfWeek currentDayOfWeek = resetDateTime.getDayOfWeek();
        DayOfWeek resetDay = DayOfWeek.valueOf(resetDayOfWeek);
        int dayDelta = Math.abs(currentDayOfWeek.getValue() - resetDay.getValue());

        //Current day is smaller than reset day, thus is before - so add the difference
        if (currentDayOfWeek.getValue() < resetDay.getValue()) {
            return resetDateTime.plusDays(dayDelta);
        }
        //Current day is larger than reset day, thus is after - so minus the difference and add a week
        else if (currentDayOfWeek.getValue() > resetDay.getValue()) {
            return resetDateTime
                    .minusDays(dayDelta)
                    .plusWeeks(1);
        }
        //Else it's the same day - so check if now is before the reset time - if it is then add a week
        else {
            return now.isBefore(resetDateTime) ?
                    resetDateTime
                    : resetDateTime.plusWeeks(1);
        }
    }
}

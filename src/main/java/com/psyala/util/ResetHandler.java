package com.psyala.util;

import com.psyala.Beltip;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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

    private ZonedDateTime calculateNextReset() {
        LocalDateTime resetDateTime = LocalDateTime.now()
                .withHour(Beltip.configuration.resetHourUtc)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        DayOfWeek dayOfWeek = resetDateTime.getDayOfWeek();
        DayOfWeek resetDay = DayOfWeek.valueOf(Beltip.configuration.resetDay.toUpperCase());
        int dayDelta = resetDay.getValue() - dayOfWeek.getValue();

        if (dayOfWeek.getValue() < resetDay.getValue()) {
            return resetDateTime.plusDays(dayDelta).atZone(ZoneId.systemDefault());
        } else if (dayOfWeek.getValue() > resetDay.getValue()) {
            return resetDateTime
                    .minusDays(dayDelta)
                    .plusWeeks(1)
                    .atZone(ZoneId.systemDefault());
        } else {
            return LocalDateTime.now().isBefore(resetDateTime) ?
                    resetDateTime.atZone(ZoneId.systemDefault())
                    : resetDateTime.plusWeeks(1).atZone(ZoneId.systemDefault());
        }
    }
}

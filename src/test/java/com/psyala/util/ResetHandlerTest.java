package com.psyala.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ResetHandlerTest {

    @Test
    public void before_reset_day_gmt() {
        ZonedDateTime now = ZonedDateTime.of(2022, 1, 3, 5, 33, 5, 12, ZoneId.of("GMT"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 1, 5, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void after_reset_day_gmt() {
        ZonedDateTime now = ZonedDateTime.of(2022, 1, 2, 5, 33, 5, 12, ZoneId.of("GMT"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 1, 5, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void on_reset_day_before_time_gmt() {
        ZonedDateTime now = ZonedDateTime.of(2022, 1, 5, 5, 33, 5, 12, ZoneId.of("GMT"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 1, 5, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void on_reset_day_after_time_gmt() {
        ZonedDateTime now = ZonedDateTime.of(2022, 1, 5, 10, 33, 5, 12, ZoneId.of("GMT"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 1, 12, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void before_reset_day_bst() {
        ZonedDateTime now = ZonedDateTime.of(2022, 5, 3, 5, 33, 5, 12, ZoneId.of("GMT+1"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 5, 4, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void after_reset_day_bst() {
        ZonedDateTime now = ZonedDateTime.of(2022, 5, 1, 5, 33, 5, 12, ZoneId.of("GMT+1"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 5, 4, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void on_reset_day_before_time_bst() {
        ZonedDateTime now = ZonedDateTime.of(2022, 5, 4, 7, 33, 5, 12, ZoneId.of("GMT+1"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 5, 4, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }

    @Test
    public void on_reset_day_after_time_bst() {
        ZonedDateTime now = ZonedDateTime.of(2022, 5, 4, 8, 0, 5, 12, ZoneId.of("GMT+1"));
        ZonedDateTime expectedReset = ZonedDateTime.of(2022, 5, 11, 7, 0, 0, 0, ZoneId.of("UTC"));

        Assert.assertEquals(
                "Date does not match",
                expectedReset.format(ResetHandler.DATE_FORMAT),
                ResetHandler.calculateNextReset(now, 7, "WEDNESDAY").format(ResetHandler.DATE_FORMAT)
        );
    }


}
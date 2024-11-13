package com.tn.musego.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date helper class
 *
 * @author Skander Ben Fredj
 */

public class DateHelper {
    /**
     * Method to convert current date from string to timestamp
     *
     * @param date date as string
     * @return date as timestamp
     */
    public static Timestamp timestampFromString(String date) {
        return Timestamp.valueOf(date);
    }

    /**
     * Method to convert date from date to timestamp
     *
     * @param date date input
     * @return date as timestamp
     */
    public static Timestamp timestampFromDate(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Date dateFromTimestamp(Timestamp timestamp) {
        return timestamp;
    }

    /**
     * Method to get current date as timestamp
     *
     * @return current date
     */
    public static Timestamp currentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Method to convert date from String to Date
     *
     * @param date date as String must be formatted as yyyy-MM-dd
     * @return date formatted date as Date
     * @throws ParseException String parsing exception
     */
    public static Date dateFromString(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to convert days to milliseconds
     *
     * @param days number of days to convert
     * @return milliseconds
     */
    private static Long dayToMilliseconds(int days) {
        return ((long) days * 24 * 60 * 60 * 1000);
    }

    /**
     * @param days      number of days to add
     * @param timestamp start timestamp
     * @return new timestamp
     */
    public static Timestamp addDays(int days, Timestamp timestamp) {
        Long milliseconds = dayToMilliseconds(days);
        return new Timestamp(timestamp.getTime() + milliseconds);
    }

    public static LocalDate dateToDatepicker(Date picker) {
        return Instant.ofEpochMilli(picker.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

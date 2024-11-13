package tn.musego.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static Date convertDateFormat(String inputDate) throws ParseException {

        // Parse input date string into a Date object
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = inputDateFormat.parse(inputDate);

        // Format the date into output string format
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String outputDateStr = outputDateFormat.format(date);

        Date outputDate = outputDateFormat.parse(outputDateStr);

        // Print the output string
        return outputDate;
    }
}

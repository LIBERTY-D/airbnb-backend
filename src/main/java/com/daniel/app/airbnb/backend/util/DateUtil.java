package com.daniel.app.airbnb.backend.util;

import com.daniel.app.airbnb.backend.exception.ParseDateException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date getDateFormat(String date) {
        String format = "yyyy-MM-dd";
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new ParseDateException(String.format("failed to parse date " +
                    "%s" +
                    " , format required %s ", date, format));
        }


    }
}

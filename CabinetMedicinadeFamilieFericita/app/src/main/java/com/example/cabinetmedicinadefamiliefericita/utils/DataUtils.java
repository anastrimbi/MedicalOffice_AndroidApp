package com.example.cabinetmedicinadefamiliefericita.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

public class DataUtils {

    public static String getStringFromEnumName(Enum e, Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier(e.name(), "string", context.getPackageName());

        // daca nu gasim resursa in res>strings.xml, atunci returnam numele enumului
        return resId != 0 ? res.getString(resId) : e.toString();
    }

    public static long dpTpToTimestamp(DatePicker dp, TimePicker tp) {
        int year = dp.getYear();
        int month = dp.getMonth();
        int day = dp.getDayOfMonth();
        int hour = tp.getCurrentHour();
        int min = tp.getCurrentMinute();

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min);

        return cal.getTimeInMillis();
   }

   public static String timestampToDateTimeStringFormat(long timestamp) {
       Calendar cal = Calendar.getInstance();
       cal.setTimeInMillis(timestamp);

       return DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
   }
}

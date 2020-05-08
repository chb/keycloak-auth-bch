package org.chip.ihl.certificates.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date futureDate(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}

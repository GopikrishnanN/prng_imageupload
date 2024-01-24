package com.prng.androidimageuploadcomponent.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class StringUtils {

    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    /**
     * Get ISO date format from date
     *
     * @param date
     */
    public static String getISODateFromDate(Date date) {
        DateFormat format = new SimpleDateFormat(ISO_DATE_FORMAT);
        return format.format(date);
    }

    /**
     * space %20 ! %21 " %22 # %23 $ %24 % %25 & %26 ' %27 ( %28 ) %29 %2A + %2B ,%2C - %2D .
     * %2E / %2F  : %3A ; %3B < %3C = %3D > %3E ? %3F  @ %40
     */
    public static String encodeString(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        value = value.trim();
        value = value.replace("\n", "%20");
        value = value.replace(" ", "%20");
        value = value.replace(",", "%2C");
        value = value.replace("!", "%21");
        value = value.replace("\"", "&quot;");
        value = value.replace("#", "%23");
        value = value.replace("$", "%24");
        value = value.replace("&", "%26");
        value = value.replace("'", "%27");
        value = value.replace("(", "%28");
        value = value.replace(")", "%29");
        value = value.replace("*", "%2A");
        value = value.replace("+", "%2B");
        value = value.replace(",", "%2C");
        value = value.replace("-", "%2D");
        value = value.replace("/", "%2F");
        value = value.replace(":", "%3A");
        value = value.replace(";", "%3B");
        value = value.replace("<", "%3C");
        value = value.replace("=", "%3D");
        value = value.replace(">", "%3E");
        value = value.replace("?", "%3F");
        value = value.replace("@", "%40");
        return value;

    }

    public static String decodeSpace(String value) {
        if (value == null) {
            return "";
        }
        value = value.replace("%2C", ",");
        value = value.trim().replace("%20", " ");
        value = value.replace("%21", "!");
        value = value.replace("%23", "#");
        value = value.replace("%24", "$");
        value = value.replace("%26", "&");
        value = value.replace("%27", "'");
        value = value.replace("%28", "(");
        value = value.replace("%29", ")");
        value = value.replace("%2A", "*");
        value = value.replace("%2B", "+");
        value = value.replace("%2C", ",");
        value = value.replace("%2D", "-");
        value = value.replace("%2F", "/");
        value = value.replace("%3A", ":");
        value = value.replace("%3B", ";");
        value = value.replace("%3C", "<");
        value = value.replace("%3D", "=");
        value = value.replace("%3E", ">");
        value = value.replace("%3F", "?");
        value = value.replace("%40", "@");
        return value;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}

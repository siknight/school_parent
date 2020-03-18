package util;

import java.util.Date;

public class DateToStringUtil {
    public static String dataToString(Date date) {

        return date.toString().replaceAll(" ", "").replaceAll(":", "");
    }
}

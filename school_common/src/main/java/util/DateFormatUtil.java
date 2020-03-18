package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public static String  DateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return  sdf.format(date);
    }

}

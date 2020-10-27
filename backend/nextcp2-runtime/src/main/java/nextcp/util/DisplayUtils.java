package nextcp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayUtils
{
    private static final SimpleDateFormat sdf_min = new SimpleDateFormat("mm:ss");
    private static final SimpleDateFormat sdf_hour = new SimpleDateFormat("HH:mm:ss");
    private static final long hourInSeconds = 60 * 60; // 60 sec * 60 min

    public static String convertToDigitString(Long durationInSeconds)
    {
        if (durationInSeconds == null)
        {
            return "00:00";
        }
        if (durationInSeconds < hourInSeconds)
        {
            return sdf_min.format(new Date(durationInSeconds * 1000));
        }
        else
        {
            return sdf_hour.format(new Date(durationInSeconds * 1000));
        }
    }
}

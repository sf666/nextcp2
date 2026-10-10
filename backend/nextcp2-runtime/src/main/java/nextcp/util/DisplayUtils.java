package nextcp.util;

import java.util.Locale;

public class DisplayUtils
{
    private static final long hourInSeconds = 60 * 60; // 60 sec * 60 min

    public static String convertToDigitString(Long durationInSeconds)
    {
        if (durationInSeconds == null)
        {
            return "00:00";
        }
        long seconds = Math.max(0L, durationInSeconds);
        // A playback duration is not a date. Keep it independent of time zones and thread-safe.
        if (seconds < hourInSeconds)
        {
            return String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60);
        }
        else
        {
            return String.format(Locale.ROOT, "%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
        }
    }
}

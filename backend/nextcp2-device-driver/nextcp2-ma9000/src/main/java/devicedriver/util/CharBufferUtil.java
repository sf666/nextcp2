package devicedriver.util;


import java.nio.CharBuffer;

/**
 * Der Buffers muss gefliped sein.
 */
public class CharBufferUtil
{
    public static int getFirstIndexOf(char c, CharBuffer buffer)
    {
        if (buffer == null)
        {
            return -1;
        }

        for (int i = 0; i < buffer.limit() - 1; i++)
        {
            if (buffer.get(i) == c)
            {
                return i;
            }
        }
        return -1;
    }

    public static int getLastIndexOf(char c, CharBuffer buffer)
    {
        if (buffer == null)
        {
            return -1;
        }
        for (int i = buffer.limit() - 1; i >= 0; i--)
        {
            if (buffer.get(i) == c)
            {
                return i;
            }
        }
        return -1;
    }

    public static boolean endsWith(char c, CharBuffer buffer)
    {
        if (buffer == null)
        {
            return false;
        }

        if (buffer.limit() < 0)
        {
            return false;
        }

        return buffer.get(buffer.limit() - 1) == c;
    }

    public static boolean endsWith(String chars, CharBuffer buffer)
    {
        if (buffer == null)
        {
            return false;
        }

        if (buffer.limit() < 0)
        {
            return false;
        }

        int comparePos = buffer.limit() - chars.length();
        for (int i = 0; i < chars.length() - 1; i++)
        {
            if (chars.charAt(i) != buffer.get(comparePos + i))
            {
//                System.out.println("chars : >" + chars + "<");
//                System.out.println("buffer : >" + buffer.toString() + "<");
//                System.out.println("pos : " + comparePos);
//                System.out.println("i :" + i);
//                System.out.println("char at : >" + chars.charAt(i) + "<");
//                System.out.println("buffer at : >" + buffer.get(comparePos + i) + "<");
                return false;
            }
        }

        return true;
    }

    public static boolean startsWith(char c, CharBuffer buffer)
    {
        if (buffer == null)
        {
            return false;
        }
        if (buffer.limit() < 0)
        {
            return false;
        }
        return buffer.get(0) == c;
    }

    public static boolean startsWith(String request, CharBuffer buffer)
    {
        if (buffer == null || request == null)
        {
            return false;
        }

        if (buffer.limit() < 0)
        {
            return false;
        }

        for (int i = 0; i < request.length() - 1; i++)
        {
            if (request.charAt(i) != buffer.get(i))
            {
                return false;
            }
        }

        return true;
    }

    public static String[] split(CharBuffer buffer, String regex)
    {
        return buffer.toString().split(regex);
    }
}

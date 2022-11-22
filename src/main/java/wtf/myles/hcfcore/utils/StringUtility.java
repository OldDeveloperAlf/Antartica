package wtf.myles.hcfcore.utils;

import org.bukkit.ChatColor;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Myles on 24/06/2015.
 */
public class StringUtility
{
    public static String trimList(List<String> list)
    {
        if (list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator i = list.iterator();
        if (i.hasNext())
        {
            sb.append(i.next());
            while (i.hasNext()) {
                sb.append(',').append(i.next());
            }
        }
        return sb.toString();
    }

    public static List<String> toStringList(String string)
    {
        if (string == null) {
            return new ArrayList();
        }
        List<String> listString = new ArrayList();
        String[] split = string.split(",");
        for (String temp : split) {
            listString.add(temp);
        }
        return listString;
    }

    public static boolean isAlpha(String str)
    {
        return str.matches("[a-zA-Z]+");
    }

    public static boolean isAlphaWithNumber(String str)
    {
        return str.matches("[a-zA-Z0-9]+");
    }

    public static String shortUUID()
    {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, 36);
    }

    public static String getDefaultUsage()
    {
        return ChatColor.RED + "Improper usage! ";
    }
}

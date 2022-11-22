package wtf.myles.hcfcore.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * Created by Myles on 25/06/2015.
 */
public class NumberUtils
{
    public static boolean isNumerical(String str)
    {
        return str.matches("[0-9]+");
    }

    public static String formatNumber(String str)
    {
        int amount = Integer.parseInt(str);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    public static boolean isNumber(String str)
    {
        int i;
        try
        {
            i = Integer.parseInt(str);
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str)
    {
        double i;
        try
        {
            i = Double.parseDouble(str);
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }

    public static NumberFormat getProperFormat()
    {
        return new DecimalFormat("##.###");
    }

    public static int getIntBetween(int x, int y)
    {
        Random r = new Random();
        int num = r.nextInt(y - x) + x;
        return num;
    }
}

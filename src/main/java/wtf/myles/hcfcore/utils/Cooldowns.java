package wtf.myles.hcfcore.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.entity.Player;

/**
 * Created by Myles on 29/06/2015.
 */
public class Cooldowns {

    private static Table<String, String, Long> cooldowns = HashBasedTable.create();

    public static long getCooldown(Player player, String key)
    {
        return calculateRemainder((Long)cooldowns.get(player.getName(), key));
    }

    public static long setCooldown(Player player, String key, long delay)
    {
        return calculateRemainder((Long)cooldowns.put(player.getName(), key, Long.valueOf(System.currentTimeMillis() + delay)));
    }

    public static boolean tryCooldown(Player player, String key, long delay)
    {
        if (getCooldown(player, key) <= 0L) {
            setCooldown(player, key, delay);
            return true;
        }
        return false;
    }

    public static void removeCooldowns(Player player)
    {
        cooldowns.row(player.getName()).clear();
    }

    public static long calculateRemainder(Long expireTime)
    {
        return expireTime != null ? expireTime.longValue() - System.currentTimeMillis() : -9223372036854775808L;
    }
}

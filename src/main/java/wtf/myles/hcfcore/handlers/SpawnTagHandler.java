package wtf.myles.hcfcore.handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.Main;

import java.util.HashMap;

/**
 * Created by Myles on 19/06/2015.
 */
public class SpawnTagHandler {
    public static final int MAX_SPAWN_TAG = 60;
    private static HashMap<String, Long> spawnTags;

    public static void removeTag(final Player player) {
        SpawnTagHandler.spawnTags.remove(player.getName());
    }

    public static void addSeconds(final Player player, final int seconds) {

        if (isTagged(player)) {
            final int secondsTaggedFor = (int)((SpawnTagHandler.spawnTags.get(player.getName()) - System.currentTimeMillis()) / 1000L);
            final int newSeconds = Math.min(secondsTaggedFor + Main.getInstance().getConfigManager().getSpawnTagTimeInSeconds(), 60);
            SpawnTagHandler.spawnTags.put(player.getName(), System.currentTimeMillis() + newSeconds * 1000L);
        }
        else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been spawn-tagged for &c" + Main.getInstance().getConfigManager().getSpawnTagTimeInSeconds() + " &eseconds!"));
            SpawnTagHandler.spawnTags.put(player.getName(), System.currentTimeMillis() + Main.getInstance().getConfigManager().getSpawnTagTimeInSeconds() * 1000L);
        }
    }

    public static long getTag(final Player player) {
        return SpawnTagHandler.spawnTags.get(player.getName()) - System.currentTimeMillis();
    }

    public static boolean isTagged(final Player player) {
        return SpawnTagHandler.spawnTags.containsKey(player.getName()) && SpawnTagHandler.spawnTags.get(player.getName()) > System.currentTimeMillis();
    }

    public static HashMap<String, Long> getSpawnTags() {
        return SpawnTagHandler.spawnTags;
    }

    static {
        SpawnTagHandler.spawnTags = new HashMap<String, Long>();
    }
}


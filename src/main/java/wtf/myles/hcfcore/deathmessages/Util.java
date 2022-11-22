package wtf.myles.hcfcore.deathmessages;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class Util {
	
    public static String getItemName(final ItemStack i) {
        if (i.getItemMeta().hasDisplayName()) {
            return ChatColor.stripColor(i.getItemMeta().getDisplayName());
        }
        return WordUtils.capitalizeFully(i.getType().name().replace('_', ' '));
    }

}

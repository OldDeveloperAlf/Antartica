package wtf.myles.hcfcore.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import wtf.myles.hcfcore.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Myles on 18/06/2015.
 */
public class InventoryUtils {

    public static HashMap<String, List<String>> getLores() {
        return lores;
    }

    public static void setLores(HashMap<String, List<String>> lores) {
        InventoryUtils.lores = lores;
    }

    public static HashMap<String, List<String>> lores = new HashMap<>();

    public static boolean confirmEnchants(final ItemStack item, final boolean removeUndefinied) {
        if (item == null || item == new ItemStack(Material.AIR)) {
            return false;
        }
        boolean fixed = false;
        final Map<Enchantment, Integer> enchants = item.getEnchantments();
        for (final Enchantment enchantment : enchants.keySet()) {
            final int level = enchants.get(enchantment);
            if (Main.getInstance().getMapHandler().getMaxEnchantments().containsKey(enchantment)) {
                final int max = Main.getInstance().getMapHandler().getMaxEnchantments().get(enchantment);
                if (level <= max) {
                    continue;
                }
                if (max == 0) {
                    item.removeEnchantment(enchantment);
                }
                if(item.containsEnchantment(enchantment)) {
                    item.addUnsafeEnchantment(enchantment, max);
                }
                fixed = true;
            } else {
                if (!removeUndefinied) {
                    continue;
                }
                item.removeEnchantment(enchantment);
                fixed = true;
            }
        }
        return fixed;
    }

    static {

    }


}

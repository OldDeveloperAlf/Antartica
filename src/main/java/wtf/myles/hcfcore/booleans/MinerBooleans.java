package wtf.myles.hcfcore.booleans;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Myles on 21/06/2015.
 */
public class MinerBooleans {

    public static PotionEffect haste = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1);
    public static PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1);
    public static PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 3);

    public static boolean invis(Player player) {
        if(player.getLocation().getY() <= 20) {
            if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                player.sendMessage(ChatColor.GREEN + "Invisibility activated.");
            }
            return true;
        }
        return false;
    }

    public static void removeEffects(Player p) {
        if(!p.hasPotionEffect(haste.getType()) || !p.hasPotionEffect(nightVision.getType())) {
            return;
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Class: &6Miner &c-> &6Disabled&7."));
        p.removePotionEffect(haste.getType());
        p.removePotionEffect(nightVision.getType());
    }

    public static void removeInvis(Player p) {
        if(!p.hasPotionEffect(invis.getType())) {
            return;
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvisibility disabled."));
        p.removePotionEffect(invis.getType());
    }

    public static boolean checkArmor(Player player) {
        PlayerInventory inv = player.getInventory();
        if(inv.getHelmet() == null || inv.getChestplate() == null || inv.getLeggings() == null || inv.getBoots() == null) {
            return false;
        }
        if(inv.getHelmet().getType() == Material.IRON_HELMET && inv.getChestplate().getType() == Material.IRON_CHESTPLATE && inv.getLeggings().getType() == Material.IRON_LEGGINGS && inv.getBoots().getType() == Material.IRON_BOOTS) {
            if(!player.hasPotionEffect(haste.getType()) || !player.hasPotionEffect(nightVision.getType())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Class: &6Miner &c-> &6Enabled&7."));
            }
            return true;
        }
        return false;
    }

    public static boolean checkArmorWithoutEffects(Player player) {
        PlayerInventory inv = player.getInventory();
        if (inv.getHelmet() == null || inv.getChestplate() == null || inv.getLeggings() == null || inv.getBoots() == null) {
            return false;
        }
        if (inv.getHelmet().getType() == Material.IRON_HELMET && inv.getChestplate().getType() == Material.IRON_CHESTPLATE && inv.getLeggings().getType() == Material.IRON_LEGGINGS && inv.getBoots().getType() == Material.IRON_BOOTS) {
            return true;
        }
        return false;
    }
}

package wtf.myles.hcfcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.utils.InventoryUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Myles on 18/06/2015.
 */
public class EnchantmentLimiterListener implements Listener {

    private Map<String, Long> lastArmorCheck;
    private Map<String, Long> lastSwordCheck;

    public EnchantmentLimiterListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        lastArmorCheck = new HashMap<>();
        lastSwordCheck = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled() && this.checkArmor((Player) event.getEntity())) {
            final ItemStack[] armor = ((Player) event.getEntity()).getInventory().getArmorContents();
            boolean fixed = false;
            for (int i = 0; i < armor.length; i++) {
                if (InventoryUtils.confirmEnchants(armor[i], false)) {
                    fixed = true;
                }
            }
            if (fixed) {
                Player p = (Player) event.getEntity();
                p.sendMessage(ChatColor.GREEN + "We detected that your armor had some illegal enchantments, and have reduced the invalid enchantments.");
                for(Player online : Bukkit.getOnlinePlayers()) {
                    if(online.hasPermission("command.mod")) {
                        online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] &6" + p.getDisplayName() + "&7 had some illegal enchants. We removed them."));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamager() instanceof Player && this.checkSword((Player) event.getDamager())) {
            final Player player = (Player) event.getDamager();
            final ItemStack hand = player.getItemInHand();
            if(hand.getType() != null) {
                if (InventoryUtils.confirmEnchants(hand, false)) {
                    player.setItemInHand(hand);
                    player.sendMessage(ChatColor.GREEN + "We detected that your sword had some illegal enchantments, and have reduced the invalid enchantments.");
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.hasPermission("command.mod")) {
                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] &6" + player.getDisplayName() + "&7 had some illegal enchants. We removed them."));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            final ItemStack hand = event.getPlayer().getItemInHand();
            if(hand.getType() == Material.BOW) {
                if (InventoryUtils.confirmEnchants(hand, true)) {
                    event.getPlayer().setItemInHand(hand);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "We detected that your bow had some illegal enchantments, and have reduced the invalid enchantments.");
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.hasPermission("command.mod")) {
                            online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] &6" + event.getPlayer().getDisplayName() + "&7 had some illegal enchants. We removed them."));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(final InventoryClickEvent event) {
        final HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }
        final Player player = (Player) humanEntity;
        final Inventory inventory = event.getInventory();
        if (event.getInventory().getType() == InventoryType.MERCHANT) {
            for (final ItemStack item : event.getInventory()) {
                if (item != null) {
                    InventoryUtils.confirmEnchants(item, true);
                }
            }
        }
        for (final ItemStack item : event.getInventory().getContents()) {
            InventoryUtils.confirmEnchants(item, true);
        }
    }

    @EventHandler
    public void onEntityDeathEvent(final EntityDeathEvent event) {
        final Iterator<ItemStack> iter = event.getDrops().iterator();
        while (iter.hasNext()) {
            InventoryUtils.confirmEnchants(iter.next(), true);
        }
    }

    @EventHandler
    public void onPlayerFishEvent(final PlayerFishEvent event) {
        if (event.getCaught() instanceof Item) {
            InventoryUtils.confirmEnchants(((Item) event.getCaught()).getItemStack(), true);
        }
    }

    public boolean checkArmor(final Player player) {
        final boolean check = !this.lastArmorCheck.containsKey(player.getName()) || System.currentTimeMillis() - this.lastArmorCheck.get(player.getName()) > 5000L;
        if (check) {
            this.lastArmorCheck.put(player.getName(), System.currentTimeMillis());
        }
        return check;
    }

    public boolean checkSword(final Player player) {
        final boolean check = !this.lastSwordCheck.containsKey(player.getName()) || System.currentTimeMillis() - this.lastSwordCheck.get(player.getName()) > 5000L;
        if (check) {
            this.lastSwordCheck.put(player.getName(), System.currentTimeMillis());
        }
        return check;
    }
}

package wtf.myles.hcfcore.deathmessages;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import wtf.myles.hcfcore.Main;

import java.io.File;

public class PVPTracker implements Listener
{

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getCause();
            if (e.getDamager() instanceof Player) {
                final Player damager = (Player)e.getDamager();
                final Player damaged = event.getPlayer();
                event.setTrackerDamage(new PVPDamage(damaged.getName(), event.getDamage(), damager.getName(), damager.getItemInHand()));
            }
        }
    }
    
    public class PVPDamage extends PlayerDamage
    {
        private String itemString;
        
        public PVPDamage(final String damaged, final double damage, final String damager, final ItemStack itemStack) {
            super(damaged, damage, damager);
            this.itemString = "Error";
            if (itemStack.getType() == Material.AIR) {
                this.itemString = "their fists";
            }
            else {
                this.itemString = Util.getItemName(itemStack);
            }
        }
        
        @Override
        public String getDescription() {
            return "Killed by " + this.getDamager();
        }
        
        @Override
        public String getDeathMessage() {
            
            File playerFile = new File(Main.getInstance().getDataFolder() + File.separator + "playerData" + File.separator + this.getDamaged() + ".yml");
            return ChatColor.RED + this.getDamaged() + ChatColor.YELLOW + " was slain by " + ChatColor.RED + this.getDamager()  + ChatColor.YELLOW + " using " + ChatColor.RED + this.itemString + ChatColor.YELLOW + ".";
        }
    }
}
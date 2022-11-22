package wtf.myles.hcfcore.listeners;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.handlers.SpawnTagHandler;
import wtf.myles.hcfcore.staff.StaffMode;

/**
 * Created by Myles on 25/06/2015.
 */
public class SpawnTagListener implements Listener {

    public SpawnTagListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || event.isCancelled()) {
            return;
        }
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player)event.getDamager();
        }
        else if (event.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile)event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                damager = (Player)projectile.getShooter();
            }
        }
        if (damager != null && damager != event.getEntity()) {
            if(StaffMode.hasModMode(damager)) {
                event.setCancelled(true);
                return;
            }
            SpawnTagHandler.addSeconds(damager, 60);
            SpawnTagHandler.addSeconds(((Player) event.getEntity()).getPlayer(), 60);
        }
    }

}

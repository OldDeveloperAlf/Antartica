package wtf.myles.hcfcore.deathmessages;

import java.util.List;

import net.minecraft.server.v1_7_R4.EntityHuman;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import wtf.myles.hcfcore.Main;

public class DamageListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final CustomPlayerDamageEvent event2 = new CustomPlayerDamageEvent(event);
        event2.setTrackerDamage(new UnknownDamage(((Player)event.getEntity()).getName(), event.getDamage()));
        Main.getInstance().getServer().getPluginManager().callEvent((Event)event2);
        if (event2.isCancelled()) {
            event.setCancelled(true);
        }
        else {
            event2.getTrackerDamage().setHealthAfter(((Player)event.getEntity()).getHealthScale());
            DeathMessageHandler.addDamage((Player)event.getEntity(), event2.getTrackerDamage());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (event.getDeathMessage() == null || event.getDeathMessage().isEmpty()) {
            return;
        }
        final List<Damage> record = DeathMessageHandler.getDamage(event.getEntity());
        if (record == null || record.isEmpty()) {
            event.setDeathMessage(ChatColor.RED + event.getEntity().getName() + ChatColor.YELLOW + " died.");
            return;
        }
        final Damage deathCause = record.get(record.size() - 1);
        if (deathCause instanceof PlayerDamage) {
            final Player killer = Main.getInstance().getServer().getPlayerExact(((PlayerDamage)deathCause).getDamager());
            ((CraftPlayer)event.getEntity()).getHandle().killer = (EntityHuman)((CraftPlayer)killer).getHandle();
        }
        event.setDeathMessage(deathCause.getDeathMessage());
        DeathMessageHandler.clearDamage(event.getEntity());
    }
}
package wtf.myles.hcfcore.deathmessages;


import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityTracker implements Listener
{
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getCause();
            if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
                event.setTrackerDamage(new EntityDamage(event.getPlayer().getName(), event.getDamage(), e.getDamager()));
            }
        }
    }
    
    public class EntityDamage extends MobDamage
    {
        public EntityDamage(final String damaged, final double damage, final Entity entity) {
            super(damaged, damage, entity.getType());
        }
        
        @SuppressWarnings("deprecation")
		@Override
        public String getDescription() {
            return this.getMobType().getName();
        }
        
        @SuppressWarnings("deprecation")
		@Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged()  + ChatColor.YELLOW + " was slain by a " + ChatColor.RED + this.getMobType().getName() + ChatColor.YELLOW + ".";
        }
    }
}

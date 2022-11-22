package wtf.myles.hcfcore.deathmessages;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import wtf.myles.hcfcore.Main;

public class ArrowTracker implements Listener
{
    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.getProjectile().setMetadata("ShotFromDistance", (MetadataValue)new FixedMetadataValue((Plugin) Main.getInstance(), (Object)event.getProjectile().getLocation()));
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getCause();
            if (e.getDamager() instanceof Arrow) {
                final Arrow a = (Arrow)e.getDamager();
                if (a.getShooter() instanceof Player) {
                    final Player shooter = (Player)a.getShooter();
                    for (final MetadataValue value : a.getMetadata("ShotFrom")) {
                        final Location shotFrom = (Location)value.value();
                        final double distance = shotFrom.distance(event.getPlayer().getLocation());
                        event.setTrackerDamage(new ArrowDamageByPlayer(event.getPlayer().getName(), event.getDamage(), shooter.getName(), shotFrom, distance));
                    }
                }
                else if (a.getShooter() instanceof Entity) {
                    final Entity shooter2 = (Entity)a.getShooter();
                    event.setTrackerDamage(new ArrowDamageByMob(event.getPlayer().getName(), event.getDamage(), shooter2));
                }
                else {
                    event.setTrackerDamage(new ArrowDamage(event.getPlayer().getName(), event.getDamage()));
                }
            }
        }
    }
    
    public class ArrowDamage extends Damage
    {
        public ArrowDamage(final String damaged, final double damage) {
            super(damaged, damage);
        }
        
        @Override
        public String getDescription() {
            return "Shot";
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.GOLD + this.getDamaged() + ChatColor.RED + " was shot.";
        }
    }
    
    public static class ArrowDamageByPlayer extends PlayerDamage
    {
        private Location shotFrom;
        double distance;
        
        public ArrowDamageByPlayer(final String damaged, final double damage, final String damager, final Location shotFrom, final double distance) {
            super(damaged, damage, damager);
            this.shotFrom = shotFrom;
            this.distance = distance;
        }
        
        @Override
        public String getDescription() {
            return "Shot by " + this.getDamager();
        }
        
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.YELLOW + " was shot by " + ChatColor.RED + this.getDamager() + ChatColor.YELLOW + ChatColor.YELLOW + " from " + ChatColor.BLUE + (int)this.distance + " blocks" + ChatColor.YELLOW + ".";
        }
        
        public double getDistance() {
            return this.distance;
        }
        
        public Location getShotFrom() {
            return this.shotFrom;
        }
    }
    
    public class ArrowDamageByMob extends MobDamage
    {
        public ArrowDamageByMob(final String damaged, final double damage, final Entity damager) {
            super(damaged, damage, damager.getType());
        }
        
        @SuppressWarnings("deprecation")
		@Override
        public String getDescription() {
            return "Shot by " + this.getMobType().getName();
        }
        
        @SuppressWarnings("deprecation")
		@Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.YELLOW + " was shot by a " + this.getMobType().getName() + ".";
        }
    }
}

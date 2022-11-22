package wtf.myles.hcfcore.deathmessages;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class CustomPlayerDamageEvent extends Event
{
    private static HandlerList handlerList;
    private EntityDamageEvent cause;
    private Damage trackerDamage;
    private boolean cancelled;
    
    public HandlerList getHandlers() {
        return CustomPlayerDamageEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return CustomPlayerDamageEvent.handlerList;
    }
    
    public CustomPlayerDamageEvent(final EntityDamageEvent cause) {
        super();
        this.cancelled = false;
        this.cause = cause;
    }
    
    public EntityDamageEvent getCause() {
        return this.cause;
    }
    
    public Player getPlayer() {
        return (Player)this.cause.getEntity();
    }
    
    public double getDamage() {
        return this.cause.getDamage();
    }
    
    public Damage getTrackerDamage() {
        return this.trackerDamage;
    }
    
    public void setTrackerDamage(final Damage trackerDamage) {
        this.trackerDamage = trackerDamage;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
    
    static {
        CustomPlayerDamageEvent.handlerList = new HandlerList();
    }
}
package wtf.myles.hcfcore.deathmessages;

import org.bukkit.ChatColor;

public class UnknownDamage extends Damage
{
    public UnknownDamage(final String damaged, final double damage) {
        super(damaged, damage);
    }
    
    @Override
    public String getDescription() {
        return "Unknown";
    }
    
    @Override
    public String getDeathMessage() {
        return ChatColor.RED + this.getDamaged() + ChatColor.YELLOW + " died.";
    }
}

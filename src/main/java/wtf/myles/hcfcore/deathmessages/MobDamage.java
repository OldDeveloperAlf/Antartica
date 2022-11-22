package wtf.myles.hcfcore.deathmessages;

import org.bukkit.entity.EntityType;

public abstract class MobDamage extends Damage
{
    private EntityType mobType;
    
    public MobDamage(final String damaged, final double damage, final EntityType mobType) {
        super(damaged, damage);
        this.mobType = mobType;
    }
    
    public EntityType getMobType() {
        return this.mobType;
    }
}

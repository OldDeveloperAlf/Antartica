package wtf.myles.hcfcore.deathmessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import wtf.myles.hcfcore.Main;

public class DeathMessageHandler {
	
	private static Map<String, List<Damage>> damage;

	
    public static void init() {
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new DamageListener(), (Plugin)Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new GeneralTracker(), (Plugin)Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new PVPTracker(), (Plugin)Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new EntityTracker(), (Plugin)Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new FallTracker(), (Plugin)Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new ArrowTracker(), (Plugin)Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents((Listener)new VoidTracker(), (Plugin)Main.getInstance());
    }
	
    public static List<Damage> getDamage(final Player player) {
        return DeathMessageHandler.damage.get(player.getName());
    }
    
    public static void addDamage(final Player player, final Damage addedDamage) {
        if (!DeathMessageHandler.damage.containsKey(player.getName())) {
            DeathMessageHandler.damage.put(player.getName(), new ArrayList<Damage>());
        }
        DeathMessageHandler.damage.get(player.getName()).add(addedDamage);
    }
    
    public static void clearDamage(final Player player) {
        DeathMessageHandler.damage.remove(player.getName());
    }
    
    static {
        DeathMessageHandler.damage = new HashMap<String, List<Damage>>();
    }
	
}

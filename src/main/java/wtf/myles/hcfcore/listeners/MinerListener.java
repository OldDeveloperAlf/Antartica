package wtf.myles.hcfcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.booleans.MinerBooleans;

/**
 * Created by Myles on 22/06/2015.
 */
public class MinerListener implements Listener {

    public MinerListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();

        new BukkitRunnable() {
            public void run() {
                if(MinerBooleans.checkArmor(p)) {
                    p.addPotionEffect(MinerBooleans.nightVision);
                    p.addPotionEffect(MinerBooleans.haste);
                    return;
                } else {
                    MinerBooleans.removeEffects(p);
                }
            }
        }.runTaskLater(Main.getInstance(), 1L);
    }
}

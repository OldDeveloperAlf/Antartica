package wtf.myles.hcfcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.managers.ProfileManager;
import wtf.myles.hcfcore.objects.Profile;

/**
 * Created by Myles on 14/07/2015.
 */
public class OreListener implements Listener {

    ProfileManager pm = Main.getInstance().getProfileManager();

    public OreListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void handleDiamondBlockPlace(BlockPlaceEvent e) {
        if(!e.isCancelled() && e.getBlock().getType() == Material.DIAMOND_ORE) {
            e.getBlock().setMetadata("diamond", (MetadataValue) new FixedMetadataValue(Main.getInstance(), true));
        }
    }

    @EventHandler
    public void handleDiamondBlockBreak(BlockBreakEvent e) {
        if (!e.isCancelled() && !e.getBlock().hasMetadata("diamond") && e.getBlock().getType() == Material.DIAMOND_ORE) {
            Player p = e.getPlayer();
            for (Profile profs : Main.getInstance().getProfileManager().getProfiles()) {
                if (profs.isStaff() && profs.isAlerts() && Bukkit.getPlayer(profs.getUuid()) != null) {
                    int diamonds = 0;
                    for(int x = -5; x < 5; x++) {
                        for(int y = -5; y < 5; y++) {
                            for(int z = -5; z < 5; z++) {
                                Block block = e.getBlock().getLocation().add(x, y, z).getBlock();
                                if(block.getType() == Material.DIAMOND_ORE && !block.hasMetadata("diamond")) {
                                    diamonds++;
                                    block.setMetadata("diamond", (MetadataValue) new FixedMetadataValue(Main.getInstance(), true));
                                }
                            }
                        }
                    }
                    String fixedBlock = e.getBlock().getType().toString().toLowerCase().replace("_", " ");
                    Bukkit.getPlayer(profs.getUuid()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] " + p.getDisplayName() + "&7 found some &c" + fixedBlock + "&7. &c[" + diamonds + "]"));
                }
            }
        }
    }

    @EventHandler
    public void handleGoldPlaceEvent(BlockPlaceEvent e) {
        if(!e.isCancelled() && e.getBlock().getType() == Material.GOLD_ORE) {
            e.getBlock().setMetadata("gold", (MetadataValue) new FixedMetadataValue(Main.getInstance(), true));
        }
    }

    @EventHandler
    public void handleGoldBreakEvent(BlockBreakEvent e) {
        if (!e.isCancelled() && !e.getBlock().hasMetadata("gold") && e.getBlock().getType() == Material.GOLD_ORE) {
            Player p = e.getPlayer();
            int gold = 0;
            for (int x = -5; x < 5; x++) {
                for (int y = -5; y < 5; y++) {
                    for (int z = -5; z < 5; z++) {
                        Block block = e.getBlock().getLocation().add(x, y, z).getBlock();
                        if (block.getType() == Material.GOLD_ORE && !block.hasMetadata("gold")) {
                            gold++;
                            block.setMetadata("gold", (MetadataValue) new FixedMetadataValue(Main.getInstance(), true));
                        }
                    }
                }
            }
            String fixedBlock = e.getBlock().getType().toString().toLowerCase().replace("_", " ");
            for (Profile profs : Main.getInstance().getProfileManager().getProfiles()) {
                if (profs.isStaff() && profs.isAlerts() && Bukkit.getPlayer(profs.getUuid()) != null) {
                    Bukkit.getPlayer(profs.getUuid()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] " + p.getDisplayName() + "&7 found some &c" + fixedBlock + "&7. &c[" + gold + "]"));
                }
            }
        }
    }

    @EventHandler
    public void handleironPlaceEvent(BlockPlaceEvent e) {
        if(!e.isCancelled() && e.getBlock().getType() == Material.IRON_ORE) {
            e.getBlock().setMetadata("iron", (MetadataValue) new FixedMetadataValue(Main.getInstance(), true));
        }
    }

    @EventHandler
    public void handleironBreakEvent(BlockBreakEvent e) {
        if (!e.isCancelled() && !e.getBlock().hasMetadata("iron") && e.getBlock().getType() == Material.IRON_ORE) {
            Player p = e.getPlayer();
            int iron = 0;
            for (int x = -5; x < 5; x++) {
                for (int y = -5; y < 5; y++) {
                    for (int z = -5; z < 5; z++) {
                        Block block = e.getBlock().getLocation().add(x, y, z).getBlock();
                        if (block.getType() == Material.IRON_ORE && !block.hasMetadata("iron")) {
                            iron++;
                            block.setMetadata("iron", (MetadataValue) new FixedMetadataValue(Main.getInstance(), true));
                        }
                    }
                }
            }
            String fixedBlock = e.getBlock().getType().toString().toLowerCase().replace("_", " ");
            for (Profile profs : Main.getInstance().getProfileManager().getProfiles()) {
                if (profs.isStaff() && profs.isAlerts() && Bukkit.getPlayer(profs.getUuid()) != null) {
                    Bukkit.getPlayer(profs.getUuid()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] " + p.getDisplayName() + "&7 found some &c" + fixedBlock + "&7. &c[" + iron + "]"));
                }
            }
        }
    }
    
}

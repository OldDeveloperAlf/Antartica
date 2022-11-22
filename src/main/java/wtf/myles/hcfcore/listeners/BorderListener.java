package wtf.myles.hcfcore.listeners;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.imagemessage.ImageMessage;

import java.io.File;

/**
 * Created by Myles on 18/06/2015.
 */
public class BorderListener implements Listener {

    public static int BORDER_SIZE;

    public BorderListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (Math.abs(event.getBlock().getX()) > BorderListener.BORDER_SIZE || Math.abs(event.getBlock().getZ()) > BorderListener.BORDER_SIZE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (Math.abs(event.getBlock().getX()) > BorderListener.BORDER_SIZE || Math.abs(event.getBlock().getZ()) > BorderListener.BORDER_SIZE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPortal(final PlayerPortalEvent event) {
        if (Math.abs(event.getTo().getBlockX()) > BorderListener.BORDER_SIZE || Math.abs(event.getTo().getBlockZ()) > BorderListener.BORDER_SIZE) {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BorderListener.BORDER_SIZE) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BorderListener.BORDER_SIZE) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "That portal's location is past the border. It has been moved inwards.");
        }
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
            return;
        }
        if (event.getTo().distance(event.getFrom()) < 0.0 || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            return;
        }
        if (Math.abs(event.getTo().getBlockX()) > BorderListener.BORDER_SIZE || Math.abs(event.getTo().getBlockZ()) > BorderListener.BORDER_SIZE) {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BorderListener.BORDER_SIZE) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BorderListener.BORDER_SIZE) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "That location is past the border.");
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if ((Math.abs(event.getTo().getBlockX()) > BorderListener.BORDER_SIZE || Math.abs(event.getTo().getBlockZ()) > BorderListener.BORDER_SIZE)) {
            if (event.getPlayer().getVehicle() != null) {
                event.getPlayer().getVehicle().eject();
            }
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BorderListener.BORDER_SIZE) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BorderListener.BORDER_SIZE) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "You have hit the border!");
        }
    }
}

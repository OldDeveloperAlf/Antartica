package wtf.myles.hcfcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import wtf.myles.hcfcore.Main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Myles on 22/06/2015.
 */
public class EnderpearlListener implements Listener {

    public EnderpearlListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if ((event.isCancelled()) || (!(event.getEntity().getShooter() instanceof Player))) {
            return;
        }
        Player shooter = (Player)event.getEntity().getShooter();
        if ((event.getEntity() instanceof EnderPearl)) {
            enderpearlCooldown.put(shooter.getName(), Long.valueOf(System.currentTimeMillis() + 16000L));
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if ((event.getItem() == null) || ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) || (event.getItem().getType() != Material.ENDER_PEARL)) {
            return;
        }
        if ((enderpearlCooldown.containsKey(event.getPlayer().getName())) && (((Long)enderpearlCooldown.get(event.getPlayer().getName())).longValue() > System.currentTimeMillis()))
        {
            long millisLeft = ((Long)enderpearlCooldown.get(event.getPlayer().getName())).longValue() - System.currentTimeMillis();
            double value = millisLeft / 1000.0D;
            double sec = Math.round(10.0D * value) / 10.0D;
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if ((event.isCancelled()) || (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            return;
        }
        Location target = event.getTo();
        Location from = event.getFrom();
        Material mat = event.getTo().getBlock().getType();
        if (((mat != Material.THIN_GLASS) && (mat != Material.IRON_FENCE)) || ((clippingThrough(target, from, 0.65D)) || (((mat == Material.FENCE) || (mat == Material.NETHER_FENCE)) && (clippingThrough(target, from, 0.45D)))))
        {
            event.setTo(from);
            return;
        }
        target.setX(target.getBlockX() + 0.5D);
        target.setZ(target.getBlockZ() + 0.5D);
        event.setTo(target);
    }

    public boolean clippingThrough(Location target, Location from, double thickness)
    {
        return ((from.getX() > target.getX()) && (from.getX() - target.getX() < thickness)) || ((target.getX() > from.getX()) && (target.getX() - from.getX() < thickness)) || ((from.getZ() > target.getZ()) && (from.getZ() - target.getZ() < thickness)) || ((target.getZ() > from.getZ()) && (target.getZ() - from.getZ() < thickness));
    }

    public static Map<String, Long> getEnderpearlCooldown()
    {
        return enderpearlCooldown;
    }

    private static Map<String, Long> enderpearlCooldown = new HashMap();

}

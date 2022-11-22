package wtf.myles.hcfcore.listeners;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.handlers.SpawnTagHandler;
import wtf.myles.hcfcore.managers.ProfileManager;
import wtf.myles.hcfcore.managers.ServerManager;
import wtf.myles.hcfcore.objects.Profile;
import wtf.myles.hcfcore.staff.StaffMode;
import wtf.myles.hcfcore.utils.Cooldowns;
import wtf.myles.hcfcore.utils.SignGUI;

import java.text.DecimalFormat;

/**
 * Created by Myles on 22/06/2015.
 */
public class PlayerListener implements Listener {

    public PlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }
    ProfileManager pm = Main.getInstance().getProfileManager();

    @EventHandler
    public void handleJoin(PlayerJoinEvent e) {
        e.setJoinMessage((String)null);
        final Player p = e.getPlayer();
        pm.createProfile(p.getUniqueId());
        Profile prof = pm.getProfile(p.getUniqueId());

        if(!p.hasPlayedBefore()) {
            p.teleport(Main.getInstance().getConfigManager().getSpawn());
        }

        if(!prof.isAlerts() && prof.isStaff()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[!&c&7] &aAlerts have been enabled."));
        }

        for(Player online : StaffMode.playerList) {
            if(p.canSee(online) && !p.hasPermission("command.mod")) {
                p.hidePlayer(online);
            }
        }

        if(p.hasPermission("command.mod")) {
            prof.setStaff(true);
            prof.setAlerts(true);
            new BukkitRunnable() {
                public void run() {
                    StaffMode.toggleModMode(p);
                }
            }.runTaskLater(Main.getInstance(), 10L);
            for(Profile profile : Main.getInstance().getProfileManager().getProfiles()) {
                if(profile.isStaff() && Bukkit.getPlayer(profile.getUuid()) != null) {
                    Bukkit.getPlayer(profile.getUuid()).sendMessage("Test");
                }
            }
        }

        if(p.hasPermission("command.vip")) {
            if(!prof.isDonator()) {
                p.sendMessage(ChatColor.YELLOW + "Updated your profile to donator status.");
                prof.setDonator(true);
            }
        }
    }



    @EventHandler
    public void handleDeath(PlayerDeathEvent e){
        SpawnTagHandler.removeTag(e.getEntity().getPlayer());
    }

    @EventHandler
    public void handlePortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if(e.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }
        if(e.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if(!Main.getRegionAt(e.getFrom()).allows(DefaultFlag.PVP)) {
                e.setCancelled(true);
                p.teleport(Bukkit.getWorld("world").getSpawnLocation());

            }
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent e){
        e.setQuitMessage((String)null);
        Player p = e.getPlayer();
        Profile prof = pm.getProfile(p.getUniqueId());
        pm.getProfiles().remove(prof);
        if(StaffMode.hasModMode(p)) {
            StaffMode.toggleModMode(p);
        }
        for (Player online : StaffMode.playerList) {
            if(!p.canSee(online)) {
                p.showPlayer(online);
            }
        }
    }



    @EventHandler
    public void handleInteractEvent(final PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission("command.admin")) {
            return;
        }
        if(p.getGameMode() != GameMode.CREATIVE) {
            return;
        }
        if(!p.isSneaking()) {
            return;
        }
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() != Material.AIR) {
            if(e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
                final Sign sign = (Sign)e.getClickedBlock().getState();
                Main.getInstance().getSignGUI().open(p, sign.getLines(), new SignGUI.SignGUIListener() {
                    public void onSignDone(Player player, String[] strings)
                    {
                        for (int i = 0; i <= 3; i++) {
                            sign.setLine(i, ChatColor.translateAlternateColorCodes('&', strings[i]));
                        }

                        sign.update();
                    }
                });

            }
        }
    }

    @EventHandler
    public void handleCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        for(Profile profs : Main.getInstance().getProfileManager().getProfiles()) {
            if (profs.isCommandSpy() && Bukkit.getPlayer(profs.getUuid()) != null) {
                Bukkit.getPlayer(profs.getUuid()).sendMessage(ChatColor.RED + "[COMMANDSPY] " + p.getName() + " > " + e.getMessage());
            }
        }

        if(e.getMessage().equalsIgnoreCase("/core")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GREEN + "Antartica - Made by " + ChatColor.UNDERLINE + "Criminal");
        }
    }

    @EventHandler
    public void handleConsumeEvent(PlayerItemConsumeEvent e) {
        for(String pots : Main.getInstance().getConfig().getStringList("blockedpotions")) {
            String[] split = pots.split(":");
            int id = Integer.parseInt(split[0]);
            short data = Short.parseShort(split[1]);

            if(e.getItem().getTypeId() == id && e.getItem().getDurability() == data) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You can not use that potion as it is disabled!");
                e.getPlayer().updateInventory();
            }
        }
    }

    @EventHandler
    public void handlePotionSplash(PotionSplashEvent e) {
        for(String pots : Main.getInstance().getConfig().getStringList("blockedpotions")) {
            String[] split = pots.split(":");
            short data = Short.parseShort(split[1]);
            ThrownPotion potion = e.getPotion();
            if(potion.getItem().getDurability() == data) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handleAsyncChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Profile profile = pm.getProfile(p.getUniqueId());
        String message = e.getMessage();
        if(p.isOp() || p.hasPermission("command.admin")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            e.setMessage(message);
        }
        if(!p.hasPermission("command.mod") && ServerManager.getInstance().isChatMuted()) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Chat is currently restricted.");
        }

        if(profile.isStaffChat()) {
            e.setCancelled(true);
            for(Player online : Bukkit.getOnlinePlayers()) {
                if(online.hasPermission("command.mod")) {
                    online.sendMessage(ChatColor.RED + p.getName() + ": " + e.getMessage());
                }
            }
        }

        if(e.getMessage().equalsIgnoreCase("!reload")) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Reloading...");
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
            Bukkit.getPluginManager().enablePlugin(Main.getInstance());
            Bukkit.broadcastMessage(ChatColor.RED + "Antatica v" + Main.getInstance().getDescription().getVersion() + " by Criminal has been reloaded!");
        }

        if(!p.hasPermission("command.mod")) {
            if(ServerManager.getInstance().isChatMuted()) {
                return;
            }
            if(ServerManager.getInstance().getChatSlow() > 1){
                if(Cooldowns.calculateRemainder(ServerManager.getInstance().getSlowCooldowns().get(p.getName())) < 0L) {
                    Cooldowns.calculateRemainder(ServerManager.getInstance().getSlowCooldowns().put(p.getName(), System.currentTimeMillis() + ServerManager.getInstance().getChatSlow() * 1000));
                } else {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You can not talk for another " + new DecimalFormat("##.#").format(Cooldowns.calculateRemainder(ServerManager.getInstance().getSlowCooldowns().get(p.getName())) / 1000.0D) + " seconds.");
                    return;
                }
            }
        }
    }


    @EventHandler
    public void handleSignChangeEvent(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("command.admin")) {
            return;
        }
        for (int i = 0; i <= 3; i++) {
            String line = event.getLine(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            event.setLine(i, line);
        }
    }

    @EventHandler
    public void handleLoginEvent(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        Profile profile = pm.getProfile(p.getUniqueId());

        if(e.getResult() == PlayerLoginEvent.Result.KICK_FULL && !ServerManager.getInstance().isLocked()) {
            if(profile.isStaff() || profile.isDonator()) {
                e.allow();
            } else {
                String message = Main.getInstance().getConfig().getString("server-full-msg");
                message = Main.getInstance().colourMessage(message);
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
            }
        }
        else if(ServerManager.getInstance().isLocked()) {
            if(profile.isStaff()) {
                e.allow();
            } else {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Server is locked.");
            }
        }
    }
}

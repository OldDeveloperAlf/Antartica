package wtf.myles.hcfcore.staff;

import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.utils.ItemUtil;

import java.util.*;


/**
 * Created by Myles on 22/06/2015.
 */
public class StaffMode extends ItemUtil implements Listener {

    public StaffMode() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public static List<Player> playerList = new ArrayList<>();
    public static Map<Player, Player> followingPlayers = new HashMap<>();
    public static Map<Player, Location> playersLocations = new HashMap<>();
    public static Map<Player, ItemStack[]> playersInvs = new HashMap<>();
    public static Map<Player, GameMode> playersGms = new HashMap<>();
    public static Map<Player, Player> examineTasks = new HashMap<>();
    public static Map<Player, Player> banReasonQueue = new HashMap();

    public static List<Player> getPlayerList() {
        return playerList;
    }

    public static Map<Player, Player> getFollowingPlayers() {
        return followingPlayers;
    }

    public static Map<Player, Location> getPlayersLocations() {
        return playersLocations;
    }

    public static Map<Player, ItemStack[]> getPlayersInvs() {
        return playersInvs;
    }

    public static Map<Player, GameMode> getPlayersGms() {
        return playersGms;
    }

    public static Map<Player, Player> getExamineTasks() {
        return examineTasks;
    }

    public static boolean hasModMode(Player p) {
        return playerList.contains(p);
    }

    public static boolean toggleModMode(Player p) {
        if(!playerList.contains(p)) {
            playerList.add(p);
            playersLocations.put(p, p.getLocation());
            playersInvs.put(p, p.getInventory().getContents());
            playersGms.put(p, p.getGameMode());

            p.setGameMode(GameMode.CREATIVE);
            p.getInventory().clear();
            p.getInventory().setArmorContents((ItemStack[])null);

            for (Player nonPerm : Bukkit.getServer().getOnlinePlayers()) {
                if (!nonPerm.hasPermission("command.mod")) {
                    nonPerm.hidePlayer(p);
                } else {
                    nonPerm.showPlayer(p);
                }
            }

            setupPlayerItems(p);
            p.setMetadata("invisible", (MetadataValue)new FixedMetadataValue(Main.getInstance(), true));
            return true;
        }
        if(followingPlayers.containsKey(p)) {
            followingPlayers.remove(p);
        }
        playerList.remove(p);
        p.getInventory().clear();
        p.getInventory().setArmorContents((ItemStack[])null);
        if(!p.hasPermission("command.mod") && playersLocations.containsKey(p)) {
            p.teleport((Location)playersLocations.get(p));
        }

        if(playersInvs.containsKey(p)) {
            p.getInventory().setContents((ItemStack[])playersInvs.get(p));
        }

        if(playersGms.containsKey(p)) {
            p.setGameMode((GameMode)playersGms.get(p));
        }

        playersLocations.remove(p);
        playersInvs.remove(p);
        playersGms.remove(p);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!player.canSee(p)) {
                player.showPlayer(p);
            }
        }
        p.removeMetadata("invisible", Main.getInstance());
        return false;
    }

    private static void setupPlayerItems(Player p) {
        Inventory inventory = p.getInventory();
        inventory.setItem(0, createItem(Material.COMPASS, 1, ChatColor.RED + "Teleport compass."));
        inventory.setItem(1, createItem(Material.BOOK, 1, ChatColor.RED + "Examine player."));
        inventory.setItem(2, createItem(Material.LEASH, 1, ChatColor.RED + "Follow a player."));
        inventory.setItem(4, createItem(Material.CARPET, ""));
        inventory.setItem(8, createItem(Material.RECORD_5, ChatColor.RED + "Teleport to a random player."));
    }

    public static void startExaminationTask(Player p, Player personToBeExamined) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + "Examining " + ChatColor.BOLD + personToBeExamined.getName());

        for(int i = 0; i < 36; i++) {
            ItemStack is = personToBeExamined.getInventory().getItem(i);
            inv.setItem(i, is);
        }

        inv.setItem(36, personToBeExamined.getInventory().getHelmet());
        inv.setItem(37, personToBeExamined.getInventory().getChestplate());
        inv.setItem(38, personToBeExamined.getInventory().getLeggings());
        inv.setItem(39, personToBeExamined.getInventory().getBoots());

        inv.setItem(40, personToBeExamined.getItemInHand());


        for(int i = 0; i < 3; i++) {
            inv.setItem(41 + i, new ItemStack(Material.THIN_GLASS, 1));
        }

        inv.setItem(44, createItem(Material.FIREBALL, 1, ChatColor.RED + "Ban: " + ChatColor.BOLD + personToBeExamined.getName()));

        inv.setItem(45, createItem(Material.SPECKLED_MELON, (int)personToBeExamined.getHealth(), ChatColor.GREEN + "Health"));

        ItemStack potionEffectItem = createItem(Material.BREWING_STAND, personToBeExamined.getActivePotionEffects().size(), ChatColor.LIGHT_PURPLE + "Active Potion Effects");

        for(PotionEffect pe : personToBeExamined.getActivePotionEffects()) {
            addLore(potionEffectItem, ChatColor.GRAY + pe.getType().getName() + " " + (pe.getAmplifier() + 1) + ": " + pe.getDuration() / 20);
        }

        inv.setItem(46, potionEffectItem);

        p.openInventory(inv);

    }

    public static void updateExaminationTask(Player p, Player personToBeExamined) {
        if(!p.isOnline()) {
            p.closeInventory();
            return;
        }
        Inventory inv = p.getOpenInventory().getTopInventory();

        for(int i = 0; i < 36; i++) {
            ItemStack is = personToBeExamined.getInventory().getItem(i);
            inv.setItem(i, is);
        }

        inv.setItem(36, personToBeExamined.getInventory().getHelmet());
        inv.setItem(37, personToBeExamined.getInventory().getChestplate());
        inv.setItem(38, personToBeExamined.getInventory().getLeggings());
        inv.setItem(39, personToBeExamined.getInventory().getBoots());

        inv.setItem(40, personToBeExamined.getItemInHand());

        for(int i = 0; i < 3; i++) {
            inv.setItem(41 + i, new ItemStack(Material.THIN_GLASS, 1));
        }

        inv.setItem(44, createItem(Material.FIREBALL, 1, ChatColor.RED + "Ban: " + ChatColor.BOLD + personToBeExamined.getName()));

        inv.setItem(45, createItem(Material.SPECKLED_MELON, (int)personToBeExamined.getHealth(), ChatColor.GREEN + "Health"));

        ItemStack potionEffectItem = createItem(Material.BREWING_STAND, personToBeExamined.getActivePotionEffects().size(), ChatColor.LIGHT_PURPLE + "Active Potion Effects");

        for(PotionEffect pe : personToBeExamined.getActivePotionEffects()) {
            addLore(potionEffectItem, ChatColor.GRAY + pe.getType().getName() + " " + (pe.getAmplifier() + 1) + ": " + pe.getDuration() / 20);
        }

        inv.setItem(46, potionEffectItem);
    }

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent e) {
        if(playerList.contains(e.getWhoClicked())) {
            if(examineTasks.containsKey(e.getWhoClicked()) && e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.FIREBALL)) {
                Player p = (Player)e.getWhoClicked();
                p.sendMessage(ChatColor.RED + "Please enter a ban reason.");
                banReasonQueue.put(p, (Player)examineTasks.get(e.getWhoClicked()));
                e.getWhoClicked().closeInventory();
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handleInteractEvent(PlayerInteractEvent e) {
        if(playerList.contains(e.getPlayer())) {
            if(e.getAction().name().contains("RIGHT")) {
                if(e.getPlayer().getItemInHand().getType() == Material.RECORD_5) {
                    List<Player> playersOnline = new ArrayList<>();
                    for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        playersOnline.add(onlinePlayers);
                    }
                    for(Player player : playerList) {
                        playersOnline.remove(player);
                    }
                    if(playersOnline.size() <= 0) {
                        e.getPlayer().sendMessage(ChatColor.RED + "There are no players online.");
                        e.setCancelled(true);
                        return;
                    }
                    Random random = new Random();
                    int i = random.nextInt(playersOnline.size());
                    Player p = (Player)playersOnline.get(i);
                    e.getPlayer().teleport(p);

                    e.getPlayer().sendMessage(ChatColor.BLUE + "Teleported to " + p.getName());
                }
                if(e.getClickedBlock() != null) {
                    if(e.getClickedBlock().getType() == Material.CHEST) {
                        Chest chest = (Chest) e.getClickedBlock().getState();
                        Inventory inv = Bukkit.createInventory(null, chest.getInventory().getSize(), "Fake Chest");
                        inv.setContents(chest.getInventory().getContents());
                        e.getPlayer().openInventory(inv);
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.BLUE + "Fake chest being opened.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleDrop(PlayerDropItemEvent e) {
        if(playerList.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBreak(BlockBreakEvent e) {
        if(playerList.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void handleRespawn(final PlayerRespawnEvent e) {
        new BukkitRunnable() {
            public void run() {
                if(!playerList.contains(e.getPlayer())) {
                    StaffMode.toggleModMode(e.getPlayer());
                }
            }
        }.runTaskLater(Main.getInstance(), 10L);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void handleInventoryClose(InventoryCloseEvent e) {
        if (examineTasks.containsKey(e.getPlayer())) {
            examineTasks.remove(e.getPlayer());
        }
    }


    @EventHandler(priority=EventPriority.HIGHEST)
    public void handlePlayerInteract(PlayerInteractEntityEvent e) {
        if (playerList.contains(e.getPlayer())) {
            Player clicked = (Player)e.getRightClicked();
            if(clicked.getType() == EntityType.PLAYER) {
                if(e.getPlayer().getItemInHand().getType() == Material.BOOK) {
                    startExaminationTask(e.getPlayer(), ((Player) e.getRightClicked()));
                    examineTasks.put(e.getPlayer(), ((Player) e.getRightClicked()));
                }
            }
        }
    }



    @EventHandler
    public void handlePickupItem(PlayerPickupItemEvent e) {
        if(playerList.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent e) {
        if(playerList.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent e) {
        if(hasModMode(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onEntityTargetPlayer(EntityTargetEvent e)
    {
        if (!(e.getTarget() instanceof Player)) {
            return;
        }
        Player target = (Player)e.getTarget();
        if (playerList.contains(target))
        {
            e.setCancelled(true);
            if ((e.getEntity() instanceof ExperienceOrb))
            {
                repellExpOrb(target, (ExperienceOrb)e.getEntity());
                e.setTarget(null);
            }
        }
    }

    private void repellExpOrb(Player player, ExperienceOrb orb)
    {
        Location pLoc = player.getLocation();
        Location oLoc = orb.getLocation();
        Vector dir = oLoc.toVector().subtract(pLoc.toVector());
        double dx = Math.abs(dir.getX());
        double dz = Math.abs(dir.getZ());
        if ((dx == 0.0D) && (dz == 0.0D)) {
            dir.setX(0.001D);
        }
        if ((dx < 3.0D) && (dz < 3.0D))
        {
            Vector nDir = dir.normalize();
            Vector newV = nDir.clone().multiply(0.3D);

            newV.setY(0);
            orb.setVelocity(newV);
            if ((dx < 1.0D) && (dz < 1.0D)) {
                orb.teleport(oLoc.clone().add(nDir.multiply(1.0D)), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
            if ((dx < 0.5D) && (dz < 0.5D)) {
                orb.remove();
            }
        }
    }
}

package wtf.myles.hcfcore.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.server.v1_7_R4.World;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.nms.FixedVillager;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Myles on 19/06/2015.
 */
public class CombatLoggerListener implements Listener {

    public CombatLoggerListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public static GameProfile getGameProfile(final String name, final UUID id) {
        return new GameProfile(id, name);
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("CombatLogger")) {
            final String playerName = event.getEntity().getCustomName().substring(2);
            if (event.getEntity().getKiller() != null) {
                for (final ItemStack item : (ItemStack[])event.getEntity().getMetadata("CombatLogger").get(0).value()) {
                    event.getDrops().add(item);
                }
            }
            Player target = Main.getInstance().getServer().getPlayer(playerName);
            if (target == null) {
                final MinecraftServer server = ((CraftServer)Main.getInstance().getServer()).getServer();
                final EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), getGameProfile(playerName, Main.getInstance().getServer().getOfflinePlayer(playerName).getUniqueId()), new PlayerInteractManager((World)server.getWorldServer(0)));
                target = (Player)entity.getBukkitEntity();
                if (target != null) {
                    target.loadData();
                }
            }
            final EntityHuman humanTarget = ((CraftHumanEntity)target).getHandle();
            target.getInventory().clear();
            target.getInventory().setArmorContents((ItemStack[])null);
            humanTarget.setHealth(0.0f);
            target.saveData();
        }
    }

    @EventHandler
    public void onEntityInteract(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked().hasMetadata("CombatLogger")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDespawn(final ChunkUnloadEvent event) {
        for (final Entity entity : event.getChunk().getEntities()) {
            if (entity.hasMetadata("CombatLogger") && !entity.isDead()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(e.getPlayer().hasMetadata("logged")) {
            e.getPlayer().removeMetadata("logged", Main.getInstance());
        }
        if(e.getPlayer().getLocation().getBlockY() <= 0) {
            return;
        }
        if(e.getPlayer().isDead()) {
            return;
        }

        boolean enemyWithinRange = false;
        for(Entity entity : e.getPlayer().getNearbyEntities(40.0D, 256.0D, 40.0D)) {
            if(entity instanceof Player) {
                final Player other = (Player)entity;
                if (other.hasMetadata("invisible")) {
                    continue;
                }
                FPlayer p = FPlayers.getInstance().getByPlayer(other);
                FPlayer toBeChecked = FPlayers.getInstance().getByPlayer(e.getPlayer());
                /*if(p.getRelationTo(toBeChecked) == Relation.ALLY || p.getRelationTo(toBeChecked) == Relation.MEMBER || p.getRelationTo(toBeChecked) == Relation.TRUCE) {
                    continue;
                }*/
                enemyWithinRange = true;
            }
        }
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && enemyWithinRange) {
            String playerName = ChatColor.RED.toString() + e.getPlayer().getName();
            ItemStack armor[] = e.getPlayer().getInventory().getArmorContents();
            ItemStack inventoryItems[] = e.getPlayer().getInventory().getContents();
            ItemStack[] drops = new ItemStack[armor.length + inventoryItems.length];
            System.arraycopy(armor, 0, drops, 0, armor.length);
            System.arraycopy(inventoryItems, 0, drops, armor.length, inventoryItems.length);
            final FixedVillager fixedVillager = new FixedVillager((World)((CraftWorld)e.getPlayer().getWorld()).getHandle());
            Location location = e.getPlayer().getLocation();
            fixedVillager.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            int i = MathHelper.floor(fixedVillager.locX / 16.0);
            int j = MathHelper.floor(fixedVillager.locZ / 16.0);
            World world = (World)((CraftWorld)e.getPlayer().getWorld()).getHandle();
            world.getChunkAt(i, j).a(fixedVillager);
            world.entityList.add(fixedVillager);
            try {
                Method method = world.getClass().getDeclaredMethod("a", net.minecraft.server.v1_7_R4.Entity.class);
                method.setAccessible(true);
                method.invoke(world, fixedVillager);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            final Villager villager = (Villager)fixedVillager.getBukkitEntity();
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
            villager.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100));
            villager.setMetadata("CombatLogger", (MetadataValue)new FixedMetadataValue(Main.getInstance(), (Object)drops));
            villager.setAgeLock(true);

            int potions = 0;
            boolean gapple = false;
            for (final ItemStack itemStack : e.getPlayer().getInventory().getContents()) {
                if (itemStack != null) {
                    if (itemStack.getType() == Material.POTION && itemStack.getDurability() == 16421) {
                        ++potions;
                    }
                    else if (!gapple && itemStack.getType() == Material.GOLDEN_APPLE && itemStack.getDurability() == 1) {
                        potions += 15;
                        gapple = true;
                    }
                }
            }
            villager.setMaxHealth(potions * 3.5 + e.getPlayer().getHealth());
            villager.setHealth(villager.getMaxHealth());
            villager.setCustomName(playerName);
            villager.setCustomNameVisible(true);

            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!villager.isDead() && villager.isValid()) {
                        villager.remove();
                    }
                }
            }, 600L);
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        for (final Entity entity : event.getPlayer().getWorld().getEntitiesByClass(Villager.class)) {
            final Villager villager = (Villager)entity;
            if (villager.isCustomNameVisible() && ChatColor.stripColor(villager.getCustomName()).equals(event.getPlayer().getName())) {
                villager.remove();
            }
        }
    }

}

package wtf.myles.hcfcore.commands;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.BlockIterator;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;

import java.util.*;

/**
 * Created by Myles on 03/07/2015.
 */
public class LOL extends CommandBase implements Listener {

    private Map<UUID, Integer> tasks = new HashMap<>();
    private Map<UUID, List<EntityPlayer>> sets = new HashMap<>();

    public LOL() {
        super("lol", "just lol", Category.DEFAULT, true);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            spawnEntities(p, "Dovah", "Jhu");
        } else if(args.length == 2) {
            spawnEntities(p, args[0], args[1]);
        } else if(args.length == 1) {
            spawnEntities(p, "Dovah", args[1]);
        } else {
            spawnEntities(p, "Dovah", "Jhu");
        }
    }

    private void spawnEntities(final Player player, String name, String otherName) {
        if(tasks.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(this.tasks.get(player.getUniqueId()));
            for(EntityPlayer e : this.sets.get(player.getUniqueId())) {
                e.setInvisible(true);
                e.setLocation(0, -100, 0, 0, 0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(e));
            }
            this.sets.remove(player.getUniqueId());
            this.tasks.remove(player.getUniqueId());
        }
        OfflinePlayer nameId = Bukkit.getOfflinePlayer(name);
        OfflinePlayer otherNameId = Bukkit.getOfflinePlayer(otherName);
        name = nameId.getName();
        otherName = otherNameId.getName();

        Location l = player.getLocation();
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.fromString(nameId.getUniqueId().toString()), name), new PlayerInteractManager(nmsWorld));
        EntityPlayer npc2 = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.fromString(otherNameId.getUniqueId().toString()), otherName), new PlayerInteractManager(nmsWorld));
        l.setPitch(0f);
        BlockIterator i = new BlockIterator(l);
        int distance = 0;
        Location loc = null;
        while(i.hasNext()) {
            loc = i.next().getLocation();
            if(distance == 3) {
                npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0f);
                npc2.setLocation(loc.getX(), loc.getY(), loc.getZ() + 0.5, loc.getYaw(), 0f);
                break;
            }
            distance ++;
        }
        npc2.setSneaking(true);
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc2));
        List<EntityPlayer> entities = new ArrayList<EntityPlayer>();
        entities.add(npc);
        entities.add(npc2);
        this.sets.put(player.getUniqueId(), entities);
        Integer task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            int i = 20;
            @Override
            public void run() {
                if(i <= 0) {
                    for(EntityPlayer e : sets.get(player.getUniqueId())) {
                        e.setInvisible(true);
                        e.setLocation(0, -10, 0, 0, 0);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(e));
                    }
                    Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()));
                    sets.remove(player.getUniqueId());
                    tasks.remove(player.getUniqueId());
                }else {
                    EntityPlayer e = sets.get(player.getUniqueId()).get(0);
                    if(e.isSneaking()) {
                        e.setSneaking(false);
                        player.playSound(player.getLocation(), Sound.FALL_BIG, 1000.0F, 1000.0F);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(e));
                    }else {
                        e.setSneaking(true);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(e));
                    }
                    i --;
                }

            }
        }, 5L, 5L);
        this.tasks.put(player.getUniqueId(), task);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if (this.tasks.containsKey(player.getUniqueId())) {
            try
            {
                Bukkit.getScheduler().cancelTask(((Integer)this.tasks.get(player.getUniqueId())).intValue());
            }
            catch (Exception localException) {}
            finally
            {
                this.tasks.remove(player.getUniqueId());
            }
        }
        if (this.sets.containsKey(player.getUniqueId())) {
            this.sets.remove(player.getUniqueId());
        }
    }
}

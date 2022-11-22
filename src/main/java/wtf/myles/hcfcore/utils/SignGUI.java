
package wtf.myles.hcfcore.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Myles on 26/06/2015.
 */

public class SignGUI
{
    protected ProtocolManager protocolManager;
    protected PacketAdapter packetListener;
    protected Map<String, SignGUIListener> listeners;
    protected Map<String, Vector> signLocations;

    public SignGUI(Plugin plugin)
    {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.listeners = new ConcurrentHashMap();
        this.signLocations = new ConcurrentHashMap();


        ProtocolLibrary.getProtocolManager().addPacketListener(this.packetListener = new PacketAdapter(plugin, new PacketType[] { PacketType.Play.Client.UPDATE_SIGN })
        {
            public void onPacketReceiving(PacketEvent event)
            {
                final Player player = event.getPlayer();

                Vector v = (Vector)SignGUI.this.signLocations.remove(player.getName());
                if (v == null) {
                    return;
                }
                List<Integer> list = event.getPacket().getIntegers().getValues();
                if (((Integer)list.get(0)).intValue() != v.getBlockX()) {
                    return;
                }
                if (((Integer)list.get(1)).intValue() != v.getBlockY()) {
                    return;
                }
                if (((Integer)list.get(2)).intValue() != v.getBlockZ()) {
                    return;
                }
                final String[] lines = (String[])event.getPacket().getStringArrays().getValues().get(0);
                final SignGUI.SignGUIListener response = (SignGUI.SignGUIListener)SignGUI.this.listeners.remove(event.getPlayer().getName());
                if (response != null)
                {
                    event.setCancelled(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
                    {
                        public void run()
                        {
                            response.onSignDone(player, lines);
                        }
                    });
                }
            }
        });
    }

    public void open(Player player, String[] defaultText, SignGUIListener response)
    {
        List<PacketContainer> packets = new ArrayList();

        int x = 0;int y = 0;int z = 0;
        if (defaultText != null)
        {
            x = player.getLocation().getBlockX();
            z = player.getLocation().getBlockZ();

            PacketContainer packet53 = this.protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            packet53.getIntegers().write(0, Integer.valueOf(x)).write(1, Integer.valueOf(y)).write(2, Integer.valueOf(z));
            packet53.getBlocks().write(0, Material.SIGN_POST);
            packets.add(packet53);

            PacketContainer packet130 = this.protocolManager.createPacket(PacketType.Play.Server.UPDATE_SIGN);
            packet130.getIntegers().write(0, Integer.valueOf(x)).write(1, Integer.valueOf(y)).write(2, Integer.valueOf(z));
            packet130.getStringArrays().write(0, defaultText);
            packets.add(packet130);
        }
        PacketContainer packet133 = this.protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_ENTITY);
        packet133.getIntegers().write(0, Integer.valueOf(x)).write(2, Integer.valueOf(z));
        packets.add(packet133);
        if (defaultText != null)
        {
            PacketContainer packet53 = this.protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            packet53.getIntegers().write(0, Integer.valueOf(x)).write(1, Integer.valueOf(0)).write(2, Integer.valueOf(z));
            packet53.getBlocks().write(0, Material.BEDROCK);
            packets.add(packet53);
        }
        try
        {
            for (PacketContainer packet : packets) {
                this.protocolManager.sendServerPacket(player, packet);
            }
            this.signLocations.put(player.getName(), new Vector(x, y, z));
            this.listeners.put(player.getName(), response);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public void destroy()
    {
        this.protocolManager.removePacketListener(this.packetListener);
        this.listeners.clear();
        this.signLocations.clear();
    }

    public static abstract interface SignGUIListener
    {
        public abstract void onSignDone(Player paramPlayer, String[] paramArrayOfString);
    }
}

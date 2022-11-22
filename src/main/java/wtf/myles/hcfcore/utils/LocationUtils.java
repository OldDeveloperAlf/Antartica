package wtf.myles.hcfcore.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import wtf.myles.hcfcore.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myles on 25/06/2015.
 */
public class LocationUtils {

    public static List<Location> getBlocks(Location position1, Location position2, boolean getOnlyAboveGround)
    {
        List<Location> blocks = new ArrayList();
        if (position1 == null) {
            return blocks;
        }
        if (position2 == null) {
            return blocks;
        }
        for (int x = Math.min(position1.getBlockX(), position2.getBlockX()); x <= Math.max(position1.getBlockX(), position2.getBlockX()); x++) {
            for (int z = Math.min(position1.getBlockZ(), position2.getBlockZ()); z <= Math.max(position1.getBlockZ(), position2.getBlockZ()); z++) {
                for (int y = Math.min(position1.getBlockY(), position2.getBlockY()); y <= Math.max(position1.getBlockY(), position2.getBlockY()); y++)
                {
                    Block b = position1.getWorld().getBlockAt(x, y, z);
                    if ((b.getType() == Material.AIR) && ((!getOnlyAboveGround) || (b.getRelative(BlockFace.DOWN).getType() != Material.AIR))) {
                        blocks.add(b.getLocation());
                    }
                }
            }
        }
        return blocks;
    }

    public static String serializeLocation(Location l)
    {
        String s = "";
        s = s + "@w;" + l.getWorld().getName();

        s = s + ":@x;" + l.getX();

        s = s + ":@y;" + l.getY();

        s = s + ":@z;" + l.getZ();

        s = s + ":@p;" + l.getPitch();

        s = s + ":@ya;" + l.getYaw();
        return s;
    }

    public static List<Location> getCuboid(Location position1, Location position2)
    {
        if (position1.getWorld().getName() != position2.getWorld().getName()) {
            throw new UnsupportedOperationException("'Position1' and 'Position2' location need to be in the same world!");
        }
        List<Location> cube = new ArrayList();

        int minX = (int)Math.min(position1.getX(), position2.getX());
        int maxX = (int)Math.max(position1.getX(), position2.getX());

        int minY = (int)Math.min(position1.getY(), position2.getY());
        int maxY = (int)Math.max(position1.getY(), position2.getY());

        int minZ = (int)Math.min(position1.getZ(), position2.getZ());
        int maxZ = (int)Math.max(position1.getZ(), position2.getZ());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cube.add(new Location(position1.getWorld(), x, y, z));
                }
            }
        }
        return cube;
    }

}

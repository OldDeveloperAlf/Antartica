package wtf.myles.hcfcore.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

/**
 * Created by Myles on 19/06/2015.
 */
public class ConfigManager {

    private File file;
    private YamlConfiguration yamlConfiguration;

    public ConfigManager(YamlConfiguration yamlConfiguration, File file) {
        this.file = file;
        this.yamlConfiguration = yamlConfiguration;
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfiguration;
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            this.yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getScoreboardTitle() {
        return ChatColor.translateAlternateColorCodes('&', getYamlConfiguration().getString("scoreboardTitle"));
    }

    public Location getEndSpawnLocation() {
        World world = Bukkit.getServer().getWorld(getYamlConfiguration().getString("end.spawn.world"));
        double x  = getYamlConfiguration().getDouble("end.spawn.x");
        double y = getYamlConfiguration().getDouble("end.spawn.y");
        double z = getYamlConfiguration().getDouble("end.spawn.z");

        Location location = new Location(world, x, y, z);
        return location;
    }

    public Location getEndLeaveEndLocation() {
        World world = Bukkit.getServer().getWorld(getYamlConfiguration().getString("end.leavelocation.world"));
        double x  = getYamlConfiguration().getDouble("end.leavelocation.x");
        double y = getYamlConfiguration().getDouble("end.leavelocation.y");
        double z = getYamlConfiguration().getDouble("end.leavelocation.z");

        Location location = new Location(world, x, y, z);
        return location;

    }

    public String getTeamSpeakIpAddress() {
        return getYamlConfiguration().getString("teamspeakip");
    }

    public boolean spawnTagEnabled() {
        if(getYamlConfiguration().getBoolean("spawntag.enabled")) {
            return true;
        } else {
            return false;
        }
    }

    public int getSpawnTagTimeInSeconds() {
        return getYamlConfiguration().getInt("spawntag.secondstotag");
    }

    public List<String> getMotd() {
        return getYamlConfiguration().getStringList("motd.text");
    }

    public boolean scoreboardEnabled() {
        if (getYamlConfiguration().getBoolean("scoreboardEnabled")) {
            return true;
        } else {
            return false;
        }

    }

    public void setSpawn(Player p) {
        getYamlConfiguration().set("spawn.world", p.getWorld().getName());
        getYamlConfiguration().set("spawn.x", Math.round(p.getLocation().getX() * 2.0D) / 2.0D);
        getYamlConfiguration().set("spawn.y", Math.round(p.getLocation().getY() * 2.0D) / 2.0D);
        getYamlConfiguration().set("spawn.z", Math.round(p.getLocation().getZ() * 2.0D) / 2.0D);
        save();
    }

    public Location getSpawn() {
        World world = Bukkit.getWorld(getYamlConfiguration().getString("spawn.world"));
        double x = getYamlConfiguration().getDouble("spawn,x");
        double y = getYamlConfiguration().getDouble("spawn.y");
        double z = getYamlConfiguration().getDouble("spawn.z");

        Location loc = new Location(world, x, y, z);
        return loc;
    }

    public boolean pvpTimerEnabled() {
        if(getYamlConfiguration().getBoolean("pvptimer.enabled")) {
            return true;
        } else {
            return false;
        }
    }

    public void reload() {
        save();
    }
}

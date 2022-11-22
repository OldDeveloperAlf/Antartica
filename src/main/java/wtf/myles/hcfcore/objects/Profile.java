package wtf.myles.hcfcore.objects;

import com.mongodb.BasicDBObject;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.managers.ProfileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Myles on 07/07/2015.
 */
public class Profile {

    File file;
    YamlConfiguration config;
    UUID uuid;
    Player player;
    String playerName;
    boolean banned = false;
    boolean staffChat = false;
    boolean commandSpy = false;
    boolean isStaff = false;
    boolean alerts = false;
    boolean donator = false;

    ProfileManager pm = Main.getInstance().getProfileManager();


    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(this.uuid);
        file = new File(Main.getInstance().getDataFolder() + File.separator + "users", this.uuid.toString() + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
        if(file.exists()) {
            staffChat = getBoolean("staffchat");
            commandSpy = getBoolean("commandspy");
            isStaff = getBoolean("staff");
            alerts = getBoolean("alerts");
            donator = getBoolean("donator");
            config.set("currentip", player.getAddress().getAddress().toString());
            config.set("name", player.getName());
        } else {
            try {
                file.createNewFile();
                config.set("name", player.getName());
                config.set("currentip", player.getAddress().getAddress().toString());
                config.set("staffchat", false);
                config.set("commandspy", false);
                config.set("staff", false);
                if(isStaff()) {
                    config.set("alerts", true);
                }
                config.set("donator", false);
                config.set("alerts", false);
                config.save(file);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        }

    public boolean getBoolean(String string) {
        if(config.contains(string)) {
            return config.getBoolean(string);
        }
        return false;
    }

    public String getString(String string) {
        if(config.contains(string)) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(string));
        }
        return "Error. \nCheck config.";
    }

    public int getInteger(String string) {
        if(config.contains(string)) {
            return config.getInt(string);
        }
        return 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isStaffChat() {
        return staffChat;
    }

    public void setStaffChat(boolean staffChat) {
        this.config.set("staffchat", staffChat);
        this.staffChat = staffChat;
        save();
    }

    public boolean isCommandSpy() {
        return commandSpy;
    }

    public void setCommandSpy(boolean commandSpy) {
        this.config.set("commandspy", commandSpy);
        this.commandSpy = commandSpy;
        save();
    }


    public boolean isBanned() {
        return this.banned;
    }

    public boolean isAlerts() {
        return alerts;
    }

    public void setAlerts(boolean alerts) {
        this.config.set("alerts", alerts);
        this.alerts = alerts;
        save();
    }

    public void setBanned(boolean banned) {
        this.config.set("banned", banned);
        this.banned = banned;
        save();
    }

    public List<String> getStrings(String path)
    {
        if (this.config.contains(path)) {
            return this.config.getStringList(path);
        }
        return null;
    }

    private void save() {
        try {
            this.config.save(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        this.config.set("staff", isStaff);
        this.isStaff = isStaff;
        save();
    }

    public boolean isDonator() {
        return donator;
    }

    public void setDonator(boolean donator) {
        this.config.set("donator", donator);
        this.donator = donator;
        save();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

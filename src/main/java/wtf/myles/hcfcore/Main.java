package wtf.myles.hcfcore;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.JedisPool;
import wtf.myles.hcfcore.commands.*;
import wtf.myles.hcfcore.deathmessages.DeathMessageHandler;
import wtf.myles.hcfcore.listeners.*;
import wtf.myles.hcfcore.handlers.MapHandler;
import wtf.myles.hcfcore.managers.*;
import wtf.myles.hcfcore.nms.EntityRegistrar;
import wtf.myles.hcfcore.objects.Profile;
import wtf.myles.hcfcore.staff.StaffMode;
import wtf.myles.hcfcore.utils.SignGUI;

import java.io.File;
import java.util.*;

/**
 * Created by Myles on 18/06/2015.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private MapHandler mapHandler;
    private ConfigManager configManager;
    private File file;
    private YamlConfiguration yamlConfiguration;
    private MongoClient mongoClient;
    private DB db;
    private JedisPool jedisPool;
    private HashMap<UUID, Integer> pvpTimerMap = new HashMap();
    private SignGUI signGUI;
    private File messageFile;
    private ProfileManager profileManager;

    public File getMessageFile() {
        return messageFile;
    }

    public SignGUI getSignGUI() {
        return signGUI;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupHandlers();
        setupListeners();
        setupEntityRegistar();
        setupCommands();

        if(!new File(getDataFolder(), "config.yml").exists()) {
            List<String> blockedPotions = Arrays.asList(new String[] { "373:8233" });
            getConfig().set("blockedpotions", blockedPotions);
            getConfig().set("server-full-msg", "&cServer is currently full.");
            saveConfig();
        }

        /*if(cfg.getBoolean("mongodb-enabled")) {
            try {
                this.db = new MongoDB(new DBAddress("localhost", 27017, "server")).getDatabase();
            } catch(UnknownHostException ex) {
                ex.printStackTrace();
                Bukkit.getLogger().log(Level.SEVERE, "Error connecting to the MongoDB.");
            }
        }*/
        try {
            File player = new File(getDataFolder(), "users");
            if(!player.exists()) {
                player.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        loadConfig();
        configManager = new ConfigManager(yamlConfiguration, file);
        saveYamlConfiguration();

        for (final Player player : getServer().getOnlinePlayers()) {
            player.removeMetadata("logged", this);
            if(player.hasPermission("command.mod")) {
                StaffMode.toggleModMode(player);
            }
        }


        this.signGUI = new SignGUI(this);
        this.messageFile = new File(getDataFolder(), "messages.json");
        if(messageFile.exists()) {
            AutoMessageManager.getMessageManager().load(this.messageFile);
            AutoMessageManager.getMessageManager().runTaskTimerAsynchronously(Main.getInstance(), 20L, 2400L);
        } else {
            AutoMessageManager.getMessageManager().addMessage("Default Message");
            AutoMessageManager.getMessageManager().runTaskTimerAsynchronously(Main.getInstance(), 20L, 2400L);
        }

        for(World world : Bukkit.getWorlds()) {
            for(Player p : world.getPlayers()) {
                profileManager.createProfile(p.getUniqueId());
            }
        }
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p.hasPermission("command.vip")) {
                        Profile profile = profileManager.getProfile(p.getUniqueId());
                        if (!profile.isDonator()) {
                            profile.setDonator(true);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 200L, 200L);
    }

    @Override
    public void onDisable() {
        for(Player player : getServer().getOnlinePlayers()) {
            player.setMetadata("logged", (MetadataValue)new FixedMetadataValue(this, true));
        }
        this.signGUI.destroy();
    }

    private void setupHandlers() {
        this.mapHandler = new MapHandler();
        new ServerManager();
        new ConversationManager();
        this.profileManager = new ProfileManager();
    }

    private void setupCommands() {
        Teleport teleport = new Teleport();
        getCommand(teleport.getCommand()).setExecutor(teleport);

        TeleportHere teleportHere = new TeleportHere();
        getCommand(teleportHere.getCommand()).setExecutor(teleportHere);

        TeleportPosition teleportPosition = new TeleportPosition();
        getCommand(teleportPosition.getCommand()).setExecutor(teleportPosition);

        Broadcast broadcast = new Broadcast();
        getCommand(broadcast.getCommand()).setExecutor(broadcast);

        ClearChat clearChat = new ClearChat();
        getCommand(clearChat.getCommand()).setExecutor(clearChat);

        Staff staff = new Staff();
        getCommand(staff.getCommand()).setExecutor(staff);

        AutoMessage autoMessage = new AutoMessage();
        getCommand(autoMessage.getCommand()).setExecutor(autoMessage);

        MuteChat muteChat = new MuteChat();
        getCommand(muteChat.getCommand()).setExecutor(muteChat);

        SlowChat slowChat = new SlowChat();
        getCommand(slowChat.getCommand()).setExecutor(slowChat);

        StaffChat staffChat = new StaffChat();
        getCommand(staffChat.getCommand()).setExecutor(staffChat);

        SpawnMob spawnMob = new SpawnMob();
        getCommand(spawnMob.getCommand()).setExecutor(spawnMob);

        ThrowBlock throwBlock = new ThrowBlock();
        getCommand(throwBlock.getCommand()).setExecutor(throwBlock);

        Clearinventory clearinventory = new Clearinventory();
        getCommand(clearinventory.getCommand()).setExecutor(clearinventory);

        Weather weather = new Weather();
        getCommand(weather.getCommand()).setExecutor(weather);

        NameItem nameItem = new NameItem();
        getCommand(nameItem.getCommand()).setExecutor(nameItem);

        Message message = new Message();
        getCommand(message.getCommand()).setExecutor(message);

        Reply reply = new Reply();
        getCommand(reply.getCommand()).setExecutor(reply);

        LOL lol = new LOL();
        getCommand(lol.getCommand()).setExecutor(lol);

        CommandSpy commandSpy = new CommandSpy();
        getCommand(commandSpy.getCommand()).setExecutor(commandSpy);

        SetSpawn setSpawn = new SetSpawn();
        getCommand(setSpawn.getCommand()).setExecutor(setSpawn);

        SetStaff setStaff = new SetStaff();
        getCommand(setStaff.getCommand()).setExecutor(setStaff);

        Alerts alerts = new Alerts();
        getCommand(alerts.getCommand()).setExecutor(alerts);

        RemoveStaff removeStaff = new RemoveStaff();
        getCommand(removeStaff.getCommand()).setExecutor(removeStaff);
    }

    private void setupListeners() {
        new EnchantmentLimiterListener();
        new BorderListener();
        new CombatLoggerListener();
        new EnderpearlListener();
        new MinerListener();
        new PlayerListener();
        new StaffMode();
        new SpawnTagListener();
        new LOL();
        new OreListener();
        DeathMessageHandler.init();
    }

    public static ApplicableRegionSet getRegionAt(Location loc) {
        return WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
    }

    private void loadConfig() {
        try {
            file = new File(getDataFolder() + File.separator + "mapSettings.yml");
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            if(!file.exists()) {
                file.createNewFile();
                List<String> messageList = yamlConfiguration.getStringList("motd.text");
                messageList.add("&cWelcome to the server!");
                messageList.add("&6We hope you have a great time.");

                yamlConfiguration.set("motd.text", messageList);
                yamlConfiguration.set("motd.system", "&cDefault system message of the day.");
                yamlConfiguration.set("spawn.world", "world");
                yamlConfiguration.set("spawn.x", 0.0D);
                yamlConfiguration.set("spawn.y", 70.0D);
                yamlConfiguration.set("spawn.z", 0.0D);
                yamlConfiguration.set("end.spawn.world", "world_the_end");
                yamlConfiguration.set("end.spawn.x", 0.0D);
                yamlConfiguration.set("end.spawn.y", 70.0D);
                yamlConfiguration.set("end.spawn.z", 0.0D);

                yamlConfiguration.set("end.leavelocation.world", "world");
                yamlConfiguration.set("end.leavelocation.x", 100.0D);
                yamlConfiguration.set("end.leavelocation.y", 70.0D);
                yamlConfiguration.set("end.leavelocation.z", 0.0D);

                yamlConfiguration.set("scoreboardEnabled", true);
                yamlConfiguration.set("scoreboardTitle", "&6&lHCF &c[Map 1]");
                yamlConfiguration.set("teamspeakip", "&6TeamSpeak IP Here");

                yamlConfiguration.set("pvptimer.enbaled", true);

                yamlConfiguration.set("spawntag.enabled", true);
                yamlConfiguration.set("spawntag.secondstotag", 60);

                yamlConfiguration.save(file);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public MapHandler getMapHandler() {
        return mapHandler;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static Main getInstance() {
        return instance;
    }

    private void saveYamlConfiguration() {
        try {
            this.yamlConfiguration.save(this.file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    private void setupEntityRegistar() {
        try {
            EntityRegistrar.registerCustomEntities();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOps(String message) {
        for(Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            if(player.isOp()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }

    public String colourMessage(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

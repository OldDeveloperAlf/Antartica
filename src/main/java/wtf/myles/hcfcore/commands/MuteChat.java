package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.ServerManager;

/**
 * Created by Myles on 29/06/2015.
 */
public class MuteChat extends CommandBase {

    public MuteChat() {
        super("mutechat", "Mute the server chat", Category.MOD, false);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(ServerManager.getInstance().isChatMuted() == false) {
                ServerManager.getInstance().setChatMuted(true);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aChat has been muted &7[&c" + sender.getName() + "&7]"));
                return;
            } else if(ServerManager.getInstance().isChatMuted() == true){
                ServerManager.getInstance().setChatMuted(false);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aChat has been unmuted &7[&c" + sender.getName() + "&7]"));
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "Usage: /" + label);
    }
}

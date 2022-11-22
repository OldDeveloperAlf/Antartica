package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;

/**
 * Created by Myles on 26/06/2015.
 */
public class ClearChat extends CommandBase {

    public ClearChat() {
        super("clearchat", "Clear the chat", Category.MOD, false);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            for(int i = 0; i < 200; i++) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!online.hasPermission("command.mod")) {
                        Bukkit.broadcastMessage(" ");
                    }
                }
            }
            Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been cleared " + ChatColor.GRAY + "[" + ChatColor.RED + sender.getName() + ChatColor.GRAY + "]");
            return;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label);
            return;
        }
    }
}

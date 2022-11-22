package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.ServerManager;
import wtf.myles.hcfcore.utils.NumberUtils;

/**
 * Created by Myles on 30/06/2015.
 */
public class SlowChat extends CommandBase {

    public SlowChat() {
        super("slowchat", "Slow the server chat", Category.MOD, false);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1) {
            if(NumberUtils.isNumber(args[0])) {
                int slow = Integer.parseInt(args[0]);
                ServerManager.getInstance().setChatSlow(slow);

                if(slow <= 0) {
                    ServerManager.getInstance().getSlowCooldowns().clear();
                    return;
                }
                Bukkit.broadcastMessage(ChatColor.RED + "Chat has been slown to " + slow + " seconds.");
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <seconds>");
    }
}

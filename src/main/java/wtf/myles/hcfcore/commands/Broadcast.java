package wtf.myles.hcfcore.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.utils.JsonBuilder;

/**
 * Created by Myles on 25/06/2015.
 */
public class Broadcast extends CommandBase {

    public Broadcast() {
        super("broadcast", "Broadcast a message to the server", Category.MOD, false);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length >= 1) {
            String message = StringUtils.join(args, " ", 0, args.length);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <message>");
        }
    }
}

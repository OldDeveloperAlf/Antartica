package wtf.myles.hcfcore.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.ConversationManager;

/**
 * Created by Myles on 03/07/2015.
 */
public class Message extends CommandBase {

    public Message() {
        super("message", "Message a player", Category.DEFAULT, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0 || args.length < 2) {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName> <message>");
            return;
        }
        if(Bukkit.getPlayer(args[0]) != null) {
            Player target = Bukkit.getPlayer(args[0]);
            String message = StringUtils.join(args, " ", 1, args.length);

            ConversationManager.getInstance().setConversations(p, target, message);
            return;
        } else {
            p.sendMessage(ChatColor.RED + "That player does not exist.");
            return;
        }
    }
}

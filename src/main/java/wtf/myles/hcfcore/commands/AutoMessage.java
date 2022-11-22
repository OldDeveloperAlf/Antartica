package wtf.myles.hcfcore.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.AutoMessageManager;
import wtf.myles.hcfcore.utils.Message;

/**
 * Created by Myles on 29/06/2015.
 */
public class AutoMessage extends CommandBase {

    public AutoMessage() {
        super("am", "AutoMessage", Category.ADMIN, false);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " add <message>");
                    return;
                }
                String message = ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, " ", 1, args.length).trim());
                AutoMessageManager.getMessageManager().addMessage(message);
                sender.sendMessage(ChatColor.GREEN + "Message added to the AutoMessage list.");
                return;
            }
            if (args[0].equalsIgnoreCase("view")) {
                if (AutoMessageManager.getMessageManager().getMessages().size() <= 0) {
                    sender.sendMessage(ChatColor.RED + "There are no set messages.");
                    return;
                }
                sender.sendMessage(ChatColor.RED + "There are currently " + ChatColor.GREEN + AutoMessageManager.getMessageManager().getMessages().size() + ChatColor.RED + " messages.");
                int count = 0;
                for (Message message : AutoMessageManager.getMessageManager().getMessages()) {
                    sender.sendMessage(ChatColor.YELLOW + "ID: " + count + ", Message: " + message.getMessage());
                    sender.sendMessage("");
                    count++;
                }
                return;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length > 2 || args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage : /" + label + " remove <messageId>");
                    return;
                }
                try {
                    int index = Integer.valueOf(args[1]);
                    AutoMessageManager.getMessageManager().getMessages().remove(index);
                    sender.sendMessage(ChatColor.RED + "That message has been removed");
                    return;
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage(ChatColor.RED + "Could not find any message at index: " + args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "The argument must be an integer");
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <add|view|remove> [args...]");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <add|view|remove> [args...]");
        }
    }
}

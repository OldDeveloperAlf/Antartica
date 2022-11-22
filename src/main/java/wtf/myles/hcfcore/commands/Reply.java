package wtf.myles.hcfcore.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
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
public class Reply extends CommandBase {

    public Reply() {
        super("reply", "Reply to a message", Category.DEFAULT, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            p.sendMessage(ChatColor.RED + "You are not in a conversation with anyone");
            return;
        }
        String message = StringUtils.join(args, " ", 0, args.length);
        ConversationManager.getInstance().reply(p, message);
    }
}

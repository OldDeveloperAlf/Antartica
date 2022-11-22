package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.staff.StaffMode;

/**
 * Created by Myles on 26/06/2015.
 */
public class Staff extends CommandBase {

    public Staff() {
        super("staff", "Toggle staff mode", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {

            if(StaffMode.toggleModMode(p) == true) {
                p.sendMessage(ChatColor.GREEN + "You are now in staff mode. (Saving previous inventory)");
                return;
            }else {
                p.sendMessage(ChatColor.RED + "You are no longer in mod mode");
                return;
            }
        }
        return;
    }
}

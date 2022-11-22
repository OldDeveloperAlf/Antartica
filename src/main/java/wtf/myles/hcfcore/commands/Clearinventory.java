package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;

/**
 * Created by Myles on 02/07/2015.
 */
public class Clearinventory extends CommandBase {

    public Clearinventory() {
        super("clearinventory", "Clear a players inventory", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            p.getInventory().clear();
            p.getInventory().setArmorContents((ItemStack[]) null);
            p.sendMessage(ChatColor.GREEN + "Clearing your inventory.");
            return;
        } else if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                target.getInventory().clear();
                target.getInventory().setArmorContents((ItemStack[]) null);
                p.sendMessage(ChatColor.GREEN + "Clearing " + target.getName() + "'s inventory.");
                return;
            }
            p.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        } else {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " [playerName]");
            return;
        }
    }
}

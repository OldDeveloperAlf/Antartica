package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;

/**
 * Created by Myles on 14/07/2015.
 */
public class SetSpawn extends CommandBase {

    public SetSpawn() {
        super("setspawn", "Set the spawn", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            Main.getInstance().getConfigManager().setSpawn(p);
            p.sendMessage(ChatColor.GREEN + "Set the spawn location.");
            return;
        }
        p.sendMessage(ChatColor.RED + "Usage: /" + label);
        return;
    }
}

package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.utils.ItemUtil;

import java.util.List;

/**
 * Created by Myles on 30/06/2015.
 */
public class ThrowBlock extends CommandBase {

    public ThrowBlock() {
        super("throwblock", "Throw a block", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <block>");
            return;
        }
        List<Material> matches = ItemUtil.closestMatches(args[0]);
        if (matches.isEmpty())
        {
            p.sendMessage(ChatColor.RED + "No matches.");
            return;
        }
        Material match = (Material)matches.get(0);
        ItemStack is = new ItemStack(match);
        if (is == null) {
            return;
        }
        Entity e = p.getWorld().spawnFallingBlock(p.getLocation(), match, (byte)0);
        e.setVelocity(p.getLocation().getDirection().normalize().multiply(2.5D));

    }
}

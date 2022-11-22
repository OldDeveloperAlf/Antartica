package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myles on 02/07/2015.
 */
public class NameItem extends CommandBase {


    String[] help = { ChatColor.WHITE + "| ", ChatColor.WHITE + "| " + ChatColor.DARK_PURPLE + "/nameitem -n <nameitem>", ChatColor.WHITE + "| " + ChatColor.DARK_PURPLE + "/nameitem -l - applies lore to item in hand", ChatColor.WHITE + "| " + ChatColor.DARK_PURPLE + "/nameitem -c - clears lores set by user", ChatColor.WHITE + "| " + ChatColor.DARK_PURPLE + "/nameitem -la - add lore to list", ChatColor.WHITE + "| " + ChatColor.DARK_PURPLE + "/nameitem -colors", ChatColor.WHITE + "| ", ChatColor.WHITE + "|--------------------------------------------" };

    public NameItem() {
        super("nameitem", "Rename an item", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            p.sendMessage(ChatColor.RED + "==== HELP ===");
            p.sendMessage(this.help);
            return;
        }
        if(args[0].equalsIgnoreCase("-n")) {
            if(p.getItemInHand() != null) {
                ItemMeta meta = p.getItemInHand().getItemMeta();

                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }

                String nameTag = stringBuilder.toString();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', nameTag));
                p.getItemInHand().setItemMeta(meta);
            }
            return;
        }
        if(args[0].equalsIgnoreCase("-colors")) {
            for(ChatColor chatColor : ChatColor.values()) {
                p.sendMessage(chatColor + "&" + chatColor.getChar() + " - ");
            }
            return;
        }
        if(args[0].equalsIgnoreCase("-la")) {
            List<String> newLore = new ArrayList<>();
            if(InventoryUtils.getLores().containsKey(p.getName())) {
                newLore = InventoryUtils.getLores().get(p.getName());
            }

            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 1; i < args.length; i++) {
                stringBuilder.append(args[i]).append(" ");
            }
            String lore = stringBuilder.toString();
            newLore.add(ChatColor.translateAlternateColorCodes('&', lore));
            InventoryUtils.getLores().put(p.getName(), newLore);
            return;
        }
        if(args[0].equalsIgnoreCase("-c") && InventoryUtils.getLores().containsKey(p.getName())) {
            List<String> lore = InventoryUtils.getLores().get(p.getName());
            lore.clear();
            InventoryUtils.getLores().put(p.getName(), lore);
            return;
        }
        if(args[0].equalsIgnoreCase("-l")) {
            if(InventoryUtils.getLores().containsKey(p.getName())) {
                if(p.getItemInHand() != null) {
                    ItemMeta itemMeta = p.getItemInHand().getItemMeta();
                    itemMeta.setLore(InventoryUtils.getLores().get(p.getName()));
                    p.getItemInHand().setItemMeta(itemMeta);
                    return;
                }
            } else {
                p.sendMessage(ChatColor.RED + "You have not set any lores.");
                return;
            }
        }
    }
}

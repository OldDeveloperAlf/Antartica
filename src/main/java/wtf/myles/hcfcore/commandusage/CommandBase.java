package wtf.myles.hcfcore.commandusage;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandBase implements CommandExecutor {
	
    private String command;
    
    private String description;
    
    private String permission;
    
    private Category category;

    private boolean isPlayerOnly;
	
	public CommandBase(String command, String description, Category category, boolean playerOnly) {
		this.command = command;
		this.description = description;
		this.category = category;
		this.permission = "command." + (getCategory());
		this.isPlayerOnly = playerOnly;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equals(this.getCommand())) {
			if(this.isPlayerOnly() && !(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Only players can execute the command '" + label + "'.");
				return true;
			}
			if(sender.hasPermission("command." + Category.OP)) {
	            execute(sender, cmd, label, args);
	            return true;
			}
	        if (!sender.hasPermission("command." + getCategory())) {
	            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
	            return true;
	         }
	        execute(sender, cmd, label, args);
		}
		
		return false;
	}
	
	
    public abstract void execute(CommandSender sender, Command cmd, String label, String[] args);
    
    public void message(CommandSender sender, String msg) {
    	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
	
	public String getCommand() {
		return command;
	}

	public String getDescription() {
		return description;
	}

	public String getPermission() {
		return permission;
	}

	public Category getCategory() {
		return category;
	}

	public boolean isPlayerOnly() {
		return isPlayerOnly;
	}

}

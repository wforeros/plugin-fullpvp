package me.theoldestwilly.fullpvp.kits.command.arguments.staff;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreateArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitCreateArgument(FullPvP plugin) {
        super("create", "Creates a kit.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        } else {
            Player player = (Player)sender;
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
                return true;
            } else {
                Kit kit = this.plugin.getKitManager().getKit(args[1]);
                if (kit != null) {
                    player.sendMessage(ChatColor.RED + "Kit with name '" + args[1] + "' already exists.");
                    return true;
                } else if (!JavaUtils.isAlphanumeric(args[1])) {
                    player.sendMessage(ChatColor.RED + "Kit names may only be alphanumeric.");
                    return true;
                } else {
                    kit = new Kit(args[1], (String)null, player.getInventory());
                    this.plugin.getKitManager().createKit(kit);
                    player.sendMessage(ChatColor.YELLOW + "Created kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + " with your inventory contents.");
                    return true;
                }
            }
        }
    }
}

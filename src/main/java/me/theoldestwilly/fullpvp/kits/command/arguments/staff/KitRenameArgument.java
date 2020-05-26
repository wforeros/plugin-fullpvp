package me.theoldestwilly.fullpvp.kits.command.arguments.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitRenameArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitRenameArgument(FullPvP plugin) {
        super("rename", "Renames a kit");
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName> <newKitName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        } else {
            Kit kit = this.plugin.getKitManager().getKit(args[2]);
            if (kit != null) {
                sender.sendMessage(ChatColor.RED + "A kit with the name '" + kit.getName() + "' already exists.");
                return true;
            } else {
                kit = this.plugin.getKitManager().getKit(args[1]);
                if (kit == null) {
                    sender.sendMessage(ChatColor.RED + "Kit with name '" + args[1] + "' doesn't exists.");
                    return true;
                } else if (kit.getName().equalsIgnoreCase(args[2])) {
                    sender.sendMessage(ChatColor.RED + "This kit is already called '" + args[2] + "'.");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "The name of kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + " has been renamed to " + ChatColor.WHITE + args[2] + ChatColor.YELLOW + ".");
                    kit.setName(args[2]);
                    return true;
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else {
            List<Kit> kits = this.plugin.getKitManager().getKits();
            ArrayList<String> results = new ArrayList(kits.size());
            Iterator var7 = kits.iterator();

            while(var7.hasNext()) {
                Kit kit = (Kit)var7.next();
                results.add(kit.getName());
            }

            return results;
        }
    }
}
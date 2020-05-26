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

public class KitDeleteArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitDeleteArgument(FullPvP plugin) {
        super("delete", "Deletes a kit.", new String[]{"remove"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        } else {
            Kit kit = this.plugin.getKitManager().getKit(args[1]);
            if (kit == null) {
                sender.sendMessage(ChatColor.RED + "Kit with name '" + args[1] + "' not found.");
            } else {
                this.plugin.getKitManager().removeKit(kit);
                sender.sendMessage(ChatColor.YELLOW + "Removed kit " + ChatColor.GREEN + args[1] + ChatColor.YELLOW + " successfully.");
            }

            return true;
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

package me.theoldestwilly.fullpvp.kits.command.arguments.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitSetDescriptionArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitSetDescriptionArgument(FullPvP plugin) {
        super("setdescription", "Sets the description of a kit", new String[]{"description"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName> <none|description>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        } else {
            Kit kit = this.plugin.getKitManager().getKit(args[1]);
            if (kit == null) {
                sender.sendMessage(ChatColor.RED + "Kit with name '" + args[1] + "' not found.");
                return true;
            } else {
                if (args[2].equalsIgnoreCase("none") || args[2].equalsIgnoreCase("null")) {
                    kit.setDescription((String)null);
                    sender.sendMessage(ChatColor.YELLOW + "Removed the description of kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + '.');
                }

                String description = ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 2, args.length));
                kit.setDescription(description);
                sender.sendMessage(ChatColor.YELLOW + "The description of kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + " has been set to " + ChatColor.WHITE + description + ChatColor.YELLOW + '.');
                return true;
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

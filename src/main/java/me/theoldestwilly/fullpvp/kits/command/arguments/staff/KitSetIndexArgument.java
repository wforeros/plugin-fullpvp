package me.theoldestwilly.fullpvp.kits.command.arguments.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitSetIndexArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitSetIndexArgument(FullPvP plugin) {
        super("setindex", "Sets new position of a kit for selector.", new String[]{"slot", "position", "index", "setslot", "setposition"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName> <index[0 = minimum]>";
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
                Integer newIndex = JavaUtils.tryParseInt(args[2]);
                if (newIndex == null) {
                    sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
                    return true;
                } else if (newIndex < 1) {
                    sender.sendMessage(ChatColor.RED + "The Kit index can not be less than " + 1 + '.');
                    return true;
                } else {
                    List<Kit> kits = this.plugin.getKitManager().getKits();
                    int totalKitAmount = kits.size();
                    if (newIndex > totalKitAmount) {
                        sender.sendMessage(ChatColor.RED + "The index must be a maximum of " + totalKitAmount + '.');
                        return true;
                    } else {
                        int previousIndex = kits.indexOf(kit) + 1;
                        if (newIndex == previousIndex) {
                            sender.sendMessage(ChatColor.RED + "Index of Kit " + kit.getName() + " is already " + newIndex + '.');
                            return true;
                        } else {
                            kits.remove(kit);
                            newIndex = newIndex - 1;
                            kits.add(newIndex, kit);
                            sender.sendMessage(ChatColor.YELLOW + "The inventory slot position of kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + " has been set to " + ChatColor.AQUA + (newIndex + 1) + ChatColor.YELLOW + '.');
                            return true;
                        }
                    }
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


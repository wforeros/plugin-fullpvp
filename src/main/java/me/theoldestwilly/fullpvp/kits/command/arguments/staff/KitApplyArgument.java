package me.theoldestwilly.fullpvp.kits.command.arguments.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitApplyArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitApplyArgument(FullPvP plugin) {
        super("apply", "Applies a kit to player.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName> <playerName>";
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
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null || sender instanceof Player && !((Player)sender).canSee(target)) {
                    sender.sendMessage(ChatColor.RED + "Player with name '" + args[2] + "' not found.");
                    return true;
                } else {
                    if (kit.apply(target, true)) {
                        sender.sendMessage(ChatColor.GREEN + "Applied kit " + ChatColor.YELLOW + kit.getName() + ChatColor.GREEN + " to " + ChatColor.GRAY + target.getName() + ChatColor.GREEN + '.');
                    }

                    return true;
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return args.length == 3 ? null : Collections.emptyList();
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

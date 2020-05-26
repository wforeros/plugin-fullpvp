package me.theoldestwilly.fullpvp.kits.command.arguments.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import net.silexpvp.nightmare.util.JavaUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitSetCooldownArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitSetCooldownArgument(FullPvP plugin) {
        super("setcooldown", "Sets the delay time per usage of a kit.", new String[]{"setdelay"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName> <duration>";
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
                long duration = JavaUtils.parseDuration(args[2]);
                if (duration <= 0L) {
                    sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format" + ChatColor.BOLD + " 9m60s" + ChatColor.RED + " (9 minutes and 60 seconds).");
                    return true;
                } else {
                    kit.setCooldown(duration);
                    sender.sendMessage(ChatColor.YELLOW + "The cooldown between usages of kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + " has been set to " + ChatColor.RED + DurationFormatUtils.formatDurationWords(duration, true, true) + ChatColor.YELLOW + ".");
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

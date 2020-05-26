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
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class KitItemsArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitItemsArgument(FullPvP plugin) {
        super("items", "Sets the items of a kit.", new String[]{"contents", "setitems", "setcontents"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.kit.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        } else {
            Player player = (Player)sender;
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
                return true;
            } else {
                Kit kit = this.plugin.getKitManager().getKit(args[1]);
                if (kit == null) {
                    player.sendMessage(ChatColor.RED + "Kit with name '" + args[1] + "' not found.");
                    return true;
                } else {
                    PlayerInventory inventory = player.getInventory();
                    kit.setContents(inventory.getContents());
                    kit.setArmour(inventory.getArmorContents());
                    player.sendMessage(ChatColor.YELLOW + "The contents of kit " + ChatColor.GREEN + kit.getName() + ChatColor.YELLOW + " have been set as your current inventory.");
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

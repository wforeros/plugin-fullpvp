package me.theoldestwilly.fullpvp.crate.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.crate.Crate;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CrateGiveArgument extends CommandArgument {
    private final FullPvP plugin;

    public CrateGiveArgument(FullPvP plugin) {
        super("give", "Gives a crate.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.crate.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <crateName> <player> <ammount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else {
            Crate crate = this.plugin.getCrateManager().getCrateByName(arguments[1]);
            if (crate == null) {
                sender.sendMessage(ChatColor.RED + "Crate with name '" + ChatColor.GRAY + arguments[1] + ChatColor.RED + "' not found.");
                return true;
            }

            if (arguments[2].equals("*")) {
                Integer intRewards = JavaUtils.tryParseInt(arguments[3]);
                if (intRewards == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid keys ammount.");
                    return true;
                }

                Iterator var7 = Bukkit.getOnlinePlayers().iterator();

                while(var7.hasNext()) {
                    Player player = (Player)var7.next();
                    player.getInventory().addItem(new ItemStack[]{crate.giveKeys(intRewards)});
                    player.sendMessage(ChatColor.YELLOW + "You have received " + ChatColor.GRAY + "x" + intRewards + ChatColor.GRAY + "[" + crate.getColor() + crate.getName() + " key" + ChatColor.GRAY + "]" + ChatColor.YELLOW + ".");
                }

                sender.sendMessage(ChatColor.GREEN + "You have succesfully given " + ChatColor.GRAY + "x" + intRewards + ChatColor.GRAY + "[" + crate.getColor() + crate.getName() + "key" + ChatColor.GRAY + "]" + ChatColor.GREEN + " to " + ChatColor.AQUA + "all online players" + ChatColor.GREEN + ".");
            } else {
                Player player = Bukkit.getServer().getPlayer(arguments[2]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player '" + ChatColor.YELLOW + arguments[2] + ChatColor.RED + "' not found");
                    return true;
                }

                Integer intRewards = JavaUtils.tryParseInt(arguments[3]);
                if (intRewards == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid keys ammount.");
                    return true;
                }

                player.getInventory().addItem(new ItemStack[]{crate.giveKeys(intRewards)});
                sender.sendMessage(ChatColor.GREEN + "You have succesfully given " + ChatColor.GRAY + "x" + intRewards + ChatColor.GRAY + "[" + crate.getColor() + crate.getName() + " key" + ChatColor.GRAY + "]" + ChatColor.GREEN + " to " + ChatColor.AQUA + player.getName() + ChatColor.GREEN + ".");
                player.sendMessage(ChatColor.YELLOW + "You hace received " + ChatColor.GRAY + "x" + intRewards + ChatColor.GRAY + "[" + crate.getColor() + crate.getName() + " key" + ChatColor.GRAY + "]" + ChatColor.YELLOW + ".");
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return args.length == 3 ? null : Collections.emptyList();
        } else {
            List<String> cratesName = new ArrayList();
            Iterator var6 = this.plugin.getCrateManager().getCratesList().iterator();

            while(var6.hasNext()) {
                String name = ((Crate)var6.next()).getName();
                cratesName.add(name);
            }

            return cratesName;
        }
    }
}

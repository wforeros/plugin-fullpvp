package me.theoldestwilly.fullpvp.crate.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.crate.Crate;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CrateGuiArgument extends CommandArgument {
    private final FullPvP plugin;

    public CrateGuiArgument(FullPvP plugin) {
        super("gui", "Open a gui of crates.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <crateName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length == 1) {
                ((Player)sender).openInventory(this.plugin.getCrateManager().openCratesGui((Player)sender));
            } else {
                Crate crate = this.plugin.getCrateManager().getCrateByName(arguments[1]);
                if (crate == null) {
                    sender.sendMessage(ChatColor.RED + "Crate with name '" + ChatColor.GRAY + arguments[1] + ChatColor.RED + "' not found.");
                    return true;
                }
                ((Player)sender).openInventory(this.plugin.getCrateManager().openCrateLoot((Player)sender, arguments[1]));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
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

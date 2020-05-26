package me.theoldestwilly.fullpvp.crate.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.crate.Crate;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CrateCreateArgument extends CommandArgument {
    private final FullPvP plugin;

    public CrateCreateArgument(FullPvP plugin) {
        super("create", "Creates a crate.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.crate.argument." + this.getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <crateName> <rewardsAmmount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (sender instanceof Player) {
            Integer intRewards = JavaUtils.tryParseInt(arguments[2]);
            if (intRewards == null) {
                sender.sendMessage(ChatColor.RED + "Rewards number must be numeric.");
            }

            this.plugin.getCrateManager().onCrateCreate((Player)sender, arguments[1], intRewards);
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
            Iterator<Crate> var6 = this.plugin.getCrateManager().getCratesList().iterator();

            while(var6.hasNext()) {
                String name = var6.next().getName();
                cratesName.add(name);
            }
            return cratesName;
        }
    }
}
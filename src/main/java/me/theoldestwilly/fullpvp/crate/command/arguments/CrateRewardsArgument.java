package me.theoldestwilly.fullpvp.crate.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.crate.Crate;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CrateRewardsArgument  extends CommandArgument {
    private final FullPvP plugin;

    public CrateRewardsArgument(FullPvP plugin) {
        super("rewards", "Changes crate's rewards.", new String[]{""});
        this.plugin = plugin;
        this.permission = "fullpvp.command.crate.argument.rewards";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <crateName> <numRewards>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else {
            Crate crate = this.plugin.getCrateManager().getCrateByName(arguments[1]);
            if (crate == null) {
                sender.sendMessage(ChatColor.RED + "Crate with name '" + ChatColor.GRAY + arguments[1] + ChatColor.RED + "' not found.");
                return true;
            }

            Integer intRewards = JavaUtils.tryParseInt(arguments[2]);
            if (intRewards == null) {
                sender.sendMessage(ChatColor.RED + "Invalid value for rewards ammount.");
            }

            crate.setRewardsNumber(intRewards);
            sender.sendMessage(ChatColor.GREEN + "The rewards ammount for " + crate.getColor() + crate.getName() + ChatColor.GREEN + " has been modified to " + ChatColor.YELLOW + crate.getRewards());
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList colors;
        if (args.length == 2) {
            colors = new ArrayList();
            Iterator var10 = this.plugin.getCrateManager().getCratesList().iterator();

            while(var10.hasNext()) {
                String name = ((Crate)var10.next()).getName();
                colors.add(name);
            }

            return colors;
        } else if (args.length != 3) {
            return Collections.emptyList();
        } else {
            colors = new ArrayList();
            ChatColor[] var6 = ChatColor.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                ChatColor color = var6[var8];
                colors.add(color.name());
            }

            return colors;
        }
    }
}

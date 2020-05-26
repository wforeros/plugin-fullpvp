package me.theoldestwilly.fullpvp.world.claims.command.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClaimTogglePearlArgument extends CommandArgument {
    private FullPvP plugin;

    public ClaimTogglePearlArgument(FullPvP plugin) {
        super("pearl", "Modify pearl permission in land.", new String[]{""});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <claimName> <enable/disable>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (this.plugin.getClaimHandler().getClaimsList().containsKey(arguments[1])) {
            if (arguments[2].equalsIgnoreCase("enable")) {
                (this.plugin.getClaimHandler().getClaimsList().get(arguments[1])).setPearlAllowed(true);
                sender.sendMessage(ChatColor.BLUE + "You have " + ChatColor.GREEN + "enabled" + ChatColor.BLUE + " the use of ender pearls into this claim.");
            } else if (arguments[2].equalsIgnoreCase("disable")) {
                (this.plugin.getClaimHandler().getClaimsList().get(arguments[1])).setPearlAllowed(false);
                sender.sendMessage(ChatColor.BLUE + "You have " + ChatColor.RED + "disabled" + ChatColor.BLUE + " the use of ender pearls into this claim.");
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid value, use <enable/disable>.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Claim " + arguments[1] + " not found.");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 2) {
            if (arguments.length == 3) {
                List<String> options = Arrays.asList("enable", "disable");
                return options;
            } else {
                return Collections.emptyList();
            }
        } else {
            List<String> claimsName = new ArrayList();
            Iterator var6 = this.plugin.getClaimHandler().getClaimsList().keySet().iterator();

            while(var6.hasNext()) {
                String name = (String)var6.next();
                claimsName.add(name);
            }

            return claimsName;
        }
    }
}

package me.theoldestwilly.fullpvp.world.claims.command.argument;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClaimDeleteArgument extends CommandArgument {
    private FullPvP plugin;

    public ClaimDeleteArgument(FullPvP plugin) {
        super("delete", "Deletes land.", new String[]{""});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <claimName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.plugin.getClaimHandler().getClaimsList().containsKey(arguments[1])) {
                if (this.plugin.getClaimHandler().getClaimsList().size() > 1) {
                    this.plugin.getClaimHandler().getClaimsList().remove(arguments[1]);
                } else {
                    this.plugin.getClaimHandler().getClaimsList().clear();
                }

                player.sendMessage(ChatColor.GREEN + "Claim " + ChatColor.GRAY + arguments[1] + ChatColor.GREEN + " was succesfully deleted.");
            } else {
                player.sendMessage(ChatColor.RED + "Claim " + arguments[1] + " not found.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
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

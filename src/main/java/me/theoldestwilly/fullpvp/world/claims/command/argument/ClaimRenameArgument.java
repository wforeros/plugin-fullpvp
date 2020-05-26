package me.theoldestwilly.fullpvp.world.claims.command.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimRenameArgument extends CommandArgument {
    private FullPvP plugin;

    public ClaimRenameArgument(FullPvP plugin) {
        super("rename", "Renames land.", new String[]{""});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <claimName> <newName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.plugin.getClaimHandler().getClaimsList().containsKey(arguments[1])) {
                (this.plugin.getClaimHandler().getClaimsList().get(arguments[1])).setName(arguments[2]);
                player.sendMessage(ChatColor.GREEN + "You have renamed the claim to " + ChatColor.YELLOW + arguments[2] + ChatColor.GREEN + ".");
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

package me.theoldestwilly.fullpvp.world.claims.command.argument;

import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCreateArgument extends CommandArgument {
    private FullPvP plugin;

    public ClaimCreateArgument(FullPvP plugin) {
        super("create", "Claims land.", new String[]{""});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <claimName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!this.plugin.getClaimHandler().getClaimsList().containsKey(arguments[1])) {
                this.plugin.getClaimHandler().onClaimLand(player, arguments[1]);
            } else {
                player.sendMessage(ChatColor.RED + "This claim already exists.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        return null;
    }
}

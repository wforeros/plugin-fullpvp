package me.theoldestwilly.fullpvp.world.claims.command.argument;

import java.util.List;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import me.theoldestwilly.fullpvp.world.claims.ClaimedLand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimLandArgument extends CommandArgument {
    private FullPvP plugin;

    public ClaimLandArgument(FullPvP plugin) {
        super("land", "Claims land.", new String[]{""});
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
                Selection selection = this.plugin.getWorldEditPlugin().getSelection(player);
                if (selection == null) {
                    player.sendMessage(ChatColor.RED + "Selection not found.");
                }

                Cuboid cuboid = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
                ((ClaimedLand)this.plugin.getClaimHandler().getClaimsList().get(arguments[1])).setProtectedArea(new ProtectedArea(cuboid));
                player.sendMessage(ChatColor.GREEN + "You have modified the " + ChatColor.YELLOW + arguments[1] + ChatColor.GREEN + " claimed land.");
            } else {
                player.sendMessage(ChatColor.RED + "Claim " + arguments[1] + " not found.");
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

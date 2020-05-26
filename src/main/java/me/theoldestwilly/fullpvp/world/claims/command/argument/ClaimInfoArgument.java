package me.theoldestwilly.fullpvp.world.claims.command.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.world.claims.ClaimedLand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClaimInfoArgument extends CommandArgument {
    private FullPvP plugin;

    public ClaimInfoArgument(FullPvP plugin) {
        super("info", "Shows information about a claim.", new String[]{""});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <claimName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (this.plugin.getClaimHandler().getClaimsList().containsKey(arguments[1])) {
            ClaimedLand claimedLand = this.plugin.getClaimHandler().getByName(arguments[1]);
            sender.sendMessage(TextUtils.formatColor("&7&m-------------------------------------------------"));
            sender.sendMessage(TextUtils.formatColor("&9&lClaim Information"));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bName&7: &f" + claimedLand.getName()));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bPermission&7: &f" + claimedLand.getPermission()));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bBroadcast&7: " + (claimedLand.isBroadcastAllowed() ? "&aEnabled" : "&cDisabled")));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bFly&7: " + (claimedLand.isFlyAllowed() ? "&aEnabled" : "&cDisabled")));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bEnder Pearl&7: " + (claimedLand.isPearlAllowed() ? "&aEnabled" : "&cDisabled")));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bPvP&7: " + (claimedLand.isPvpAllowed() ? "&aEnabled" : "&cDisabled")));
            Cuboid protectedArea = claimedLand.getProtectedArea().getArea();
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bWorld&7: &f" + protectedArea.getWorldName()));
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bMinimum Point&7:"));
            Location location = protectedArea.getMinimumPoint();
            sender.sendMessage(TextUtils.formatColor("   &7X: &f" + location.getBlockX()));
            sender.sendMessage(TextUtils.formatColor("   &7Y: &f" + location.getBlockY()));
            sender.sendMessage(TextUtils.formatColor("   &7Z: &f" + location.getBlockZ()));
            location = protectedArea.getMaximumPoint();
            sender.sendMessage(TextUtils.formatColor(" &b&l* &bMaximum Point&7: &f"));
            sender.sendMessage(TextUtils.formatColor("   &7X: &f" + location.getBlockX()));
            sender.sendMessage(TextUtils.formatColor("   &7Y: &f" + location.getBlockY()));
            sender.sendMessage(TextUtils.formatColor("   &7Z: &f" + location.getBlockZ()));
            sender.sendMessage(TextUtils.formatColor("&7&m-------------------------------------------------"));
        } else {
            sender.sendMessage(ChatColor.RED + "Claim " + arguments[1] + " not found.");
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

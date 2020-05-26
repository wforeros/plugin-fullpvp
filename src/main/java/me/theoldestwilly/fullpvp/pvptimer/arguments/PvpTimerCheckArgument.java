package me.theoldestwilly.fullpvp.pvptimer.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PvpTimerCheckArgument extends CommandArgument {
    private FullPvP plugin;
    public PvpTimerCheckArgument(FullPvP plugin) {
        super("check", "Check if player has an active pvp timer.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            return true;
        }
        Profile profile = plugin.getProfileHandler().getProfile(arguments[1]);
        if (profile == null) {
            sender.sendMessage(Lang.ERROR_GLOBAL_PLAYER_NOT_ONLINE);
            return true;
        }
        OfflinePlayer player = profile.toOfflinePlayer();
        String prefix = player.isOnline() ? ((Player) player).getDisplayName() : ChatColor.GOLD + player.getName();
        String msg = prefix + (profile.hasPvpTimer() ?  "'s" + ChatColor.YELLOW + " remaining pvp timer: " + ChatColor.GRAY + JavaUtils.setFormat(profile.getRemainingPvpTimer()) + ChatColor.YELLOW + ".": " has not an active pvp timer.");
        sender.sendMessage(msg);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}


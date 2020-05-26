package me.theoldestwilly.fullpvp.pvptimer.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.ConvertFormat;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PvpTimerSetArgument extends CommandArgument {
    private FullPvP plugin;
    public PvpTimerSetArgument(FullPvP plugin) {
        super("set", "Sets a new timer to the target player.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player> <duration(h/m/s)>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            return true;
        }

        Profile profile = plugin.getProfileHandler().getProfile(arguments[1]);
        if (profile == null) {
            sender.sendMessage(Lang.ERROR_GLOBAL_PLAYER_NOT_ONLINE);
            return true;
        }
        String duration = "";
        for (int i = 2; i < arguments.length; i++) duration += arguments[i] + " ";
        Long durationL = ConvertFormat.convertDelay2Long2(duration, sender);
        if (durationL != null) {
            profile.startPvpTimer(duration);
        }
        Player player = Bukkit.getPlayer(profile.getUniqueId());
        sender.sendMessage(ChatColor.YELLOW + "The player " + player.getDisplayName() + ChatColor.YELLOW + " has a pvp timer now of " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(durationL, true, true) + ChatColor.YELLOW + ".");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}

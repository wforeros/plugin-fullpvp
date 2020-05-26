package me.theoldestwilly.fullpvp.modes.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.ConvertFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ServerModeStartArgument extends CommandArgument {
    private final FullPvP plugin;

    public ServerModeStartArgument(FullPvP plugin) {
        super("start", "Sets a new server mode where pvp is disabled.", new String[]{});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " graceperiod <duration(h/m/s)>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "This command only can be executed in the console.");
            return true;
        }
        if (arguments.length < 3) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        } else {
            if (!arguments[1].equalsIgnoreCase("graceperiod")) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
                return true;
            }
            Long duration = ConvertFormat.convertDelay2Long2(arguments[2], sender);
            if (duration == null) {
                sender.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
                return true;
            }
            plugin.getModesManager().startMode(sender, duration);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? Collections.singletonList("graceperiod") : Collections.emptyList();
    }
}


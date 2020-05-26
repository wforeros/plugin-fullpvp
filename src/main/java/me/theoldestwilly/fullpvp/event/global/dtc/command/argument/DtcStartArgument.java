package me.theoldestwilly.fullpvp.event.global.dtc.command.argument;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.GlobalEventType;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DtcStartArgument extends CommandArgument {
    private FullPvP plugin;
    public DtcStartArgument(FullPvP plugin) {
        super("start", "It starts a new dtc event by its name");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <event-name>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            else plugin.getGlobalEventsManager().onStartNewEvent(sender, arguments[1], GlobalEventType.DTC, null);
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? FullPvP.getInstance().getGlobalEventsManager().getDestroyTheCoreManager().getEventNames() : Collections.emptyList();
    }
}

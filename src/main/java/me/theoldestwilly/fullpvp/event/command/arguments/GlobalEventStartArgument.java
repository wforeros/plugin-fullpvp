package me.theoldestwilly.fullpvp.event.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.GlobalEventType;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GlobalEventStartArgument extends CommandArgument {
    private FullPvP plugin;
    public GlobalEventStartArgument(FullPvP plugin) {
        super("start", "Starts a new event (koths, dtc or dropper)");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <type> <name>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length >= 3) {
            GlobalEventType type = GlobalEventType.fromString(arguments[1]);
            if (type == null) {
                sender.sendMessage(Lang.ERROR_COLOR + "There is not an event type named " + ChatColor.GRAY + arguments[1]);
                return true;
            }
            plugin.getGlobalEventsManager().onStartNewEvent(sender, arguments[2], type, null);
        } else if (arguments.length == 2){
            GlobalEventType type = GlobalEventType.fromString(arguments[1]);
            if (type != GlobalEventType.DROPPER) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
                return true;
            }
            plugin.getGlobalEventsManager().onStartNewEvent(sender, "", type, null);
        }else {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            return GlobalEventType.getValuesAsString();
        } else if (args.length == 3) {
            GlobalEventType type = GlobalEventType.fromString(args[1]);
            if (type == GlobalEventType.DTC) return FullPvP.getInstance().getGlobalEventsManager().getDestroyTheCoreManager().getEventNames();
            else if (type == GlobalEventType.KOTH) return FullPvP.getInstance().getGlobalEventsManager().getKothManager().getEventsName();
        }
        return null;
    }
}

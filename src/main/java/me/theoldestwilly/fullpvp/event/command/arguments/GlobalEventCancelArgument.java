package me.theoldestwilly.fullpvp.event.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class GlobalEventCancelArgument extends CommandArgument {
    private FullPvP plugin;
    public GlobalEventCancelArgument(FullPvP plugin) {
        super("cancel", "Cancels the current global event (koths, dtc or dropper)");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String var1) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        plugin.getGlobalEventsManager().cancelEvent(sender);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}

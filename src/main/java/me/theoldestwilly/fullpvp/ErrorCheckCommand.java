package me.theoldestwilly.fullpvp;

import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ErrorCheckCommand extends ArgumentExecutor {
    private FullPvP plugin;
    public ErrorCheckCommand(FullPvP plugin) {
        super("error", new String[]{"errorcheck", "errorreport"});
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 1) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE("/" + label + " <number>"));
        } else {
            PluginError error = PluginError.getErrorByNumber(arguments[0]);
            if (error != null) {
                sender.sendMessage(error.getDescription());
            }
        }
        return true;
    }
}

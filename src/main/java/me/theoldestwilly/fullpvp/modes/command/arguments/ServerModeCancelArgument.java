package me.theoldestwilly.fullpvp.modes.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ServerModeCancelArgument extends CommandArgument {
    private final FullPvP plugin;

    public ServerModeCancelArgument(FullPvP plugin) {
        super("cancel", "Sets the server mode to the default mode (PvP Enabled).", new String[]{});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "This command only can be executed in console.");
            return true;
        }
        plugin.getModesManager().cancelCurrentMode(sender);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}


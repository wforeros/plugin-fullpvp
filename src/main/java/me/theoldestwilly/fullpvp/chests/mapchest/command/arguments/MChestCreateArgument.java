package me.theoldestwilly.fullpvp.chests.mapchest.command.arguments;


import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MChestCreateArgument extends CommandArgument {
    private final FullPvP plugin;

    public MChestCreateArgument(FullPvP plugin) {
        super("create", "Command used to create a new map chest.", new String[]{"c"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " delay<d/h/m/s>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2)
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            else
                plugin.getMapChestManager().saveMapChest((Player) sender, arguments);
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}


package me.theoldestwilly.fullpvp.chests.kitschest.command.arguments;


import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KChestRemoveArgument extends CommandArgument {
    private final FullPvP plugin;

    public KChestRemoveArgument(FullPvP plugin) {
        super("remove", "Command used to remove an existing kit chest.", new String[]{"r"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            plugin.getMapChestManager().removeMapChest((Player) sender);
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}


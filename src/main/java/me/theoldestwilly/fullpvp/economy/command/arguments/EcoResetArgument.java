package me.theoldestwilly.fullpvp.economy.command.arguments;


import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class EcoResetArgument extends CommandArgument {
    private final FullPvP plugin;

    public EcoResetArgument(FullPvP plugin) {
        super("reset", "Command used to reset a player balance.", new String[]{});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[1]);
            plugin.getEconomyManager().setMoneyToTargetPlayer(sender, target, 0D);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}


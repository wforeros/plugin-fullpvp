package me.theoldestwilly.fullpvp.economy.command.arguments;


import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class EcoSetArgument extends CommandArgument {
    private final FullPvP plugin;

    public EcoSetArgument(FullPvP plugin) {
        super("set", "Command used to set a specigfic valor to a player balance.", new String[]{});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player> <amount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        } else {
            Double amount = JavaUtils.tryParseDouble(arguments[2]);
            if (amount != null && amount > 0) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[1]);
                plugin.getEconomyManager().setMoneyToTargetPlayer(sender, target, amount);
            } else {
                sender.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}


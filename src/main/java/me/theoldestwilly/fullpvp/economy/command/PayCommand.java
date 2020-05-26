package me.theoldestwilly.fullpvp.economy.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PayCommand extends ArgumentExecutor {
    private FullPvP plugin;
    public PayCommand (FullPvP plugin) {
        super("pay");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE('/' + label + " <player> <amount>"));
            } else {
                Double amount = JavaUtils.tryParseDouble(arguments[1]);
                if (amount == null) {
                    sender.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
                    return true;
                }
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(arguments[0]);
                plugin.getEconomyManager().sendMoneyToOtherPlayer((Player) sender, receiver, amount);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

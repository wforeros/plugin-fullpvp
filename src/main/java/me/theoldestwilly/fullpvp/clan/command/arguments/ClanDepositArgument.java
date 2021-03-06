package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.Collections;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanDepositArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanDepositArgument(FullPvP plugin) {
        super("deposit", "Deposit money to clan's balance.", new String[]{"d"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <ammount>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            } else {
                Double amount = JavaUtils.tryParseDouble(arguments[1]);
                if (amount == null) {
                    sender.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
                } else {
                    this.plugin.getClanManager().clanDeposit((Player)sender, amount);
                }
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
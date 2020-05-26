package me.theoldestwilly.fullpvp.economy.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BalanceCommand extends ArgumentExecutor {
    private FullPvP plugin;
    public BalanceCommand (FullPvP plugin) {
        super("balance", new String[]{"bal"});
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 0) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(arguments[0]);
            plugin.getEconomyManager().checkTargetsBalance(sender, player);
        } else {
            if (sender instanceof Player) {
                plugin.getEconomyManager().checkOwnBalance((Player) sender);
            } else {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(label + " <player>"));
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 && (!(sender instanceof Player) || sender.isOp()) ? null : Collections.emptyList();
    }
}

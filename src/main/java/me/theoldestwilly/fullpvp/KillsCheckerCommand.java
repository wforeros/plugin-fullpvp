package me.theoldestwilly.fullpvp;

import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import net.silexpvp.nightmare.util.JavaUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillsCheckerCommand extends ArgumentExecutor {
    private FullPvP plugin;
    public KillsCheckerCommand(FullPvP plugin) {
        super("checkkills");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length >= 1) {
                Integer integer = JavaUtils.tryParseInteger(arguments[0]);
                sender.sendMessage(Lang.SUCCESS_COLOR + "Kills needed to reach level " + integer + ChatColor.GRAY + ": " + GameUtils.getKills(plugin.getPlayersHandler().getRankupKills(), integer).intValue());
            } else sender.sendMessage(ChatColor.RED + "Usage: /checkkills <level>");
        } else sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        return true;
    }
}
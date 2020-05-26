package me.theoldestwilly.fullpvp.scoreboard;

import me.theoldestwilly.fullpvp.FullPvP;
import net.silexpvp.nightmare.util.command.ExecutableCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class ToggleScoreboardCommand extends ExecutableCommand<FullPvP> {

    public ToggleScoreboardCommand() {
        super("togglescoreboard", "Toggles your scoreboard visibility.", "togglesidebar", "togglehud", "togglesb");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }

        Player player = (Player) sender;

        PlayerScoreboard scoreboard = plugin.getScoreboardManager().getPlayerScoreboard(player);
        if (scoreboard == null) {
            player.sendMessage(ChatColor.RED + "You do not have a scoreboard, please inform this issue if this error consists.");
            return true;
        }

        boolean newVisible = !scoreboard.isSidebarVisible();
        scoreboard.setSidebarVisible(newVisible);
        sender.sendMessage(ChatColor.YELLOW + "Your scoreboard is " + (newVisible ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " visible.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        return Collections.emptyList();
    }
}
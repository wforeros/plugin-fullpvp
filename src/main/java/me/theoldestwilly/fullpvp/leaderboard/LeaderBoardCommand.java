package me.theoldestwilly.fullpvp.leaderboard;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import net.silexpvp.nightmare.util.BukkitUtils;
import net.silexpvp.nightmare.util.command.ExecutableCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class LeaderBoardCommand extends ArgumentExecutor {

    public LeaderBoardCommand() {
        super("leaderboard", new String[]{"lb"});
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FullPvP plugin = FullPvP.getInstance();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            LeaderBoard leaderBoard;

            if (args.length < 1) {
                leaderBoard = plugin.getLeaderBoardManager().getLeaderboard("Kills");
            } else {
                leaderBoard = plugin.getLeaderBoardManager().getLeaderboard(args[0]);
                if (leaderBoard == null) {
                    sender.sendMessage(ChatColor.RED + "Leaderboard category with name '" + args[0] + "' could not be found.");
                    sender.sendMessage(ChatColor.RED + "Available leaderboard categories are " + ChatColor.GRAY + plugin.getLeaderBoardManager().getLeaderBoards().stream().map(LeaderBoard::getName).collect(Collectors.joining(ChatColor.RED + ", " + ChatColor.GRAY)) + ChatColor.RED + '.');
                    return;
                }
            }
            if (leaderBoard.getName().equalsIgnoreCase(LeaderBoardIndex.RANDOM.getDbIndex())) leaderBoard = plugin.getLeaderBoardManager().getLeaderboard(plugin.getLeaderBoardManager().getCurrentIndex().getDbIndex());
                    List<LeaderBoardPlayer> users = leaderBoard.getPlayers(10);
            if (users.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Could not find any results for leaderboard " + leaderBoard.getDisplayName() + '.');
                return;
            }

            sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55));
            sender.sendMessage(ChatColor.YELLOW + " Displaying top player statistics based off " + ChatColor.GOLD + leaderBoard.getDisplayName() + ChatColor.YELLOW + '.');
            for (int position = 1; position <= users.size(); position++) {
                LeaderBoardPlayer player = users.get(position - 1);
                sender.sendMessage("  " + (position == 1 ? ChatColor.GOLD.toString() + ChatColor.BOLD : ChatColor.GRAY.toString()) + position + ". " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " - " + ChatColor.WHITE + player.getScore() + ' ' + ChatColor.YELLOW + leaderBoard.getDisplayName() + '.');
            }
            sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55));
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? FullPvP.getInstance().getLeaderBoardManager().getLeaderBoards().stream().map(LeaderBoard::getName).collect(Collectors.toList()) : Collections.emptyList();
    }
}
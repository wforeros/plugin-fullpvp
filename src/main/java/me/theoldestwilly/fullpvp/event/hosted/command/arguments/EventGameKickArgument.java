package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventGameKickArgument extends CommandArgument {
    private final FullPvP plugin;

    public EventGameKickArgument(FullPvP plugin) {
        super("kick", "Kicks a player from the hosted event.", new String[]{"j"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.eventgame.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player> <reason>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else {
            HostedEvent event = this.plugin.getHostedEventsManager().getCurrentEvent();
            if (event == null) {
                sender.sendMessage(ChatColor.RED + "Hosted event not found.");
            } else {
                Player player = Bukkit.getPlayer(arguments[1]);
                if (player == null) {
                    player.sendMessage(ChatColor.RED + "Player with name '" + arguments[1] + "' not found.");
                    return true;
                }
                if (!event.isAlive(player)) {
                    player.sendMessage(ChatColor.RED + "That player is not alive in the host game.");
                    return true;
                }
                String reason = ChatColor.RED + "You were kicked by a staff member. ";
                int length = arguments.length;
                if (length >= 3) {
                    for(int i = 2; i < length; ++i) {
                        reason = arguments[i] + (length - 1 == i ? "" : " ");
                    }
                }
                event.removePlayer(player, reason, false, false);
                sender.sendMessage(event.getPrefix() + player.getDisplayName() + ChatColor.YELLOW + " was succesfully kicked.");
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            HostedEvent event = this.plugin.getHostedEventsManager().getCurrentEvent();
            if (event != null) {
                List<String> list = new ArrayList();
                event.getAlivePlayers().stream().forEach(player -> list.add(player.getName()));
                return list;
            }
        }
        return null;
    }
}

package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventGameLeaveArgument extends CommandArgument {
    private final FullPvP plugin;

    public EventGameLeaveArgument(FullPvP plugin) {
        super("leave", "Leave a hosted event.", new String[]{"j"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            HostedEvent event = this.plugin.getHostedEventsManager().getCurrentEvent();
            if (event == null) {
                player.sendMessage(ChatColor.RED + "Hosted event not found.");
            } else {
                if (!event.isParticipant(player)) {
                    player.sendMessage(ChatColor.RED + "You can not execute if you are not in an event.");
                    return true;
                }
                if (event.isJoinable()) {
                    event.removePlayer(player, ChatColor.RED + "You left the event succesfully.", false, false);
                } else {
                    player.sendMessage(ChatColor.RED + "You can not leave a started event.");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can execute this command on console");
        }

        return true;
    }
}

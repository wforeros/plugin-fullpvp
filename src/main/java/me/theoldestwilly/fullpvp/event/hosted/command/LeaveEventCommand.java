package me.theoldestwilly.fullpvp.event.hosted.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveEventCommand extends ArgumentExecutor {

    private final FullPvP plugin;

    public LeaveEventCommand(FullPvP plugin) {
        super("leaveevent", new String[]{"leave"});
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            HostedEvent event = this.plugin.getHostedEventsManager().getCurrentEvent();
            if (event == null) {
                player.sendMessage(ChatColor.RED + "Hosted event not found.");
            } else {
                if (event.isParticipant(player)) {
                    player.sendMessage(ChatColor.RED + "You can not execute this command while in an event.");
                    return true;
                }
                if (event.isSpectator(player)) {
                    event.removeSpectator(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You are not in spectator mode.");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can execute this command on console");
        }


        return true;
    }
}

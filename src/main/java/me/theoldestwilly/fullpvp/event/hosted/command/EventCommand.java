package me.theoldestwilly.fullpvp.event.hosted.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand extends ArgumentExecutor {

    private final FullPvP plugin;

    public EventCommand(FullPvP plugin) {
        super("event");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getHostedEventsManager().openEventsGui(player);
        } else {
            sender.sendMessage(ChatColor.RED + "You can execute this command on console");
        }
        return true;
    }
}

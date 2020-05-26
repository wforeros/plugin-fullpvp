package me.theoldestwilly.fullpvp;

import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;

public class EventSimulatorCommand extends ArgumentExecutor {
    public EventSimulatorCommand() {
        super("simulateevent");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Player player = Bukkit.getPlayer(arguments[0]);
        player.spigot().respawn();
        return true;
    }

}

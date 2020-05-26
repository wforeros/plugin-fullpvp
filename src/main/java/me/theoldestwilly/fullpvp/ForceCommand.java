package me.theoldestwilly.fullpvp;

import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceCommand extends ArgumentExecutor {
    public ForceCommand() {
        super("forcerespawn", new String[]{"forcespawn"});
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Player player = Bukkit.getPlayer(arguments[0]);
        if (player != null) player.spigot().respawn();
        else sender.sendMessage(ChatColor.RED + "Player can not be null.");
        return true;
    }

}

package me.theoldestwilly.fullpvp.users.commands;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public HelpCommand(FullPvP plugin) {
        super("help");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            this.plugin.getPlayersCommandsManager().sendHelpMessage(player);
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
        }

        return true;
    }
}
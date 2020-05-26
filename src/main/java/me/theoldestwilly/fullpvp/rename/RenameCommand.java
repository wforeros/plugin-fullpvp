package me.theoldestwilly.fullpvp.rename;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public RenameCommand(FullPvP plugin) {
        super("rename");
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (arguments.length != 0) {
                if (arguments[0].equalsIgnoreCase("reset")) {
                    this.plugin.getRenameManager().resetName(player, player.getItemInHand());
                } else {
                    this.plugin.getRenameManager().renameTool(player, arguments, player.getItemInHand());
                }
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /rename <newName>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
        }

        return true;
    }
}


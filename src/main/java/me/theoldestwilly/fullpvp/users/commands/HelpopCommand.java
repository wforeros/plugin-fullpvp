package me.theoldestwilly.fullpvp.users.commands;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpopCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public HelpopCommand(FullPvP plugin) {
        super("helpop");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (arguments.length >= 1) {
                this.plugin.getPlayersCommandsManager().onHelpopUse(player, arguments);
            } else {
                player.sendMessage(TextUtils.formatColor("&cUsage: /" + label + " <message>"));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
        }

        return true;
    }
}


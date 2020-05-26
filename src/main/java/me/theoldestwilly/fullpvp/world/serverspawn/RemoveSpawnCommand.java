package me.theoldestwilly.fullpvp.world.serverspawn;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class RemoveSpawnCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public RemoveSpawnCommand(FullPvP plugin) {
        super("removespawn");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("fullpvp.command.removespawn")) {
                if (arguments.length == 0) {
                    sender.sendMessage(Lang.SHOW_COMMAND_USAGE("/" + getLabel() + " <spawn-name>"));
                } else {
                    plugin.getSpawnHandler().removeSpawn(player, arguments[0]);
                }
            } else {
                player.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return FullPvP.getInstance().getSpawnHandler().getSpawnNames();
        }
        return Collections.emptyList();
    }
}
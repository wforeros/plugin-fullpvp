package me.theoldestwilly.fullpvp.world.serverspawn;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public SetSpawnCommand(FullPvP plugin) {
        super("setspawn");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("fullpvp.command.setspawn")) {
                if (arguments.length == 0) {
                    sender.sendMessage(Lang.SHOW_COMMAND_USAGE("/" + getLabel() + " <spawn-name>"));
                } else {
                    Location location = player.getLocation();
                    plugin.getSpawnHandler().addGlobalSpawn(player, arguments[0], location);
                    player.sendMessage(Lang.PREFIX + ChatColor.YELLOW + "Spawn saved.");
                }
            } else {
                player.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }
}

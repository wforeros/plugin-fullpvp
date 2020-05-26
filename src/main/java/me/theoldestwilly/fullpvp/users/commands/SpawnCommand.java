package me.theoldestwilly.fullpvp.users.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import net.silexpvp.nightmare.Nightmare;
import net.silexpvp.nightmare.timer.type.TeleportTimer.Request;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public SpawnCommand(FullPvP plugin) {
        super("spawn");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //if (!this.plugin.getTagsManager().playerIsTagged(player.getUniqueId())) {
            /*} else {
                player.sendMessage(ChatColor.RED + "You are not allowed to execute this command whilst you are combat tagged.");
            }*/
            String spawn = arguments != null && arguments.length >= 1 ? arguments[0]:"main";
            plugin.getSpawnHandler().teleportToSpawn(player, spawn);
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
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

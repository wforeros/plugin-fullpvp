package me.theoldestwilly.fullpvp.profileviewer.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ProfileVillagerSpawnArgument extends CommandArgument {
    private final FullPvP plugin;

    public ProfileVillagerSpawnArgument(FullPvP plugin) {
        super("spawn", "Spawns a profile viewer villager.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.profilemanager.argument.spawn";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            this.plugin.getProfileViewerHandler().spawnProfileVillager((Player)sender);
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}

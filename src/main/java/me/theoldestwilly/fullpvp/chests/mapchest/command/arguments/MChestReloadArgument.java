package me.theoldestwilly.fullpvp.chests.mapchest.command.arguments;


import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MChestReloadArgument extends CommandArgument {
    private final FullPvP plugin;

    public MChestReloadArgument(FullPvP plugin) {
        super("reload", "Command used to reload mapchest's names and checks if in the locations are placed chests, if not the empty locs will be deleted.", new String[]{});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " delay<d/h/m/s>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        plugin.getMapChestManager().reloadMapChests(sender);
        sender.sendMessage(Lang.SUCCES_RELOAD);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}


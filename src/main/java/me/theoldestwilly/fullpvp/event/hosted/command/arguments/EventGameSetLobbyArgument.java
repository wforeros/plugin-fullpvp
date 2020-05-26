package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventGameSetLobbyArgument extends CommandArgument {
    private final FullPvP plugin;

    public EventGameSetLobbyArgument(FullPvP plugin) {
        super("setlobby", "Modifies the events prelobby.", new String[]{"sl"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.eventgame.argument." + getName();
    }

    public String getUsage(String label) {
        return ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " <type>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (PermissionsManager.hasEventGameAdminPermission(player)) {
                Config config = this.plugin.getHostedEventsManager().getConfig();
                Location location = player.getLocation();
                config.set("lobby", location);
                config.save();
                player.sendMessage(ChatColor.GREEN + "Lobby saved, it is the same for all events.");
            } else {
                player.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can execute this command on console");
        }

        return true;
    }

}

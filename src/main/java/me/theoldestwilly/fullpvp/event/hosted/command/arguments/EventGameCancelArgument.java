package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventGameCancelArgument extends CommandArgument {
    private final FullPvP plugin;

    public EventGameCancelArgument(FullPvP plugin) {
        super("cancel", "Cancel the event.", new String[]{"c"});
        this.plugin = plugin;
        permission = "fullpvp.command.eventgame.argument." + getName();
    }

    public String getUsage(String label) {
        return ChatColor.RED + "Usage: /" + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 1) {
            sender.sendMessage(this.getUsage(label));
        } else {
            if (!(sender instanceof Player) || PermissionsManager.hasEventCancelPermission((Player) sender)) {
                HostedEvent hostedEvent = this.plugin.getHostedEventsManager().getCurrentEvent();
                String reason = "";
                if (arguments.length >= 2) {
                    for (int i = 1; i < arguments.length; ++i) {
                        reason = reason + arguments[i];
                        if (i < arguments.length - 1) {
                            reason = reason + " ";
                        }
                    }
                } else {
                    reason = "Prevention";
                }

                if (hostedEvent != null) {
                    hostedEvent.cancel(reason);
                } else {
                    sender.sendMessage(ChatColor.RED + "There is not hosted event now.");
                }
            } else {
                sender.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
            }
        }

        return true;
    }
}


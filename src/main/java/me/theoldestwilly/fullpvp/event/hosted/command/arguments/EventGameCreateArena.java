package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.events.SpleefEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventGameCreateArena extends CommandArgument {
    private final FullPvP plugin;

    public EventGameCreateArena(FullPvP plugin) {
        super("createarena", "Creates an event arena.", new String[]{"j"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            HostedEventType type = HostedEventType.fromString(arguments[1]);
            if (type == null) {
                sender.sendMessage(ChatColor.RED + "Event type not found. Available options:");
                HostedEventType[] var10 = HostedEventType.values();
                int var11 = var10.length;
                for (int var12 = 0; var12 < var11; ++var12) {
                    HostedEventType type1 = var10[var12];
                    sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + type1.toString().toLowerCase());
                }
                return true;
            }
            if (type != HostedEventType.SPLEEF) {
                sender.sendMessage(ChatColor.RED + "This event does not requires an arena.");
                return true;
            }
            SpleefEvent.createArena(player);
        } else {
            sender.sendMessage(ChatColor.RED + "You can execute this command on console");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList("spleef");
        }
    }
}

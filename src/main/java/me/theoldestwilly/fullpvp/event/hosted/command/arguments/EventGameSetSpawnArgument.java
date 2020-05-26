package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventGameSetSpawnArgument extends CommandArgument {
    private final FullPvP plugin;

    public EventGameSetSpawnArgument(FullPvP plugin) {
        super("setspawn", "Set the spawn(s) event, it depends of the required event.", new String[]{"ss"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.eventgame.argument." + getName();
    }

    public String getUsage(String label) {
        return ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " <type>";
    }

    public String getSpleefUsage(String label) {
        return ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " spleef <1/2>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(this.getUsage(label));
            } else {
                HostedEventType type = HostedEventType.fromString(arguments[1]);
                if (type == null) {
                    sender.sendMessage(ChatColor.RED + "Event type not found. Available options:");
                    HostedEventType[] var10 = HostedEventType.values();
                    int var11 = var10.length;
                    for(int var12 = 0; var12 < var11; ++var12) {
                        HostedEventType type1 = var10[var12];
                        sender.sendMessage(ChatColor.GRAY + " * " + ChatColor.YELLOW + type1.toString().toLowerCase());
                    }
                    return true;
                }

                Player player = (Player)sender;
                if (!PermissionsManager.hasEventGameAdminPermission(player)) {
                    player.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
                    return true;
                }

                Config config = this.plugin.getHostedEventsManager().getConfig();
                Location location = player.getLocation();
                String path;
                if (type == HostedEventType.SPLEEF) {
                    if (arguments.length < 3) {
                        player.sendMessage(this.getSpleefUsage(label));
                        return true;
                    }
                    Integer integer = JavaUtils.tryParseInt(arguments[2]);
                    if (integer == null ||(integer != 1  && integer != 2)) {
                        player.sendMessage(getSpleefUsage(label));
                        return true;
                    }
                    path = type.toLowerString() + ".spawn." + arguments[2];
                    config.set(path, location);
                    config.save();
                    player.sendMessage(ChatColor.GREEN + "Spleef spawn number " + arguments[2] + " saved.");
                } else {
                    path = type.toString().toLowerCase() + ".spawn";
                    config.set(path, location);
                    config.save();
                    player.sendMessage(type.getDisplayName() + ChatColor.GREEN + " spawn saved.");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can execute this command on console");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return null;
        } else {
            List<String> list = new ArrayList();
            for(HostedEventType type : HostedEventType.values()) {
                list.add(type.toString().toLowerCase());
            }
            return list;
        }
    }
}

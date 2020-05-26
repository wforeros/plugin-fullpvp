package me.theoldestwilly.fullpvp.event.hosted.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventGameSetRewardArgument extends CommandArgument {
    private final FullPvP plugin;

    public EventGameSetRewardArgument(FullPvP plugin) {
        super("setreward", "Set the reward for a specific event.", new String[]{"sa"});
        this.plugin = plugin;
        this.permission = "fullpvp.command.eventgame.argument." + getName();
    }

    public String getUsage(String label) {
        return ChatColor.RED + "Usage: /" + label + ' ' + this.getName() + " <type>";
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
                ItemStack[] contents = player.getInventory().getContents();
                if (Checkers.isInventoryEmpty(Arrays.asList(contents))) {
                    sender.sendMessage(ChatColor.RED + "You can not set the event reward with an empty inventory.");
                    return true;
                }

                if (PermissionsManager.hasEventGameAdminPermission(player)) {
                    Config config = this.plugin.getHostedEventsManager().getConfig();
                    config.set("rewards", Checkers.removeNullItems(contents));
                    config.save();
                    player.sendMessage(ChatColor.GREEN + "Reward for event " + type.getDisplayName() + ChatColor.GREEN + " succesfully saved!");
                } else {
                    player.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
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
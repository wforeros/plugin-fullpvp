package me.theoldestwilly.fullpvp.event.global.koths.command.arguments;

import java.util.ArrayList;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KothCreateArgument extends CommandArgument {
    private final FullPvP plugin;

    public KothCreateArgument(FullPvP plugin) {
        super("create", "Creates a new koth.", new String[]{""});
        this.plugin = plugin;
        this.permission = "fullpvp.command.koth.argument.create";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kothName> <duration(d/h/m/s)>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length >= 3 && arguments.length <= 6) {
            if (sender instanceof Player) {
                List<String> delay = new ArrayList();
                if (arguments.length != 2) {
                    for(int i = 2; i < arguments.length; ++i) {
                        delay.add(arguments[i]);
                    }
                } else {
                    delay.add(this.plugin.getGlobalEventsManager().getKothManager().getDefaultKothDuration() + "");
                }

                this.plugin.getGlobalEventsManager().getKothManager().onKothCreate((Player)sender, arguments[1], (String[])delay.toArray(new String[0]));
            } else {
                sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        }

        return true;
    }
}

package me.theoldestwilly.fullpvp.event.global.koths.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KothClaimArgument extends CommandArgument {
    private final FullPvP plugin;

    public KothClaimArgument(FullPvP plugin) {
        super("claim", "Reclaims a koth.", new String[]{""});
        this.plugin = plugin;
        this.permission = "fullpvp.command.koth.argument.claim";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kothName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else if (sender instanceof Player) {
            this.plugin.getGlobalEventsManager().getKothManager().onReclaimKoth((Player)sender, arguments[1]);
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else {
            List<String> kothsName = new ArrayList();
            Iterator var6 = this.plugin.getGlobalEventsManager().getKothManager().getKothsList().keySet().iterator();

            while(var6.hasNext()) {
                String name = (String)var6.next();
                kothsName.add(name);
            }

            return kothsName;
        }
    }
}
package me.theoldestwilly.fullpvp.event.global.koths.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothCapzone;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KothRenameArgument extends CommandArgument {
    private final FullPvP plugin;

    public KothRenameArgument(FullPvP plugin) {
        super("rename", "Changes koth name.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.koth.argument.rename";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <koth> <duration(d/h/m/s)>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else {
            String newName = arguments[2];
            KothEvent kothEvent = this.plugin.getGlobalEventsManager().getKothManager().getByName(arguments[1]);
            if (kothEvent != null) {
                kothEvent.setName(newName);
                KothCapzone capzone = kothEvent.getKothCapzone();
                capzone.setName(newName);
                kothEvent.setKothCapzone(capzone);
                this.plugin.getGlobalEventsManager().getKothManager().getKothsList().remove(arguments[1]);
                this.plugin.getGlobalEventsManager().getKothManager().getKothsList().put(kothEvent.getName(), kothEvent);
                sender.sendMessage(ChatColor.AQUA + "You have modified this koth. Old name: " + ChatColor.RED + arguments[1] + ChatColor.AQUA + ". New name: " + ChatColor.YELLOW + kothEvent.getName() + ChatColor.AQUA + ".");
            } else {
                sender.sendMessage(ChatColor.RED + "Koth with name '" + ChatColor.GRAY + arguments[1] + ChatColor.RED + "' not found.");
            }
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

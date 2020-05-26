package me.theoldestwilly.fullpvp.event.global.koths.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.ConvertFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KothDurationArgument extends CommandArgument {
    private final FullPvP plugin;

    public KothDurationArgument(FullPvP plugin) {
        super("duration", "Change koth duration.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.koth.argument.duration";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <koth> <duration(d/h/m/s)>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
        } else {
            String dur = StringUtils.join(arguments, ' ', 2, arguments.length);
            KothEvent kothEvent = this.plugin.getGlobalEventsManager().getKothManager().getByName(arguments[1]);
            if (kothEvent != null) {
                kothEvent.setDuration(ConvertFormat.convertDelay2Long2(dur, sender));
                sender.sendMessage(ChatColor.BLUE + "You have modified the duration of " + ChatColor.YELLOW + kothEvent.getName() + ChatColor.BLUE + " koth to " + ChatColor.YELLOW + DurationFormatUtils.formatDurationWords(kothEvent.getDuration(), true, true) + ChatColor.BLUE + " succesfully.");
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
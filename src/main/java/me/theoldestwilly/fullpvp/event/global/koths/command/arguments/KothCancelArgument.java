package me.theoldestwilly.fullpvp.event.global.koths.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KothCancelArgument extends CommandArgument {
    private final FullPvP plugin;

    public KothCancelArgument(FullPvP plugin) {
        super("cancel", "Cancels a koth.");
        this.plugin = plugin;
        this.permission = "fullpvp.command.koth.argument.cancel";
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kothName>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        this.plugin.getGlobalEventsManager().getKothManager().onKothCancel(sender);
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

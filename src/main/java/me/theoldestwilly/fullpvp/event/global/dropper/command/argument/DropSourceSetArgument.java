package me.theoldestwilly.fullpvp.event.global.dropper.command.argument;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.global.dropper.DropsSource;
import me.theoldestwilly.fullpvp.event.global.dropper.DropsSourceEvent;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DropSourceSetArgument extends CommandArgument {
    private FullPvP plugin;
    public DropSourceSetArgument(FullPvP plugin) {
        super("set", "Sets a drops source",  new String[]{});
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String var1) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Integer uses = 1;
            if (arguments.length == 2)
                uses = JavaUtils.tryParseInt(arguments[1]);
            plugin.getGlobalEventsManager().addSource(new DropsSource(((Player) sender).getLocation(), uses, true));
            sender.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + "Drops source saved successfully!");
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}

package me.theoldestwilly.fullpvp.event.global.dropper.command.argument;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.global.dropper.DropsSourceEvent;
import me.theoldestwilly.fullpvp.temporal.TemporalDroppedItem;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DropSourceSetItemsArgument  extends CommandArgument {
    private FullPvP plugin;
    public DropSourceSetItemsArgument(FullPvP plugin) {
        super("setitems", "Sets the items which are going to be dropped.",  new String[]{});
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String var1) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            List<TemporalDroppedItem> list = new ArrayList<>();
            Checkers.removeNullItems(((Player) sender).getInventory().getContents()).stream().forEach(drop -> list.add(new TemporalDroppedItem(9999, drop)));
            plugin.getGlobalEventsManager().getConfig().set(DropsSourceEvent.itemsAlias, list.toArray(new TemporalDroppedItem[0]));
            sender.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + "Items saved successfully!");
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}
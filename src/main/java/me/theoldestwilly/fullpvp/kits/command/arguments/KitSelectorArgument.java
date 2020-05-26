package me.theoldestwilly.fullpvp.kits.command.arguments;

import java.util.Collections;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.menu.KitSelectorMenu;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitSelectorArgument extends CommandArgument {
    private final FullPvP plugin;

    public KitSelectorArgument(FullPvP plugin) {
        super("selector", "Opens kits selector GUI.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        } else {
            Player player = (Player)sender;
            if (this.plugin.getKitManager().getKits().isEmpty()) {
                player.sendMessage(ChatColor.RED + "No kits have been defined.");
            } else {
                (new KitSelectorMenu(player)).open(player);
            }

            return true;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

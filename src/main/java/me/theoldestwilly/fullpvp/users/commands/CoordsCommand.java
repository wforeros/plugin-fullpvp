package me.theoldestwilly.fullpvp.users.commands;

import java.util.Iterator;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public CoordsCommand(FullPvP plugin) {
        super("coords");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            sender.sendMessage(TextUtils.formatColor("&7&m&l-----------------------------"));
            sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Koths" + ChatColor.GRAY + " ( X |" + ChatColor.GRAY + " Z )");
            Iterator var6 = this.plugin.getGlobalEventsManager().getKothManager().getKothsList().values().iterator();

            while(var6.hasNext()) {
                KothEvent kothEvent = (KothEvent)var6.next();
                if (kothEvent.getKothCapzone() != null) {
                    Cuboid cuboid = kothEvent.getKothCapzone().getProtectedArea().getArea();
                    String status = kothEvent.isActive() ? ChatColor.AQUA + "(Active)" : "";
                    sender.sendMessage(TextUtils.formatColor(ChatColor.GRAY + "" + ChatColor.BOLD + " *" + ChatColor.YELLOW + "" + kothEvent.getName() + ": " + ChatColor.GRAY + cuboid.getCenter().getX() + ChatColor.BOLD + " ｜ " + ChatColor.GRAY + cuboid.getCenter().getZ() + " " + status));
                }
            }

            /*DestroyTheCore dtc = this.plugin.getDtcHandler().getDestroyTheCoreEvent();
            if (dtc != null) {
                sender.sendMessage(ChatColor.BOLD + Values.DTC_DISPLAY_NAME + ChatColor.GRAY + " ( X |" + ChatColor.GRAY + " Z )");
                String status = dtc.isActive() ? ChatColor.AQUA + "(Active)" : "";
                Location location = dtc.getCore().getLocation();
                sender.sendMessage(TextUtils.formatColor(ChatColor.GRAY + "" + ChatColor.BOLD + " *" + ChatColor.YELLOW + "" + location.getBlockX() + ChatColor.GRAY + " ｜ " + ChatColor.YELLOW + location.getBlockZ() + " " + status));
            }*/

            sender.sendMessage(TextUtils.formatColor("&7&m&l-----------------------------"));
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
        }

        return true;
    }
}

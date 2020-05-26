package me.theoldestwilly.fullpvp.event.global.koths.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothCapzone;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.ConvertFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KothInfoArgument extends CommandArgument {
    private final FullPvP plugin;

    public KothInfoArgument(FullPvP plugin) {
        super("info", "Information about koth.", new String[]{""});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (this.plugin.getGlobalEventsManager().getKothManager().isKothActive()) {
            label26: {
                KothEvent kothEvent = this.plugin.getGlobalEventsManager().getKothManager().getActiveKoth();
                KothCapzone kothCapzone = kothEvent.getKothCapzone();
                Cuboid cuboid = kothCapzone.getProtectedArea().getArea();
                sender.sendMessage(TextUtils.formatColor("&8&m&l-------------------------------------------------"));
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Koth Information:");
                sender.sendMessage(TextUtils.formatColor(" &8&l* &bName: &7" + kothEvent.getName()));
                sender.sendMessage(TextUtils.formatColor(" &8&l* &bDuration: &7" + ConvertFormat.setFormat(kothEvent.getRemaining()) + " &7&l｜ &7" + ConvertFormat.setFormat(kothEvent.getDuration())));
                sender.sendMessage(TextUtils.formatColor(" &8&l* &bCoords: &7X: " + cuboid.getCenter().getX() + " &7| &7Z: " + cuboid.getCenter().getZ()));
                if (!(sender instanceof CommandSender)) {
                    if (!(sender instanceof Player)) {
                        break label26;
                    }

                    if (!PermissionsManager.hasStaffPermission((Player)sender)) {
                        break label26;
                    }
                }

                sender.sendMessage(TextUtils.formatColor(" &8&l* " + (kothEvent.getKothCapzone().getCaper() != null ? "&bCapturing: &7" + Bukkit.getServer().getPlayer(kothCapzone.getCaper()).getName() : "&cNobody is capturing the koth")));
            }

            sender.sendMessage(TextUtils.formatColor("&8&m&l-------------------------------------------------"));
        } else {
            sender.sendMessage(ChatColor.RED + "There is not an active koth.");
        }

        return true;
    }
}

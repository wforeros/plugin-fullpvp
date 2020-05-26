package me.theoldestwilly.fullpvp.modes;

import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModesManager {
    private FullPvP plugin;
    @Getter @Setter private ServerMode currentMode;

    public ModesManager(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void startMode(CommandSender sender, Long duration) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "This action only can be executed in the console.");
            return;
        }
        if (currentMode != null) {
            sender.sendMessage(ChatColor.RED + "There is an active global mode active.");
            return;
        }
        currentMode = new GlobalGracePeriod(plugin, duration);
        Bukkit.broadcastMessage(Lang.PREFIX + ChatColor.YELLOW + "Server mode updated to Global Grace Period. " + ChatColor.GREEN + "PvP disabled" + ChatColor.YELLOW + ".");
    }

    public void cancelCurrentMode(CommandSender sender) {
        if ((sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This action only can be executed in the console.");
            return;
        }
        if (currentMode == null) {
            sender.sendMessage(ChatColor.RED + "There is not an active global mode active.");
            return;
        }
        currentMode.finish();
    }

}

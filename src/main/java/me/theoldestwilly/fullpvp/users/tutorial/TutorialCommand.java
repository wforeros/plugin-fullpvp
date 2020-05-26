package me.theoldestwilly.fullpvp.users.tutorial;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TutorialCommand extends ArgumentExecutor {
    private FullPvP plugin;
    public TutorialCommand(FullPvP plugin) {
        super("tutorial");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getTutorialHandler().openTutorialInventory(player);
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }
}

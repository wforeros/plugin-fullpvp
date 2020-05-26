package me.theoldestwilly.fullpvp.economy.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.chests.kitschest.command.arguments.KChestCreateArgument;
import me.theoldestwilly.fullpvp.chests.kitschest.command.arguments.KChestReloadArgument;
import me.theoldestwilly.fullpvp.chests.kitschest.command.arguments.KChestRemoveArgument;
import me.theoldestwilly.fullpvp.economy.command.arguments.EcoGiveArgument;
import me.theoldestwilly.fullpvp.economy.command.arguments.EcoResetArgument;
import me.theoldestwilly.fullpvp.economy.command.arguments.EcoSetArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public EconomyCommand(FullPvP plugin) {
        super("economy", new String[]{"eco"});
        this.plugin = plugin;
        addArgument(new EcoResetArgument(plugin));
        addArgument(new EcoSetArgument(plugin));
        addArgument(new EcoGiveArgument(plugin));
    }

    /*@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length == 0) {
            if (sender instanceof Player) {
                plugin.getEconomyManager().checkOwnBalance((Player) sender);
            }
        }
        return true;
    }*/
}

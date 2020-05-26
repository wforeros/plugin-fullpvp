package me.theoldestwilly.fullpvp.crate.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.crate.command.arguments.*;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public CrateCommand(FullPvP plugin) {
        super("crate");
        this.plugin = plugin;
        this.addArgument(new CrateCreateArgument(plugin));
        this.addArgument(new CrateDeleteArgument(plugin));
        this.addArgument(new CrateGiveArgument(plugin));
        this.addArgument(new CrateCreateArgument(plugin));
        this.addArgument(new CrateColorArgument(plugin));
        this.addArgument(new CrateItemsArgument(plugin));
        this.addArgument(new CrateGuiArgument(plugin));
        this.addArgument(new CrateRewardsArgument(plugin));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                ((Player) sender).openInventory(plugin.getCrateManager().openCratesGui((Player) sender));
            } else {
                super.onCommand(sender, command, label, args);
            }
        } else {
            super.onCommand(sender, command, label, args);
        }

        return true;
    }
}
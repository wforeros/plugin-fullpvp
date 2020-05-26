package me.theoldestwilly.fullpvp.kits.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.command.arguments.KitPreviewArgument;
import me.theoldestwilly.fullpvp.kits.command.arguments.KitSelectorArgument;
import me.theoldestwilly.fullpvp.kits.command.arguments.staff.*;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends ArgumentExecutor {
    private final FullPvP plugin;
    private final KitSelectorArgument selectorArgument;

    public KitCommand(FullPvP plugin) {
        super("kits", new String[]{"kit"});
        this.plugin = plugin;
        this.addArgument(new KitPreviewArgument(plugin));
        this.addArgument(this.selectorArgument = new KitSelectorArgument(plugin));
        this.addArgument(new KitApplyArgument(plugin));
        this.addArgument(new KitCreateArgument(plugin));
        this.addArgument(new KitDeleteArgument(plugin));
        this.addArgument(new KitToggleUsageArgument(plugin));
        this.addArgument(new KitRenameArgument(plugin));
        this.addArgument(new KitSetCooldownArgument(plugin));
        this.addArgument(new KitSetDescriptionArgument(plugin));
        this.addArgument(new KitSetIconArgument(plugin));
        this.addArgument(new KitSetIndexArgument(plugin));
        this.addArgument(new KitItemsArgument(plugin));
        this.addArgument(new KitSetMaximumUsesArgument(plugin));
        this.addArgument(new KitToggleWipeArgument(plugin));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                this.selectorArgument.onCommand(sender, command, label, args);
            } else {
                super.onCommand(sender, command, label, args);
            }
        } else {
            super.onCommand(sender, command, label, args);
        }

        return true;
    }
}

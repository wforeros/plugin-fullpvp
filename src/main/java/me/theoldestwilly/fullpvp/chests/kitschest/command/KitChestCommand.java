package me.theoldestwilly.fullpvp.chests.kitschest.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.chests.kitschest.command.arguments.KChestCreateArgument;
import me.theoldestwilly.fullpvp.chests.kitschest.command.arguments.KChestReloadArgument;
import me.theoldestwilly.fullpvp.chests.kitschest.command.arguments.KChestRemoveArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class KitChestCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public KitChestCommand(FullPvP plugin) {
        super("kitchest", new String[]{"kchest"});
        this.plugin = plugin;
        addArgument(new KChestCreateArgument(plugin));
        addArgument(new KChestRemoveArgument(plugin));
        addArgument(new KChestReloadArgument(plugin));
    }
}

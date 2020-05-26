package me.theoldestwilly.fullpvp.chests.mapchest.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class MapChestCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public MapChestCommand(FullPvP plugin) {
        super("mapchest", new String[]{"mchest"});
        this.plugin = plugin;
        addArgument(new me.theoldestwilly.fullpvp.chests.mapchest.command.arguments.MChestCreateArgument(plugin));
        addArgument(new me.theoldestwilly.fullpvp.chests.mapchest.command.arguments.MChestRemoveArgument(plugin));
        addArgument(new me.theoldestwilly.fullpvp.chests.mapchest.command.arguments.MChestReloadArgument(plugin));
    }
}

package me.theoldestwilly.fullpvp.event.global.koths.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.koths.command.arguments.*;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class KothCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public KothCommand(FullPvP plugin) {
        super("koth");
        this.plugin = plugin;
        addArgument(new KothClaimArgument(plugin));
        addArgument(new KothDurationArgument(plugin));
        addArgument(new KothInfoArgument(plugin));
        addArgument(new KothRenameArgument(plugin));
        //addArgument(new KothRewardsArgument(plugin));
        addArgument(new KothSetCapzoneArgument(plugin));
        addArgument(new KothStartArgument(plugin));
        addArgument(new KothDeleteArgument(plugin));
        addArgument(new KothCancelArgument(plugin));
        addArgument(new KothCreateArgument(plugin));
    }
}

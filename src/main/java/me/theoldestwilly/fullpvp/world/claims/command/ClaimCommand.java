package me.theoldestwilly.fullpvp.world.claims.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.world.claims.command.argument.*;

public class ClaimCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public ClaimCommand(FullPvP plugin) {
        super("claim");
        this.plugin = plugin;
        this.addArgument(new ClaimCreateArgument(plugin));
        this.addArgument(new ClaimRenameArgument(plugin));
        this.addArgument(new ClaimLandArgument(plugin));
        this.addArgument(new ClaimDeleteArgument(plugin));
        this.addArgument(new ClaimInfoArgument(plugin));
        this.addArgument(new ClaimToggleFlyArgument(plugin));
        this.addArgument(new ClaimTogglePvPArgument(plugin));
        this.addArgument(new ClaimToggleBroadcastArgument(plugin));
        this.addArgument(new ClaimTogglePearlArgument(plugin));
    }
}

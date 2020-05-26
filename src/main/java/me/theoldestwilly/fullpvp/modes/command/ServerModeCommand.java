package me.theoldestwilly.fullpvp.modes.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.modes.command.arguments.ServerModeCancelArgument;
import me.theoldestwilly.fullpvp.modes.command.arguments.ServerModeStartArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class ServerModeCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public ServerModeCommand(FullPvP plugin) {
        super("servermode", new String[]{"smode"});
        this.plugin = plugin;
        addArgument(new ServerModeCancelArgument(plugin));
        addArgument(new ServerModeStartArgument(plugin));
    }
}
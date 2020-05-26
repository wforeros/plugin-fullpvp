package me.theoldestwilly.fullpvp.event.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.command.arguments.GlobalEventCancelArgument;
import me.theoldestwilly.fullpvp.event.command.arguments.GlobalEventSetRewardArgument;
import me.theoldestwilly.fullpvp.event.command.arguments.GlobalEventStartArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class GlobalEventCommand extends ArgumentExecutor {
    public GlobalEventCommand(FullPvP plugin) {
        super("globalevent", new String[]{"gevent"});
        addArgument(new GlobalEventCancelArgument(plugin));
        addArgument(new GlobalEventStartArgument(plugin));
        addArgument(new GlobalEventSetRewardArgument(plugin));
    }

}

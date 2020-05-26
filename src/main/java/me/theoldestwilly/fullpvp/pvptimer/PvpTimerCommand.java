package me.theoldestwilly.fullpvp.pvptimer;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.pvptimer.arguments.PvpTimerCheckArgument;
import me.theoldestwilly.fullpvp.pvptimer.arguments.PvpTimerSetArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class PvpTimerCommand extends ArgumentExecutor {
    public PvpTimerCommand(FullPvP plugin) {
        super("pvptimer");
        addArgument(new PvpTimerSetArgument(plugin));
        addArgument(new PvpTimerCheckArgument(plugin));
    }
}

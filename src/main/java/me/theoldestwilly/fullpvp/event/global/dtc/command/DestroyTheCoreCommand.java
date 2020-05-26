package me.theoldestwilly.fullpvp.event.global.dtc.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.dtc.command.argument.DtcCreateArgument;
import me.theoldestwilly.fullpvp.event.global.dtc.command.argument.DtcStartArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class DestroyTheCoreCommand extends ArgumentExecutor {
    public DestroyTheCoreCommand(FullPvP plugin) {
        super("dtc", new String[]{"destroythecore"});
        addArgument(new DtcCreateArgument(plugin));
        addArgument(new DtcStartArgument(plugin));
    }
}

package me.theoldestwilly.fullpvp.event.global.dropper.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.dropper.command.argument.DropSourceSetArgument;
import me.theoldestwilly.fullpvp.event.global.dropper.command.argument.DropSourceSetItemsArgument;
import me.theoldestwilly.fullpvp.event.global.dropper.command.argument.DropSourceStartArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class DropSourceCommand extends ArgumentExecutor {
    public DropSourceCommand(FullPvP plugin) {
        super("dropsource", new String[]{"dsource", "drops"});
        addArgument(new DropSourceSetArgument(plugin));
        addArgument(new DropSourceStartArgument(plugin));
        addArgument(new DropSourceSetItemsArgument(plugin));
    }
}

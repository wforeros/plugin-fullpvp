package me.theoldestwilly.fullpvp.event.global.dropper.command.argument;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.GlobalEventType;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DropSourceStartArgument extends CommandArgument {
    private FullPvP plugin;
    public DropSourceStartArgument(FullPvP plugin) {
        super("start", "Used to start the dropper event.", new String[]{});
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String var1) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        plugin.getGlobalEventsManager().onStartNewEvent(var1, null, GlobalEventType.DROPPER, null);
        return true;
    }
}

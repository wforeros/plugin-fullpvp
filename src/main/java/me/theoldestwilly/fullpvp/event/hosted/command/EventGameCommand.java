package me.theoldestwilly.fullpvp.event.hosted.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.command.arguments.*;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class EventGameCommand extends ArgumentExecutor {
    public EventGameCommand(FullPvP plugin) {
        super("eventgame", new String[]{"evg", "egame", "hostedevent", "hevent"});
        addArgument(new EventGameSetLobbyArgument(plugin));
        addArgument(new EventGameSetSpawnArgument(plugin));
        addArgument(new EventGameStartArgument(plugin));
        addArgument(new EventGameSetRewardArgument(plugin));
        addArgument(new EventGameSetKitArgument(plugin));
        addArgument(new EventGameCancelArgument(plugin));
        addArgument(new EventGameJoinArgument(plugin));
        addArgument(new EventGameKickArgument(plugin));
        addArgument(new EventGameLeaveArgument(plugin));
        addArgument(new EventGameCreateArena(plugin));
        addArgument(new EventGameSetSpectatorsSpawnArgument(plugin));
    }
}

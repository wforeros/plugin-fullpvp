package me.theoldestwilly.fullpvp.profileviewer;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.profileviewer.arguments.ProfileVillagerSpawnArgument;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;

public class ProfileVillagerCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public ProfileVillagerCommand(FullPvP plugin) {
        super("profilevillager", new String[]{"pvillager", "profilev"});
        this.plugin = plugin;
        this.addArgument(new ProfileVillagerSpawnArgument(plugin));
    }
}

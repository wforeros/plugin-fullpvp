package me.theoldestwilly.fullpvp.users;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ProfileHandler implements Listener {
    private FullPvP plugin;
    private @Getter Map<UUID, Profile> onlinePlayers = Collections.synchronizedMap(new HashMap<>());
    private Map<UUID, Profile> cachedOfflineProfiles = Collections.synchronizedMap(new HashMap<>());
    private @Getter MongoCollection profilesDatabase;

    public ProfileHandler(FullPvP plugin) {
        this.plugin = plugin;
        profilesDatabase = plugin.getPluginDatabase().getDatabase().getCollection("profiles");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + "Profile manager started succesfully.");
    }

    public Profile getProfileByUniqueID(UUID uniqueID) {
        return onlinePlayers.get(uniqueID);
    }

    public Profile getProfile (String name) {
        for (Profile profile : onlinePlayers.values()) {
            if (profile.getName().equalsIgnoreCase(name)) return profile;
        }
        return null;
    }
    public Profile getOfflineProfile (OfflinePlayer target, boolean saveAsCachedProfile) {
        return getOfflineProfile(target.getUniqueId(), saveAsCachedProfile);
    }

    public Profile getOfflineProfile (UUID uniqueId, boolean saveAsCachedProfile) {
        Profile profile = getProfileByUniqueID(uniqueId);
        if (profile == null) {
            Document document = (Document) profilesDatabase.find(Filters.eq(uniqueId)).first();
            if (document != null) {
                profile = new Profile(document);
                if (saveAsCachedProfile)
                    cachedOfflineProfiles.put(uniqueId, profile);
            }
        }
        return profile;
    }


    public void loadOnlineProfiles() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            final int[] profilesLoaded = {0};
            Bukkit.getOnlinePlayers().forEach(player -> {
                loadProfile(player.getUniqueId(), player.getName());
                profilesLoaded[0]++;
            });
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + "Online profiles loaded: " + profilesLoaded[0] + ".");
        }
    }


    /**
     *
     * @param uniqueId
     * @param name
     * @return True if profile players already exists or false if is a new player
     */
    public boolean loadProfile(UUID uniqueId, String name) {
        boolean ans = true;
        if (!onlinePlayers.containsKey(uniqueId)) {
            Document document = (Document) getProfilesDatabase().find(Filters.eq(uniqueId)).first();
            Profile profile;
            if (document == null) {
                profile = new Profile(uniqueId);
                ans = false;
            } else {
                profile = new Profile(document);
            }
            if (profile.getName() == null || !profile.getName().equals(name)) {
                profile.setName(name);
                profile.save();
            }
            this.onlinePlayers.put(uniqueId, profile);
        }
        return ans;
    }

    public void saveOnlineProfiles() {
        this.onlinePlayers.values().forEach(Profile::save);
        this.plugin.getLogger().info("Saved a total of " + this.onlinePlayers.size() + " profile(s) to the Mongo Database.");
        this.onlinePlayers.clear();
    }


    public void saveProfileAsync(Player player, UUID uniqueID, String name,boolean async) {
        Profile profile = this.onlinePlayers.remove(uniqueID);
        if (profile != null) {
            if (async) profile.asyncSave();
            else profile.save();
        } else {
            if (player != null) player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_SAVING_PROFILE));
            Bukkit.getLogger().log(Level.WARNING, "[FullPvP Error] Failed saving " + name + " into database, profile was null!");
        }
    }

    public void saveProfileAsync(Profile profile) {
        if (profile.isOnline())
            onlinePlayers.put(profile.getUniqueId(), profile);
        else {
            profile.asyncSave();
            cachedOfflineProfiles.remove(profile.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoinEvent (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if(!loadProfile(player.getUniqueId(), player.getName())) {
                    player.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + ChatColor.YELLOW + "Bienvenido a FullPvP! Esta modalidad al igual que muchas otras usa un Core propio");
                    plugin.getTutorialHandler().sendRecommendation(Bukkit.getPlayer(player.getUniqueId()));
                    plugin.getSpawnHandler().directSpawnTeleport(player, "main");

                }
            }
        });
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        saveProfileAsync(player, player.getUniqueId(), player.getName(), true);
    }
}

package me.theoldestwilly.fullpvp.world.serverspawn;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.Config;
import net.silexpvp.nightmare.Nightmare;
import net.silexpvp.nightmare.timer.type.TeleportTimer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SpawnHandler implements Listener {

    private @Getter List<ServerSpawn> globalSpawns = new ArrayList<>();
    private @Getter FullPvP plugin;

    public SpawnHandler (FullPvP plugin) {
        this.plugin = plugin;
        loadSpawns();
    }

    public void loadSpawns() {
        Object o = plugin.getClaimHandler().getConfig().get("spawn");
        if (o != null && o instanceof List)
        for (Object object : (List) o)
            if (object instanceof ServerSpawn) globalSpawns.add((ServerSpawn) object);
    }

    public void saveSpawns() {
        Config config = plugin.getClaimHandler().getConfig();
        config.set("spawn", globalSpawns.toArray(new ServerSpawn[0]));
        config.save();
    }

    public void addGlobalSpawn(Player player, String name, Location location){
        ServerSpawn spawn = getSpawn(name);
        if (spawn != null) {
            player.sendMessage(ChatColor.RED + "There is an existing spawn with name " + spawn.getName() + ".");
            return;
        }
        globalSpawns.add(new ServerSpawn(UUID.randomUUID(), name, location));
    }

    public void removeSpawn(Player player, String name) {
        ServerSpawn spawn = getSpawn(name);
        if (spawn == null) {
            player.sendMessage(ChatColor.RED + "Spawn with name " + name + " not found.");
            return;
        }
        globalSpawns.remove(spawn);
        player.sendMessage(ChatColor.YELLOW + "Spawn with name " + name + " removed.");
    }

    public ServerSpawn getSpawn(String name) {
        return globalSpawns.stream().filter(spawn -> spawn.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void teleportToSpawn(Player player, String name) {
        ServerSpawn spawn = getSpawn(name);
        if (spawn == null) {
            spawn = getSpawn("main");
            player.sendMessage(ChatColor.BLUE + "Spawn not found, you will be teleported to the main spawn.");
        }
        TeleportTimer timer = plugin.getNightmare().getTimerManager().getPlayerTimer(TeleportTimer.class);
        TeleportTimer.Request request = new TeleportTimer.Request(spawn.getLocation(), (requester) -> requester.sendMessage(ChatColor.YELLOW + "You were teleported to " + ChatColor.BLUE + "Spawn" + ChatColor.YELLOW + "."));
        timer.teleport(player, request, TimeUnit.SECONDS.toMillis(5L), ChatColor.YELLOW + "Teleporting to the " + ChatColor.BLUE + "Spawn" + ChatColor.YELLOW + " in " + ChatColor.GRAY + "5 seconds" + ChatColor.YELLOW + ". Don't move take any damage.");
    }

    public void directSpawnTeleport(Player player, String name) {
        ServerSpawn spawn = getSpawn(name);
        if (spawn == null) {
            spawn = getSpawn("main");
            player.sendMessage(ChatColor.BLUE + "Spawn not found, you will be teleported to the main spawn.");
        }
        player.teleport(spawn.getLocation());
    }

    public List<String> getSpawnNames() {
        List<String> list = new ArrayList<>();
        globalSpawns.forEach(spawn -> list.add(spawn.getName()));
        return list;
    }
}

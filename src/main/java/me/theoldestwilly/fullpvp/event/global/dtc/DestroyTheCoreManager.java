package me.theoldestwilly.fullpvp.event.global.dtc;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DestroyTheCoreManager {
    private FullPvP plugin;
    private List<DestroyTheCore> cores = new ArrayList<>();
    private String alias = "cores";

    public DestroyTheCoreManager(FullPvP plugin, Config config) {
        this.plugin = plugin;
        loadCores(config);
    }

    public void loadCores(Config config) {
        Object object = config.get(alias);
        if (object instanceof List) {
            GenericUtils.createList(object, DestroyTheCore.class).forEach(core -> {
                if (core.getCore().getType() == Material.OBSIDIAN) cores.add(core);
            });
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + cores.size() + " DTC events loaded.");
        }
    }

    public void saveCores(Config config) {
        config.set(alias, cores);
    }

    public void startDtcEvent(CommandSender sender, String name) {
        DestroyTheCore destroyTheCore = getDtcEvent(name);
        if (destroyTheCore == null) {
            sender.sendMessage(ChatColor.RED + "Error while finding the DTC event called " + name);
            return;
        }
        plugin.getGlobalEventsManager().setGlobalEvent(destroyTheCore);
        destroyTheCore.startEvent();
    }

    public DestroyTheCore getDtcEvent (String name) {
        return cores.stream().filter(dtc -> dtc.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public DestroyTheCore getDtcEvent (Location loc) {
        return cores.stream().filter(dtc -> dtc.getCore().getLocation().equals(loc)).findFirst().orElse(null);
    }

    public void createNewEvent(Player player, String name) {
        DestroyTheCore dtc = getDtcEvent(name);
        if (dtc != null) {
            player.sendMessage(Lang.ERROR_GLOBALEVENT_ALREADY_EXISTS);
            return;
        }
        Block block = player.getTargetBlock((HashSet<Byte>) null, 25);
        if (block.getType() != Material.OBSIDIAN) {
            player.sendMessage(Lang.ERROR_COLOR + "You are not looking at a obsidian block.");
            return;
        }
        dtc = getDtcEvent(block.getLocation());
        if (dtc != null) {
            player.sendMessage(ChatColor.RED + "There is an event in this place, its name is " + ChatColor.YELLOW + dtc.getName());
            return;
        }
        if (!JavaUtils.isAlphanumeric(name)) {
            player.sendMessage(Lang.ERROR_GLOBAL_INVALID_CHARACTERS);
            return;
        }
        if (name.length() < 3 ) {
            player.sendMessage(Lang.ERROR_CLAN_INSUFFICENT_CHARACTERS);
            return;
        }
        if (name.length() > 10) {
            player.sendMessage(Lang.ERROR_CLAN_EXCEEDED_CHARACTERS_LIMIT);
            return;
        }
        dtc = new DestroyTheCore(name, block);
        cores.add(dtc);
        player.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + "DTC event created succesfully with name " + name);
    }

    public void removeEvent(CommandSender sender, String name) {
        DestroyTheCore dtc = getDtcEvent(name);
        if (dtc == null) {
            sender.sendMessage(Lang.ERROR_GLOBALEVENT_NOT_EXISTS);
            return;
        }
        cores.remove(dtc);
    }

    public List<String> getEventNames() {
        List<String> names = new ArrayList<>();
        cores.forEach(core -> names.add(core.getLocationName()));
        return names;
    }


}

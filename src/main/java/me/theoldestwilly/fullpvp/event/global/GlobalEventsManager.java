package me.theoldestwilly.fullpvp.event.global;


import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.GlobalEventType;
import me.theoldestwilly.fullpvp.event.global.dropper.DropsSource;
import me.theoldestwilly.fullpvp.event.global.dropper.DropsSourceEvent;
import me.theoldestwilly.fullpvp.event.global.dtc.DestroyTheCore;
import me.theoldestwilly.fullpvp.event.global.dtc.DestroyTheCoreManager;
import me.theoldestwilly.fullpvp.event.global.koths.KothManager;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothCapzone;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.event.scheduler.ScheduledEventFinishEvent;
import me.theoldestwilly.fullpvp.event.scheduler.ScheduledEvent;
import me.theoldestwilly.fullpvp.temporal.TemporalDroppedItem;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GlobalEventsManager {
    private FullPvP plugin;
    private @Getter KothManager kothManager;
    private @Getter
    DestroyTheCoreManager destroyTheCoreManager;
    private @Getter @Setter
    GlobalEvent globalEvent;
    private @Getter Config config;
    private @Getter List<DropsSource> sources = new ArrayList<>();
    private List<ItemStack> reward = new ArrayList();
    private String alias = "globalevents";

    public GlobalEventsManager(FullPvP plugin) {
        this.plugin = plugin;
        registerSerializedClasses();
        //The config file is called in the specific event's manager like "KothManager"
        this.config = new Config(plugin, alias);
        Object object = config.get("rewards");
        if (object != null && object instanceof List)
            this.reward.addAll(GenericUtils.createList(object, ItemStack.class));
        kothManager = new KothManager(plugin, config);
        destroyTheCoreManager = new DestroyTheCoreManager(plugin, config);
        sources.addAll(getSourcesFromYml());
    }

    public void registerSerializedClasses() {
        ConfigurationSerialization.registerClass(TemporalDroppedItem.class);
        ConfigurationSerialization.registerClass(ProtectedArea.class);
        ConfigurationSerialization.registerClass(KothCapzone.class);
        ConfigurationSerialization.registerClass(KothEvent.class);
        ConfigurationSerialization.registerClass(DropsSource.class);
        ConfigurationSerialization.registerClass(DestroyTheCore.class);
    }

    public void saveEvents() {
        config.set("rewards", this.reward.toArray(new ItemStack[0]));
        kothManager.saveKoths(config);
        destroyTheCoreManager.saveCores(config);
        config.set(DropsSourceEvent.sourcesAlias, sources.toArray(new DropsSource[0]));
        config.save();
    }

    public List<DropsSource> getSourcesFromYml() {
        List<DropsSource> list = new ArrayList<>();
        Object object = config.get(DropsSourceEvent.sourcesAlias);
        if (object instanceof List) {
            if (object != null && object instanceof List) {
                GenericUtils.createList(object, DropsSource.class).stream().forEach(source -> {
                    if (source.getLocation().getBlock().getType() == Material.IRON_BLOCK) list.add(source);
                });
                Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + list.size() + " drops sources loaded from globalevents.yml.");
            }
        }
        return list;
    }

    /**
     *
     * @param sender Command executor, if is a scheduled event us console sender
     * @param name Event name (cracks koth, main dtc...)
     * @param type Use the enum EventType
     * @param scheduledEvent If it is not a scheduled event use null
     */
    public void onStartNewEvent(CommandSender sender, String name, GlobalEventType type, ScheduledEvent scheduledEvent) {
        if (this.globalEvent == null) {
            if (name != null && type == GlobalEventType.KOTH) {
                this.getKothManager().onKothStart(sender, name);
                this.globalEvent = this.getKothManager().getActiveKoth();
                if (scheduledEvent != null) globalEvent.setScheduled(true);
            } else if (type == GlobalEventType.DROPPER) {
                globalEvent = new DropsSourceEvent(plugin, sources);
            }else if (type == GlobalEventType.DTC) {
                destroyTheCoreManager.startDtcEvent(sender, name);
            }
            if (globalEvent != null) {
                globalEvent.setRewards(getReward());
            }
        } else if (scheduledEvent == null){
            sender.sendMessage(ChatColor.RED + "Sorry but there is an active event.");
        }

    }

    public void addSource(DropsSource source) {
        sources.add(source);
    }

    public boolean isEventActive() {
        return this.getKothManager().isKothActive() || this.globalEvent != null;
    }

    public boolean isActiveEventAKoth() {
        return this.getKothManager().isKothActive();
    }

    public void onEventFinish() {
        if (globalEvent.wasScheduled())
            Bukkit.getPluginManager().callEvent(new ScheduledEventFinishEvent(globalEvent.getName()));
        this.globalEvent = null;
    }

    public void cancelEvent(CommandSender sender) {
        if (globalEvent.wasScheduled())
            Bukkit.getPluginManager().callEvent(new ScheduledEventFinishEvent(globalEvent.getName()));
        this.globalEvent.cancelEvent(this.plugin);
        if (globalEvent instanceof KothEvent) {
            kothManager.onKothCancel(sender);
        }
        globalEvent = null;
    }

    public boolean isActiveEventDtc() {
        //return this.globalEvent instanceof DestroyTheCore;
        return false;
    }

    public FullPvP getPlugin() {
        return this.plugin;
    }

    public void setPlugin(FullPvP plugin) {
        this.plugin = plugin;
    }

    public GlobalEvent getGlobalEvent() {
        return this.globalEvent;
    }

    public void setGlobalEvent(GlobalEvent globalEvent) {
        this.globalEvent = globalEvent;
    }

    public void setActiveEvent(GlobalEvent event) {
        this.globalEvent = event;
    }

    public List<ItemStack> getReward() {
        return this.reward;
    }

    public void setReward(List<ItemStack> reward) {
        this.reward = reward;
    }
}

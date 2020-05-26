package me.theoldestwilly.fullpvp.event.global.dropper;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.global.GlobalEvent;
import me.theoldestwilly.fullpvp.temporal.TemporalDroppedItem;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DropsSourceEvent extends GlobalEvent implements Listener {
    private @Getter List<DropsSource> sources;
    //The boolean value is to check if item was already dropped, if yes use true
    private @Getter List<TemporalDroppedItem> items = new ArrayList<>();
    private FullPvP plugin;
    private int task, roundDuration = 300, lastX = 0, lastZ = 0;
    public static String sourcesAlias = "drops-sources", itemsAlias = "drops-items";
    private Long lastDropTimeMillis;

    public DropsSourceEvent(FullPvP plugin, List<DropsSource> sources) {
        super(false);
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(sources);
        this.plugin = plugin;
        this.sources = sources;
        items = getItemsFromYml();
        lastDropTimeMillis = System.currentTimeMillis();
        startEvent();
    }

    @Override
    public void startEvent() {
        if (getSourcesAmount() > 0 && getItemsAmount() > 0) {
            setAllSourcesAsAvailable();
            setAllItemsAvailable();
            plugin.getGlobalEventsManager().setActiveEvent(this);
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, dropsTimer(), 20L * roundDuration,  20L * roundDuration);
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + "The event has started! Lets find the drops sources around the map!");
        }
    }

    @Override
    public void finishEvent(Player var2) {
        saveSources();
        plugin.getGlobalEventsManager().onEventFinish();
        Bukkit.getScheduler().cancelTask(task);
        Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + "The event has finished.");
    }

    @Override
    public void cancelEvent(FullPvP plugin) {
        saveSources();
        Bukkit.getScheduler().cancelTask(task);
        Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + "The event has been cancelled.");
    }

    public List<TemporalDroppedItem> getItemsFromYml() {
        List<TemporalDroppedItem> list = new ArrayList<>();
        Object object = plugin.getGlobalEventsManager().getConfig().get(itemsAlias);
        if (object instanceof List) {
            if (object != null && object instanceof List) {
                list.addAll(GenericUtils.createList(object, TemporalDroppedItem.class));
                Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + list.size() + " items sources loaded from global-events.yml.");
            }
        }
        return list;
    }

    public BukkitRunnable dropsTimer() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!hasActiveSources()) {
                    setAllSourcesAsAvailable();
                }
                if (hasUnusedItems()) {
                    String coords = dropRandomItemFromRandomActiveSource();
                    Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + "A new item has spawned at: " + ChatColor.GRAY + coords);
                }
                //Redundant conditional to finish the event
                // when there are not more items to drop
                if (!hasUnusedItems()) {
                    finishEvent(null);
                }
            }
        };
    }

    public void addSource(DropsSource dropsSource) {
        sources.add(dropsSource);
    }

    public void dropItemFromRandomActiveSource (ItemStack item) {
        getRandomActiveSource().spawnItem(item);
    }

    /**
     *
     * @return Drops source's coordinates in format "x: + xValue + y: + yValue"
     */
    public String dropRandomItemFromRandomActiveSource() {
        DropsSource source = getRandomActiveSource();
        TemporalDroppedItem item = getRandomTemporalItem();
        source.spawnItem(item.getItem());
        item.setDropped(true);
        lastDropTimeMillis = System.currentTimeMillis();
        lastX = source.getLocation().getBlockX();
        lastZ = source.getLocation().getBlockZ();
        return "x: " + lastX + " z: " + lastZ;
    }

    public boolean hasActiveSources() {
        return sources.stream().anyMatch(dropsSource -> dropsSource.isAvailable());
    }

    public boolean hasUnusedItems() {
        return items.stream().anyMatch(item -> !item.wasDropped());
    }

    public DropsSource getRandomActiveSource() {
        if (!hasActiveSources()) return null;
        DropsSource dropsSource;
        do {
            dropsSource = sources.get(new Random().nextInt(sources.size()));
            if (dropsSource.isAvailable()) {
                return dropsSource;
            }
        } while (true);
    }

    public TemporalDroppedItem getRandomTemporalItem() {
        if (!hasUnusedItems()) return  null;
        TemporalDroppedItem temporalItem;
        do {
            temporalItem = items.get(new Random().nextInt(items.size()));
            if (!temporalItem.wasDropped()) {
                return temporalItem;
            }
        } while (true);
    }

    public String getRemainingToNextSpawn() {
        return JavaUtils.setFormat(TimeUnit.SECONDS.toMillis(roundDuration) - (System.currentTimeMillis() - lastDropTimeMillis));
    }


    public void saveSources() {
        plugin.getGlobalEventsManager().getConfig().set(sourcesAlias, getSources().toArray(new DropsSource[0]));
    }

    public void saveItems() {
        plugin.getGlobalEventsManager().getConfig().set(itemsAlias, getItems().toArray(new TemporalDroppedItem[0]));
    }

    public int getSourcesAmount() {
        return sources.size();
    }

    public int getItemsAmount() {
        return items.size();
    }

    public void setAllSourcesAsAvailable() {
        sources.forEach(source -> source.resetUses());
    }

    public void setAllItemsAvailable() {
        items.forEach(item -> item.setDropped(false));
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + "Dropper Event";
    }

    @Override
    public List<String> getScoreboardLines() {
        List<String> list = new LinkedList<>();
        list.add(ChatColor.BOLD + getDisplayName());
        list.add(ChatColor.GRAY + " » " + ChatColor.YELLOW + "Next spawn" + ChatColor.GRAY + ": " + ChatColor.RED + getRemainingToNextSpawn());
        if (lastX != 0 && lastZ != 0) list.add(ChatColor.GRAY + " » " + ChatColor.YELLOW + "Last " + ChatColor.RED + lastX + ChatColor.GRAY + " | " + ChatColor.RED + lastZ);
        return list;
    }

}

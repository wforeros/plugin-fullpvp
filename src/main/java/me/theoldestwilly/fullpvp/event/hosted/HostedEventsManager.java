package me.theoldestwilly.fullpvp.event.hosted;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class HostedEventsManager implements Listener {
    private FullPvP plugin;
    private @Getter @Setter HostedEvent currentEvent;
    private @Getter TObjectLongMap<HostedEventType> eventsDelay = new TObjectLongHashMap<>();
    private @Getter Long delay;
    private @Getter Long countdownDuration;
    private @Getter Config config;

    public HostedEventsManager (FullPvP plugin) {
        this.plugin = plugin;
        delay = (plugin.getConfig().getLong("events.delay", 45));
        countdownDuration = plugin.getConfig().getLong("events.countdown", 15);
        config = new Config(plugin, "events");
    }

    public void startEvent(Player hoster, HostedEventType type) {
        if (currentEvent != null) {
            hoster.sendMessage(ChatColor.RED + "There is an active event.");
            return;
        }
        if (hasDelay(type) && !hoster.isOp()) {
            hoster.sendMessage(ChatColor.RED + "This event will be available in" + ChatColor.GRAY + ": " + getPrettyRemainingTime(type));
            return;
        }
        currentEvent = HostedEvent.createEvent(hoster, type);
        if (currentEvent == null) {
            hoster.sendMessage(ChatColor.RED + "Event not found.");
            return;
        }
        if (currentEvent.isPossibleToStart()) {
            addDelayToEvent(type);
            plugin.getServer().getPluginManager().registerEvents(currentEvent, plugin);
        } else currentEvent = null;
    }

    public boolean hasDelay(HostedEventType type) {
        return getRemainingTime(type) > 0;
    }

    public void addDelayToEvent(HostedEventType type) {
        eventsDelay.put(type, System.currentTimeMillis());
    }

    public Long getRemainingTime(HostedEventType type) {
        return TimeUnit.MINUTES.toMillis(delay) - (System.currentTimeMillis() - eventsDelay.get(type));
    }

    public String getPrettyRemainingTime(HostedEventType type) {
        return DurationFormatUtils.formatDurationWords(getRemainingTime(type), true, true);
    }

    public boolean isAnActiveEvent() {
        return currentEvent != null;
    }

    public void openEventsGui(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, ChatColor.GOLD + "Event Selector");
        int slot = 11;
        ItemStack glass = (new ItemBuilder(Material.STAINED_GLASS_PANE)).setDurability(4).setDisplayName("&e&lSelect an event!").build();
        for (HostedEventType type : HostedEventType.values()) {
            if (type.getMaterial() != Material.AIR) {
                inventory.setItem(slot, type.getIcon());
                slot += 2;
            }
        }
        for(int i = 0; i < 10; ++i) {
            inventory.setItem(i, glass);
        }

        for(int i = 17; i < 27; ++i) {
            inventory.setItem(i, glass);
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getInventory().getTitle().equals(ChatColor.GOLD + "Event Selector") && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            String title = event.getCurrentItem().getItemMeta().getDisplayName();
            if (title == null) return;
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            HostedEventType type = HostedEventType.fromString(ChatColor.stripColor(title));
            if (type == null) return;
            if (PermissionsManager.hasEventGameHostPermission(player, type.toLowerString())) {
                startEvent(player, type);
                player.closeInventory();
            } else {
                player.sendMessage(ChatColor.RED + "You are not allowed to host this event. Use " + ChatColor.YELLOW + "/buy" + ChatColor.RED + " if tou want more information.");
            }
        }
    }
}

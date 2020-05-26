package me.theoldestwilly.fullpvp.event.global.dtc;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.global.GlobalEvent;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

@Getter
public class DestroyTheCore extends GlobalEvent implements Listener, ConfigurationSerializable {
    private Integer health, maxHealth;
    private boolean fullHealed = true;
    private String locationName;
    private Block core;
    private Long currentDelayToHealth, timeToHealth;
    private int taskId;

    public DestroyTheCore(String name, Block core) {
        super(false);
        this.core = core;
        this.locationName = name;
    }

    public DestroyTheCore (Map<String, Object> map) {
        this((String) map.get("name"), (Location) map.get("core"));
    }

    public DestroyTheCore (String name, Location location) {
        this(name, location.getBlock());
    }

    @Override
    public void startEvent() {
        this.maxHealth = FullPvP.getInstance().getMaxCoreHealthDtc();
        this.health = maxHealth;
        this.timeToHealth = FullPvP.getInstance().getSecondsBeforeHealthDtc();
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
        this.startTaks(FullPvP.getInstance());
        this.currentDelayToHealth = 0L;
        this.setActive(true);
        Bukkit.broadcastMessage(getPrefix() + Lang.SUCCESS_COLOR + "The event has started!");
    }

    @Override
    public void finishEvent(Player winner) {
        HandlerList.unregisterAll(this);
        FullPvP.getInstance().getGlobalEventsManager().onEventFinish();
        this.setActive(false);
        this.cancelTask();
        this.giveRewards(winner);
        Bukkit.broadcastMessage(getPrefix() + Lang.SUCCESS_COLOR + "Congratulations to " + winner.getDisplayName() + Lang.SUCCESS_COLOR + " for winning the event!");
        winner.sendMessage(ChatColor.DARK_GREEN + "You have broken the core! Save your reward!");
    }

    @Override
    public void cancelEvent(FullPvP plugin ) {
        HandlerList.unregisterAll(this);
        this.setActive(false);
        this.cancelTask();
        Bukkit.broadcastMessage(ChatColor.RED + "The DTC event has been cancelled.");
    }

    public void startTaks(FullPvP plugin) {
        this.taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            if (!isFullHealed() && currentDelayToHealth != 0 && DestroyTheCore.this.health < plugin.getMaxCoreHealthDtc() && timeToHealth - (System.currentTimeMillis() - currentDelayToHealth) <= 0) {
                currentDelayToHealth = System.currentTimeMillis();
                health += 1;
                Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + "The core has healed one point!");
                if (health == maxHealth) fullHealed = true;
            }

        }, 0L, 20L);
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_GREEN + "DTC";
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    public void onDamageReceived(Player player) {
        if (getPlugin().getProfileHandler().getProfileByUniqueID(player.getUniqueId()).hasPvpTimer()) {
            player.sendMessage(ChatColor.RED + "You can not participate in an event while you have an enabled pvp timer.");
            return;
        }
        this.health -= 1;
        this.currentDelayToHealth = System.currentTimeMillis();
        this.fullHealed = false;
        player.sendMessage(ChatColor.GREEN + "You have worn the core successfully. Remaining life: " + ChatColor.GRAY + this.getHealth() + "/" + ChatColor.RED + FullPvP.getInstance().getMaxCoreHealthDtc());
    }

    public String getRemainingTimeToStartHealingToShow() {
        Long l = FullPvP.getInstance().getSecondsBeforeHealthDtc() - (System.currentTimeMillis() - this.currentDelayToHealth);
        return l > 1L ? JavaUtils.setFormat(l) : null;
    }

    @Override
    public List<String> getScoreboardLines() {
        Location location = this.getCore().getLocation();
        List<String> lines = new LinkedList();
        lines.addAll(Arrays.asList(getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RED + getLocationName(), ChatColor.translateAlternateColorCodes('&', " &7» &eLoc&7: &f" + location.getBlockX() + " &7| &f" + location.getBlockZ()),
                ChatColor.translateAlternateColorCodes('&', " &7» &eHealth&7: &f" + this.getHealth() + ChatColor.GRAY + "/" + ChatColor.RED + FullPvP.getInstance().getMaxCoreHealthDtc())));
        String st = this.getRemainingTimeToStartHealingToShow();
        if (st != null) {
            lines.add(ChatColor.translateAlternateColorCodes('&', " &7» &eHealing In&7: &f" + st));
        }
        return lines;
    }

    @Override
    public String getName() {
        return locationName;
    }



    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.getBlock().equals(core)) {
            Player player = event.getPlayer();
            onDamageReceived(player);
            if (getHealth() <= 0) {
                finishEvent(player);
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", locationName);
        map.put("core", core.getLocation());
        return map;
    }
}

package me.theoldestwilly.fullpvp.users.tutorial;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

@Getter
public class TutorialTour {
    private FullPvP plugin;
    private String playerName, displayName;
    UUID uniqueId;
    private Integer locationNumber = 1, taskId, tourStopDuration = 12;

    public TutorialTour(Player player) {
        this.playerName = player.getName();
        this.displayName = player.getDisplayName();
        this.uniqueId = player.getUniqueId();
        player.setWalkSpeed(0.0F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
        plugin = FullPvP.getInstance();
        GameUtils.hidePlayers(player, Bukkit.getOnlinePlayers());
        taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::tpNextLocation, tourStopDuration * 20L, tourStopDuration * 20L);
    }

    public void tpNextLocation() {
        Player player = Bukkit.getPlayer(uniqueId);
        TutorialStep step = plugin.getTutorialHandler().getNextTutorialStep(this);
        if (step == null) {
            finishTour();
            return;
        }
        player.teleport(step.getLocation());
        player.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + TextUtils.formatColor(step.getMessage()));
        locationNumber++;
    }

    public void finishTour() {
        Bukkit.getScheduler().cancelTask(taskId);
        Player player = Bukkit.getPlayer(uniqueId);
        Checkers.removeAllPotionEffects(player);
        plugin.getSpawnHandler().directSpawnTeleport(player, "main");
        Bukkit.getScheduler().runTask(plugin, () -> GameUtils.showPlayers(player, Bukkit.getOnlinePlayers()));
        plugin.getTutorialHandler().removeTutorialPlayer(uniqueId);
        player.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + "Hemos finalizado el tour, Disfruta del servidor!\nSi tienes alguna duda usa los comandos /help o /helpop");
    }

}
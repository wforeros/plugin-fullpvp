package me.theoldestwilly.fullpvp.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class LeaderBoardPlayer {

    private final UUID uniqueId;
    private final Object score;

    public String getName() {
        Player player = Bukkit.getPlayer(uniqueId);
        return player == null ? Bukkit.getOfflinePlayer(uniqueId).getName() : player.getName();
    }
}
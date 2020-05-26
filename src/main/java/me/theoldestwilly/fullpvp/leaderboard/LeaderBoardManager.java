package me.theoldestwilly.fullpvp.leaderboard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.PersistableLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.*;
import java.util.stream.Stream;

public final class LeaderBoardManager implements Listener {

    private static final BlockFace[] BLOCK_FACES = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

    private final FullPvP plugin;

    private final Config config;

    private @Getter LeaderBoardIndex currentIndex = LeaderBoardIndex.KILLS;
    private LeaderBoardIndex[] leaderBoardIndices = new LeaderBoardIndex[]{LeaderBoardIndex.KILLS, LeaderBoardIndex.ECONOMY, LeaderBoardIndex.EVENT_WINS, LeaderBoardIndex.MAX_KILLSTREAK};
    private @Getter int indexPosition = 0;

    private final List<LeaderBoard> leaderBoards = Lists.newArrayList(
            new LeaderBoard(LeaderBoardIndex.KILLS.getDbIndex(), LeaderBoardIndex.KILLS.getDisplayName()),
            new LeaderBoard(LeaderBoardIndex.DEATHS.getDbIndex(), LeaderBoardIndex.DEATHS.getDisplayName()),
            new LeaderBoard(LeaderBoardIndex.KILLSTREAK.getDbIndex(), LeaderBoardIndex.KILLSTREAK.getDisplayName()),
            new LeaderBoard(LeaderBoardIndex.ECONOMY.getDbIndex(), LeaderBoardIndex.ECONOMY.getDisplayName()),
            new LeaderBoard(LeaderBoardIndex.RANDOM.getDbIndex(), LeaderBoardIndex.RANDOM.getDbIndex()),
            new LeaderBoard(LeaderBoardIndex.EVENT_WINS.getDbIndex(), LeaderBoardIndex.EVENT_WINS.getDisplayName())
    );

    public LeaderBoardManager(FullPvP plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(PersistableLocation.class);
        config = new Config(plugin, "leaderboard-signs");
        nextIndex();
        leaderBoards.forEach(leaderBoard -> leaderBoard.loadSignLocations(config));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateLeaderBoards, 0L, 20L * 10 * 2);
    }

    public void nextIndex() {
        currentIndex = leaderBoardIndices[indexPosition];
        indexPosition++;
        if (indexPosition >= leaderBoardIndices.length) indexPosition = 0;
    }

    public ImmutableList<LeaderBoard> getLeaderBoards() {
        return ImmutableList.copyOf(leaderBoards);
    }

    public LeaderBoard getLeaderboard(String name) {
        return leaderBoards.stream().filter(leaderBoard -> leaderBoard.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void saveLeaderBoards() {
        leaderBoards.forEach(leaderBoard -> leaderBoard.saveSignLocations(config));
        config.save();
    }

    public void updateLeaderBoards() {
        leaderBoards.forEach(leaderBoard -> leaderBoard.getSigns().entrySet().forEach(entry -> setSignAndSkull(leaderBoard, entry.getKey().getLocation(), entry.getValue())));
        //plugin.getServer().broadcastMessage(ChatColor.GREEN + "Leaderboards have been updated!");
        nextIndex();
    }

    private Block getNearSkull(Block block) {
        return Stream.of(BLOCK_FACES).map(block::getRelative).filter(relative -> relative.getState() instanceof Skull).findFirst().orElse(null);
    }

    public void setSignAndSkull(LeaderBoard leaderBoard, Location location, int position) {
        Block block = location.getBlock();
        BlockState state = block.getState();
        PersistableLocation persistableLocation = new PersistableLocation(location);

        Block near = getNearSkull(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()));
        if (near == null || near != null && near.getType() != Material.SKULL) {
            leaderBoard.getSigns().remove(persistableLocation);
            return;
        }

        if (state instanceof Sign) {
            Sign sign = (Sign) state;
            Skull skull = (Skull) near.getState();
            try {
                LeaderBoardPlayer player = leaderBoard.getPlayers(position + 1).get(position);
                skull.setOwner(player.getName());
                skull.update();
                String displayName = leaderBoard.isRandom() ? currentIndex.getDisplayName() : leaderBoard.getDisplayName();
                sign.setLine(0, ChatColor.BLUE + "[" + displayName + "]");
                sign.setLine(1, "#" + ChatColor.RESET + (position + 1));
                sign.setLine(2, player.getName());
                Object score = player.getScore();
                sign.setLine(3, ((
                        (leaderBoard.getName().equalsIgnoreCase("balance") || (leaderBoard.isRandom() && displayName.equalsIgnoreCase("balance"))
                                ? ChatColor.DARK_GREEN + "$":"")
                        + player.getScore())));
            } catch (Exception ignored) {
                sign.setLine(0, ChatColor.BLUE + "[" + leaderBoard.getDisplayName() + "]");
                sign.setLine(1,  "#" + ChatColor.RESET + (position + 1));
                sign.setLine(2, "Waiting for");
                sign.setLine(3, "Player...");
            } finally {
                sign.update();
            }

            leaderBoard.getSigns().putIfAbsent(persistableLocation, position);
        } else {
            leaderBoard.getSigns().remove(persistableLocation);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        String header = event.getLine(0);
        if (header.equals("[Leaderboard]")) {
            LeaderBoard leaderBoard = getLeaderboard(event.getLine(1));
            if (leaderBoard != null && player.hasPermission("factions.signs.leaderboard.create")) {
                Block block = event.getBlock();
                Integer position = JavaUtils.tryParseInt(event.getLine(2));
                if (position == null) {
                    block.breakNaturally();
                    player.sendMessage(ChatColor.RED + "'" + event.getLine(2) + "' is not a valid position.");
                    return;
                }

                if (position > 0) {
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> setSignAndSkull(leaderBoard, block.getLocation(), position - 1));
                }
            }
        }
    }
}
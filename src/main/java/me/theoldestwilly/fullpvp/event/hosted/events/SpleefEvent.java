package me.theoldestwilly.fullpvp.event.hosted.events;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.hosted.Participant;
import me.theoldestwilly.fullpvp.event.hosted.ParticipantRoll;
import me.theoldestwilly.fullpvp.event.hosted.structure.DuelsSystem;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedIndividualEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.Phase;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SpleefEvent extends HostedIndividualEvent implements DuelsSystem {
    private Cuboid arena;
    private BukkitTask selectorTask, arenaRestartTask;
    private Player mario, luigi;
    private Location spawn1, spawn2;
    private Long timeAutoArenaReset;

    public SpleefEvent(Player hoster) {
        super(hoster, HostedEventType.SPLEEF, false);
        Config config = getPlugin().getHostedEventsManager().getConfig();
        arena = (Cuboid) config.get(getType().toLowerString() + ".arena");
        String path = getType().toLowerString() + ".spawn.";
        spawn1 = (Location) config.get(path + "1");
        spawn2 = (Location) config.get(path + "2");
        if (getSpectatorsSpawn() == null && spawn1 != null && spawn2 != null) {
            setSpectatorsSpawn(spawn1);
        }
        if (arena != null && spawn1 != null && spawn2 != null && getSpectatorsSpawn() != null) {
            //for (Chunk chunk : arena.getChunks()) chunk.();
            setPossibleToStart(true);
        }
        if (getSpectatorsSpawn() == null) {
            cancel("Spectators spawn not found, contact a server administrator.");
        }
    }

    @Override
    public void start() {
        setPhase(Phase.IN_GAME);
        Bukkit.getScheduler().runTask(getPlugin(), () -> resetArena());
        startNewRound();
        getAlivePlayers().forEach(player -> player.teleport(getSpectatorsSpawn()));
    }



    @Override
    public List<String> getScoreboardLines() {
        List<String> lines = new LinkedList<>();
        lines.add(ChatColor.GOLD + "Event " + ChatColor.RED + getDisplayName());
        lines.add(ChatColor.GOLD + "Host" + ChatColor.GRAY + ": " + ChatColor.GREEN + getHostName());
        lines.add(ChatColor.GOLD + "Participants" + ChatColor.GRAY + ": " + ChatColor.YELLOW + getAlivePlayers().size() + ChatColor.GRAY + "/" + ChatColor.RED + getInitialPlayersAmount());
        if (hasStarted()) {
            //lines.add(ChatColor.GOLD + "Arena Reset" + ChatColor.GRAY + ": " + ChatColor.RED + getRoundRemaining());
            lines.add(mario != null && luigi != null ? ChatColor.GOLD + mario.getName() + ChatColor.YELLOW + " vs " + ChatColor.GOLD + luigi.getName() : ChatColor.GRAY + "Choosing Duel");
        }
        if (getSpectators().size() != 0) lines.add(ChatColor.GOLD + "Spectators" + ChatColor.GRAY + ": " + ChatColor.YELLOW + getSpectators().size());
        return lines;
    }

    @Override
    public void setSpectatorsSpawn(Location location) {

    }

    public void startNewRound() {
        this.selectorTask = (new BukkitRunnable() {
            int counter = 6;
            public void run() {
                --this.counter;
                if ((this.counter % 10 == 0 || this.counter <= 5) && this.counter > 0) {
                    broadcast(ChatColor.YELLOW + "The next duel will be selected in " + this.counter + " seconds.");
                } else if (this.counter <= 0) {
                    selectorTask.cancel();
                    selectorTask = null;
                    resetArena();
                    selectDuel();
                    broadcast(getPrefix() + ChatColor.YELLOW + luigi.getDisplayName() + ChatColor.YELLOW + " vs " + mario.getDisplayName() + ChatColor.YELLOW + ".");
                }

            }
        }).runTaskTimer(getPlugin(), 0L, 20L);
    }

    /*public void startArenaResetTask() {
        this.timeAutoArenaReset = System.currentTimeMillis();
        this.arenaRestartTask = Bukkit.getScheduler().runTaskTimer(getPlugin(), new BukkitRunnable() {
            int counter = 40;
            public void run() {
                --this.counter;
                if (this.counter <= 0) {
                    resetArena();
                    broadcast(ChatColor.YELLOW + "The arena has been restarted! ");
                }
            }
        }, 10L, 20L);
    }

    public String getRoundRemaining() {
        return this.arenaRestartTask != null ? JavaUtils.setFormat(TimeUnit.SECONDS.toMillis((long)40) - (System.currentTimeMillis() - this.timeAutoArenaReset)) : ChatColor.YELLOW + "00:00";
    }*/

    @Override
    public void selectDuel() {
        Participant participant = getParticipantWithWaitingDuelRoll();
        mario = participant.getPlayer();
        prepareSpleefPlayer(mario, participant);
        mario.teleport(spawn1);
        Participant participant2;
        do {
            participant2 = getParticipantWithWaitingDuelRoll();
        } while (participant2 == null || participant2.getPlayer() == null || mario.getUniqueId().equals(participant2.getUniqueId())) ;
        luigi = participant2.getPlayer();
        prepareSpleefPlayer(luigi, participant2);
        luigi.teleport(spawn2);
    }

    public Participant prepareSpleefPlayer (Player player, Participant participant) {
        participant.setSubRoll(ParticipantRoll.SubRoll.PLAYING);
        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SPADE).build());
        return participant;
    }


    public Participant getParticipantWithWaitingDuelRoll() {
        List<Participant> participants = new ArrayList<>();
        for (Participant participant : getEventPlayers().values()) {
            if (participant.getRoll() == ParticipantRoll.PLAYER && participant.getSubRoll() == ParticipantRoll.SubRoll.WAITING_DUEL)
                participants.add(participant);
        }
        if (participants.size() <= 1) {
            setAlivePlayersWithWaitingSubRoll();
            participants.addAll(getAliveParticipants());
        }
        Random random = new Random();
        return participants.get(random.nextInt(participants.size()));
    }

    public void setAlivePlayersWithWaitingSubRoll() {
        getEventPlayers().values().forEach(participant -> {
            if (participant.getRoll() == ParticipantRoll.PLAYER && participant.getSubRoll() != ParticipantRoll.SubRoll.PLAYING) participant.setSubRoll(ParticipantRoll.SubRoll.WAITING_DUEL);
        });
    }

    @Override
    public boolean isInDuel(UUID uuid) {
        for (Participant participant : getAliveParticipants()) {
            if (participant.getUniqueId().equals(uuid) && participant.getSubRoll() == ParticipantRoll.SubRoll.PLAYING)
                return true;
        }
        return false;
    }

    public void asyncArenaReset() {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), this::resetArena);
    }

    public void resetArena() {
        arenaRestartTask = null;
        Iterator<Block> iterator = arena.iterator();
        Block block;
        while (iterator.hasNext()) {
            block = iterator.next();
            block.setType(Material.SNOW_BLOCK);
        }
        //startArenaResetTask();
    }

    public Participant getWinnerByLoser (UUID loserUniqueId) {
        if (mario.getUniqueId().equals(loserUniqueId)) return getParticipant(luigi.getUniqueId());
        else if (luigi.getUniqueId().equals(loserUniqueId)) return getParticipant(mario.getUniqueId());
        return null;
    }

    public static void createArena(Player player) {
        Selection selection = FullPvP.getInstance().getWorldEditPlugin().getSelection(player);
        if (selection != null) {
            Cuboid arena = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
            Location max = selection.getMaximumPoint(), min = selection.getMinimumPoint();
            int xmax = max.getBlockX() , ymax = max.getBlockY(), zmax = max.getBlockZ(), xmin = min.getBlockX(), ymin = min.getBlockY(), zmin = min.getBlockZ();
            player.sendMessage(ChatColor.YELLOW + "Arena created. Max point " + ChatColor.GREEN + "x: " + xmax + " y: " + ymax + " z: " + zmax + ChatColor.YELLOW +
                    " Min Point " + ChatColor.GREEN + "x: " + xmin + " y: " + ymin + " z: " + zmin);
            Config config = FullPvP.getInstance().getHostedEventsManager().getConfig();
            config.set("spleef.arena", arena);
            config.save();
        } else {
            player.sendMessage(ChatColor.RED + "Selection not found.");
        }
    }

    public void finishRound(Player roundWinner, Player roundLoser) {
        mario.getInventory().clear();
        luigi.getInventory().clear();
        arenaRestartTask = null;
        mario = null;
        luigi = null;
        removePlayer(roundLoser, "", false, true);
        Participant winner = getParticipant(roundWinner.getUniqueId()), loser = getParticipant(roundLoser.getUniqueId());
        winner.setSubRoll(ParticipantRoll.SubRoll.WAITING_NEXT_ROUND);
        loser.setSubRoll(ParticipantRoll.SubRoll.SPECTATOR);
        if (hasEnded()) finish(false);
        //updateParticipants(winner, loser);
        if (!hasEnded()) startNewRound();
    }

    @Override
    public void onCancel() {
        cancelTasks();
    }

    public void cancelTasks() {
        if (selectorTask != null) selectorTask.cancel();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBreakBlock(BlockBreakEvent event) {
       if (event.getBlock().getType() == Material.SNOW_BLOCK && isInDuel(event.getPlayer().getUniqueId())) {
            event.setCancelled(false);
            Player player = event.getPlayer();
            player.getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
            player.getInventory().addItem(new ItemBuilder(Material.SNOW_BALL).setAmount(3).build());
        }
    }

    @EventHandler
    public void onEnityDamageByEntity (EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball) && event.getEntity() instanceof Player && isAlive((Player) event.getEntity())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event) {
        Location from = event.getFrom(), to = event.getTo();
        boolean isOtherBlock = from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
        if (isOtherBlock) {
            Player player;
            if (getPhase() == Phase.IN_GAME && isInDuel((player = event.getPlayer()).getUniqueId()) && to.getBlock().isLiquid()) {
                Participant winner = getWinnerByLoser(player.getUniqueId());
                winner.setSubRoll(ParticipantRoll.SubRoll.WAITING_NEXT_ROUND);
                winner.getPlayer().teleport(getSpectatorsSpawn());
                broadcast(getPrefix() + ChatColor.YELLOW + "The player " + player.getDisplayName() + ChatColor.YELLOW + " has been eliminated by " + winner.getDisplayName() + ChatColor.YELLOW + ".");
                finishRound(winner.getPlayer(), player);
            }
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (isParticipant(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

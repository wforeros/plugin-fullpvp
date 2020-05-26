package me.theoldestwilly.fullpvp.event.hosted.structure;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.combatlogger.event.CombatLoggerSpawnEvent;
import me.theoldestwilly.fullpvp.combatlogger.event.TagApplicationEvent;
import me.theoldestwilly.fullpvp.event.hosted.*;
import me.theoldestwilly.fullpvp.event.hosted.events.LastManStandingEvent;
import me.theoldestwilly.fullpvp.event.hosted.events.SpleefEvent;
import me.theoldestwilly.fullpvp.event.hosted.events.TntTagEvent;
import me.theoldestwilly.fullpvp.event.hosted.tasks.CountdownTask;
import me.theoldestwilly.fullpvp.event.hosted.tasks.GracePeriodTask;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
/**
 * This abstract class does not contains the spawns
 * create it into the new event class
 */
public abstract class HostedEvent implements Listener, Spectable {
    private String name;
    private String explanation;
    private FullPvP plugin;
    private String hostWithRank;
    private String hostName;
    private Location lobby = null;
    private Set<Player> spectators = new ConcurrentSet();
    private ConcurrentMap<UUID, Participant> eventPlayers = new ConcurrentHashMap<>();
    private HostedEventType type;
    private List<ItemStack> rewards = new ArrayList<>();
    private @Setter boolean winnerSelected = false;
    private ItemStack[] inventoryContents = null;
    private ItemStack[] armorContents = null;
    private int initialPlayersAmount = 0;
    private @Setter GracePeriodTask gracePeriodTask;
    private @Setter Phase phase;
    private @Setter int requiredPlayers = 4;
    private @Setter int maximumPlayers = 50;
    private CountdownTask countdownTask;
    private @Setter boolean possibleToStart, gracePeriodEnabled;
    private long initializedMillis;
    public Location spectatorsSpawn;
    private ItemStack spectatorsItem, randomTpSpectators;

    public HostedEvent(Player hoster, HostedEventType type, boolean gracePeriodEnabled) {
        plugin = FullPvP.getInstance();
        Integer integer = plugin.getHostedEventsManager().getCountdownDuration().intValue();
        if (integer == null) integer = 31;
        Config config = plugin.getHostedEventsManager().getConfig();
        lobby = (Location) config.get("lobby");
        hostWithRank = hoster.getDisplayName();
        this.type = type;
        hostName = hoster.getName();
        plugin = FullPvP.getInstance();
        phase = Phase.IN_LOBBY;
        this.gracePeriodEnabled = gracePeriodEnabled;
        if (lobby == null) {
            cancel("Lobby not found.");
            possibleToStart = false;
            hoster.sendMessage(ChatColor.RED + "Lobby not found, report it to a staff member.");
            return;
        }
        addPlayer(hoster);
        countdownTask = new CountdownTask(plugin, integer, this);
        Object object;
        if (type.isModifiableKit()) {
            String path = type.toString().toLowerCase();
            object = config.getList(path + ".kit.contents");
            if (object != null && object instanceof List) {
                this.inventoryContents = (ItemStack[]) ((List) object).toArray(new ItemStack[0]);
            }
            object = config.getList(path + ".kit.armor");
            if (object != null && object instanceof List) {
                this.armorContents = (ItemStack[]) ((List) object).toArray(new ItemStack[0]);
            }
        }
        object = config.get("rewards");
        if (object != null && object instanceof List)
            this.rewards.addAll(GenericUtils.createList(object, ItemStack.class));
        possibleToStart = true;
        spectatorsItem = new ItemBuilder(Material.INK_SACK).setDurability(1).setDisplayName(ChatColor.RED + "Leave Spectator").setLore(Arrays.asList("&7Right click to leave the event.")).build();
        randomTpSpectators = new ItemBuilder(Material.RECORD_3).setDisplayName(ChatColor.GREEN + "Random Teleport").setLore(Arrays.asList("&7Right click to random teleport")).build();
        initializedMillis = System.currentTimeMillis();
        loadSpectatorsSpawn();
    }

    public void startGracePeriodTask() {
        gracePeriodTask = new GracePeriodTask(plugin, this);
        setPhase(Phase.GRACE_PERIOD);
    }

    public void loadSpectatorsSpawn() {
        String path = type.toString().toLowerCase() + ".spawn.spectators";
        Object object = this.plugin.getHostedEventsManager().getConfig().get(path);
        if (object != null && object instanceof Location){
            spectatorsSpawn = ((Location) object);
        }
    }

    public void addPlayer(Player player) {
        if (hasStarted()) {
            player.sendMessage(Lang.ERROR_HOSTEDEVENT_STARTED);
            return;
        }
        if (isFull()) {
            player.sendMessage(Lang.ERROR_HOSTEDEVENT_IS_FULL);
            return;
        }
        Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (Hosted Event)");
            return;
        }
        if (profile.hasPvpTimer()) {
            player.sendMessage(ChatColor.RED + "You can not do this while you have an enabled pvp timer.");
            return;
        }
        if (profile.isInStaffMode()) profile.setInStaffMode(false, true);
        Participant participant = new Participant(player, type, ParticipantRoll.PLAYER);
        eventPlayers.put(player.getUniqueId(), participant);
        preparePlayer(player, false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.teleport(lobby);
        initialPlayersAmount++;
        broadcast(Lang.SUCCESS_HOSTEDEVENT_JOINED_BROADCAST_REP_DISPLAY_NAME.replace("%DISPLAY_NAME%", player.getDisplayName()));
    }

    public void addSpectator(Player player, boolean deathInEvent) {
        if (spectatorsSpawn == null) {
            plugin.getSpawnHandler().directSpawnTeleport(player, "main");
            player.sendMessage(ChatColor.RED + "Spectator mode is not allowed in this event.");
            return;
        }
        Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (Hosted Event #2)");
            return;
        }
        if (profile.isInStaffMode()) profile.setInStaffMode(false, true);
        if (player.getGameMode() == GameMode.CREATIVE) player.setGameMode(GameMode.SURVIVAL);
        Participant participant;
        if (deathInEvent) {
            participant = getParticipant(player.getUniqueId());
            participant.setRoll(ParticipantRoll.SPECTATOR);
            participant.setAlive(false);
        } else {
            participant = new Participant(player, getType(), ParticipantRoll.SPECTATOR);
            eventPlayers.put(player.getUniqueId(), participant);
        }
        preparePlayer(player, true);
        GameUtils.showPlayers(player, getSpectators());
        GameUtils.hidePlayer(getAlivePlayers(), player);
        player.teleport(spectatorsSpawn);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().setItem(8, spectatorsItem);
        player.getInventory().setItem(0, randomTpSpectators);
        player.sendMessage(getPrefix() + ChatColor.YELLOW + "You are now in spectator mode.");
    }

    /**
     *This method contains the condition to finish the event
     * @param player Player who is going to be removed
     * @param reason Reason which will be sent to the removed player
     * @param eventCancelled If the reason of removing players is that the hosted event was cancelled use true
     *
     */
    public void removePlayer(Player player, String reason, boolean eventCancelled, boolean sendAsSpectator) {
        if (!hasStarted()) {
            initialPlayersAmount--;
        }
        Participant participant = getParticipant(player.getUniqueId());
        if (participant == null) {
            Bukkit.getLogger().log(Level.WARNING, "Error loading " + player.getName() + "'s participant profile.");
            return;
        }
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        player.setFireTicks(0);
        player.setFlying(false);
        GameUtils.showPlayers(player, getSpectators());
        GameUtils.showPlayers(player, getAlivePlayers());
        Checkers.removeAllPotionEffects(player);
        Location spawn = plugin.getSpawnHandler().getSpawn("main").getLocation();
        if (eventCancelled) {
            if (spawn != null) player.teleport(spawn);
            participant.returnCachedInventory();
        }
        if (reason != null) player.sendMessage(reason);
        participant.setAlive(false);
        player.setAllowFlight(participant.getAllowFlight());
        if (sendAsSpectator && !winnerSelected) {
            participant.setRoll(ParticipantRoll.SPECTATOR);
            participant.setSubRoll(ParticipantRoll.SubRoll.SPECTATOR);
            addSpectator(player, true);
        } else plugin.getSpawnHandler().directSpawnTeleport(player, "main");
        if (hasEnded() && !winnerSelected) {
            finish(eventCancelled);
        }
        else if (!sendAsSpectator) {
            participant.returnCachedInventory();
            eventPlayers.remove(player.getUniqueId());
            plugin.getSpawnHandler().directSpawnTeleport(player, "main");
        }
    }

    public void removeParticipant(Player player, boolean eventCancelled, boolean sendAsSpectator) {
        Participant participant = getParticipant(player.getUniqueId());
        if (participant != null) {
            if (participant.getRoll() == ParticipantRoll.PLAYER) removePlayer(player, null, eventCancelled, sendAsSpectator);
            else if (participant.getRoll() == ParticipantRoll.SPECTATOR) removeSpectator(player);
        }
    }

    public void removeSpectator(Player player) {
        Participant participant = getParticipant(player.getUniqueId());
        if (participant == null) {
            Bukkit.getLogger().log(Level.WARNING, "Error loading " + player.getName() + "'s participant profile.");
            return;
        }
        if (!participant.isSpectator()) {
            player.sendMessage(ChatColor.RED + "You are not an event spectator.");
            return;
        }
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        player.setFireTicks(0);
        player.setFlying(false);
        if (!participant.getAllowFlight()) player.setAllowFlight(false);
        participant.returnCachedInventory();
        Checkers.removeAllPotionEffects(player);
        GameUtils.showPlayer(getAlivePlayers(), player);
        GameUtils.showPlayer(getSpectators(), player);
        eventPlayers.remove(player.getUniqueId());
        Location spawn = plugin.getSpawnHandler().getSpawn("main").getLocation();
        if (spawn != null) player.teleport(spawn);
        player.sendMessage(getPrefix() + Lang.SUCCESS_COLOR + "You have left the spectator event mode successfully");
    }

    public void preparePlayers() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Starting the " + this.getDisplayName() + ChatColor.YELLOW + " event with " + this.getInitialPlayersAmount() + " players. Good luck!");
        Iterator<Player> iterator = this.getAlivePlayers().iterator();
        while (iterator.hasNext()) {
            preparePlayer(iterator.next(), false);
        }
    }

    /**
     * Create the Participant instance before executing this method
     * @param player
     */
    public void preparePlayer(Player player, boolean isSpectator) {
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        player.setFireTicks(0);
        Checkers.removeAllPotionEffects(player);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        Profile profile = FullPvP.getInstance().getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile.isInStaffMode()) profile.setInStaffMode(false, true);
        if (player.getGameMode() == GameMode.CREATIVE) player.setGameMode(GameMode.SURVIVAL);
        if (getPhase() != Phase.IN_LOBBY && !isSpectator && this.getArmorContents() != null) player.getInventory().setArmorContents(this.getArmorContents());
        if (getPhase() != Phase.IN_LOBBY && !isSpectator && this.getInventoryContents() != null) player.getInventory().setContents(this.getInventoryContents());
        player.updateInventory();
    }

    public void setRewards(Player winner) {
        if (getRewards() != null && !getRewards().isEmpty()) {
            for (ItemStack itemStack : getRewards())
                winner.getInventory().addItem(itemStack);
        } else winner.sendMessage(getPrefix() + "Rewards not found, contact an administrator.");
    }

    /**
     * This method contains the methods:
     * preparePlayers(), startGracePeriodTask() and the abstract method "start()"
     */
    public void preStart() {
        start();
        if (gracePeriodEnabled) startGracePeriodTask();
        preparePlayers();
    }


    public void finish(boolean cancelled) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            getSpectators().forEach(spectator -> removeSpectator(spectator));
            if (!cancelled) {
                onFinish();
            }
            HandlerList.unregisterAll(HostedEvent.this);
            plugin.getHostedEventsManager().setCurrentEvent(null);
        }, 20L * 3);
    }

    public abstract void addEventWinToWinnersProfiles(List<Player> winners);

    public void cancel(String reason) {
        if (countdownTask != null && !countdownTask.hasCountdownEnded()) {
            countdownTask.cancel();
        } else if (gracePeriodTask != null && !gracePeriodTask.hasCountdownEnded()) {
            gracePeriodTask.cancel();
        }
        onCancel();
        getAlivePlayers().forEach(player -> removePlayer(player, ChatColor.RED + "Event cancelled for" + ChatColor.GRAY + ": " + TextUtils.formatColor(reason), true, false));
        getSpectators().forEach(player -> removeSpectator(player));
        plugin.getHostedEventsManager().setCurrentEvent(null);
        HandlerList.unregisterAll(this);
    }

    public abstract void onCancel();

    public void onPluginDisabled() {
        for (Player player : getAlivePlayers()) {
            GameUtils.showPlayers(player, getSpectators());
            removePlayer(player, ChatColor.RED + "The plugin was unloaded, cancelling the event.", true, false);
        }
        getSpectators().forEach(player -> removeSpectator(player));
    }

    /**
     * Use this method to create the condition when the event will be considered as ended.
     *
     * @return
     */
    public abstract boolean hasEnded();

    /**
     * This method will be executed when an event is finished, do not
     * Override the meethod called "finish()"
     */
    public abstract void onFinish();

    /**
     * This method will be executed into the preStart method
     * is not recommended to start the event with start, always use "preStart"
     */
    public abstract void start();

    @Override
    public boolean isSpectable() {
        return spectatorsSpawn != null && getPhase() != Phase.IN_LOBBY;
    }

    public abstract List<String> getScoreboardLines();

    public List<Player> getAlivePlayers() {
        List<Player> list = new ArrayList<>();
        eventPlayers.values().forEach(participant -> {
            if (participant.isAlive() && !participant.isSpectator()) list.add(participant.getPlayer());
        });
        return list;
    }

    public List<Participant> getAliveParticipants() {
        List<Participant> list = new ArrayList<>();
        eventPlayers.values().forEach(participant -> {
            if (participant.isAlive() && !participant.isSpectator()) list.add(participant);
        });
        return list;
    }

    public List<Player> getSpectators() {
        List<Player> list = new ArrayList<>();
        eventPlayers.values().forEach(participant -> {
            if (participant.isSpectator()) list.add(participant.getPlayer());
        });
        return list;
    }

    public List<Player> getParticipantsAsPlayers() {
        List<Player> list = new ArrayList<>();
        eventPlayers.values().stream().forEach(participant -> {
            list.add(participant.getPlayer());
        });
        return list;
    }

    public String getDisplayName() {
        return type.getDisplayName();
    }

    public Participant getParticipant(UUID uniqueId) {
        return eventPlayers.get(uniqueId);
    }

    public Player getAlivePlayer(UUID uniqueId) {
        if (eventPlayers.containsKey(uniqueId)) return Bukkit.getPlayer(uniqueId);
        return null;
    }

    public boolean isFull() {
        return eventPlayers.size() >= maximumPlayers;
    }

    /**
     * This method returns false if phase equals to IN_LOBBY or GRACE_PERIOD
     * @return
     */
    public boolean hasStarted() {
        return phase != Phase.IN_LOBBY && phase != Phase.GRACE_PERIOD;
    }

    public void broadcast(String msg) {
        eventPlayers.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) player.sendMessage(msg);
        });
    }

    /**
     *
     * @param player
     * @return This method would return an alive player or spectator
     */
    public boolean isParticipant(Player player) {
        return eventPlayers.containsKey(player.getUniqueId());
    }

    public boolean isSpectator(Player player) {
        Participant participant = getParticipant(player.getUniqueId());
        if (participant != null)
            return participant.isSpectator();
        return false;
    }
    public boolean isJoinable() {
        return phase == Phase.IN_LOBBY;
    }

    public boolean isAlive(Player player) {
        Participant participant = getParticipant(player.getUniqueId());
        if (participant != null)
            return participant.isAlive();
        return false;
    }

    /*public void updateParticipant(Participant participant) {
        getEventPlayers().replace(participant.getUniqueId(), participant);
        Bukkit.broadcastMessage("After: " + getParticipant(participant.getUniqueId()).getSubRoll());
    }

    public void updateParticipants(Participant... participants) {
        for (Participant participant : participants) {
            Bukkit.broadcastMessage("Update " + participant.getSubRoll().toString());
            updateParticipant(participant);
        }
    }*/

    public String getPrefix() {
        return type.getPrefix();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isParticipant(player)) {
            removeParticipant(player, false, false);
            plugin.getSpawnHandler().teleportToSpawn(player, "main");
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (isAlive(player)) {
            player.spigot().respawn();
            removePlayer(player, "", false, true);
            plugin.getSpawnHandler().directSpawnTeleport(player, "main");
        }
    }


    @EventHandler
    public void onPlayerPickUpItem(PlayerPickupItemEvent event) {
        if (isParticipant(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCombatLoggerSpawn(CombatLoggerSpawnEvent event) {
        if (isParticipant(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onTagApplication(TagApplicationEvent event) {
        if (isParticipant(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExecuteCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!isParticipant(player)) {
            return;
        }
        if (!player.isOp()) {
            String[] command = event.getMessage().split(" ");
            if (!this.plugin.getCombatLoggerHandler().getAllowedCommands().contains(command[0]) && !PermissionsManager.hasStaffPermission(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can not execute this command while in an event.");
            } else if (command[0].equalsIgnoreCase("/suicide") || command[0].equalsIgnoreCase("/kill") && command.length >= 2 && isParticipant(Bukkit.getPlayer(command[1]))) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can not kill a player if target is in an event, use /evg kick <player>.");
            }
        }
    }

    @EventHandler
    public void onEntityDamage (EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isSpectator(player)) event.setCancelled(true);
            else if  (isAlive(player) && !hasStarted()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onParticipantDamagedByParticipand1 (EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity(), damager = (Player) event.getDamager();
            if (isSpectator(damager) && isAlive(player)) {
                event.setCancelled(true);
                return;
            }
            if (isParticipant(damager) && (getPhase() == Phase.GRACE_PERIOD || getPhase() == Phase.IN_LOBBY)) {
                event.setCancelled(true);
                damager.sendMessage(ChatColor.RED + "You can no attack players while the event is in grace period phase.");
            }
        }
    }

    @EventHandler
    public void onPlayedFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && isParticipant((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isSpectator(player) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (player.getItemInHand().isSimilar(spectatorsItem)) {
                removeSpectator(player);
            } else if (player.getItemInHand().isSimilar(randomTpSpectators)) {
                Player tp = null;
                while (tp == null || tp.getUniqueId().equals(player.getUniqueId())) tp = getAlivePlayers().get(new Random().nextInt(getAlivePlayers().size()));
                player.teleport(tp);
            }
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public static HostedEvent createEvent(Player hoster, HostedEventType type) {
        HostedEvent event = null;
        if (type == HostedEventType.LMS) event = new LastManStandingEvent(hoster);
        else if (type == HostedEventType.TNT_TAG) event = new TntTagEvent(hoster);
        else if (type == HostedEventType.SPLEEF) event = new SpleefEvent(hoster);
        return event;
    }
}

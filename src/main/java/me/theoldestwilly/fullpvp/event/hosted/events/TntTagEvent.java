package me.theoldestwilly.fullpvp.event.hosted.events;

import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEventType;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedIndividualEvent;
import me.theoldestwilly.fullpvp.event.hosted.structure.Phase;
import me.theoldestwilly.fullpvp.event.hosted.structure.Spectable;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.particles.Packet;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TntTagEvent extends HostedIndividualEvent implements Spectable {
    private Location spawn;

    private BukkitTask selectorTask;
    private BukkitTask roundTask;
    private int round = 0;
    private List<Player> targets = new ArrayList();
    private Long timeRound = 0L;

    public TntTagEvent(Player host) {
        super(host, HostedEventType.TNT_TAG,  false);
        spawn = (Location) getPlugin().getHostedEventsManager().getConfig().get(getType().toString().toLowerCase() + ".spawn");
        if (spawn == null) {
            cancel(ChatColor.RED + "Spawn not found, contact an administrator.");
            setPossibleToStart(false);
            return;
        }
        setPossibleToStart(true);
        if (getSpectatorsSpawn() == null) setSpectatorsSpawn(spawn);
    }

    public String getDisplayName() {
        return getType().getDisplayName();
    }

    @Override
    public void onCancel() {
        cancelTasks();
    }

    @Override
    public void start() {
        Iterator var1 = this.getAlivePlayers().iterator();
        while(var1.hasNext()) {
            Player player = (Player)var1.next();
            player.teleport(this.spawn);
        }
        this.selectNewTargets();
        //this.plugin.getEventsManager().setEventCooldown(this.getType());
    }

    @Override
    public void setSpectatorsSpawn(Location location) {
        spectatorsSpawn = location;
    }

    public String getRoundRemaining() {
        return this.roundTask != null ? JavaUtils.setFormat(TimeUnit.SECONDS.toMillis((long)this.getRoundDurationByParticipantsNumber()) - (System.currentTimeMillis() - this.timeRound)) : ChatColor.YELLOW + "00:00";
    }

    public int getRoundDurationByParticipantsNumber() {
        int participants = this.getEventPlayers().size();
        byte duration;
        if (participants >= 15) {
            duration = 45;
        } else if (participants < 15 && participants >= 10) {
            duration = 40;
        } else {
            duration = 30;
        }
        return duration;
    }

    public void cancelTasks() {
        if (roundTask != null) roundTask.cancel();
        if (selectorTask != null ) selectorTask.cancel();
    }

    public void selectNewTargets() {
        this.selectorTask = (new BukkitRunnable() {
            int counter = 6;

            public void run() {
                --this.counter;
                if ((this.counter % 10 == 0 || this.counter <= 5) && this.counter > 0) {
                    broadcast(ChatColor.YELLOW + "The target player(s) will be chosen in " + this.counter + " seconds.");
                } else if (this.counter <= 0) {
                    selectorTask.cancel();
                    selectorTask = null;
                    setTagguedPlayersByParticipants();
                    startRound();
                }

            }
        }).runTaskTimer(getPlugin(), 0L, 20L);
    }

    public void setTagguedPlayersByParticipants() {
        Random random = new Random();
        if (getPhase() != Phase.IN_GAME) setPhase(Phase.IN_GAME);
        List<Player> players = this.getAlivePlayers();
        int participants = players.size();
        int targetsAmount = 1;
        int playersAmount = players.size();
        int rand = playersAmount == 0 ? 0 : random.nextInt(playersAmount);
        Player target = null;
        if (participants >= 22) {
            targetsAmount = 4;
        } else if (participants >= 14) {
            targetsAmount = 3;
        } else if (participants >= 6) {
            targetsAmount = 2;
        }

        for(; this.targets.size() < targetsAmount; rand = random.nextInt(playersAmount)) {
            target = players.get(rand <= 0 ? 0 : rand);
            if (target != null && !this.targets.contains(target)) {
                Checkers.removeAllPotionEffects(target);
                target.getInventory().setHelmet(new ItemStack(Material.TNT));
                target.getInventory().addItem(new ItemStack(Material.TNT));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 3));
                this.broadcast(target.getDisplayName() + ChatColor.YELLOW + " is now tagged!");
                target.sendMessage(ChatColor.RED + "Run and hit someone to lose the tag!");
                this.targets.add(target);
            }
        }

        if (target == null) {
            this.targets.add(this.getAlivePlayers().get(0));
        }

    }

    public void startRound() {
        ++this.round;
        if (this.targets != null) {
            Iterator var1 = this.getAlivePlayers().iterator();
            while(var1.hasNext()) {
                Player player = (Player)var1.next();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1));
                if (this.getAlivePlayers().size() < 5) {
                    player.teleport(this.spawn);
                }
            }
            this.timeRound = System.currentTimeMillis();
            this.roundMessage();
            this.roundTask = Bukkit.getScheduler().runTaskTimer(getPlugin(), new BukkitRunnable() {
                int counter = TntTagEvent.this.getRoundDurationByParticipantsNumber();
                public void run() {
                    --this.counter;
                    if (this.counter <= 0) {
                        TntTagEvent.this.finishRound();
                        TntTagEvent.this.roundTask.cancel();
                        TntTagEvent.this.roundTask = null;
                    }
                }
            }, 10L, 20L);
        }
    }

    public void finishRound() {
        if (this.targets != null) {
            Iterator var1 = this.targets.iterator();
            while(var1.hasNext()) {
                Player target = (Player)var1.next();
                Location loc = target.getLocation();
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, false, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 1.0f, 1.0f, 1.0f, 0.0f, 2);
                Iterator var5 = this.getAlivePlayers().iterator();
                while(var5.hasNext()) {
                    Player player = (Player)var5.next();
                    Packet.sendTntTagParticles(packet, player);
                }
                loc.getWorld().playSound(loc, Sound.EXPLODE, 1f, 1f);
                broadcast(ChatColor.RED + "The TNT has exploded and " + target.getDisplayName() + ChatColor.RED + " was eliminated.");
                removePlayer(target, getPrefix() + ChatColor.RED + "The TNT has exploded and you were removed from the event.", false, true);
            }

            this.targets.clear();
            if (this.getAlivePlayers().size() > 1) {
                this.selectNewTargets();
            } else {
                cancelTasks();
            }
        }
    }

    public String getTargetsName() {
        if (this.targets.size() == 1) {
            return ((Player)this.targets.get(0)).getName();
        } else if (this.targets.size() > 1) {
            return this.targets.size() + "";
        } else {
            return this.targets == null && this.targets.isEmpty() ? ChatColor.DARK_RED + "ERROR" : ChatColor.YELLOW + "Choosing";
        }
    }

    /*public void startCoundown() {
        getTask().setCounter(31);
        getTask().run();
    }*/

    public void changeTarget(Player oldTarget, Player newTarget) {
        if (!this.targets.contains(newTarget)) {
            oldTarget.getInventory().setHelmet(new ItemStack(Material.AIR));
            newTarget.getInventory().setHelmet(new ItemStack(Material.TNT));
            Checkers.removeAllPotionEffects(oldTarget);
            Checkers.removeAllPotionEffects(newTarget);
            oldTarget.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1));
            oldTarget.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.TNT)});
            this.targets.remove(oldTarget);
            this.targets.add(newTarget);
            newTarget.getInventory().addItem(new ItemStack[]{new ItemStack(Material.TNT)});
            newTarget.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 3));
            this.broadcast(newTarget.getDisplayName() + ChatColor.YELLOW + " is now tagged!");
        }
    }

    public boolean isTarget(Player player) {
        return this.targets.contains(player);
    }

    public void roundMessage() {
        this.broadcast(TextUtils.formatColor("&7&f&m--------------------------\n&e           Round #" + this.round + "           \n \n&c      Tagged players&7:     \n" + this.getTaggeds() + "\n&7&f&m--------------------------"));
    }

    public String getTaggeds() {
        String names = "";
        int max = this.targets.size();
        int counter = 0;
        Iterator var4 = this.targets.iterator();

        while(var4.hasNext()) {
            Player player = (Player)var4.next();
            names = names + ChatColor.YELLOW + player.getName();
            ++counter;
            if (max > counter) {
                names = names + ChatColor.GRAY + ", ";
            } else {
                names = names + ChatColor.GRAY + ".";
            }
        }

        return names;
    }

    @Override
    public List<String> getScoreboardLines() {
        List<String> lines = new LinkedList<>();
        lines.add(ChatColor.GOLD + "Event " + ChatColor.RED + getDisplayName());
        lines.add(ChatColor.GOLD + "Host" + ChatColor.GRAY + ": " + ChatColor.GREEN + getHostName());
        lines.add(ChatColor.GOLD + "Participants" + ChatColor.GRAY + ": " + ChatColor.YELLOW + getAlivePlayers().size() + ChatColor.GRAY + "/" + ChatColor.RED + getInitialPlayersAmount());
        //lines.add(ChatColor.GOLD + "Phase" + ChatColor.GRAY + ": " + getPhase().getName());
        if (hasStarted()) {
            lines.add(ChatColor.GOLD + "Round Remaining" + ChatColor.GRAY + ": " + ChatColor.RED + getRoundRemaining());
            lines.add(ChatColor.GOLD + "Taggeds" + ChatColor.GRAY + ": " + ChatColor.RED + getTargetsName());
        }
        if (getSpectators().size() != 0) lines.add(ChatColor.GOLD + "Spectators" + ChatColor.GRAY + ": " + ChatColor.YELLOW + getSpectators().size());
        return lines;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public List<Player> getTargets() {
        return this.targets;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isAlive(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && isParticipant((Player) event.getEntity()) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (isParticipant(damager)) event.setDamage(0.0D);
            if (isTarget(damager) && isAlive((Player) event.getEntity())) {
                changeTarget(damager, (Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onPlayerFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && isParticipant((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (isParticipant(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

package me.theoldestwilly.fullpvp.users;


import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.combatlogger.event.TagApplicationEvent;
import me.theoldestwilly.fullpvp.users.rewards.RewardType;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayersHandler implements Listener {
    private FullPvP plugin;
    private @Getter Double killPayQiaq, killPayFoses, killPayTrojan, killPayKain, killPayBolq, killPayDefault;
    private @Getter int rankupKills;
    private String path = "kill.money.";

    public PlayersHandler(FullPvP plugin) {
        this.plugin = plugin;
        Config config = plugin.getConfig();
        killPayQiaq = config.getDouble(path + "qiaq");
        killPayFoses = config.getDouble(path + "foses");
        killPayTrojan = config.getDouble(path + "trojan");
        killPayKain = config.getDouble(path + "kain");
        killPayBolq = config.getDouble(path + "bolq");
        killPayDefault = config.getDouble(path + "default");
        rankupKills = config.getInt("kill.rankup");
    }

    public Double getPayByRank(Player player) {
        Double payBase;
        if (PermissionsManager.hasQiaqPerm(player)) {
            payBase = killPayQiaq;
        } else if (PermissionsManager.hasFosesPerm(player)) {
            payBase = killPayFoses;
        } else if (PermissionsManager.hasTrojanPerm(player)) {
            payBase = killPayTrojan;
        } else if (PermissionsManager.hasKainPerm(player)) {
            payBase = killPayKain;
        } else if (PermissionsManager.hasBolqPerm(player)) {
            payBase = killPayBolq;
        } else {
            payBase = killPayDefault;
        }
        return payBase;
    }

    /**
     *
     * Method used to update kills and deaths from fullpvp database
     * @param killed Entity killed, it would be a logger or a player
     * @param uniqueId Killed's uuid used to find the profile form fullpvp database
     */
    public void onEntityDeath(Entity killed, UUID uniqueId) {
        Player killer;
        if (killed instanceof Villager) killer = ((Villager) killed).getKiller();
        else if (killed instanceof Player) killer = ((Player) killed).getKiller();
        else return;
        ProfileHandler handler = this.plugin.getProfileHandler();
        Profile profile = handler.getOfflineProfile(uniqueId, false);
        if (profile == null) return;
        profile.addDeath();
        if (!profile.isOnline()) handler.saveProfileAsync(profile);
        else plugin.getCombatLoggerHandler().removeTag((Player) killed);
        Clan clan = this.plugin.getClanManager().getPlayersClan(ChatColor.stripColor(killed.getCustomName()));
        if (clan != null) {
            clan.addDeath();
        }
        if (killer == null) return;
        profile = this.plugin.getProfileHandler().getProfileByUniqueID(killer.getUniqueId());
        if (profile == null) return;
        Double killsForRankupD = GameUtils.getKills(rankupKills, profile.getLevel());
        profile.addKill();
        Integer killsForRankup = killsForRankupD.intValue();
        if (killsForRankup <= profile.getKills()) {
            profile.levelUp();
            Bukkit.broadcastMessage(ChatColor.AQUA + "The player " + ChatColor.YELLOW + killer.getName() + ChatColor.AQUA + " has reached level " + ChatColor.GRAY + "[" + ChatColor.YELLOW + profile.getLevel() + ChatColor.GRAY + "]");
        }
        Double payBase = getPayByRank(killer);
        payBase = payBase + (payBase * ((profile.getLevel() - 1) / 100.0D));
        this.plugin.getEconomyManager().deposit(killer, payBase);
        killer.sendMessage(ChatColor.YELLOW + "You earned $" + JavaUtils.showDoubleWith2Decimals(payBase) + " by killing " + ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + ".");
        if (profile.getKillStreak() % 5 == 0) {
            Integer killStreak = profile.getKillStreak();
            String killerName = killer.getName();
            Bukkit.broadcastMessage(TextUtils.formatColor("&9KS &7» &eThe player &a" + killerName + " &ehas reached &a" + killStreak + " &ekills without die!"));
            /*if (killStreak == 5 || killStreak == 10 || killStreak % 30 == 0) {
                profile.addReward(this.plugin.getRewardsManager().createReward(RewardType.COMMON));
                killer.sendMessage(TextUtils.formatColor("&eYou have earned a &5Common Chest &fby achieve &c" + killStreak + " &ekills! Use &6/reclaim &eto see your rewards!"));
            }

            if (killStreak % 50 == 0) {
                profile.addReward(this.plugin.getRewardsManager().createReward(RewardType.MYSTERIOUS));
                Bukkit.broadcastMessage(TextUtils.formatColor("&eThe player &a" + killerName + "&e has earned a &5Legendary Chest &eby achieve &c" + killStreak + " &ekills!"));
                killer.sendMessage(TextUtils.formatColor("&fUse &e/reclaim &fto see your rewards!"));
            }

            if (killStreak % 110 == 0) {
                profile.addReward(this.plugin.getRewardsManager().createReward(RewardType.LEGENDARY));
                Bukkit.broadcastMessage(TextUtils.formatColor("&eThe player &a" + killerName + "&e has earned a &cMysterious Chest &eby achieve &c" + killStreak + " &ekills!"));
                killer.sendMessage(TextUtils.formatColor("&fUse &e/reclaim &fto see your rewards!"));
            }*/
        }
    }



    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        if (PermissionsManager.hasChatColorPermission(player)) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        }
        Clan clan = this.plugin.getClanManager().getPlayersClan(player);
        String clanPrefix = "";
        if (clan != null) {
            clanPrefix = clan.getMemPrefix(player.getUniqueId());
        }
        event.setFormat(ChatColor.YELLOW + clanPrefix + ChatColor.YELLOW + player.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + msg);
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onPlayerJoinMessages(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        event.setJoinMessage((String)null);
        if (player.hasPermission("fullpvp.join.broadcast") && !player.hasPermission("fullpvp.join.bypassmsg")) {
            String prefix = ChatColor.translateAlternateColorCodes('&', player.getDisplayName());
            event.setJoinMessage(TextUtils.formatColor("&a" + prefix + player.getName() + "&e has joined to the server!"));
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage((String)null);
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = event.getPlayer();
        if (player.hasPermission("fullpvp.leave.broadcast") && !player.hasPermission("fullpvp.leave.bypassmsg")) {
            String prefix = ChatColor.translateAlternateColorCodes('&', player.getDisplayName());
            event.setQuitMessage(TextUtils.formatColor("&c" + prefix + player.getName() + " &cleft the server!"));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage(null);
        onEntityDeath(player, player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Player player = (Player) event.getEntity();
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.sendMessage(ChatColor.RED + "You have been killed by the void!");
            onEntityDeath(player, player.getUniqueId());
            plugin.getSpawnHandler().directSpawnTeleport(player, "main");
        }
    }

    @EventHandler
    public void onPlayerDamageByEntity (EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = GameUtils.getPlayerDamagerFromEntity(event.getDamager());
            if (damager != null) {
                Profile damagerProfile = plugin.getProfileHandler().getProfileByUniqueID(damager.getUniqueId()),
                        playerProfile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (damagerProfile == null || playerProfile == null) return;
                if (damagerProfile.hasPvpTimer()) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You can not attack other players while your pvp timer is enabled.");
                } else if (playerProfile.hasPvpTimer()) {
                    event.setCancelled(true);
                    damager.sendMessage(player.getDisplayName() + ChatColor.RED + " has a grace period active, he/she can not receive damage because is a new player.");
                }
            }
        }
    }

    @EventHandler
    public void onTagApplication (TagApplicationEvent event) {
        Player damager = event.getDamager();
        Player player = event.getPlayer();
        Profile damagerProfile = null, playerProfile = null;
        if (damager != null) damagerProfile = plugin.getProfileHandler().getProfileByUniqueID(damager.getUniqueId());
        if (player != null) playerProfile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (damagerProfile != null && damagerProfile.hasPvpTimer()) event.setCancelled(true);
        else if (playerProfile != null && playerProfile.hasPvpTimer()) event.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getCombatLoggerHandler().removeTag(event.getPlayer());
        event.setRespawnLocation(plugin.getSpawnHandler().getSpawn("main").getLocation());
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME && !event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAchieveEvent(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }
}


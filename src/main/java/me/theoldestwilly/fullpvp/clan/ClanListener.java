package me.theoldestwilly.fullpvp.clan;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ClanListener implements Listener {
    private FullPvP plugin;

    public ClanListener(FullPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerFight(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Player damager = (Player) event.getDamager();
            HostedEvent hostedEvent = plugin.getHostedEventsManager().getCurrentEvent();
            if (hostedEvent == null || (!hostedEvent.isParticipant(damager) && !hostedEvent.isParticipant(player))) {
                Clan clan = this.plugin.getClanManager().getPlayersClan(damager);
                if (clan != null) {
                    Clan clan1 = this.plugin.getClanManager().getPlayersClan(player);
                    if (clan1 != null && clan.getName().equals(clan1.getName()) && !clan.isFriendlyFire()) {
                        event.setCancelled(true);
                        damager.sendMessage(Lang.WARNING_CLAN_CANNOT_ATTACK_MATES);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onClanChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Clan clan = this.plugin.getClanManager().getPlayersClan(player);
        if (clan != null) {
            ClanMember clanMember = clan.getMember(player.getUniqueId());
            if (clanMember.isAtClanChat()) {
                event.setCancelled(true);
                clan.sendChatMessage(player.getName(), event.getMessage());
            }
        }
    }
}

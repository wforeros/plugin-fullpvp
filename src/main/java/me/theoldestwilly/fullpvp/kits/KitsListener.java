package me.theoldestwilly.fullpvp.kits;


import java.util.Arrays;
import java.util.Iterator;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.kits.event.PlayerKitApplyEvent;
import me.theoldestwilly.fullpvp.users.Profile;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitsListener implements Listener {
    private final FullPvP plugin;

    public KitsListener(FullPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.MONITOR
    )
    public void onKitApplyMonitor(PlayerKitApplyEvent event) {
        if (!event.isForced()) {
            Kit kit = event.getKit();
            Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(event.getPlayer().getUniqueId());
            //profile.incrementKitUsages(kit);
            //profile.updateKitCooldown(kit);
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGHEST
    )
    public void onKitApply(PlayerKitApplyEvent event) {
        if (!event.isForced()) {
            Player player = event.getPlayer();
            Kit kit = event.getKit();
            if (!player.isOp() && kit.isDisabled()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can not apply the '" + kit.getName() + "' kit because it is currently disabled.");
            } else {
                String permission = String.format(Kit.KIT_PERMISSION_NODE, kit.getName());
                if (permission != null && !player.hasPermission(permission)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this kit.");
                } else {
                    Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                    if (profile.isInStaffMode()) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You can not apply kits while you are in staff mode.");
                    } else {
                        long remaining = 0L;//profile.getRemainingKitCooldown(kit);
                        if (remaining > 0L && !player.isOp()) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You can not use the " + kit.getName() + " kit for " + DurationFormatUtils.formatDurationWords(remaining, true, true) + '.');
                        } else {
                            int maxUses = kit.getMaximumUses();
                            int currentlyUses = 0;//profile.getRemainingKitUsages(kit);
                            if (currentlyUses >= maxUses && maxUses != 2147483647) {
                                event.setCancelled(true);
                                player.sendMessage(ChatColor.RED + "You have already used this kit " + currentlyUses + '/' + maxUses + " times.");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.NORMAL
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign)state;
                Iterator var6 = this.plugin.getKitManager().getKits().iterator();

                while(var6.hasNext()) {
                    Kit kit = (Kit)var6.next();
                    if (sign.getLine(0).equals(ChatColor.BLUE + "- Kit -") && sign.getLine(1).equals(kit.getName())) {
                        boolean applied = kit.apply(player, false);
                        String[] fakeLines = (String[])Arrays.copyOf(sign.getLines(), 4);
                        if (applied) {
                            fakeLines[2] = "has been";
                            fakeLines[3] = "equipped.";
                        } else {
                            long remaining = 0L;//this.plugin.getUsersManager().getUserData(player.getUniqueId()).getRemainingKitCooldown(kit);
                            if (remaining > 0L) {
                                fakeLines[2] = "has " + DurationFormatUtils.formatDurationWords(remaining, true, true);
                                fakeLines[3] = "of cooldown.";
                            }
                        }

                        this.plugin.getNightmare().getAnimatedSignHandler().showLines(player, sign, fakeLines, 100L, true);
                    }
                }
            }
        }

    }
}
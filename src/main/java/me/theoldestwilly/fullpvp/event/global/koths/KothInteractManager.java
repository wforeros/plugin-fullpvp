package me.theoldestwilly.fullpvp.event.global.koths;

import java.util.Iterator;
import java.util.UUID;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothCapzone;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KothInteractManager {
    private FullPvP plugin;

    public KothInteractManager(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void checkPlayerAtKoth(Player player, Location from, Location to) {
        Iterator var4 = this.plugin.getGlobalEventsManager().getKothManager().getKothsList().values().iterator();

        while(var4.hasNext()) {
            KothEvent kothEvent = (KothEvent)var4.next();
            Cuboid cuboid = kothEvent.getProtectedArea().getArea();
            if (cuboid.contains(to) && !cuboid.contains(from)) {
                player.sendMessage(ChatColor.YELLOW + "You are now entering to " + ChatColor.GOLD + kothEvent.getName() + ChatColor.YELLOW + " koth.");
                break;
            }

            if (cuboid.contains(from) && !cuboid.contains(to)) {
                player.sendMessage(ChatColor.YELLOW + "You are now leaving " + ChatColor.RED + kothEvent.getName() + ChatColor.YELLOW + " koth.");
                break;
            }

            if (kothEvent.isActive()) {
                Cuboid cuboid1 = kothEvent.getKothCapzone().getProtectedArea().getArea();
                if (cuboid1.contains(to) && !cuboid1.contains(from)) {
                    player.sendMessage(ChatColor.YELLOW + "Entering to " + ChatColor.GOLD + kothEvent.getName() + ChatColor.YELLOW + " koth capzone.");
                    this.onPlayerCapzoneIteract(player, kothEvent, false);
                    break;
                }

                if (!cuboid1.contains(to) && cuboid1.contains(from)) {
                    player.sendMessage(ChatColor.YELLOW + "Leaving " + ChatColor.GOLD + kothEvent.getName() + ChatColor.YELLOW + " koth capzone.");
                    this.onPlayerCapzoneIteract(player, kothEvent, true);
                    break;
                }

                if (cuboid1.contains(to) && cuboid1.contains(from)) {
                    this.onPlayerCapzoneIteract(player, kothEvent, false);
                }
            }
        }

    }

    public void onPlayerCapzoneIteract(Player player, KothEvent kothEvent, Boolean leavingCapzone) {
        KothCapzone kothCapzone = kothEvent.getKothCapzone();
        if (kothCapzone.getCaper() == null && !leavingCapzone) {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
           if (!profile.hasPvpTimer() && !profile.isInStaffMode()) {
               kothCapzone.setCaper(player.getUniqueId());
               player.sendMessage(ChatColor.GREEN + "You are now capturing koth " + kothCapzone.getName() + ".");
               kothEvent.setStartTimeCaping(System.currentTimeMillis());
           }
        } else if (kothCapzone.getCaper() == player.getUniqueId() && leavingCapzone) {
            player.sendMessage(ChatColor.RED + "You are not longer capturing the koth " + ChatColor.GRAY + kothCapzone.getName() + ChatColor.RED + ".");
            kothCapzone.setCaper(null);
            kothEvent.setStartTimeCaping(null);
        }

    }

    public void onPlayerCaptureKoth(KothEvent koth) {
        if ((double)koth.getRemaining() <= 0.8D && this.plugin.getGlobalEventsManager().getKothManager().isKothActive()) {
            Player capper = Bukkit.getPlayer(koth.getKothCapzone().getCaper());
            Bukkit.broadcastMessage(ChatColor.GREEN + "The koth event " + ChatColor.YELLOW + koth.getName() + ChatColor.GREEN + " has been captured by " + ChatColor.YELLOW + "" + ChatColor.BOLD + capper.getName() + ChatColor.GREEN + ".");
            if (this.plugin.getGlobalEventsManager().getReward().size() != 0) {
                capper.getInventory().addItem((ItemStack[])this.plugin.getGlobalEventsManager().getReward().toArray(new ItemStack[0]));
            } else {
                Bukkit.getServer().getPlayer(koth.getKothCapzone().getCaper()).sendMessage(ChatColor.RED + "Reward not found, please contact an Administrator.");
            }
            this.plugin.getGlobalEventsManager().getKothManager().finishActiveKoth();
        }

    }
}
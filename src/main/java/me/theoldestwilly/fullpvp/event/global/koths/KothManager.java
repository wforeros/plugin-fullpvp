package me.theoldestwilly.fullpvp.event.global.koths;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.sk89q.worldedit.bukkit.selections.Selection;
import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothCapzone;
import me.theoldestwilly.fullpvp.event.global.koths.classes.KothEvent;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.cuboid.Cuboid;
import me.theoldestwilly.fullpvp.utilities.cuboid.CuboidDirection;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.ConvertFormat;
import me.theoldestwilly.fullpvp.world.ProtectedArea;
import me.theoldestwilly.fullpvp.world.claims.ClaimHandler;
import net.silexpvp.nightmare.util.GenericUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class KothManager {
    private FullPvP plugin;
    private Map<String, KothEvent> kothsList;
    private boolean kothActive;
    private KothEvent activeKoth;
    private String alias = "koths";
    private @Getter Long defaultKothDuration;
    private @Getter KothInteractManager kothInteractManager;
    private KothListener listener;

    public KothManager(FullPvP plugin, Config config) {
        this.plugin = plugin;
        this.defaultKothDuration = TimeUnit.MINUTES.toMillis(plugin.getConfig().getLong("koth-default-duration"));
        this.kothsList = new HashMap();
        this.kothActive = false;
        this.activeKoth = null;
        this.kothsTask();
        loadKothsAndRewards(config);
        kothInteractManager = new KothInteractManager(plugin);
    }

    public void loadKothsAndRewards(Config config) {
        Object object = config.get(alias);
        if (object != null && object instanceof List) {
            GenericUtils.createList(object, KothEvent.class).forEach(koth -> {
                kothsList.put(koth.getName(), koth);
            });
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + kothsList.size() + " Koths successfully loaded.");
        }
    }

    public void saveKoths(Config config) {
        config.set(alias, this.kothsList.values().toArray(new KothEvent[0]));
        config.save();
    }

    /*public void onDisable() {
        List<KothEvent> list = new ArrayList(this.getKothsList().values());
        if (!list.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("1");
            this.kothsDataYml.set("koths", list);
            this.kothsDataYml.set("rewards", this.reward);
        }

        DestroyTheCore dtc = this.plugin.getDtcHandler().getDestroyTheCoreEvent();
        if (dtc != null) {
            this.kothsDataYml.set("dtc", dtc);
        }

        this.saveKothsDataOnConfig();
    }*/

    public void kothsTask() {
        (new BukkitRunnable() {
            public void run() {
                if (KothManager.this.isKothActive()) {
                    KothManager.this.getKothInteractManager().onPlayerCaptureKoth(KothManager.this.getActiveKoth());
                }

            }
        }).runTaskTimerAsynchronously(this.plugin, 0L, 2L);
    }

    public void onKothCreate(Player player, String name, String[] duration) {
        if (this.plugin.getWorldEditPlugin().getSelection(player) == null) {
            player.sendMessage(ChatColor.RED + "Selection not found.");
        } else if (this.getByName(name) != null) {
            player.sendMessage(ChatColor.RED + "This koth already exists.");
        } else {
            Selection selection = this.plugin.getWorldEditPlugin().getSelection(player);
            Cuboid cuboid = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
            cuboid = cuboid.expand(CuboidDirection.UP, cuboid.getWorld().getMaxHeight());
            cuboid = cuboid.expand(CuboidDirection.DOWN, selection.getMinimumPoint().getBlockY());
            KothEvent kothEvent = new KothEvent(name, new ProtectedArea(cuboid));
            Long durationAsLong;
            if (duration != null) {
                durationAsLong = ConvertFormat.convertDelay2Long2(duration, player);
            } else {
                durationAsLong = this.getDefaultKothDuration();
            }
            kothEvent.setDuration(durationAsLong);
            this.getKothsList().put(kothEvent.getName(), kothEvent);
            player.sendMessage(ChatColor.BLUE + "You have created the koth: " + ChatColor.GREEN + kothEvent.getName() + ChatColor.BLUE + " with cap duration " + ChatColor.GREEN + DurationFormatUtils.formatDurationWords(kothEvent.getDuration(), true, true) + ChatColor.BLUE + " succesfully.");
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Remember to mark the 'capzone' koth with /koth setcapzone <koth> and the plugin will use your selection.");
        }
    }

    public void onSetCapZone(Player player, String name) {
        KothEvent kothEvent = this.getByName(name);
        if (kothEvent != null) {
            Selection selection = this.plugin.getWorldEditPlugin().getSelection(player);
            if (selection == null) {
                player.sendMessage(ChatColor.RED + "No selection found, please select the capzone with an axe.");
                return;
            }

            if (kothEvent.getProtectedArea().getArea().contains(selection.getMinimumPoint()) && kothEvent.getProtectedArea().getArea().contains(selection.getMaximumPoint())) {
                Cuboid cuboid = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
                kothEvent.setKothCapzone(name, cuboid);
                player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Koth capzone to '" + ChatColor.YELLOW + name + ChatColor.BLUE + ChatColor.BOLD + "'. Koth creation completed.");
            } else {
                player.sendMessage(ChatColor.RED + "The selection must be into the koth area to mark its  '" + ChatColor.YELLOW + "capzone" + ChatColor.RED + "'.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Koth '" + name + "' not found.");
        }

    }

    public void onDeleteKoth(Player player, String name) {
        KothEvent kothEvent = this.getByName(name);
        if (kothEvent != null) {
            if (this.getActiveKoth() != null && kothEvent.getName().equalsIgnoreCase(this.getActiveKoth().getName())) {
                this.onKothCancel(player);
            }

            this.getKothsList().remove(ChatColor.stripColor(name));
            player.sendMessage(ChatColor.GREEN + "The koth '" + ChatColor.RED + name + ChatColor.GREEN + "' has been eliminated.");
        } else {
            player.sendMessage(ChatColor.RED + "Koth " + name + " not found.");
        }

    }

    public void onReclaimKoth(Player player, String name) {
        if (this.getByName(name) != null) {
            if (this.plugin.getWorldEditPlugin().getSelection(player) == null) {
                player.sendMessage(ChatColor.RED + "Selection not found.");
                return;
            }

            Selection selection = this.plugin.getWorldEditPlugin().getSelection(player);
            Cuboid cuboid = new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint());
            cuboid = cuboid.expand(CuboidDirection.UP, cuboid.getWorld().getMaxHeight());
            cuboid = cuboid.expand(CuboidDirection.DOWN, selection.getMinimumPoint().getBlockY());
            ProtectedArea protectedArea = new ProtectedArea(cuboid);
            KothEvent kothEvent = this.getByName(name);
            kothEvent.setProtectedArea(protectedArea);
            player.sendMessage(ChatColor.GREEN + "You have successfully changed the protected area of koth " + kothEvent.getName() + ".");
            if (kothEvent.getKothCapzone() == null || !protectedArea.getArea().contains(kothEvent.getKothCapzone().getProtectedArea().getArea())) {
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Remember to mark the 'capzone' koth with /koth setcapzone <koth> and the plugin will use your selection.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Koth " + name + " not found.");
        }

    }

    /**
     *
     * @param sender If the event is scheduled use Bukkit.getConsoleSender()
     * @param name The koth name
     */
    public void onKothStart(CommandSender sender, String name) {
        KothEvent kothEvent = this.getByName(name);
        if (kothEvent != null) {
            if (this.isKothActive()) {
                sender.sendMessage(ChatColor.RED + "Currently there is an active koth, cancel it if you want to start this one.");
                return;
            }
            listener = new KothListener(plugin);
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            if (kothEvent.getKothCapzone() != null) {
                kothEvent.setActive(true);
                kothEvent.setStartTimeCaping(null);
                this.activeKothEvent(kothEvent);
                Bukkit.broadcastMessage(ChatColor.GREEN + "The koth " + ChatColor.RED + "" + ChatColor.BOLD + name + ChatColor.GREEN + " is now active! Use " + ChatColor.YELLOW + "/coords " + ChatColor.GREEN + "to see its ubication.");
                if (kothEvent.getKothCapzone().getProtectedArea().getArea().getPlayers().size() != 0) {
                    kothEvent.setStartTimeCaping(System.currentTimeMillis());
                    kothEvent.getKothCapzone().setCaper(kothEvent.getKothCapzone().getProtectedArea().getArea().getPlayer(0).getUniqueId());
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The koth " + ChatColor.YELLOW + name + ChatColor.GREEN + " is being captured.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Capzone not found for koth '" + ChatColor.GRAY + name + ChatColor.RED + "'.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Koth with name '" + ChatColor.GRAY + name + ChatColor.RED + "' not found.");
        }

    }

    public void onKothCancel(CommandSender sender) {
        KothEvent kothEvent = this.getActiveKoth();
        if (kothEvent == null) {
            sender.sendMessage(ChatColor.RED + "There is not an active koth.");
        } else {
            String name = kothEvent.getName();
            this.finishActiveKoth();
            this.disableKoth(kothEvent);
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "The koth " + name + " has been cancelled.");
        }
    }

    public void finishActiveKoth() {
        this.kothActive = false;
        this.activeKoth.setActive(false);
        this.activeKoth.getKothCapzone().setCaper(null);
        this.activeKoth = null;
        this.plugin.getGlobalEventsManager().onEventFinish();
        HandlerList.unregisterAll(listener);
        listener = null;
    }

    public void activeKothEvent(KothEvent kothEvent) {
        this.activeKoth = kothEvent;
        this.activeKoth.setActive(true);
        this.kothActive = true;
    }

    public void disableKoth(KothEvent kothEvent) {
        kothEvent.setActive(false);
        kothEvent.setStartTimeCaping((Long)null);
        kothEvent.getKothCapzone().setCaper((UUID)null);
    }

    public KothEvent getByName(String name) {
        Iterator var2 = this.getKothsList().values().iterator();

        KothEvent koth;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            koth = (KothEvent)var2.next();
        } while(!koth.getName().equalsIgnoreCase(name));

        return koth;
    }

    public List<String> getEventsName() {
        List<String> names = new ArrayList<>();
        kothsList.keySet().forEach(koth -> names.add(koth));
        return names;
    }

    public Map<String, KothEvent> getKothsList() {
        return this.kothsList;
    }

    public boolean isKothActive() {
        return this.kothActive;
    }

    public void setKothActive(boolean kothActive) {
        this.kothActive = kothActive;
    }

    public KothEvent getActiveKoth() {
        return this.activeKoth;
    }
}

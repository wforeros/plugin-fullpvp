package me.theoldestwilly.fullpvp.users.tutorial;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class TutorialHandler implements Listener {
    private FullPvP plugin;
    private Map<Integer, TutorialStep> tutorialPoints = new HashMap<>();
    private Map<UUID, TutorialTour> tutorialPlayers = new HashMap<>();

    public TutorialHandler(FullPvP plugin) {
        this.plugin = plugin;
        loadLocations();
    }

    public void loadLocations() {
        Object object = plugin.getConfig().get("tutorial.locations");
        if (object != null && object instanceof List) {
            int counter = 1;
            for (Object o : (List) object) {
                tutorialPoints.put(counter, new TutorialStep(o.toString(), counter));
                counter++;
            }
        }
    }

    public void startTutorial(Player player) {
        GameUtils.clearChat(player);
        player.sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + "Bienvenido al servidor, tendrás un tour en español con el fin de que entiendas la modalidad y la disfrutes!");
        tutorialPlayers.put(player.getUniqueId(), new TutorialTour(player));
    }

    public void removeTutorialPlayer (UUID uniqueId) {
        tutorialPlayers.remove(uniqueId);
    }

    public TutorialStep getNextTutorialStep(TutorialTour tour) {
        return tutorialPoints.get(tour.getLocationNumber());
    }

    /*@EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoin (PlayerJoinEvent event) {
        openTutorialInventory(event.getPlayer());
    }*/

    @EventHandler
    public void onPlayerPreprocessCommand(PlayerCommandPreprocessEvent event) {
        if (tutorialPlayers.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Lang.ERROR_COLOR + "You are not allowed to execute this command while in tutorial.");
        }
    }
    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory() != null && event.getInventory().getName().contains("Tutorial")) {
            int slot = event.getRawSlot();
            event.setCancelled(true);
            if (slot == 11) {
                player.closeInventory();
                startTutorial(player);
            } else if (slot == 15) {
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerCloseInventory (InventoryCloseEvent event) {
        if (event.getInventory() != null && event.getInventory().getTitle().contains("Tutorial")) {
            sendRecommendation((Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        if (tutorialPlayers.containsKey(event.getPlayer().getUniqueId())) {
            tutorialPlayers.get(event.getPlayer().getUniqueId()).finishTour();
        }
    }

    public void sendRecommendation (Player player) {
        player.sendMessage("Recuerda que si tienes alguna duda puedes usar los comandos /helpop o /help.");
    }

    public void openTutorialInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, ChatColor.GOLD.toString() + ChatColor.BOLD + "Tutorial");
        inventory.setItem(11, new ItemBuilder(Material.WOOL).setDurability(5).setDisplayName("&aIniciar Tour").setLore(Arrays.asList(new String[]{"&7Eres nuevo?", "&7Inicia un tour en el que", "&7se te explicarán conceptos básicos", "&7de la modalidad."})).build());
        inventory.setItem(15, new ItemBuilder(Material.WOOL).setDurability(14).setDisplayName("&cEmpezar a Jugar").setLore(Arrays.asList(new String[]{"&7Tienes experiencia en FullPvP?", "&7Empieza a disfrutar de la modalidad"})).build());
        player.openInventory(inventory);
    }
}

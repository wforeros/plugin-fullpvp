package me.theoldestwilly.fullpvp.users;


import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import lombok.Getter;
import lombok.Setter;
import me.theoldestwilly.fullpvp.chests.mapchest.ChestItem;
import me.theoldestwilly.fullpvp.chests.mapchest.MapChest;
import me.theoldestwilly.fullpvp.chests.mapchest.MapChestManager;
import me.theoldestwilly.fullpvp.kits.Kit;
import me.theoldestwilly.fullpvp.utilities.ItemStackSerializer;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.theoldestwilly.fullpvp.FullPvP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public final class ProfileHands {

    private final FullPvP plugin = FullPvP.getInstance();

    private final UUID uniqueId;
    @Setter private String name;

    private TObjectLongMap<UUID> delayedMapChests = new TObjectLongHashMap<>();
    private HashMap<UUID, Inventory> noDelayedItemsIntoMapChest = new HashMap<>();

    private @Setter Double balance, balanceBeforeLastReset;

    @Setter private int kills, deaths, killStreak, maximumKillStreak, lmsWins, kitsChestLevel;

    @Setter private boolean ignoringEventMessages, frozen;

    private boolean inStaffMode, vanished;

    private final TObjectIntMap<UUID> kitUseMap = new TObjectIntHashMap();
    private final TObjectLongMap<UUID> kitCooldownMap = new TObjectLongHashMap();

    private int freezeTaskId;

    private SavedInventory savedInventory;

    public ProfileHands(UUID uniqueId) {
        this.uniqueId = Preconditions.checkNotNull(uniqueId, "Unique ID cannot be null");
    }

    public ProfileHands(Document document) {
        this((UUID) document.get("_id"));

        name = document.getString("name");

        kitsChestLevel = document.getInteger("kits-chest", 1);
        balance = (balance = document.getDouble("balance")) != null ? balance : 0D;
        balanceBeforeLastReset = (balanceBeforeLastReset = document.getDouble("balance")) != null ? balanceBeforeLastReset : 0D;

        kills = document.getInteger("kills", 0);
        deaths = document.getInteger("deaths", 0);
        killStreak = document.getInteger("killStreak", 0);
        maximumKillStreak = document.getInteger("maximumkill-streak", 0);
        lmsWins = document.getInteger("lmsWins", 0);

        //ignoringEventMessages = document.getBoolean("ignoringEventMessages", false);
        //showCapzoneEntryAlerts = document.getBoolean("showCapzoneEntryAlerts", false);
        frozen = document.getBoolean("frozen", false);

        inStaffMode = document.getBoolean("staff-mode", false);
        vanished = document.getBoolean("vanished", false);


        if (document.containsKey("kitUsage") && document.get("kitUsage") instanceof String) {
            JsonArray array = new JsonParser().parse(document.getString("kitUsage")).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                kitUseMap.put(UUID.fromString(object.get("uniqueId").getAsString()), object.get("uses").getAsInt());
            }
        }

        if (document.containsKey("kitCooldown") && document.get("kitCooldown") instanceof String) {
            JsonArray array = new JsonParser().parse(document.getString("kitCooldown")).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                kitCooldownMap.put(UUID.fromString(object.get("uniqueId").getAsString()), object.get("remaining").getAsLong());
            }
        }

        if (document.containsKey("delayedMapChests") && document.get("delayedMapChests") instanceof String) {
            JsonArray array = new JsonParser().parse(document.getString("delayedMapChests")).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                delayedMapChests.put(UUID.fromString(object.get("uniqueId").getAsString()), object.get("remaining").getAsLong());
            }
        }

        if (document.containsKey("noDelayedChestItems")) {
            JsonArray array = new JsonParser().parse(document.getString("noDelayedChestItems")).getAsJsonArray();
            MapChest mapChest;
            MapChestManager manager = plugin.getMapChestManager();
            Inventory inventory;
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                UUID uniqueId = UUID.fromString(object.get("uniqueId").getAsString());
                List<ChestItem> list = new ArrayList<>();
                for (JsonElement element1 : object.get("noDelayedItems").getAsJsonArray()) {
                    JsonObject object1 = element1.getAsJsonObject();

                    //Primero va el slot luego el item stack
                    String[] itemString = object1.getAsString().split(", ");
                    list.add(new ChestItem(Integer.parseInt(itemString[0]),
                            ItemStackSerializer.deserialize(itemString[1])));
                }
                mapChest = manager.getMapChestByUniqueId(uniqueId);
                inventory = mapChest.createInventory();
                inventory = (GameUtils.setChestItemsIntoInventory(list.toArray(new ChestItem[0]), inventory));
                noDelayedItemsIntoMapChest.put(uniqueId, inventory);
            }
        }
    }

    public Document serialize() {
        Document document = new Document("_id", uniqueId);
        document.put("name", name);

        if (kills != 0) document.put("kills", kills);
        if (deaths != 0) document.put("deaths", deaths);
        if (killStreak != 0) document.put("killStreak", killStreak);
        if (maximumKillStreak != 0) document.put("maximumKillStreak", maximumKillStreak);
        // TODO: Reemplazar todo esto por un Sistema de Map con el nombre del Evento y la cantidad. Y al guardar si el total es mayor a 0 que setee totalEventWins
        if (lmsWins != 0) document.put("lmsWins", lmsWins);

        if (ignoringEventMessages) document.put("ignoringEventMessages", ignoringEventMessages);
        if (frozen) document.put("frozen", frozen);

        if (inStaffMode) document.put("inStaffMode", inStaffMode);
        if (vanished) document.put("vanished", vanished);

        JsonArray kitUsageArray = new JsonArray();
        kitUseMap.forEachEntry((uniqueId, uses) -> {
            JsonObject object = new JsonObject();
            object.addProperty("uniqueId", uniqueId.toString());
            object.addProperty("uses", uses);

            kitUsageArray.add(object);

            return true;
        });

        if (kitUsageArray.size() != 0) document.put("kitUsage", kitUsageArray.toString());

        JsonArray kitCooldownArray = new JsonArray();
        kitCooldownMap.forEachEntry((uniqueId, remaining) -> {
            JsonObject object = new JsonObject();
            object.addProperty("uniqueId", uniqueId.toString());
            object.addProperty("remaining", remaining);

            kitCooldownArray.add(object);

            return true;
        });

        if (kitCooldownArray.size() != 0) document.put("kitCooldown", kitCooldownArray.toString());

        JsonArray mapChestsCooldownArray = new JsonArray();
        delayedMapChests.forEachEntry((uniqueId, remaining) -> {
            JsonObject object = new JsonObject();
            object.addProperty("uniqueId", uniqueId.toString());
            object.addProperty("remaining", remaining);
            mapChestsCooldownArray.add(object);
            return true;
        });

        if (mapChestsCooldownArray.size() != 0) document.put("delayedMapChests", mapChestsCooldownArray.toString());

        JsonArray noDelayedChestItemsArray = new JsonArray();
        noDelayedItemsIntoMapChest.forEach((uniqueId, inventory) -> {
            JsonObject object = new JsonObject();
            object.addProperty("uniqueId", uniqueId.toString());
            String[] itemsAsString = GameUtils.serializeChestItemsArray(
                    GameUtils.convertContentsToChestItemsArray(inventory.getContents())
            );
            JsonArray array = new JsonArray();
            Stream.of(itemsAsString).forEach(item -> {
                JsonPrimitive primitive = new JsonPrimitive(item);
                array.add(primitive);
            });
            object.addProperty("noDelayedItems", array.toString());

            noDelayedChestItemsArray.add(object);

        });

        if (noDelayedChestItemsArray.size() != 0) document.put("noDelayedChestItems", noDelayedChestItemsArray.toString());

        return document;
    }

    public void save() {
        plugin.getProfileHandler().getProfilesDatabase().replaceOne(Filters.eq(uniqueId), serialize(), new UpdateOptions().upsert(true));
    }

    public void asyncSave() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::save);
    }

    public void startFreezeTask() {
        toPlayer().setWalkSpeed(0.0F);
        toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));
        toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
        toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128));

        freezeTaskId = new BukkitRunnable() {
            @Override
            public void run() {
                if (toPlayer() == null) {
                    cancel();
                    return;
                }

                toPlayer().sendMessage("");
                toPlayer().sendMessage(ChatColor.WHITE + "████" + ChatColor.RED + "█" + ChatColor.WHITE + "████");
                toPlayer().sendMessage(ChatColor.WHITE + "███" + ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█" + ChatColor.WHITE + "███" + ChatColor.DARK_RED + ChatColor.BOLD + " ATTENTION!");
                toPlayer().sendMessage(ChatColor.WHITE + "██" + ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.BLACK + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█" + ChatColor.WHITE + "██");
                toPlayer().sendMessage(ChatColor.WHITE + "██" + ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.BLACK + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█" + ChatColor.WHITE + "██" + ChatColor.RED + " You have been frozen by an Staff Member.");
                toPlayer().sendMessage(ChatColor.WHITE + "█" + ChatColor.RED + "█" + ChatColor.GOLD + "██" + ChatColor.BLACK + "█" + ChatColor.GOLD + "██" + ChatColor.RED + "█" + ChatColor.WHITE + "█" + ChatColor.RED + " If you log out, you will be permanently " + ChatColor.DARK_RED + ChatColor.BOLD + "BANNED" + ChatColor.RED + ".");
                toPlayer().sendMessage(ChatColor.WHITE + "█" + ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█" + ChatColor.WHITE + "█");
                toPlayer().sendMessage(ChatColor.RED + "█" + ChatColor.GOLD + "███" + ChatColor.BLACK + "█" + ChatColor.GOLD + "███" + ChatColor.RED + "█ Connect to our TeamSpeak " + ChatColor.GRAY + ChatColor.UNDERLINE + "ts.silexpvp.net" + ChatColor.RED + ".");
                toPlayer().sendMessage(ChatColor.RED + "█████████");
                toPlayer().sendMessage("");
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 3 * 20L).getTaskId();
    }

    public void setFrozen(boolean frozen) {
        if (toPlayer() != null) {
            if (plugin.getServer().getScheduler().isCurrentlyRunning(freezeTaskId)) {
                plugin.getServer().getScheduler().cancelTask(freezeTaskId);
            }

            if (frozen) {
                startFreezeTask();
            } else {
                toPlayer().setWalkSpeed(0.2F);
                toPlayer().removePotionEffect(PotionEffectType.SLOW);
                toPlayer().removePotionEffect(PotionEffectType.JUMP);
                toPlayer().removePotionEffect(PotionEffectType.BLINDNESS);

                toPlayer().sendMessage(ChatColor.GREEN + "You are not longer frozen.");
            }
        }

        this.frozen = frozen;
    }

    public void setInStaffMode(boolean inStaffMode) {
        this.inStaffMode = inStaffMode;

        if (toPlayer() != null) {
            toPlayer().setHealth(20D);
            toPlayer().setFoodLevel(20);
            toPlayer().setFireTicks(0);
            toPlayer().setFallDistance(0F);
            toPlayer().setSaturation(4.0F);

            toPlayer().getActivePotionEffects().forEach(effect -> toPlayer().removePotionEffect(effect.getType()));

            if (inStaffMode) {
                saveCurrentInventory();

                toPlayer().getInventory().setArmorContents(null);
                toPlayer().getInventory().clear();
                toPlayer().setItemOnCursor(null);

                toPlayer().setAllowFlight(true);

                /*toPlayer().getInventory().setItem(0, TELEPORATION_COMPASS_ITEM);
                toPlayer().getInventory().setItem(1, INVENTORY_VIEWER_ITEM);
                toPlayer().getInventory().setItem(2, RIDE_PLAYER_ITEM);
                toPlayer().getInventory().setItem(3, FREEZE_PLAYER_ITEM);
                toPlayer().getInventory().setItem(8, RANDOM_TELEPORTATION_ITEM);*/

                setVanished(true);
            } else {
                setVanished(false);
                toPlayer().getInventory().setArmorContents(null);
                toPlayer().getInventory().clear();
                toPlayer().setItemOnCursor(null);

                if (toPlayer().getGameMode() != GameMode.CREATIVE) {
                    toPlayer().setAllowFlight(false);
                }

                if (savedInventory != null) {
                    toPlayer().getInventory().setContents(savedInventory.getContents());
                    toPlayer().getInventory().setArmorContents(savedInventory.getArmor());
                } else {
                    toPlayer().sendMessage(ChatColor.RED + "An error occurred whilst attempting to rollback your saved inventory contents, please report this issue if this error consists.");
                }
            }

            toPlayer().updateInventory();
        }
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;

        if (toPlayer() != null) {
            if (vanished) {
                for (Player online : plugin.getServer().getOnlinePlayers()) {
                    if (toPlayer().equals(online) || online.hasPermission("nightmare.staff")) continue;

                    online.hidePlayer(toPlayer());
                }
            } else {
                for (Player online : plugin.getServer().getOnlinePlayers()) {
                    online.showPlayer(toPlayer());
                }
            }

            if (isInStaffMode()) {
                //toPlayer().getInventory().setItem(7, vanished ? VANISH_ON_ITEM : VANISH_OFF_ITEM);
            }
        }
    }

    public int getRemainingKitUsages(Kit kit) {
        int result = kitUseMap.get(kit.getUniqueId());
        return result == kitUseMap.getNoEntryValue() ? 0 : result;
    }

    public int incrementKitUsages(Kit kit) {
        return kitUseMap.adjustOrPutValue(kit.getUniqueId(), 1, 1);
    }

    public long getRemainingKitCooldown(Kit kit) {
        long remaining = kitCooldownMap.get(kit.getUniqueId());
        if (remaining == kitCooldownMap.getNoEntryValue()) return 0L;

        return remaining - System.currentTimeMillis();
    }

    public void updateKitCooldown(Kit kit) {
        kitCooldownMap.put(kit.getUniqueId(), System.currentTimeMillis() + kit.getCooldown());
    }

    public SavedInventory saveCurrentInventory() {
        return savedInventory = new SavedInventory();
    }

    public Player toPlayer() {
        return plugin.getServer().getPlayer(uniqueId);
    }

    public OfflinePlayer toOfflinePlayer() {
        return plugin.getServer().getOfflinePlayer(uniqueId);
    }

    @Getter
    public final class SavedInventory {

        private final ItemStack[] armor;
        private final ItemStack[] contents;

        public SavedInventory() {
            armor = toPlayer().getInventory().getArmorContents();
            contents = toPlayer().getInventory().getContents();
        }
    }
}
package me.theoldestwilly.fullpvp.profileviewer;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.clan.ClanMember;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ProfileViewerHandler implements Listener {
    private FullPvP plugin;
    private Config config;
    private String alias = "pvillagers";
    private List<ProfileVillager> profileVillagers = new ArrayList<>();

    public ProfileViewerHandler(FullPvP plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(ProfileVillager.class);
        config = new Config(plugin, "profilevillagers");
        loadVillagers();
    }

    public void loadVillagers() {
        Object object = this.config.get(alias);
        if (object != null && object instanceof List) {
            this.profileVillagers.addAll(GenericUtils.createList(object, ProfileVillager.class));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + ChatColor.YELLOW + profileVillagers.size() + " ProfileVillagers successfully loaded.");
        }
    }

    public void saveVillagers() {
        profileVillagers.forEach(villager -> villager.removeEntity());
        this.config.set(alias, this.profileVillagers);
        this.config.save();
    }

    public void spawnProfileVillager(Player player) {
        profileVillagers.add(new ProfileVillager(String.valueOf(profileVillagers.size() + 1), player.getLocation(), false));
        player.sendMessage(ChatColor.GREEN + "Villager created successfully.");
    }


    public ProfileVillager getProfileVillager(Villager entity) {
        return profileVillagers.stream().filter(profileVillager -> profileVillager.getVillager().equals(entity)).findFirst().orElse(null);
    }

    public ProfileVillager getProfileVillager(String name) {
        return profileVillagers.stream().filter(profileVillager -> profileVillager.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Inventory getProfileGui(Player player) {
        Inventory inventory = Bukkit.getServer().createInventory(player, 27, ChatColor.BLUE + "My Profile");
        inventory.setItem(11, (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner(player.getName()).setDisplayName("&a&lMy Statistics").setLore(Arrays.asList("&7View your statistics from", "&7your arrival to the server!")).build());
        //inventory.setItem(13, (new ItemBuilder(Material.CHEST)).setDisplayName("&a&lUpgraders").setLore(Arrays.asList("&7Check this to see available", "&7improvements")).build());
        inventory.setItem(15, (new ItemBuilder(Material.DIAMOND_SWORD)).setDisplayName("&a&lClan").setLore(Arrays.asList("&7Click to see your clan information")).build());
        return inventory;
    }

    public Inventory getEditorGui(Player player, ProfileVillager profileVillager) {
        Inventory inventory = Bukkit.getServer().createInventory(player, 27, ChatColor.DARK_GREEN + profileVillager.getName());
        inventory.setItem(10, (new ItemBuilder(Material.WOOL)).setDurability(14).setDisplayName("&c&lRemove").setLore(Arrays.asList("&7Remove this villager.")).build());
        boolean b = profileVillager.isShowingChallenges();
        inventory.setItem(16, (new ItemBuilder(Material.WOOL)).setDurability(b ? 5 : 14).setDisplayName("&e&lShowing Challenges " + (b ? "&aTrue":"&cFalse")).setLore(Arrays.asList("&7Click to modify the status.", "&7Feature under development.")).build());
        return inventory;
    }

    public Inventory getStatsGui(Player player) {
        Inventory inventory = Bukkit.getServer().createInventory(player, 27, ChatColor.AQUA + "My Statistics");
        Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        inventory.setItem(10, (new ItemBuilder(Material.PAPER)).setDisplayName("&a&lInformation").setLore(Arrays.asList("", "&f* &7Level: &f" + profile.getLevel(), "&f* &7Kills: &f" + profile.getKills(), "&f* &7Deaths&7: &f" + profile.getDeaths(), "&f* &7Killstreak&7: &f" + profile.getKillStreak(), "&f* &7Maximum Killstreak&7: &f" + profile.getMaximumKillStreak())).build());
        inventory.setItem(13, (new ItemBuilder(Material.WOOL)).setDurability(14).setDisplayName("&c&lGo Back").setLore(Arrays.asList("&7Previous page")).build());
        return inventory;
    }

    public Inventory getClansGui(Player player, Clan clan) {
        Inventory inventory = Bukkit.getServer().createInventory(player, 54, ChatColor.DARK_AQUA + "Clan " + clan.getName());
        inventory.addItem(new ItemStack[]{(new ItemBuilder(Material.WOOL)).setDurability(14).setDisplayName("&c&lGo Back").setLore(Arrays.asList("&7Previous page")).build()});
        inventory.setItem(4, (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner(clan.getLeader().getName()).setDisplayName("&9&l" + clan.getLeader().getName()).setLore(Arrays.asList("&7Leader and clan owner.")).build());
        ItemStack itemStack = (new ItemBuilder(Material.STAINED_GLASS_PANE)).setDurability(14).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Officers").setLore(Arrays.asList("&7In the next section you will find: ", "&7Clan officers.")).build();

        for(Integer i = 9; i < 18; i = i + 1) {
            inventory.setItem(i, itemStack);
        }

        int counter = 18;
        Iterator var6 = clan.getOfficers().iterator();

        ClanMember clanMember;
        String name;
        while(var6.hasNext()) {
            clanMember = (ClanMember)var6.next();
            name = clanMember.getName();
            inventory.setItem(counter, (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner(name).setDisplayName("&3&l" + name).setLore(Arrays.asList("&7Clan officer: ", "&7He/She can invite and kick players.")).build());
            if (counter < 27) {
                ++counter;
            } else {
                break;
            }
        }

        itemStack = (new ItemBuilder(Material.STAINED_GLASS_PANE)).setDurability(14).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Members").setLore(Arrays.asList("&7In the next section you will find: ", "&7Clan members.")).build();

        for(Integer i = 27; i < 36; i = i + 1) {
            inventory.setItem(i, itemStack);
        }

        counter = 36;
        var6 = clan.getMembers().iterator();

        while(var6.hasNext()) {
            clanMember = (ClanMember)var6.next();
            name = clanMember.getName();
            inventory.setItem(counter, (new ItemBuilder(Material.SKULL_ITEM)).setDurability(3).setOwner(name).setDisplayName("&b&l" + name).setLore(Arrays.asList("&7Clan Member.")).build());
            if (counter < 45) {
                ++counter;
            } else {
                break;
            }
        }

        inventory.setItem(47, (new ItemBuilder(Material.DIAMOND_SWORD)).setDisplayName("&a&l" + clan.getName() + " &7[LvL. " + clan.getLevel() + "&7]").setLore(Arrays.asList(" &7Kills: &f" + clan.getClanKills(), " &7Deaths: &f" + clan.getClanDeaths(), " &7K/D&7: &f" + clan.getKd(), " &7Members: &f" + clan.getMembersNumber())).build());
        inventory.setItem(51, (new ItemBuilder(Material.BOW)).setDisplayName(clan.getLevel() < 10 ? "&a&lNext Level&7: &f" + (clan.getLevel() + 1) : "&a&lMaximum Level&7: &f10").setLore(Arrays.asList(" &7Advantages: ", " " + this.plugin.getClanManager().levelInfo(clan.getLevel() + 1), "", clan.getLevel() < 10 ? " &7Cost: &f$" + this.plugin.getClanManager().getNextLevelCost(clan.getLevel()) : "")).build());
        return inventory;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            Villager villager = (Villager)event.getRightClicked();
            String villagerName = ChatColor.stripColor(villager.getCustomName());
            if (villagerName != null && villagerName.equals("MY PROFILE")) {
                Player player = event.getPlayer();
                if (!player.isSneaking() || !player.isOp()) {
                    player.openInventory(getProfileGui(player));
                    player.sendMessage(ChatColor.YELLOW + "Welcome to the statistics menu!");
                    event.setCancelled(true);
                } else {
                    ProfileVillager profileVillager = getProfileVillager(villager);
                    if (profileVillager != null) {
                        player.openInventory(getEditorGui(player, profileVillager));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getCurrentItem() != null) {
            Inventory inventory = event.getInventory();
            Player player = (Player)event.getWhoClicked();
            Integer rawSlot = event.getRawSlot();
            String inventoryTitle = ChatColor.stripColor(inventory.getTitle());
            if (inventoryTitle.equals("My Profile")) {
                event.setCancelled(true);
                if (11 == rawSlot) {
                    player.openInventory(getStatsGui(player));
                } else if (15 == rawSlot) {
                    Clan clan = this.plugin.getClanManager().getPlayersClan(player);
                    if (clan != null) {
                        player.openInventory(getClansGui(player, clan));
                    } else {
                        player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
                    }
                } else if (22 == rawSlot) {
                    player.closeInventory();
                }
            } else if (inventoryTitle.equals("My Statistics")) {
                event.setCancelled(true);
                if (13 == rawSlot) {
                    player.openInventory(getProfileGui(player));
                }
            } else if (inventoryTitle.contains("Clan ")) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                if (slot == 0) {
                    player.openInventory(getProfileGui(player));
                } else if (slot == 51) {
                    Clan clan = this.plugin.getClanManager().getPlayersClan(player);
                    if (clan != null) {
                        if (clan.getLeader().getUniqueID().equals(player.getUniqueId())) {
                            if (clan.getLevel() < 10) {
                                int lvlCost = this.plugin.getClanManager().getNextLevelCost(clan.getLevel());
                                double balance = clan.getClanBalance();
                                if (balance >= (double)lvlCost) {
                                    clan.rankUp();
                                    clan.setClanBalance(balance - (double)lvlCost);
                                    Iterator var10 = clan.getAllMembers().iterator();

                                    while(var10.hasNext()) {
                                        ClanMember clanMember = (ClanMember)var10.next();
                                        Player player1 = Bukkit.getServer().getPlayer(clanMember.getUniqueID());
                                        if (player1 != null) {
                                            player1.sendMessage(ChatColor.YELLOW + "Your clan has increased to level: " + ChatColor.GREEN + clan.getLevel() + ChatColor.YELLOW + ".");
                                        }
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Clan money is not enough to increase the level.");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Your clan has reached the maximum level.");
                            }

                            player.closeInventory();
                        } else {
                            player.sendMessage(ChatColor.RED + "Only the clan leader can execute this action.");
                            player.closeInventory();
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You are not in a clan.");
                    }
                }
            } else if (inventoryTitle.contains("Villager #")) {
                ProfileVillager profileVillager = getProfileVillager(inventoryTitle);
                if (profileVillager != null) {
                    event.setCancelled(true);
                    if (rawSlot == 10) {
                        profileVillager.removeEntity();
                        profileVillagers.remove(profileVillager);
                        player.sendMessage(Lang.SUCCES_PROFILEVILLAGER_REMOVED);
                        player.closeInventory();
                    } else if (rawSlot == 16) {
                        profileVillager.updateViewerStatusWithMessage(player);
                        player.closeInventory();
                    }
                } else {
                    player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE_VILLAGER_BY_NAME) );
                }
            }
        }

    }
}

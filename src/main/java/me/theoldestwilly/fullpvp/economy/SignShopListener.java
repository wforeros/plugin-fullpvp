package me.theoldestwilly.fullpvp.economy;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.UUID;

public class SignShopListener implements Listener {
    private FullPvP plugin;

    public SignShopListener(FullPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onSignCreate(SignChangeEvent event) {
        String line3;
        if (event.getLine(0) != null && (event.getLine(0).equals("[fBuy]") || event.getLine(0).equals("[fSell]"))) {
            if (event.getPlayer().hasPermission("fullpvp.shop.sign")) {
                long elapsed = System.currentTimeMillis();
                line3 = event.getLine(1);
                String subIndex = "";
                if (line3.contains(":")) {
                    String[] args = line3.split(":");
                    line3 = args[0];
                    subIndex = args[1];
                }

                if (subIndex.equals("") && Material.getMaterial(Integer.parseInt(line3)) != null || JavaUtils.tryParseInt(line3) != null && JavaUtils.tryParseInt(subIndex) != null) {
                    if (ChatColor.stripColor(event.getLine(0)).equals("[fBuy]")) {
                        event.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.BOLD + "" + ChatColor.GREEN + "Buy" + ChatColor.DARK_GRAY + "]");
                    } else if (ChatColor.stripColor(event.getLine(0)).equals("[fSell]")) {
                        event.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.BOLD + "" + ChatColor.BLUE + "Sell" + ChatColor.DARK_GRAY + "]");
                    }

                    if (subIndex.equals("")) {
                        event.setLine(1, JavaUtils.formalFormat((new ItemStack(Material.getMaterial(Integer.parseInt(event.getLine(1))))).getType().toString().toLowerCase().replace("_", " ")));
                    }

                    event.setLine(3, "$ " + event.getLine(3));
                    event.getPlayer().sendMessage(TextUtils.formatColor("&eSign shop successfully created."));
                } else {
                    event.getPlayer().sendMessage(TextUtils.formatColor("&cError while creating Sign Shop: Invalid values."));
                    event.setCancelled(true);
                }

                if (this.plugin.isDebugEnabled()) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Sign created in " + (System.currentTimeMillis() - elapsed) + " ms.");
                }
            }
        } else if (event.getLine(0) != null && event.getLine(0).equals("[fRepair]") && event.getLine(1) != null && event.getPlayer().hasPermission("fullpvp.repair.sign")) {
            String line2 = event.getLine(1);
            Player player = event.getPlayer();
            if (line2.equalsIgnoreCase("Hand")) {
                line2 = ChatColor.BLACK + "Hand";
            } else {
                if (!line2.equalsIgnoreCase("All")) {
                    player.sendMessage(ChatColor.RED + "Error while creating repair sign: Invalid option. Available options: Hand / All");
                    event.setCancelled(true);
                    return;
                }

                line2 = ChatColor.BLACK + "All";
            }

            line3 = event.getLine(2);
            if (line3 != null) {
                Integer integer = JavaUtils.tryParseInt(line3);
                if (integer == null) {
                    event.setCancelled(true);
                    player.sendRawMessage(ChatColor.RED + "Error while creating repair sign: Invalid cost.");
                } else {
                    event.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.DARK_BLUE + "Repair" + ChatColor.DARK_GRAY + "]");
                    event.setLine(1, line2);
                    event.setLine(2, ChatColor.BLACK + "$ " + integer);
                    player.sendMessage(ChatColor.GREEN + "Repair sign created!");
                }
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)) {
            Sign sign = (Sign)event.getClickedBlock().getState();
            Player player;
            if (!ChatColor.stripColor(sign.getLine(0)).equals("[Buy]") && !ChatColor.stripColor(sign.getLine(0)).equals("[Sell]")) {
                if (ChatColor.stripColor(sign.getLine(0)).equals("[Repair]")) {
                    player = event.getPlayer();
                    String line2 = ChatColor.stripColor(sign.getLine(1));
                    if (line2 == null) {
                        return;
                    }

                    Integer cost = JavaUtils.tryParseInt(ChatColor.stripColor(sign.getLine(2).split(" ")[1]));
                    if (cost != null) {
                        Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                        if (!profile.hasEnoughtMoneyToPay(cost.doubleValue())) {
                            player.sendMessage(ChatColor.RED + "You do not have enought money to use this option.");
                            return;
                        }

                        if (line2.equalsIgnoreCase("Hand")) {
                            ItemStack item = player.getItemInHand();
                            Material type = item.getType();
                            if (item.getDurability() == 0 || type == Material.POTION && type == Material.GOLDEN_APPLE) {
                                player.sendMessage(ChatColor.RED + "Could not repair: This item is not worn.");
                            } else {
                                player.getItemInHand().setDurability((short)0);
                                profile.removeMoneyFromBalance((double)cost);
                                player.sendMessage(ChatColor.YELLOW + "Your item has been repaired for $ " + cost + ".");
                            }
                        } else if (line2.equalsIgnoreCase("All")) {
                            Material type = null;
                            ItemStack[] var23 = player.getInventory().getContents();
                            int var24 = var23.length;

                            int var25;
                            ItemStack itemStack;
                            for(var25 = 0; var25 < var24; ++var25) {
                                itemStack = var23[var25];
                                if (itemStack != null && itemStack.getType() != Material.AIR) {
                                    type = itemStack.getType();
                                }

                                if (type != Material.POTION && type != Material.GOLDEN_APPLE) {
                                    try {
                                        itemStack.setDurability((short)0);
                                    } catch (NullPointerException var17) {
                                        ;
                                    }
                                }
                            }

                            var23 = player.getInventory().getArmorContents();
                            var24 = var23.length;

                            for(var25 = 0; var25 < var24; ++var25) {
                                itemStack = var23[var25];
                                if (itemStack != null && itemStack.getType() != Material.AIR) {
                                    type = itemStack.getType();

                                    try {
                                        itemStack.setDurability((short)0);
                                    } catch (NullPointerException var16) {
                                        ;
                                    }
                                }
                            }

                            player.updateInventory();
                            profile.removeMoneyFromBalance((double)cost);
                            player.sendMessage(ChatColor.YELLOW + "All your items have been repaired.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Option not found, contact an administrator.");
                    }
                }
            } else {
                player = event.getPlayer();

                Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                Double bal = profile.getBalance();
                String[] stringCost = sign.getLine(3).split(" ");
                if (stringCost.length < 2) return;
                Integer itemCost = Integer.parseInt(stringCost[1]);
                if (bal < (double)itemCost && ChatColor.stripColor(sign.getLine(0)).equals("[Buy]")) {
                    player.sendMessage(ChatColor.RED + "You do not have enough money to buy this item.");
                    return;
                }

                String materialStr = sign.getLine(1);
                String[] itemSplit = null;
                Material material;
                if (materialStr.contains(":")) {
                    itemSplit = materialStr.split(":");
                    material = Material.getMaterial(Integer.parseInt(itemSplit[0]));
                } else {
                    material = Material.getMaterial(materialStr.toUpperCase().replace(" ", "_"));
                }

                if (ChatColor.stripColor(sign.getLine(0)).equals("[Sell]")) {
                    if (!player.getInventory().contains(material)) {
                        player.sendMessage(ChatColor.RED + "This item has not been found in your inventory for sale.");
                        return;
                    }

                    ItemStack[] var30;
                    int var32;
                    int var36;
                    ItemStack item;
                    if (itemSplit != null) {
                        int subId = Integer.parseInt(itemSplit[1]);
                        boolean hasMaterial = false;
                        var30 = player.getInventory().getContents();
                        var32 = var30.length;

                        for(var36 = 0; var36 < var32; ++var36) {
                            item = var30[var36];
                            if (item != null && item.getType() == material && item.getDurability() == subId) {
                                hasMaterial = true;
                                break;
                            }
                        }

                        if (!hasMaterial) {
                            player.sendMessage(ChatColor.RED + "This item has not been found in your inventory for sale.");
                            return;
                        }
                    }

                    boolean itemRenamed = false;
                    int amount = 0;
                    var30 = player.getInventory().getContents();
                    var32 = var30.length;

                    for(var36 = 0; var36 < var32; ++var36) {
                        item = var30[var36];
                        if (item != null && item.getType() == material) {
                            if (item.getItemMeta().hasDisplayName()) {
                                itemRenamed = true;
                            }

                            amount += item.getAmount();
                        }
                    }

                    if (!itemRenamed) {
                        ItemBuilder itemBuilder;
                        if (amount >= 16) {
                            itemBuilder = (new ItemBuilder(material)).setAmount(Integer.parseInt(sign.getLine(2)));
                            if (itemSplit != null) {
                                itemBuilder.setDurability(Integer.parseInt(itemSplit[1]));
                            }

                            player.getInventory().removeItem(new ItemStack[]{itemBuilder.build()});
                        } else {
                            itemBuilder = (new ItemBuilder(material)).setAmount(amount);
                            if (itemSplit != null) {
                                itemBuilder.setDurability(Integer.parseInt(itemSplit[1]));
                            }

                            player.getInventory().removeItem(new ItemStack[]{itemBuilder.build()});
                            itemCost = itemCost * amount / Integer.parseInt(sign.getLine(2));
                        }

                        player.updateInventory();
                        profile.addMoneyToBalance((double)itemCost);
                        player.sendMessage(ChatColor.YELLOW + "You earned " + ChatColor.RED + "$" + itemCost + ChatColor.YELLOW + " by selling your items.");
                        return;
                    }


                    player.sendMessage(ChatColor.RED + "An error has ocurred! Do not rename your items for sale!");
                    return;
                }

                if (ChatColor.stripColor(sign.getLine(0)).equals("[Buy]")) {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ChatColor.RED + "You do not have free slots in your inventory.");
                        return;
                    }

                    String item = sign.getLine(1);
                    ItemStack targetItem = null;
                    if (item.contains(":")) {
                        String[] args = item.split(":");
                        item = args[0];
                        String subIndex = args[1];
                        Integer itemID = Integer.parseInt(item);
                        Integer subIndexID = Integer.parseInt(subIndex);
                        targetItem = (new ItemBuilder(Material.getMaterial(itemID))).setDurability(subIndexID).setAmount(Integer.parseInt(sign.getLine(2))).build();
                    } else {
                        targetItem = new ItemStack(material, Integer.parseInt(sign.getLine(2)));
                    }
                    player.getInventory().addItem(new ItemStack[]{targetItem});
                    player.updateInventory();
                    profile.removeMoneyFromBalance((double)itemCost);
                    player.sendMessage(ChatColor.YELLOW + "The purchase has been completed with a total cost of: " + ChatColor.RED + "$" + itemCost + ChatColor.YELLOW + ".");
                    return;
                }

                if (ChatColor.stripColor(sign.getLine(0)).equals("[Sell]")) {
                    ;
                }
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onEnchantShopCreate(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("fullpvp.enchant.sign")) {
            long elapsed = System.currentTimeMillis();
            if (event.getLine(0).equals("[fEnchant]")) {
                if (Enchantment.getByName(event.getLine(1)) == null && Enchantment.getById(Integer.parseInt(event.getLine(1))) == null) {
                    event.getPlayer().sendMessage(TextUtils.formatColor("&cError while creating Sign Shop: Invalid values."));
                    event.setCancelled(true);
                } else {
                    event.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "Enchant" + ChatColor.DARK_GRAY + "]");
                    String enchantName = event.getLine(1).toLowerCase().replace("_", " ");
                    String prettyName = "";
                    enchantName = JavaUtils.formalFormat(enchantName);
                    Integer id = JavaUtils.tryParseInt(enchantName);
                    if (id != null && this.isInteger(enchantName, 1)) {
                        if (id != 0) {
                            String enchantment = Enchantment.getById(Integer.parseInt(enchantName)).getName().replace("_", " ");
                            event.setLine(1, JavaUtils.formalFormat(enchantment));
                        } else {
                            event.setLine(1, "Protection");
                        }
                    }

                    event.setLine(3, "$ " + event.getLine(3));
                    event.getPlayer().sendMessage(TextUtils.formatColor("&eEnchant sign shop successfully created."));
                }
            }

            if (this.plugin.isDebugEnabled()) {
                event.getPlayer().sendMessage(ChatColor.GREEN + "Sign created in " + (System.currentTimeMillis() - elapsed) + " ms.");
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onEnchantShopPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)) {
            Sign sign = (Sign)event.getClickedBlock().getState();
            if (ChatColor.stripColor(sign.getLine(0)).equals("[Enchant]")) {
                Player player = event.getPlayer();
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    player.sendMessage(ChatColor.RED + "You must have an item in your hand to complete this action.");
                    return;
                }

                Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                Double bal = profile.getBalance();
                String[] stringCost = sign.getLine(3).split(" ");
                Integer enchantCost = Integer.parseInt(stringCost[1]);
                if (bal < (double)enchantCost) {
                    player.sendMessage(ChatColor.RED + "You do not have enough money to buy this enchantment.");
                    return;
                }

                ItemStack itemStack = event.getItem();
                if (this.isInteger(sign.getLine(1), 1)) {
                    if (itemStack.containsEnchantment(Enchantment.getById(Integer.parseInt(sign.getLine(1))))) {
                        player.sendMessage(ChatColor.RED + "This item already has this enchantment.");
                        return;
                    }

                    if (Enchantment.getById(Integer.parseInt(sign.getLine(1))).canEnchantItem(itemStack)) {
                        itemStack.addEnchantment(Enchantment.getById(Integer.parseInt(sign.getLine(1))), Integer.parseInt(sign.getLine(2)));
                        player.sendMessage(ChatColor.GREEN + "This enchantment is now in your item!");
                        profile.removeMoneyFromBalance((double)enchantCost);
                    } else {
                        player.sendMessage(ChatColor.RED + "The enchantment can not be applied to this item.");
                    }
                } else {
                    String enchant = sign.getLine(1);
                    if (enchant.equals(ChatColor.stripColor("Protection"))) {
                        enchant = "PROTECTION_ENVIRONMENTAL";
                    }

                    enchant = enchant.toUpperCase().replace(" ", "_");
                    if (itemStack.containsEnchantment(Enchantment.getByName(enchant))) {
                        player.sendMessage(ChatColor.RED + "This item already has this enchantment.");
                        return;
                    }

                    if (Enchantment.getByName(enchant).canEnchantItem(itemStack)) {
                        itemStack.addUnsafeEnchantment(Enchantment.getByName(enchant), Integer.parseInt(sign.getLine(2)));
                        player.sendMessage(ChatColor.GREEN + "This enchantment is now in your item by " + ChatColor.RED + "$ " + enchantCost + ChatColor.GREEN + ".");
                        profile.removeMoneyFromBalance((double)enchantCost);
                    } else {
                        player.sendMessage(ChatColor.RED + "The enchantment can not be applied to this item.");
                    }
                }
            }
        }

    }

    public boolean isInteger(String string, Integer maxDigits) {
        if (string.length() == maxDigits) {
            char[] var3 = string.toCharArray();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                char c = var3[var5];
                if (!Character.isDigit(c)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}


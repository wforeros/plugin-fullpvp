package me.theoldestwilly.fullpvp.economy;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class EconomyManager {
    private FullPvP plugin;

    public EconomyManager(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void checkOwnBalance(Player player) {
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (profile != null) {
            player.sendMessage(Lang.SHOW_OWN_BALANCE(profile.getDisplayBalance()));
        } else {
            player.sendMessage(Lang.ERROR_LOADING_PROFILE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (Economy Manager)");
        }
    }

    public void checkTargetsBalance(CommandSender sender, OfflinePlayer target) {
        Profile profile = plugin.getProfileHandler().getOfflineProfile(target, false);
        if (profile != null) {
            sender.sendMessage(Lang.SHOW_TARGET_BALANCE(target.getName(), profile.getDisplayBalance()));
        } else {
            sender.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
        }
    }

    public void deposit(Player receiver, Double amount) {
        if (amount != null && amount < 0) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Failed to deposit money because the value is invalid.");
            return;
        }
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(receiver.getUniqueId());
        if (profile != null) {
            profile.addMoneyToBalance(amount);
        }
    }

    public void sendMoneyToOtherPlayer(Player sender, OfflinePlayer receiver, Double amount) {
        if (amount <= 0) {
            sender.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
            return;
        }
        if (sender.getUniqueId().equals(receiver.getUniqueId())) {
            sender.sendMessage(Lang.ERROR_TARGET_IS_SAME_SENDER);
            return;
        }
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(sender.getUniqueId());
        if (profile != null) {
            if (profile.hasEnoughtMoneyToPay(amount)) {
                Profile pReceiver = plugin.getProfileHandler().getOfflineProfile(receiver, false);
                if (pReceiver != null) {
                    profile.removeMoneyFromBalance(amount);
                    pReceiver.addMoneyToBalance(amount);
                    String receiverName;
                    if (receiver.isOnline()) {
                        receiverName = ((Player)receiver).getDisplayName();
                        receiver.getPlayer().sendMessage(Lang.SUCCESS_ECONOMY_TRANSACTION_RECEIVER(sender.getName(), amount));
                    } else {
                        pReceiver.asyncSave();
                        receiverName = pReceiver.getName();
                    }
                    sender.sendMessage(Lang.SUCCESS_ECONOMY_TRANSACTION_SENDER(receiverName, amount));
                }
            } else {
                sender.sendMessage(Lang.ERROR_ECONOMY_INSUFFICENT_MONEY);
            }
        } else {
            sender.sendMessage(Lang.ERROR_LOADING_PROFILE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (Economy Manager #2)");
        }
    }

    public void setMoneyToTargetPlayer (CommandSender sender, OfflinePlayer target, Double amount) {
        if (target == null) {
            sender.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!PermissionsManager.hasEconomyAdministratorPermission(player)) {
                player.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
                return;
            }
        }
        if (target != null) {
            Profile profile = plugin.getProfileHandler().getOfflineProfile(target, false);
            if (profile != null) {
                String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : ChatColor.RED + "CONSOLE";
                if (amount > 0D) {
                    profile.setBalance(amount);
                    sender.sendMessage(Lang.SUCCESS_ECONOMY_BALANCE_SET_VALUE_SENDER(target.getName(), amount));
                    if (target.isOnline()) {
                        ((Player)target).sendMessage(Lang.SUCCESS_ECONOMY_BALANCE_SET_VALUE_TARGET(senderName, amount));
                    } else {
                        profile.asyncSave();
                    }
                } else  {
                    profile.resetBalance();
                    sender.sendMessage(Lang.SUCCES_ECONOMY_BALANCE_RESET_SENDER(target.getName()));
                    if (target.isOnline()) {
                        ((Player)target).sendMessage(Lang.SUCCES_ECONOMY_BALANCE_RESET_TARGET(senderName));
                    }
                }
            } else {
                sender.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
            }
        } else {
            sender.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
        }
    }

}

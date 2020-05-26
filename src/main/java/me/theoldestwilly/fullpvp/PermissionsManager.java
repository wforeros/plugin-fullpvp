package me.theoldestwilly.fullpvp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PermissionsManager {
    private static String permissionNode = "fullpvp.";

    public static String ADMIN_KIT_PERM = permissionNode + "command.kit.permission.admin";

    public static boolean hasChatColorPermission(Player player) {
        return player.hasPermission("fullpvp.chat.color");
    }
    public static boolean hasStaffPermission(Player player) {
        return player.hasPermission(permissionNode + "command.staffmode");
    }

    public static boolean hasMapChestManagePermission(Player player) {
        return player.hasPermission(permissionNode + "manage.chests");
    }

    public static boolean hasEventGameHostPermission(Player player, String type) {
        return player.hasPermission(permissionNode + "command.eventgame.argument.start.type." + type.toLowerCase());
    }

    public static boolean hasEventCooldownBypassPermission(Player player, String type) {
        return player.hasPermission(permissionNode + "hostedevent." + type.toLowerCase() + ".cooldown.bypass");
    }

    public static boolean hasEventGameAdminPermission(Player player) {
        return player.hasPermission(permissionNode + "command.eventgame.permission.admin");
    }

    public static boolean hasEventCancelPermission(Player player) {
        return player.hasPermission(permissionNode + "command.eventgame.argument.cancel");
    }

    public static boolean hasDonatorPermission(Player player) {
        return player.hasPermission(permissionNode + "rank.donator.allow.fly");
    }

    public static boolean hasQiaqPerm(Player player) {
        return player.hasPermission(permissionNode + "rank.qiaq");
    }
    //kain ya//qiaq ya
    //foses ya
    //trojan ya


    public static boolean hasFosesPerm(Player player) {
        return player.hasPermission(permissionNode + "rank.foses");
    }

    public static boolean hasTrojanPerm(Player player) {
        return player.hasPermission(permissionNode + "rank.trojan");
    }

    public static boolean hasKainPerm(Player player) {
        return player.hasPermission(permissionNode + "rank.kain");
    }

    public static boolean hasBolqPerm(Player player) {
        return player.hasPermission(permissionNode + "rank.bolq");
    }

    public static boolean hasBagPermission(Player player, Integer bagNumber) {
        if (bagNumber > 20) {
            player.sendMessage(ChatColor.RED + "This value exceeds the maximum bags number allowed.");
            return false;
        } else if (bagNumber <= 0) {
            player.sendMessage(ChatColor.RED + "Invalid value.");
            return false;
        } else {
            for(int i = 1; i < 20; ++i) {
                if ((player.isOp() || player.hasPermission(permissionNode + "bags.ammount." + i)) && i >= bagNumber) {
                    return true;
                }
            }
            return false;
        }
    }

    /*
    Economy
     */
    public static boolean hasEconomyAdministratorPermission(Player player) {
        return player.hasPermission(permissionNode + "economy.admin");
    }
}

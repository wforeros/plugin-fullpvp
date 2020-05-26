package me.theoldestwilly.fullpvp;

import me.theoldestwilly.fullpvp.chests.mapchest.MapChest;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;

public class Lang {

    public static String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "FullPvP" + ChatColor.GRAY + "] " ;
    public static String ERROR_COLOR = ChatColor.RED.toString();
    public static String SUCCESS_COLOR = ChatColor.YELLOW.toString();
    private static String GRAY = ChatColor.GRAY.toString();
    //Initial messages
    public static String ON_COMMAND_REGISTER(String command) {
        return PREFIX + ChatColor.GREEN +  "The command " + command + " has been successfully registered";
    }

    //Global error messages
    public static String ERROR_NOT_LOOKING_REQUIRED_OBJECT(String object) {
        return PREFIX + ChatColor.RED + "You are not looking at a " + object + ".";
    }

    public static String ERROR_EXCEEDED_CHARACTERS_LIMIT = ERROR_COLOR + "You have exceeded the character limit for the name " + ChatColor.GRAY + "(10)" + ChatColor.RED + ".";
    public static String ERROR_INSUFFICENT_CHARACTERS = ERROR_COLOR + "The name must have at least three characters.";


    public static String ERROR_GLOBAL_PLAYER_NOT_ONLINE = ChatColor.RED + "That player is not online.";
    public static String ERROR_TARGET_IS_SAME_SENDER = ERROR_COLOR + "You can not do this action with your self.";
    public static String ERROR_INSUFFICENT_PERMISSIONS = ERROR_COLOR + "Your are not allowed to execute this command.";
    public static String ERROR_GLOBAL_DETECTED_OBJECT_WITH_THAT_NAME = ERROR_COLOR + "There is an object with the same name saved.";
    public static String ERROR_GLOBAL_INVALID_CHARACTERS = ChatColor.RED + "Invalid characters found.";

    public static String ERROR_CHEST_IS_EMPTY = PREFIX + ERROR_COLOR + "The chest you are looking at is empty.";
    public static String CONSOLE_NOT_ALLOWED = PREFIX + ERROR_COLOR + "You can not execute this command on console.";
    public static String ERROR_DB_ALREADY_CONTAINS_OBJECT = ERROR_COLOR + "The database already contains this object.";
    public static String ERROR_DB_DOESNT_CONTAINS_OBJECT = ERROR_COLOR + "The database does not contain this chest.";
    public static String ERROR_PLAYER_HASNT_PLAYED_BEFORE = ERROR_COLOR + "The player has not played before.";
    public static String ERROR_LOADING_PROFILE (PluginError error) {
        return ERROR_COLOR + "There was a problem while loading your profile, report it to the staff with error number #" + error.getErrorNumber() + ".";
    }
    public static String ERROR_COMMON_MESSAGE(PluginError error) {
        return PREFIX + ERROR_COLOR + "An error has occurred! Report it to the staff with a screenshot of this message or with the error number " + ChatColor.BOLD + "#" + error.getErrorNumber();
    }

    //KitChest
    public static String SUCCESS_KITCHEST_CREATED = PREFIX + SUCCESS_COLOR + "Kit chest succesfully saved.";
    public static String SUCCESS_KITCHEST_UPDATE(int level) {
        return SUCCESS_COLOR + "You have successfully increased the level of your kit chest. New level: " + ChatColor.GRAY + level + SUCCESS_COLOR + ".";
    }

    //MapChest
    public static String SUCCESS_MAPCHEST_CREATED(MapChest mapChest) {
        String msg = DurationFormatUtils.formatDurationWords(mapChest.getDelay(), true, true);
        return PREFIX + SUCCESS_COLOR + "Chest succesfully created! Characteristics" + ChatColor.GRAY + ": \n" +
                SUCCESS_COLOR + " Name" + ChatColor.GRAY + ": " + mapChest.getName() +"\n" + SUCCESS_COLOR + " Delay " + GRAY + ": " + msg;
    }
    public static String SUCCESS_OBJECT_REMOVED_FROM_DB = PREFIX + SUCCESS_COLOR + "The object has been removed successfully removed from the database.";
    public static String SUCCES_RELOAD = PREFIX + SUCCESS_COLOR + "Reload completed!";

    public static String WARNING_REMAINING_TIME_MAP_CHEST(Long remaining) {
        return SUCCESS_COLOR + "Remaining time for recharge: " + GRAY + DurationFormatUtils.formatDurationWords(remaining, true, true) + ChatColor.YELLOW + ".";
    }

    //Economy
    public static String SHOW_OWN_BALANCE (String bal) {
        return SUCCESS_COLOR + "Your balance is: " + ChatColor.GREEN + "$" + bal + SUCCESS_COLOR + ".";
    }
    public static String SHOW_TARGET_BALANCE (String name, String bal) {
        return SUCCESS_COLOR + name + "'s balance is: " + ChatColor.GREEN + "$" + bal + SUCCESS_COLOR + ".";
    }

    public static String SUCCESS_ECONOMY_TRANSACTION_SENDER (String receiver, double amount) {
        return SUCCESS_COLOR + "Successful transaction. You have paid " + ChatColor.GREEN + "$" + amount + SUCCESS_COLOR + " to " + receiver + SUCCESS_COLOR + ".";
    }
    public static String SUCCESS_ECONOMY_TRANSACTION_RECEIVER (String sender, double amount) {
        return SUCCESS_COLOR + "Successful transaction. You have received " + ChatColor.GREEN + "$" + amount + SUCCESS_COLOR + " sent by " + sender + SUCCESS_COLOR + ".";
    }
    public static String SUCCES_ECONOMY_BALANCE_RESET_SENDER (String target) {
        return SUCCESS_COLOR + "You have restarted " + ChatColor.GREEN + target + SUCCESS_COLOR + "'s balance.";
    }
    public static String SUCCES_ECONOMY_BALANCE_RESET_TARGET (String sender) {
        return SUCCESS_COLOR + "Your economy has been restarted by " + sender + SUCCESS_COLOR + ".";
    }
    public static String SUCCESS_ECONOMY_BALANCE_SET_VALUE_SENDER(String target, double amount) {
        return SUCCESS_COLOR + "You have set " + ChatColor.GREEN + target + SUCCESS_COLOR + "'s balance to " + ChatColor.GREEN + "$" + amount + SUCCESS_COLOR + ".";
    }
    public static String SUCCESS_ECONOMY_BALANCE_SET_VALUE_TARGET(String sender, double amount) {
        return sender + SUCCESS_COLOR + " set your balance to " + ChatColor.GREEN + "$" + amount + SUCCESS_COLOR + ".";
    }

    //Claims
    public static String ERROR_CLAIMS_PVP_NOT_ALLOWED = ChatColor.RED + "PvP is not allowed here.";

    //Clan
    //Errores
    public static String ERROR_CLAN_NAME_USED = ERROR_COLOR + "There is an existing clan with that name.";
    public static String ERROR_CLAN_CANNOT_CREATE_YOU_ARE_INTO_ONE = ERROR_COLOR + "You can not create a clan while you belong to one.";
    public static String ERROR_CLAN_EXCEEDED_CHARACTERS_LIMIT = ERROR_COLOR + "You have exceeded the character limit for your clan name " + ChatColor.GRAY + "(10)" + ChatColor.RED + ".";
    public static String ERROR_CLAN_INSUFFICENT_CHARACTERS = ERROR_COLOR + "Your clan name must have at least three characters.";
    public static String ERROR_CLAN_NOT_IN_CLAN = ERROR_COLOR + "You are not in a clan.";
    public static String ERROR_CLAN_YOU_ARE_NOT_LEADER = ERROR_COLOR + "You are not allowed to do this because you are not the clan leader.";
    public static String ERROR_CLAN_NO_LEADER_NO_OFFICER = ERROR_COLOR + "Only clan leaders and officers are allowed to perform this action.";
    public static String ERROR_CLAN_TARGET_PLAYER_IS_IN_CLAN = ERROR_COLOR + "The player %s is already in a clan.";
    public static String ERROR_MAX_PLAYERS_INVITED_OR_MEMBERS = ERROR_COLOR + "Your clan has reached the maximum amount of invited players/members.";
    public static String ERROR_CLAN_CAN_NOT_LEAVE_WHILE_LEADER = ERROR_COLOR + "You can not leave the clan being a leader.";
    public static String ERROR_CLAN_CAN_NOT_KICK_LEADER = ERROR_COLOR + "You can not kick the clan leader.";
    public static String ERROR_CLAN_CANNOT_KICK_OFFICERS = ERROR_COLOR + "You can not kick officers.";
    public static String ERROR_CLAN_TARGET_IS_NOT_IN_YOUR_CLAN = ERROR_COLOR + "This player is not in the clan.";
    public static String ERROR_CLAN_INSUFFICET_BANK_BALANCE = ChatColor.RED + "The clan does not have this money ammount.";
    public static String ERROR_CLAN_YOU_ARE_LEADER_ALREADY = ChatColor.RED + "You are already the clan's leader. Maybe you meant " + ChatColor.GRAY + "/clan leader <player>";
    public static String ERROR_CLAN_NOT_FOUND = ChatColor.RED + "That clan does not exists.";
    public static String ERROR_CLAN_PLAYER_IS_NOT_MEMBER = ChatColor.RED + "This player is not a member of your clan.";
    public static String ERROR_CLAN_TARGET_IS_NOT_IN_CLAN = ERROR_COLOR + "That player is not in a clan.";

    public static String WARNING_CLAN_CANNOT_ATTACK_MATES = ChatColor.RED + "You can not attack your clan mates.";

    //Accion Completada
    public static String SUCCESS_CLAN_CREATED_BROADCAST = SUCCESS_COLOR + "The " + ChatColor.DARK_GREEN + "%s" + ChatColor.YELLOW + " clan has been created by " + ChatColor.GREEN + "%s" + ChatColor.YELLOW + ".";
    public static String SUCCESS_CLAN_RENAMED = ChatColor.GREEN + "Your clan has been successfully renamed!";
    public static String SUCCESS_CLAN_INVITATIONS_CLEANED = SUCCESS_COLOR + "You have cleared the invited players list.";
    public static String SUCCESS_CLAN_LEAVED = SUCCESS_COLOR + "You have successfully left the clan.";
    public static String SUCCES_CLAN_LEAVED_BROADCAST_REQ_PLAYERNAME = ChatColor.RED + "(Clan) " + ChatColor.GRAY + "%s" + ChatColor.RED + " has left the clan.";
    public static String SUCCESS_CLAN_DISBANDED_BROADCAST = ChatColor.RED + "(Clan) Your clan has been disbanded by the leader.";
    public static String SUCCESS_CLAN_DISBANDED = ChatColor.YELLOW + "The clan has been successfully eliminated.";
    public static String SUCCESS_CLAN_PLAYER_KICKED_REQ_TARGETS_NAME = ChatColor.GREEN + "You have succesfully kicked " + ChatColor.YELLOW + "%s" + ChatColor.GREEN + ".";
    public static String SUCCESS_CLAN_LEADER_WITHDRAW_BANK_BRADCAST_REQ_LEADERNAME_AMOUNT = ChatColor.RED + "" + ChatColor.BOLD + "(Clan) " + ChatColor.GRAY + "%s" + ChatColor.YELLOW + " has withdrawn $%.1f from the clan bank.";
    public static String SUCCESS_CLAN_LEADER_WITHDRAW_BANK_MSG_REQ_AMOUNT = ChatColor.GREEN + "You have withdrawn $%.1f from the clan bank.";
    public static String SUCCESS_CLAN_DEPOSIT_BROADCAST_REQ_SENDERNAME_AMOUNT = ChatColor.BLUE + "" + ChatColor.BOLD + "(Clan) " + ChatColor.DARK_GREEN + "%s" + ChatColor.YELLOW + " has deposited " + ChatColor.GREEN + "$%.1f" + ChatColor.YELLOW + " to the clan bank.";
    public static String SUCCESS_CLAN_CLAN_CHAT_ENABLED = ChatColor.YELLOW + "You are now in " + ChatColor.LIGHT_PURPLE + "clan" + ChatColor.YELLOW + " chat mode.";
    public static String SUCCESS_CLAN_CLAN_CHAT_DISABLED = ChatColor.YELLOW + "You are now in " + ChatColor.RED + "public" + ChatColor.YELLOW + " chat mode.";
    public static String SUCCESS_CLAN_LEADER_CHANGED_REQ_NEWLEADERNAME = ChatColor.YELLOW + "(Clan) " + ChatColor.RED + "%s" + ChatColor.YELLOW + " is now the clan's leader.";
    public static String SUCCESS_CLAN_LEADER_CHANGED_REQ_OLDLEADERNAME_NEWLEADERNAME = ChatColor.GRAY + "%s" + ChatColor.YELLOW + " gives clan's leader rank to " + ChatColor.GREEN + "%s" + ChatColor.YELLOW + ".";
    public static String SUCCESS_CLAN_REQUIRES_INVITATION_FORMAT_CLANNAME = ChatColor.BLUE + "(Clan)" + ChatColor.YELLOW + " Your clan now requires invitation to join.";
    public static String SUCCESS_CLAN_DOESNT_REQUIRES_INVITATION_FORMAT_CLANNAME =ChatColor.BLUE + "(Clan)" + ChatColor.RED + " Your clan now does not requires invitation to join.";
    public static String SUCCESS_CLAN_FRIENDLYPVP_ENABLED_FORMAT_CLANNAME = ChatColor.BLUE + "(Clan)" + ChatColor.RED + " Now the clan members can attack each other.";
    public static String SUCCESS_CLAN_FRIENDLYPVP_DISABLED_FORMAT_CLANNAME = ChatColor.BLUE + "(Clan)" + ChatColor.YELLOW + " Now the clan members can not attack each other.";
    public static String SUCCESS_CLAN_LEVEL_CHANGED_FORMAT_CLANNAME_NEWLEVEL = ChatColor.YELLOW + "You have modified the level of " + ChatColor.DARK_GREEN + "%s" + ChatColor.YELLOW + " to " + ChatColor.GREEN + "%s" + ChatColor.YELLOW + ".";

    public static String ERROR_ECONOMY_INSUFFICENT_MONEY = ERROR_COLOR + "You do not have enought money.";
    public static String SHOW_COMMAND_USAGE(String usage) {
        return ERROR_COLOR + "Usage: " + usage;
    }
    public static String ERROR_ECONOMY_INVALID_VALUE = ERROR_COLOR + "You are trying to use an invalid value.";

    //Tag
    public static String WARNING_TAG_PLAYER = ChatColor.RED + "You are tagged now!";
    public static String ERROR_TAG_COMMAND_NOT_ALLOWED_WHILE_TAGGED = Lang.ERROR_COLOR + "You are not allowed to execute this command while tagged.";

    //Profile Villagers
    public static String SUCCES_PROFILEVILLAGER_REMOVED = PREFIX + SUCCESS_COLOR + "Profile villager removed.";
    public static String SUCCESS_PROFILEVILLAGER_VIEWER_ENABLED = PREFIX + SUCCESS_COLOR + "Challenges viewer enabled for this villager.";
    public static String SUCCESS_PROFILEVILLAGER_VIEWER_DISABLED = PREFIX + SUCCESS_COLOR + "Challenges viewer disabled for this villager.";

    //Events
    public static String SUCCESS_HOSTEDEVENT_JOINED_BROADCAST_REP_DISPLAY_NAME = "%DISPLAY_NAME% " + SUCCESS_COLOR + "has joined to the event!";

    public static String ERROR_HOSTEDEVENT_IS_FULL = ERROR_COLOR + "The event has reached the maximum amount of participants.";
    public static String ERROR_HOSTEDEVENT_STARTED = ERROR_COLOR + "You can not join to a started event.";

    public static String ERROR_GLOBALEVENT_ALREADY_EXISTS = ERROR_COLOR + "There is an event with that name.";
    public static String ERROR_GLOBALEVENT_NOT_EXISTS = ERROR_COLOR + "There is not an event with that name in the database.";
 }

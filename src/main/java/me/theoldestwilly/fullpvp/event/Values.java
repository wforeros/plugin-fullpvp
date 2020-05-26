package me.theoldestwilly.fullpvp.event;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Values {
    public static Integer SECONDS_TO_START = 46;
    public static Location LMS_SPAWN = new Location(Bukkit.getServer().getWorld("world"), 1000.0D, 101.0D, 1000.0D);
    public static String NOT_ALLOWED_HOST_EVENTS;
    public static String NO_PERMS;
    public static String LEAVE_LMS;
    public static String KICKED_REASON;
    public static String DEATH_IN_EVENT_REASON;
    public static String CANCELLED_EVENT;
    public static String SAFE_DISCONNECT;
    public static String OITC_DISPLAY_NAME;
    public static String LMS_DISPLAY_NAME;
    public static String TNTTAG_DISPLAY_NAME;
    public static String LMS_DESCRIPTION;
    public static String OITC_DESCRIPTION;
    public static String TNTTAG_DESCRIPTION;
    public static String OITC_PATH;
    public static String TNT_PATH;
    public static String EVENT_GUI_SELECTOR_TITLE;
    public static Location GLOBAL_SPAWN;
    public static int MAX_CORE_HEALTH;
    public static int SECONDS_BEFORE_HEALING;
    public static String DTC_DISPLAY_NAME;
    public static String KOTH_DISPLAY_NAME;
    public static String DTC_HEADER;
    public static String ON_CORE_HEALING_BROADCAST;
    public static String ON_CORE_EVENT_STARTS;
    public static String ON_CORE_EVENT_FINISH_BROADCAST;

    public Values() {
        NOT_ALLOWED_HOST_EVENTS = ChatColor.RED + "If you want to start an event you must have a minimum donor rank, for more information use the command: " + ChatColor.YELLOW.toString() + ChatColor.BOLD + "/buy" + ChatColor.RED + ".";
        NO_PERMS = ChatColor.RED + "You are no allowed to execute this command.";
        LEAVE_LMS = ChatColor.GREEN + "You left the LMS event succesfully";
        DEATH_IN_EVENT_REASON = "death in event";
        CANCELLED_EVENT = ChatColor.RED + "The event has been cancelled for: ";
        SAFE_DISCONNECT = "safe";
        OITC_DISPLAY_NAME = ChatColor.LIGHT_PURPLE + "OITC";
        LMS_DISPLAY_NAME = ChatColor.AQUA + "Last Man Standing";
        TNTTAG_DISPLAY_NAME = ChatColor.RED + "TNT Tag";
        LMS_DESCRIPTION = "Every man for himself. The last man standing wins the event.";
        OITC_DESCRIPTION = "One in the chamber.   Free for all.";
        TNTTAG_DESCRIPTION = "If you are the chosen one, mark someone. If you are not, just run.";
        OITC_PATH = "oitc";
        TNT_PATH = "tnt-tag";
        EVENT_GUI_SELECTOR_TITLE = ChatColor.GOLD + "Event Hoster";
        GLOBAL_SPAWN = new Location(Bukkit.getWorld("world"), 0.413D, 102.0D, 0.45D);
        MAX_CORE_HEALTH = 50;
        SECONDS_BEFORE_HEALING = 45;
        DTC_DISPLAY_NAME = ChatColor.DARK_GREEN + "DTC";
        KOTH_DISPLAY_NAME = ChatColor.BLUE + "Koth";
        DTC_HEADER = ChatColor.GRAY + "[" + DTC_DISPLAY_NAME + ChatColor.GRAY + "] ";
        ON_CORE_HEALING_BROADCAST = DTC_HEADER + ChatColor.YELLOW + "The core has begun to heal itself!";
        ON_CORE_EVENT_STARTS = DTC_HEADER + ChatColor.YELLOW + "The event has started!";
        ON_CORE_EVENT_FINISH_BROADCAST = DTC_HEADER + ChatColor.YELLOW + "The event has finished and %VALUE% " + ChatColor.YELLOW + " has broken the core!";

    }

    static {
        }
}

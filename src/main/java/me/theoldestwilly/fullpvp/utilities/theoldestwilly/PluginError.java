package me.theoldestwilly.fullpvp.utilities.theoldestwilly;


import org.bukkit.ChatColor;

public enum PluginError {
    ERROR_LOADING_PROFILE("1", "This error happens when a profile from the database has not been correctly loaded", "Re-login"),
    ERROR_LOADING_MAP_CHEST_BY_NAME("2", "Error loading a MapChest with the method 'getByName()'", "Report the issue to high rank staffs with coordinates"),
    ERROR_LOADING_TARGETS_PROFILE("3", "Error while loading the profile of a target player, it can happen when executing commands, opening MapChests, cases related to economy", "Report the error to high rank staffs"),
    ERROR_SAVING_PROFILE("4", "Error while saving player profile, it happens while reloading the plugin with online users", "Fatal error, check databases back ups"),
    ERROR_LOADING_PROFILE_VILLAGER_BY_NAME("5", "Error while loading a profile villager using the method by name", "Report the error to the developer"),
    ERROR_LOADING_PROFILE_VILLAGER_BY_ENTITY("6", "Error while loading a profile villager using the method by entity", "Report the error to the developer"),
    ERROR_LOADING_EVENT_CACHED_INVENTORY("7", "Error while returning a event participant cached inventory, the items have been lost", "It is an unusual error but report it to the developer");

    private String errorNumber;
    private String description;
    private String recommendation;


    private String showDescription = ChatColor.GRAY + "[" + ChatColor.GOLD.toString() + "FullPvP" + ChatColor.GRAY + "]: " + ChatColor.RED + "Error: " + ChatColor.GRAY + "#%s\n " + ChatColor.RED + "Description: " + ChatColor.GRAY + "%s.\n" + ChatColor.RED + " Recomendation: " + ChatColor.GRAY + "%s.";

    PluginError(String errorNumber, String description, String recommendation) {
        this.errorNumber = errorNumber;
        this.description = description;
        this.recommendation = recommendation;
    }

    public String getErrorNumber() {
        return errorNumber;
    }

    public String getDescription() {
        return "\n" + String.format(showDescription, errorNumber, description, recommendation) + "\n";
    }

    public static PluginError getErrorByNumber(String number) {
        for (PluginError error : PluginError.values()) {
            if (error.getErrorNumber().equals(number))
                return error;
        }
        return null;
    }
}

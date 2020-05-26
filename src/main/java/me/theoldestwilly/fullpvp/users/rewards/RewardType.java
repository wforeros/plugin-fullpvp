package me.theoldestwilly.fullpvp.users.rewards;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum  RewardType {
    COMMON(ChatColor.GREEN + "Common"), LEGENDARY(ChatColor.GOLD + "Legendary"), MYSTERIOUS(ChatColor.AQUA + "Mysterious");

    private String displayName;
    RewardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RewardType getRewardBySkullOwner(String owner) {
        return  owner.equalsIgnoreCase("Chest") ? COMMON : owner.equalsIgnoreCase("MHF_Present1") ? MYSTERIOUS : owner.equalsIgnoreCase("MHF_Present2") ? LEGENDARY : null;
    }

    public static RewardType getType(String string) {
        for (RewardType type : RewardType.values())
            if (type.toString().equalsIgnoreCase(string))
                return type;
        return null;
    }

    public static List<String> valuesAsString() {
        List<String> list = new ArrayList<>();
        for (RewardType type : RewardType.values())
            list.add(type.toString().toLowerCase());
        return list;
    }
}

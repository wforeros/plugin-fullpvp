package me.theoldestwilly.fullpvp.event.hosted.structure;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum HostedEventType {

    LMS(ChatColor.AQUA + "Last Man Standing", true,  ChatColor.GRAY + "[" + ChatColor.AQUA + "LMS" + ChatColor.GRAY + "] ",
            new String[]{"&7Every man for himself.", "&7The last man standing wins the event."},
            Material.IRON_SWORD),
    TNT_TAG (ChatColor.RED + "TNT-Tag", false,  ChatColor.GRAY + "[" + ChatColor.RED + "TNT-Tag" + ChatColor.GRAY + "] ",
            new String[]{ "&7If you are the chosen one, mark someone.", "&7If you are not, just run."},
            Material.TNT),
    SPLEEF (ChatColor.GREEN + "Spleef", false, ChatColor.GRAY + "[" + ChatColor.GREEN + "Spleef" + ChatColor.GRAY + "] ",
            new String[]{"&7Your best weapon are snowballs and your shovel", "&7Break your opponent's floor to win"}, Material.WATER_BUCKET)
    /*BTF(ChatColor.GREEN + "Big Team Fight", true,  ChatColor.GRAY + "[" + ChatColor.GREEN + "BTF" + ChatColor.GRAY + "] ",
            new String[]{}, Material.AIR),*/
    ;

    private String displayName;
    private String prefix;
    private String[] description;
    private Material iconMaterial;
    private boolean modifiableKit;

    HostedEventType(String displayName, boolean modifiableKit, String prefix, String[] description, Material iconMaterial) {
        this.displayName = displayName;
        this.modifiableKit = modifiableKit;
        this.prefix = prefix;
        this.description = description;
        this.iconMaterial = iconMaterial;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isModifiableKit() {
        return modifiableKit;
    }

    public String getPrefix() {
        return prefix;
    }

    public String[] getDescription() {
        return description;
    }

    public Material getMaterial() {
        return iconMaterial;
    }

    public String toLowerString() {
        return this.toString().toLowerCase();
    }

    public ItemStack getIcon() {
        List<String> lore = new LinkedList<>();
        lore.add("&7&m----------------------------------");
        for (String s1 : getDescription()) lore.add(s1);
        Long delay = FullPvP.getInstance().getHostedEventsManager().getRemainingTime(this);
        if (delay != null && delay > 0) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Remaining delay: " + ChatColor.WHITE + DurationFormatUtils.formatDurationWords(delay, true, true));
        }
        lore.add("&7&m----------------------------------");
        return new ItemBuilder(getMaterial()).setDisplayName(getDisplayName()).setLore(lore).build();
    }

    public static HostedEventType fromString(String s) {
        for(HostedEventType type : HostedEventType.values()) {
            if (type.toString().equalsIgnoreCase(s))
                return type;
            else if (ChatColor.stripColor(type.getDisplayName()).equalsIgnoreCase(s))
                return type;
        }
        return null;
    }

    public static ItemStack getIcon(String s) {
        HostedEventType type = fromString(s);
        List<String> lore = new LinkedList<>();
        lore.add("&7&m----------------------------------");
        for (String s1 : type.getDescription()) lore.add(s1);
        Long delay = FullPvP.getInstance().getHostedEventsManager().getRemainingTime(type);
        if (delay != null) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Remaining delay: " + ChatColor.WHITE + DurationFormatUtils.formatDurationWords(delay, true, true));
        }
        lore.add("&7&m----------------------------------");
        return new ItemBuilder(type.getMaterial()).setDisplayName(type.getDisplayName()).setLore(lore).build();
    }

}

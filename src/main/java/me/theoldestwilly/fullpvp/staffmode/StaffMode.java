package me.theoldestwilly.fullpvp.staffmode;

import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum StaffMode {
    TELEPORT_COMPASS {
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.COMPASS).setDisplayName("&e&lTeleport Compass").build();
        }
    },
    INSPECTION_TOOL {
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.BOOK).setDisplayName("&e&lInspection Tool").build();
        }
    },
    FOLLOW_TOOL {
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.LEASH).setDisplayName("&e&lFollow Tool").build();
        }
    },
    FREEZE_TOOL {
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.ICE).setDisplayName("&e&lFreeze Tool").build();
        }
    },
    VANISH_TOOL_ON{
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.INK_SACK).setDisplayName("&e&lVanish Tool&7: &aOn").setDurability(8).build();
        }
    },
    VANISH_TOOL_OFF{
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.INK_SACK).setDisplayName("&e&lVanish Tool&7: &cOff").setDurability(10).build();
        }
    },
    RANDOM_TELEPORT_TOOL{
        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.RECORD_3).setDisplayName("&e&lRandom TP Tool").build();
        }
    };

    public abstract ItemStack getItem();
}

package me.theoldestwilly.fullpvp.rename;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameManager {
    private FullPvP plugin;
    private List<String> ilegalWords;

    public RenameManager(FullPvP plugin) {
        this.plugin = plugin;
        ilegalWords = plugin.getConfig().getStringList("ilegal-words");
        if (ilegalWords == null) ilegalWords = new ArrayList<>();
    }

    public void renameTool(Player player, String[] newNameArgs, ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            String newName = StringUtils.join(newNameArgs, ' ');
            String check = newName.toLowerCase();
            Iterator var6 = this.ilegalWords.iterator();

            String string;
            do {
                if (!var6.hasNext()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(" " + TextUtils.formatColor(newName) + " ");
                    itemStack.setItemMeta(itemMeta);
                    player.sendMessage(ChatColor.BLUE + "You have successfully renamed your item!");
                    return;
                }
                string = (String)var6.next();
            } while(!check.contains(string.toLowerCase()));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warn -s " + player.getName() + " Use of offensive words with /rename " + newName);
        } else {
            player.sendMessage(ChatColor.RED + "You may not rename your hand :(");
        }
    }

    public void resetName(Player player, ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(null);
            itemStack.setItemMeta(itemMeta);
            player.sendMessage(ChatColor.BLUE + "You have successfully rebooted your item name!");
        } else {
            player.sendMessage(ChatColor.RED + "You have not renamed your hand :(");
        }
    }
}


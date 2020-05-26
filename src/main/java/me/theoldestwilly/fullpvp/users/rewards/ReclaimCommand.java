package me.theoldestwilly.fullpvp.users.rewards;

import java.util.Arrays;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.ItemBuilder;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReclaimCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public ReclaimCommand(FullPvP plugin) {
        super("reclaim");
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.GOLD + "Rewards GUI");
            List<ItemStack> list = profile.getRewards();
            if (list != null && !list.isEmpty()) {
                inventory.addItem((ItemStack[])profile.getRewards().toArray(new ItemStack[0]));
                inventory.setItem(8, (new ItemBuilder(Material.WOOL)).setDurability(13).setLore(Arrays.asList("", "&7Click to reclaim &fALL", "&7your rewards!", "&a&l* Available Rewards: &f" + profile.getRewards().size())).build());
            }
            player.openInventory(inventory);
        } else {
            sender.sendMessage(ChatColor.RED + "You can not execute this command on console");
        }

        return true;
    }
}

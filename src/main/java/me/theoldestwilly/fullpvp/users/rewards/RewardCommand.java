package me.theoldestwilly.fullpvp.users.rewards;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RewardCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public RewardCommand(FullPvP plugin) {
        super("reward", new String[]{"rewards"});
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <nick> <type> <amount>");
        } else {
            Player player = Bukkit.getPlayer(arguments[0]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(ChatColor.RED + "Player with name " + arguments[0] + " not found");
                return true;
            }

            RewardType type = RewardType.getType(arguments[1].toLowerCase());
            ItemStack itemStack = this.plugin.getRewardsManager().createReward(type);
            if (itemStack == null) {
                sender.sendMessage(ChatColor.RED + "Reward type not found.");
                return true;
            }

            Integer amount = JavaUtils.tryParseInt(arguments[2]);
            if (amount == null || amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Invalid value");
                return true;
            }

            Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            if (profile == null) {
                sender.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
                return true;
            }
            itemStack.setAmount(amount);
            profile.addReward(itemStack);
            sender.sendMessage(ChatColor.YELLOW + "You sent " + amount + " " + type.getDisplayName() + ChatColor.YELLOW + " reward(s) to " + player.getDisplayName() + ChatColor.YELLOW + ".");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return null;
        } else if (args.length == 2){
            return RewardType.valuesAsString();
        }
        else {
            return Collections.emptyList();
        }
    }
}


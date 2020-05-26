package me.theoldestwilly.fullpvp.event.command.arguments;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.Checkers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GlobalEventSetRewardArgument extends CommandArgument {
        private final FullPvP plugin;

        public GlobalEventSetRewardArgument(FullPvP plugin) {
            super("setrewards", "Set your inventory contents as global events rewards.");
            this.plugin = plugin;
            this.permission = "fullpvp.command.globalevent.argument.rewards";
        }

        public String getUsage(String label) {
            return '/' + label + ' ' + this.getName();
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if (player.getInventory().getContents().length == 0) {
                    player.sendMessage(ChatColor.RED + "You can not save the events reward with an empty inventory.");
                    return true;
                }
                this.plugin.getGlobalEventsManager().setReward(Checkers.removeNullItems(player.getInventory().getContents()));
                sender.sendMessage(ChatColor.BLUE + "You have modified the rewards of all events for the content of your inventory successfully.");
            }

            return true;
        }

        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
            if (args.length != 2) {
                return Collections.emptyList();
            } else {
                List<String> kothsName = new ArrayList();
                Iterator var6 = this.plugin.getGlobalEventsManager().getKothManager().getKothsList().keySet().iterator();

                while(var6.hasNext()) {
                    String name = (String)var6.next();
                    kothsName.add(name);
                }

                return kothsName;
            }
        }
}

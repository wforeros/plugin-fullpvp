package me.theoldestwilly.fullpvp.anvil;


import me.theoldestwilly.fullpvp.FullPvP;
import net.silexpvp.nightmare.util.command.CommandArgument;
import net.silexpvp.nightmare.util.command.ExecutableCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class AnvilCommand extends ExecutableCommand<FullPvP> {
    private AnvilCommand instance;
    public AnvilCommand() {
        super("anvil");
        instance = this;
        this.addArgument(new AnvilCommand.SaveArgument());
        this.addArgument(new AnvilCommand.DeleteArgument());
    }

    private class DeleteArgument extends CommandArgument {

        public DeleteArgument() {
            super(instance, "delete");
        }

        public String getUsage(String label) {
            return '/' + label + ' ' + this.getName();
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
            if (arguments.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            } else if (sender instanceof Player) {
                plugin.getAnvilHandler().removeLocation((Player)sender);
            } else {
                sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
            }

            return true;
        }

        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
            return arguments.length == 2 ? null : Collections.emptyList();
        }
    }

    private class SaveArgument extends CommandArgument {
        public SaveArgument() {
            super(instance, "save", "Saves a new anvil.", new String[]{""});
        }

        public String getUsage(String label) {
            return '/' + label + ' ' + this.getName();
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
            if (arguments.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            } else if (sender instanceof Player) {
                plugin.getAnvilHandler().saveLocation((Player)sender);
            } else {
                sender.sendMessage(ChatColor.RED + "You can not execute this command on console.");
            }

            return true;
        }

        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
            return arguments.length == 2 ? null : Collections.emptyList();
        }
    }
}

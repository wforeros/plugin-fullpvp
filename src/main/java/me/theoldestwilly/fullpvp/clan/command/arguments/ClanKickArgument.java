package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanKickArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanKickArgument(FullPvP plugin) {
        super("kick", "Kick a player from a clan.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            } else {
                Player officer = (Player)sender;
                if (officer.getName().equalsIgnoreCase(arguments[1])) {
                    officer.sendMessage(Lang.ERROR_TARGET_IS_SAME_SENDER);
                    return true;
                }
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(arguments[1]);
                if (player == null) {
                    sender.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
                    return true;
                }

                this.plugin.getClanManager().asyncKickPlayer(officer, player);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else {
            List<String> list = new ArrayList();
            this.plugin.getClanManager().getPlayersClan((Player)sender)
                    .getAllMembers().forEach(
                    clanMember -> list.add(clanMember.getName()));
            return list;
        }
    }
}


package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanLeaderArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanLeaderArgument(FullPvP plugin) {
        super("leader", "Leader gives his rank to an other member.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        } else if (sender instanceof Player) {
            Player leader = (Player)sender;
            Clan ownClan = this.plugin.getClanManager().getPlayersClan(leader);
            if (!ownClan.getLeader().getUniqueID().equals(leader.getUniqueId())) {
                leader.sendMessage(Lang.ERROR_CLAN_YOU_ARE_NOT_LEADER);
                return true;
            }

            if (arguments[1].equalsIgnoreCase(leader.getName())) {
                leader.sendMessage(Lang.ERROR_TARGET_IS_SAME_SENDER);
                return true;
            }

            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(arguments[1]);
            if (player == null) {
                sender.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
                return true;
            }

            Clan clan = this.plugin.getClanManager().getPlayersClan(player);
            if (clan == null) {
                sender.sendMessage(Lang.ERROR_CLAN_PLAYER_IS_NOT_MEMBER);
                return true;
            }

            if (clan.getName().equals(ownClan.getName())) {
                clan.setLeader(player.getUniqueId());
                clan.broadcast(String.format(Lang.SUCCESS_CLAN_LEADER_CHANGED_REQ_OLDLEADERNAME_NEWLEADERNAME, sender.getName(), player.getName()));

            } else {
                leader.sendMessage(Lang.ERROR_CLAN_PLAYER_IS_NOT_MEMBER);
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

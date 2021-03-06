package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanDemoteArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanDemoteArgument(FullPvP plugin) {
        super("demote", "Demotes a clan member.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(arguments[1]);
            if (target == null) {
                player.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
                return true;
            }

            Clan clan = this.plugin.getClanManager().getPlayersClan(player);
            if (clan == null) {
                player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
                return true;
            }

            UUID uuid = player.getUniqueId();
            if (!clan.isLeader(uuid)) {
                player.sendMessage(Lang.ERROR_CLAN_YOU_ARE_NOT_LEADER);
                return true;
            }

            clan.demoteMember(target.getUniqueId());
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

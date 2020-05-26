package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.clan.ClanMember;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanForceLeaderArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanForceLeaderArgument(FullPvP plugin) {
        super("forceleader", "Staff: Forces sender to be clan's leader.");
        this.permission = "fullpvp.command.clan.argument.forceleader";
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            Clan clan = this.plugin.getClanManager().getPlayersClan(player);
            if (clan == null) {
                player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
                return true;
            }

            if (clan.getLeader().getUniqueID().equals(player.getUniqueId())) {
                player.sendMessage(Lang.ERROR_CLAN_YOU_ARE_LEADER_ALREADY);
            } else {
                clan.setLeader(player.getUniqueId());
                clan.broadcast(String.format(Lang.SUCCESS_CLAN_LEADER_CHANGED_REQ_NEWLEADERNAME, player.getName()));
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

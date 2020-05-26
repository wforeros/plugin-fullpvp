package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanPrivacityArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanPrivacityArgument(FullPvP plugin) {
        super("privacy", "Modify clan privacity.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <public/private>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            } else {
                Player player = (Player)sender;
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

                if (arguments[1].equalsIgnoreCase("private")) {
                    clan.setInvitation(true);
                    player.sendMessage(Lang.SUCCESS_CLAN_REQUIRES_INVITATION_FORMAT_CLANNAME);
                } else if (arguments[1].equalsIgnoreCase("public")) {
                    clan.setInvitation(false);
                    player.sendMessage(Lang.SUCCESS_CLAN_DOESNT_REQUIRES_INVITATION_FORMAT_CLANNAME);
                } else {
                    player.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
                }
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            List<String> list = new ArrayList();
            list.add("public");
            list.add("private");
            return list;
        } else {
            return Collections.emptyList();
        }
    }
}

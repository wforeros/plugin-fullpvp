package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.Collections;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.clan.ClanMember;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanChatArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanChatArgument(FullPvP plugin) {
        super("chat", "Chat with clan members.", new String[]{"c"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            Clan clan = this.plugin.getClanManager().getPlayersClan(player);
            if (clan != null) {
                ClanMember clanMember = clan.getMember(player.getUniqueId());
                if (arguments.length == 1) {
                    if (!clanMember.isAtClanChat()) {
                        clanMember.setAtClanChat(true);
                        player.sendMessage(Lang.SUCCESS_CLAN_CLAN_CHAT_ENABLED);
                    } else {
                        clanMember.setAtClanChat(false);
                        player.sendMessage(Lang.SUCCESS_CLAN_CLAN_CHAT_DISABLED);
                    }
                } else {
                    String message = "";
                    for (int i = 1; i < arguments.length; i++) {
                        message += arguments[i] + " ";
                    }
                    clan.sendChatMessage(player.getName(), message);
                }
            } else {
                player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
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

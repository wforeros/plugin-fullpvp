package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClanSetLevelArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanSetLevelArgument(FullPvP plugin) {
        super("setlevel", "Staff: Sets clan's level.");
        this.permission = "fullpvp.command.clan.argument.setlevel";
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <clanName> <level>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length < 2) {
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        } else {
            Clan clan = this.plugin.getClanManager().getClanByName(arguments[1]);
            if (clan == null) {
                sender.sendMessage(Lang.ERROR_CLAN_NOT_FOUND);
            } else {
                Integer newLevel = JavaUtils.tryParseInt(arguments[2]);
                if (newLevel != null) {
                    clan.setLevel(newLevel);
                    sender.sendMessage(String.format(Lang.SUCCESS_CLAN_LEVEL_CHANGED_FORMAT_CLANNAME_NEWLEVEL, clan.getName(), newLevel + ""));
                } else {
                    sender.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
                }
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        } else {
            List<String> list = new ArrayList();
            this.plugin.getClanManager().getClansList().keySet()
                    .forEach(name -> list.add(name));
            return list;
        }
    }
}

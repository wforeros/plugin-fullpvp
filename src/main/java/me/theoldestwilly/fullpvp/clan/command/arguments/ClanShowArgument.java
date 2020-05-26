package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.clan.ClanMember;
import me.theoldestwilly.fullpvp.clan.ClanRank;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanShowArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanShowArgument(FullPvP plugin) {
        super("show", "Shows any clan info.", new String[]{"info", "i", "s"});
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <clan/player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Clan clan;
        if (arguments.length < 2) {
            if (sender instanceof Player) {
                clan = this.plugin.getClanManager().getPlayersClan((OfflinePlayer)sender);
                if (clan != null) {
                    this.showInfo(sender, clan);
                } else {
                    sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
                }
                return true;
            }
            sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
        } else {
            clan = this.plugin.getClanManager().getClanByName(arguments[1].toLowerCase());
            if (clan == null) {
                OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(arguments[1]);
                if (!player.isOnline() && !player.hasPlayedBefore()) {
                    sender.sendMessage(Lang.ERROR_CLAN_NOT_FOUND);
                    return true;
                }

                clan = this.plugin.getClanManager().getPlayersClan(player);
                if (clan == null) {
                    sender.sendMessage(Lang.ERROR_CLAN_TARGET_IS_NOT_IN_CLAN);
                    return true;
                }

                this.showInfo(sender, clan);
            } else {
                this.showInfo(sender, clan);
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }

    public String showMembers(Clan clan, ClanRank rank) {
        String members = "";
        int counter = 0;
        Iterator var6;
        ClanMember clanMember;
        Player player;
        int max;
        if (rank == ClanRank.OFFICER) {
            if (!clan.getOfficers().isEmpty()) {
                max = clan.getOfficers().size();
                var6 = clan.getOfficers().iterator();

                while(var6.hasNext()) {
                    clanMember = (ClanMember)var6.next();
                    player = Bukkit.getServer().getPlayer(clanMember.getUniqueID());
                    members = members + (player != null ? ChatColor.GREEN + player.getName() : ChatColor.RED + clanMember.getName());
                    ++counter;
                    if (max > counter) {
                        members = members + ChatColor.GRAY + ", ";
                    } else {
                        members = members + ChatColor.GRAY + ".";
                    }
                }
            } else {
                members = members + ChatColor.RED + "There are not players.";
            }
        } else if (rank == ClanRank.MEMBER) {
            if (!clan.getMembers().isEmpty()) {
                max = clan.getMembers().size();
                var6 = clan.getMembers().iterator();

                while(var6.hasNext()) {
                    clanMember = (ClanMember)var6.next();
                    player = Bukkit.getServer().getPlayer(clanMember.getUniqueID());
                    members = members + (player != null ? ChatColor.GREEN + player.getName() : ChatColor.RED + clanMember.getName());
                    ++counter;
                    if (max > counter) {
                        members = members + ChatColor.GRAY + ", ";
                    } else {
                        members = members + ChatColor.GRAY + ".";
                    }
                }
            } else {
                members = members + ChatColor.RED + "There are not players.";
            }
        }

        return members;
    }

    public void showInfo(CommandSender sender, Clan clan) {
        sender.sendMessage(TextUtils.formatColor("&7&m&l-------------------------------------------------"));
        sender.sendMessage(TextUtils.formatColor("&6&lClan " + clan.getName() + " &7[Level: " + clan.getLevel() + "&7]"));
        sender.sendMessage(TextUtils.formatColor(""));
        sender.sendMessage(TextUtils.formatColor(" &eLeader: " + (Bukkit.getServer().getPlayer(clan.getLeader().getUniqueID()) != null ? ChatColor.GREEN : ChatColor.RED) + clan.getLeader().getName()));
        if (clan.getOfficers().size() != 0) {
            sender.sendMessage(TextUtils.formatColor(" &eOfficers: " + this.showMembers(clan, ClanRank.OFFICER)));
        }

        if (clan.getMembers().size() != 0) {
            sender.sendMessage(TextUtils.formatColor(" &eMembers: " + this.showMembers(clan, ClanRank.MEMBER)));
        }

        int members = clan.getMembersNumber();
        int maxMembers = this.plugin.getClanManager().getMaxClanMembers();
        sender.sendMessage(TextUtils.formatColor(" &eTotal Members: " + (members < maxMembers ? "&7" : "&e") + members + " &7out of &e" + clan.getSlots()));
        sender.sendMessage(TextUtils.formatColor(" &eKills: &f" + clan.getClanKills()));
        sender.sendMessage(TextUtils.formatColor(" &eDeaths: &f" + clan.getClanDeaths()));
        sender.sendMessage(TextUtils.formatColor(" &eBank: &f$" + String.format("%.1f", clan.getClanBalance())));
        sender.sendMessage(TextUtils.formatColor(" &eK/D: &f" + String.format("%.1f",clan.getKd())));
        sender.sendMessage(TextUtils.formatColor(" &ePrivacity: " + (clan.requiresInvitation() ? ChatColor.AQUA + "Requires invitation" : ChatColor.GREEN + "Public")));
        sender.sendMessage(TextUtils.formatColor(" &eFriendly Fire: " + (clan.isFriendlyFire() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled")));
        sender.sendMessage(TextUtils.formatColor("&7&m&l-------------------------------------------------"));
    }
}

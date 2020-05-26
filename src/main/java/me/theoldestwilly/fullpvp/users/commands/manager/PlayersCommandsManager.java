package me.theoldestwilly.fullpvp.users.commands.manager;

import java.util.Iterator;
import java.util.UUID;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.clan.Clan;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.chat.ClickAction;
import me.theoldestwilly.fullpvp.utilities.chat.Text;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.GameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayersCommandsManager {
    private FullPvP plugin;

    public PlayersCommandsManager(FullPvP plugin) {
        this.plugin = plugin;
    }

    public void onHelpopUse(Player user, String[] arguments) {
         Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                    if (PermissionsManager.hasStaffPermission(player)) {
                        Text text = (new Text("[")).setColor(ChatColor.GRAY).append(
                                (new Text("Request")).setColor(ChatColor.BLUE))
                                .append((new Text("] ")).setColor(ChatColor.GRAY))
                                .append((new Text(user.getName() + " ")).setColor(ChatColor.BLUE))
                                .append((new Text("has send a request: ")).setColor(ChatColor.YELLOW))
                                .append((new Text(StringUtils.join(arguments, ' '))).setColor(ChatColor.GRAY))
                                .append((new Text(".")).setColor(ChatColor.YELLOW))
                                .setHoverText(ChatColor.GREEN + "Click to teleport to " + user.getName() + ".")
                                .setClick(ClickAction.RUN_COMMAND, "/tp " + user.getName());
                        text.send(player);
                    }
                }
        );

        user.sendMessage(TextUtils.formatColor("&7[&9Request&7] &aYour message has been sent to the online staff."));
    }

    public void onReportUse(Player user, Player target, String[] arguments) {
        arguments[0] = "";
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (PermissionsManager.hasStaffPermission(player)) {
                Text text = (new Text("")).setColor(ChatColor.GRAY)
                        .append((new Text("[")).setColor(ChatColor.GRAY))
                        .append((new Text("Report")).setColor(ChatColor.RED))
                        .append((new Text("] ")).setColor(ChatColor.GRAY))
                        .append((new Text(user.getName() + " ")).setColor(ChatColor.YELLOW))
                        .append((new Text("has reported ")).setColor(ChatColor.RED))
                        .append((new Text(target.getName())).setColor(ChatColor.YELLOW))
                        .append(new Text(" for")).setColor(ChatColor.RED)
                        .append((new Text(StringUtils.join(arguments, ' '))).setColor(ChatColor.GRAY))
                        .append((new Text(".")).setColor(ChatColor.RED))
                        .setHoverText(ChatColor.RED + "Click to teleport to " + target.getName() + ".")
                        .setClick(ClickAction.RUN_COMMAND, "/tp " + target.getName());
                text.send(player);
            }
        });
        user.sendMessage(TextUtils.formatColor("&7[&c&lReporte&7] &eYour report has been sent to the online staff."));
    }

    public void sendStatsMessage(final CommandSender sender, final OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()) {
            (new BukkitRunnable() {
                public void run() {
                    Profile userData = PlayersCommandsManager.this.plugin.getProfileHandler().getOfflineProfile(offlinePlayer, false);
                    sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------");
                    sender.sendMessage(ChatColor.GOLD + offlinePlayer.getName() + "'s statistics: ");
                    sender.sendMessage(ChatColor.YELLOW + " Kills" + ChatColor.GRAY + ": " + ChatColor.RED + userData.getKills());
                    sender.sendMessage(ChatColor.YELLOW + " Deaths" + ChatColor.GRAY + ": " + ChatColor.RED + userData.getDeaths());
                    sender.sendMessage(ChatColor.YELLOW + " Level" + ChatColor.GRAY + ": " + ChatColor.RED + userData.getLevel() + ChatColor.GRAY + " (Kills for Rankup: " + (GameUtils.getKills(plugin.getPlayersHandler().getRankupKills(), userData.getLevel() + 1).intValue() - userData.getKills()) + ")");
                    sender.sendMessage(ChatColor.YELLOW + " Killstreak" + ChatColor.GRAY + ": " + ChatColor.RED + userData.getKillStreak());
                    sender.sendMessage(ChatColor.YELLOW + " Max Killstreak" + ChatColor.GRAY + ": " +  ChatColor.RED + userData.getMaximumKillStreak());
                    sender.sendMessage(ChatColor.YELLOW + " Event wins" + ChatColor.GRAY + ": " +  ChatColor.RED + userData.getEventWins());
                    Clan clan = PlayersCommandsManager.this.plugin.getClanManager().getPlayersClan(offlinePlayer);
                    if (clan != null) {
                        sender.sendMessage(ChatColor.YELLOW + " Clan" + ChatColor.GRAY + ": " + ChatColor.RED + clan.getName() + " " + ChatColor.GRAY + "[LvL: " + clan.getLevel() + "]");
                    }

                    sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------");

                }
            }).runTaskAsynchronously(this.plugin);
        } else {
            sender.sendMessage(ChatColor.RED + "Player with name " + offlinePlayer.getName() + " not found.");
        }

    }

    public void sendHelpMessage(Player player) {
        player.sendMessage(TextUtils.formatColor("&7&m&l-------------------------------------------------"));
        String msg = TextUtils.formatColor(" \n&6&lSilex: &e&lFullPvP" +
                "\n \n" +
                "&6Staff contact&7: \n" +
                " &7* &e/helpop&7: &fIf you have any questions, tell it to online staff.\n" +
                " &7* &e/report&7: &fIf you have suspicions of a player violating the rules of the modality you can report it with this command.\n\n \n" +
                "&6Important Pages&7:\n" +
                " &7* &eTeamSpeak3&7: &fts.silexpvp.net\n" +
                " &7* &eShop&7: &fshop.silexpvp.net\n \n \n" +
                "&6Other Commands: \n" +
                " &7* &e/warp&7: &fList disponible warps.\n" +
                " &7* &e/clan&7: &fShows clans subcommands." +
                " \n &7* &e/pv&7: &fOpen your private bag/vault." +
                "\n &7* &e/koth&7: &fShows available subcommands about koths." +
                " \n &7* &e/event&7: &fDisplay event GUI &7(get your rank at our shop to host events)&f. \n"
        );
        player.sendMessage(msg);
        player.sendMessage(TextUtils.formatColor("&7&m&l-------------------------------------------------"));
    }

    public void onLastUpdateCommand(CommandSender sender) {
        String msg = TextUtils.formatColor("&7&m&l----------------------------------------\n&6&lLast Core Update Changes&7:\n&7* &eNew events system.\n&7* &e+20 Map chests arount the map.\n&7* &eNew builds and modifications in the map.\n&7* &eNew OITC event.\n&7* &eNew systems for: kits, warps, events, database.\n&7&m&l----------------------------------------\n");
        sender.sendMessage(msg);
    }

    public void sendClanHelp(Player player, ArgumentExecutor executor) {
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------");
        player.sendMessage(ChatColor.GOLD + "Clans Help: \n");
        for (CommandArgument argument : executor.getArguments()) {
            if (argument.getPermission() == null || argument.getPermission() == "" || player.hasPermission(argument.getPermission())) {
                player.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "/clan " + argument.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + argument.getDescription());
            }
        }
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------------------");
    }
}

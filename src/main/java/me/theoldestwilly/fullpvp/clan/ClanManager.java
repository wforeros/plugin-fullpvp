package me.theoldestwilly.fullpvp.clan;

import lombok.Getter;
import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.events.ClanDisbandEvent;
import me.theoldestwilly.fullpvp.clan.events.PlayerJoinClanEvent;
import me.theoldestwilly.fullpvp.clan.events.PlayerKickedFromClanEvent;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.JavaUtils;
import me.theoldestwilly.fullpvp.utilities.chat.ClickAction;
import me.theoldestwilly.fullpvp.utilities.chat.Text;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import net.silexpvp.nightmare.util.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ClanManager {
    private FullPvP plugin;
    private @Getter Map<String, Clan> clansList = new HashMap();
    private Config config;
    private String alias = "clans";
    private @Getter Double clanCreateCost;
    private @Getter int maxClanMembers;
    private int[] rankUpClansCost;

    public ClanManager(FullPvP plugin) {
        this.plugin = plugin;
        clanCreateCost = plugin.getConfig().getDouble("clan.creation-cost");
        maxClanMembers = plugin.getConfig().getInt("clan.max-members");
        ConfigurationSerialization.registerClass(ClanMember.class);
        ConfigurationSerialization.registerClass(Clan.class);
        config = new Config(plugin, alias);
        loadClans();
        Config plConfig = plugin.getConfig();
        String path = "clan.rank-up.lvl-";
        rankUpClansCost = new int[]{plConfig.getInt(path + "2"), plConfig.getInt(path + "3"),
                plConfig.getInt(path + "4"), plConfig.getInt(path + "5"), plConfig.getInt(path + "6"),
                plConfig.getInt(path + "7"), plConfig.getInt(path + "8"), plConfig.getInt(path + "9"),
                plConfig.getInt(path + "10")};

    }

    public void loadClans() {
        Object object = config.get(alias);
        if (object != null && object instanceof List) {
            GenericUtils.createList(object, Clan.class).forEach(clan ->
                    clansList.put(clan.getName().toLowerCase(), clan));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + clansList.size() + " clans loaded.");
        }
    }

    public void saveClans() {
        List<Clan> clans = new ArrayList<>();
        for (Clan clan : clansList.values()) {
            if (clan.getClanBalance() < 0) clan.setClanBalance(0.0);
            clans.add(clan);
        }
        config.set(alias, clans.toArray(new Clan[0]));
        config.save();
    }

    public Clan getClanByName(String name) {
        return clansList.values().stream().filter(clan ->
                name.equalsIgnoreCase(clan.getName())
        ).findAny().orElse(null);
    }

    public Clan getPlayersClan(OfflinePlayer player) {
        return clansList.values().stream().filter(clan ->
                clan.isMember(player.getUniqueId(), player.getName()))
                .findFirst().orElse(null);
    }

    public Clan getPlayersClan(String name) {
        return clansList.values().stream().filter(clan ->
                clan.isMember(name))
                .findFirst().orElse(null);
    }

    public int getNextLevelCost(int currentLevel) {
        return rankUpClansCost[currentLevel - 1];
    }

    /*public Clan getPlayersClanAsync(OfflinePlayer player) {
        final Clan[] clan = new Clan[1];
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                clan[0] = getPlayersClan(player);
            }
        });
        return clan[0];
    }*/

    public void asyncCreateNewClan(Player player, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                createNewClan(player, name);
            }
        });
    }

    private void createNewClan(Player player, String name) {
        Clan clan = getClanByName(name);
        if (clan != null) {
            player.sendMessage(Lang.ERROR_CLAN_NAME_USED);
        } else {
            Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            if (profile == null) {
                player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE) + ChatColor.GRAY + " (Clan Manager)");
                return;
            }
            clan = getPlayersClan(player);
            if (clan != null) {
                player.sendMessage(Lang.ERROR_CLAN_CANNOT_CREATE_YOU_ARE_INTO_ONE);
                return;
            }
            if (!JavaUtils.isAlphanumeric(name)) {
                player.sendMessage(Lang.ERROR_GLOBAL_INVALID_CHARACTERS);
                return;
            }
            if (name.length() < 3) {
                player.sendMessage(Lang.ERROR_INSUFFICENT_CHARACTERS);
                return;
            }
            if (name.length() > 10) {
                player.sendMessage(Lang.ERROR_EXCEEDED_CHARACTERS_LIMIT);
                return;
            }
            if (!profile.hasEnoughtMoneyToPay(clanCreateCost)) {
                player.sendMessage(Lang.ERROR_ECONOMY_INSUFFICENT_MONEY);
                return;
            }

            clan = new Clan(name, player.getUniqueId(), player.getName(), profile);
            clansList.put(name.toLowerCase(), clan);
            profile.removeMoneyFromBalance(clanCreateCost);
            Bukkit.broadcastMessage(String.format(Lang.SUCCESS_CLAN_CREATED_BROADCAST, clan.getName(), player.getName()));
        }
    }

    public void renameClan(Player player, String newName) {
        Clan clan = this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (!clan.getLeader().getUniqueID().equals(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_YOU_ARE_NOT_LEADER);
        } else if (this.getClanByName(newName) != null) {
            player.sendMessage(Lang.ERROR_CLAN_NAME_USED);
        } else if (newName.length() < 3) {
            player.sendMessage(Lang.ERROR_CLAN_INSUFFICENT_CHARACTERS);
        } else if (!JavaUtils.isAlphanumeric(newName)) {
            player.sendMessage(Lang.ERROR_GLOBAL_INVALID_CHARACTERS);
        } else if (newName.length() > 10) {
            player.sendMessage(Lang.ERROR_CLAN_EXCEEDED_CHARACTERS_LIMIT);
        } else {
            Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            if (profile == null) {
                player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_PROFILE)+ ChatColor.GRAY + " (Clan Manager)");
                return;
            }
            String oldName = clan.getName();
            clan.setName(newName);
            this.clansList.remove(oldName.toLowerCase());
            this.clansList.put(newName.toLowerCase(), clan);
            player.sendMessage(Lang.SUCCESS_CLAN_RENAMED);
        }
    }

    public void invitePlayer(Player player, Player invited) {
        Clan clan = this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (!clan.isLeader(player.getUniqueId()) && !clan.isOfficer(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_NO_LEADER_NO_OFFICER);
        } else {
            clan = this.getPlayersClan(invited);
            if (clan != null) {
                player.sendMessage(String.format(Lang.ERROR_CLAN_TARGET_PLAYER_IS_IN_CLAN, invited.getName()));
            } else {
                clan = this.getPlayersClan(player);
                int membersAndInvited = clan.getInvitedPlayers().size() + clan.getMembersNumber();
                if (membersAndInvited >= maxClanMembers) {
                    player.sendMessage(Lang.ERROR_MAX_PLAYERS_INVITED_OR_MEMBERS);
                } else if (clan.getSlots() > clan.getMembersNumber() && clan.getMembersNumber() < maxClanMembers) {
                    if (clan.getInvitedPlayers().contains(invited.getUniqueId())) {
                        if (clan.getInvitedPlayers().size() == 1) {
                            clan.setInvitedPlayers(new ArrayList());
                        } else {
                            clan.getInvitedPlayers().remove(invited.getUniqueId());
                        }
                        player.sendMessage(TextUtils.formatColor("&cYou have canceled the invitation to &b" + invited.getName() + "&e."));
                    } else {
                        clan.getInvitedPlayers().add(invited.getUniqueId());
                        getInvitationText(player.getName(), clan.getName()).send(invited);
                        clan.broadcast(ChatColor.YELLOW + "(Clan) " + ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has invited "
                                            + ChatColor.BLUE + invited.getName() + ChatColor.YELLOW + " to the clan.");
                        player.sendMessage(TextUtils.formatColor("&eYou have send the invation to &b" + invited.getName() + " &e."));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You can not invite this player because your clan has reached the membership limit.");
                }
            }
        }
    }

    public void deinviteAllPlayers(Player executor) {
        Clan clan = this.getPlayersClan(executor);
        if (clan == null) {
            executor.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (!clan.isLeader(executor.getUniqueId()) && !clan.isOfficer(executor.getUniqueId())) {
            executor.sendMessage(Lang.ERROR_CLAN_NO_LEADER_NO_OFFICER);
        } else {
            clan.clearInvitations();
            executor.sendMessage(Lang.SUCCESS_CLAN_INVITATIONS_CLEANED);
        }
    }



    public void joinClan(Player player, String clanName, boolean isForced) {
        Clan check = this.getPlayersClan(player);
        if (check != null) {
            player.sendMessage(ChatColor.RED + "You are already a member of a clan, you must leave it to enter a new one.");
        } else {
            Clan clan = this.getClanByName(clanName.toLowerCase());
            if (clan.isMember(player.getUniqueId(), player.getName())) {
                player.sendMessage(ChatColor.RED + "You are already a member of a clan, you must leave it to enter a new one.");
                return;
            }
            if (clan == null) {
                player.sendMessage(ChatColor.RED + "Clan not found.");
                return;
            }
            String msg;
            if (!isForced) {
                if (clan.getMembersNumber() >= maxClanMembers) {
                    player.sendMessage(ChatColor.RED + "You have not been able to join because this clan has reached the limit of members.");
                    return;
                }
                if (clan.requiresInvitation() && !clan.getInvitedPlayers().contains(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You need to be invited to join this clan.");
                    return;
                }
                msg = ChatColor.YELLOW + "(Clan) " + ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " is now a member.";
            } else {
                msg = ChatColor.YELLOW + "(Clan) " + ChatColor.RED + player.getName() + ChatColor.YELLOW + " has force joined to the clan.";
            }
            Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            if (profile == null) {
                player.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
                return;
            }
            PlayerJoinClanEvent event = new PlayerJoinClanEvent(player, clan, profile);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clan.addMember(player, profile);
                clan.getInvitedPlayers().remove(player.getUniqueId());
                if (!isForced && clan.getMembersNumber() >= clan.getSlots())
                    clan.getInvitedPlayers().clear();
                clan.broadcast(msg);
                player.sendMessage(ChatColor.GREEN + "You have joined to the clan: " + ChatColor.YELLOW + clanName + ChatColor.GREEN + ".");
            }
        }
    }

    public void leaveClan(Player player) {
        Clan clan = this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (clan.isLeader(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_CAN_NOT_LEAVE_WHILE_LEADER);
        } else {
            Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
            clan.removeMember(player, profile);
        }
    }

    public void asyncKickPlayer(Player player, OfflinePlayer target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                kickPlayer(player, target);
            }
        });
    }

    private void kickPlayer(Player player, OfflinePlayer target) {
        Clan clan = ClanManager.this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (target == null) {
            player.sendMessage(Lang.ERROR_PLAYER_HASNT_PLAYED_BEFORE);
        } else if (!clan.isLeader(player.getUniqueId()) && !clan.isOfficer(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_NO_LEADER_NO_OFFICER);
        } else if (clan.isLeader(target.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_CAN_NOT_KICK_LEADER);
        } else if (!clan.isLeader(player.getUniqueId()) && clan.isOfficer(target.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_CANNOT_KICK_OFFICERS);
        } else if (!clan.isMember(target.getUniqueId(), target.getName())) {
            player.sendMessage(Lang.ERROR_CLAN_TARGET_IS_NOT_IN_YOUR_CLAN);
        } else {
            Profile profile = ClanManager.this.plugin.getProfileHandler().getOfflineProfile(target, false);
            if (profile == null) {
                player.sendMessage(ChatColor.RED + "Check if player is not online. If is online: " + Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
            } else {
                PlayerKickedFromClanEvent playerKickedFromClanEvent = new PlayerKickedFromClanEvent(player, target, clan);
                Bukkit.getPluginManager().callEvent(playerKickedFromClanEvent);
                if (playerKickedFromClanEvent.isCancelled()) return;
                clan.kickMember(player, target, profile);
                player.sendMessage(String.format(Lang.SUCCESS_CLAN_PLAYER_KICKED_REQ_TARGETS_NAME, target.getName()));
                if (target.isOnline()) {
                    ((Player) target).sendMessage(ChatColor.RED + "You have been kicked from " + ChatColor.GRAY + clan.getName() + ChatColor.RED + " by " + player.getName());
                }
                Iterator var3 = clan.getAllMembers().iterator();

                while (var3.hasNext()) {
                    ClanMember clanMember1 = (ClanMember) var3.next();
                    Player player1 = Bukkit.getServer().getPlayer(clanMember1.getUniqueID());
                    if (player1 != null) player1.sendMessage(ChatColor.RED + "(Clan) " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " has kicked " + ChatColor.GRAY + target.getName() + ChatColor.RED + ".");
                }

            }
        }
    }

    public void asyncClanDisband(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                clanDisband(player);
            }
        });
    }


    private void clanDisband(Player player) {
        Clan clan = this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (!clan.isLeader(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_YOU_ARE_NOT_LEADER);
        } else {
            ClanDisbandEvent event = new ClanDisbandEvent(player, clan);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clan.broadcast(Lang.SUCCESS_CLAN_DISBANDED_BROADCAST);
                clan.getClanMembersAsPlayers().forEach(player1 -> clan.removeMember(player1, null));
                this.clansList.remove(clan.getName().toLowerCase(), clan);
                player.sendMessage(Lang.SUCCESS_CLAN_DISBANDED);
            }
        }
    }

    public void clanWithdraw(Player player, Double amount) {
        Clan clan = this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
        } else if (!clan.getLeader().getUniqueID().equals(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_CLAN_YOU_ARE_NOT_LEADER);
        } else if (clan.getClanBalance() < amount) {
            player.sendMessage(Lang.ERROR_CLAN_INSUFFICET_BANK_BALANCE);
        } else {
            clan.setClanBalance(clan.getClanBalance() - amount);
            this.plugin.getEconomyManager().deposit(player, amount);
            clan.broadcast(String.format(Lang.SUCCESS_CLAN_LEADER_WITHDRAW_BANK_BRADCAST_REQ_LEADERNAME_AMOUNT, player.getName(), amount));
            player.sendMessage(String.format(Lang.SUCCESS_CLAN_LEADER_WITHDRAW_BANK_MSG_REQ_AMOUNT, amount));
        }
    }

    public void clanDeposit(Player player, Double amount) {
        Clan clan = this.getPlayersClan(player);
        if (clan == null) {
            player.sendMessage(Lang.ERROR_CLAN_NOT_IN_CLAN);
            return;
        }
        if (amount <= 0) {
            player.sendMessage(Lang.ERROR_ECONOMY_INVALID_VALUE);
            return;
        }
        Profile profile = plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
        if (!profile.hasEnoughtMoneyToPay(amount)) {
            player.sendMessage(Lang.ERROR_ECONOMY_INSUFFICENT_MONEY);
        } else {
            clan.deposit(amount);
            profile.removeMoneyFromBalance(amount);
            player.sendMessage(String.format(Lang.SUCCESS_CLAN_DEPOSIT_BROADCAST_REQ_SENDERNAME_AMOUNT, player.getName(), amount));
        }
    }

    public Text getInvitationText(String sender, String clanName) {
        Text text = (new Text(sender)).setColor(ChatColor.YELLOW);
        text.append((new Text(" has invited you to ")).setColor(ChatColor.GREEN));
        text.append((new Text(clanName)).setColor(ChatColor.YELLOW));
        text.append(
                (new Text(" click here to join"))
                        .setColor(ChatColor.AQUA)
                        .setClick(ClickAction.RUN_COMMAND, "/clan join " + clanName)
                        .setHoverText(ChatColor.GREEN + "Click here to join the clan")
        );
        text.append((new Text(" or type ")).setColor(ChatColor.GREEN));
        text.append((new Text("/clan join")).setColor(ChatColor.YELLOW));
        text.append((new Text(" to accept the invitation.")).setColor(ChatColor.GREEN));
        return text;
    }

    public String levelInfo(Integer level) {
        String info = ChatColor.GRAY + "Modify your prefix clan color to:  ";
        if (level == 2) {
            info = info + ChatColor.YELLOW + "Yellow" + ChatColor.GRAY + ".";
        } else if (level == 3) {
            info = ChatColor.GRAY + "It will allow you to increase one slot to the maximum number of members.";
        } else if (level == 4) {
            info = info + ChatColor.BLUE + "Blue" + ChatColor.GRAY + ".";
        } else if (level == 5) {
            info = info + ChatColor.GREEN + "Green" + ChatColor.GRAY + ".";
        } else if (level == 6) {
            info = ChatColor.GRAY + "It will allow you to increase one slot to the maximum number of members.";
        } else if (level == 7) {
            info = info + "Add " + ChatColor.GREEN + "★★" + ChatColor.GRAY + " to your prefix.";
        } else if (level == 8) {
            info = ChatColor.GRAY + "It will allow you to increase one slot to the maximum number of members.";
        } else if (level == 9) {
            info = ChatColor.GRAY + "It will allow you to increase one slot to the maximum number of members.";
        } else if (level == 10) {
            info = info + "Add " + ChatColor.GREEN + "✩✩" + ChatColor.GRAY + " to your prefix.";
        } else {
            info = ChatColor.GRAY + "Your clan has reached the highest level.";
        }

        return info;
    }
}

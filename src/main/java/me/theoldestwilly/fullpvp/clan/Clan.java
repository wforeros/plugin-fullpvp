package me.theoldestwilly.fullpvp.clan;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.clan.events.PlayerKickedFromClanEvent;
import me.theoldestwilly.fullpvp.clan.events.PlayerLeaveClanEvent;
import me.theoldestwilly.fullpvp.scoreboard.ScoreboardManager;
import me.theoldestwilly.fullpvp.users.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class Clan implements ConfigurationSerializable {
    protected String name;
    protected List<ClanMember> members;
    protected List<ClanMember> officers;
    protected ClanMember leader;
    protected UUID uniqueID;
    protected Integer clanKills;
    protected Integer clanDeaths;
    protected Integer level;
    protected Integer slots;
    private Double kd;
    private boolean invitation;
    private boolean friendlyFire;
    private Double clanBalance;

    protected List<UUID> invitedPlayers;

    public Clan(String name, String uuid, Integer clanKills, Integer clanDeaths, Double clanBalance, Boolean invitation, Boolean friendlyFire, ClanMember leader, List officers, List members, Integer level) {
        this.members = new ArrayList();
        this.officers = new ArrayList();
        this.invitedPlayers = new ArrayList();
        this.name = name;
        this.uniqueID = UUID.fromString(uuid);
        this.leader = leader;
        this.clanKills = clanKills;
        this.clanDeaths = clanDeaths;
        this.setClanOfficers(officers);
        this.setClanMembers(members);
        this.kd = calcKd();
        this.invitation = invitation;
        this.friendlyFire = friendlyFire;
        this.clanBalance = clanBalance;
        this.level = level;
        this.setClanSlots(this.level);
        for (ClanMember clanMember : getAllMembers()) {
            OfflinePlayer offlinePlayer = null;
            if (clanMember.getUniqueID() != null) offlinePlayer = Bukkit.getOfflinePlayer(clanMember.getUniqueID());
            if (offlinePlayer == null && clanMember.getName() == null) Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Fatal error while loading a member of " + name + ".");
        }

    }

    public Clan(Map<String, Object> map) {
        this((String)map.get("name"), (String)map.get("uuid"), (Integer)map.get("kills"), (Integer)map.get("deaths"), (Double)map.get("balance"), (Boolean)map.get("invitation"), (Boolean)map.get("ffire"), (ClanMember)map.get("leader"), (List)map.get("officers"), (List)map.get("members"), (Integer)map.get("level"));
    }

    public Clan(String name, UUID leader, String playerName, Profile profile) {
        this.members = new ArrayList();
        this.officers = new ArrayList();
        this.invitedPlayers = new ArrayList();
        this.name = name;
        this.leader = new ClanMember(name, leader, playerName, ClanRank.LEADER);
        this.uniqueID = UUID.randomUUID();
        this.clanKills = 0;
        this.clanDeaths = 0;
        this.invitation = true;
        this.addPlayerStats(profile);
        this.friendlyFire = false;
        this.clanBalance = 0.0D;
        this.level = 1;
        this.slots = 3;
    }

    public Double calcKd() {
        return this.clanDeaths != 0 ? (Double.parseDouble(this.clanKills.toString()) / Double.parseDouble(this.clanDeaths.toString())) : 0.0D;
    }

    public Integer getMembersNumber() {
        return 1 + this.officers.size() + this.members.size();
    }

    public Boolean isMember(UUID uniqueId, String name) {
        boolean b = getAllMembers().stream().filter(clanMember ->
                clanMember.getUniqueID().equals(uniqueId))
                .findFirst().orElse(null) != null;
        if (!b) b = getAllMembers().stream().filter(clanMember ->
                clanMember.getName().equalsIgnoreCase(name)).findFirst().orElse(null) != null;
        return b;
    }

    public Boolean isMember(String name) {
        return getAllMembers().stream().filter(clanMember ->
                clanMember.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null) != null;
    }

    public void deposit(Double amount) {
        this.clanBalance += amount;
    }

    public ClanMember getMember(UUID uuid) {
        if (this.leader.getUniqueID().equals(uuid)) {
            return this.leader;
        } else {
            for (ClanMember member : this.officers) {
                if (member.getUniqueID().equals(uuid)) return member;
            }
            for (ClanMember member : this.members) {
                if (member.getUniqueID().equals(uuid)) return member;
            }
        }
        return null;
    }

    public ClanMember getMember(String name) {
        if (this.leader.getName().equalsIgnoreCase(name)) {
            return this.leader;
        } else {
            for (ClanMember member : this.officers) {
                if (member.getName().equalsIgnoreCase(name)) return member;
            }
            for (ClanMember member : this.members) {
                if (member.getName().equalsIgnoreCase(name)) return member;
            }
        }
        return null;
    }

    public List<Player> getClanMembersAsPlayers() {
        List<Player> players = new ArrayList<>();
        getAllMembers().forEach(clanMember -> {
            if(clanMember.isOnline()) players.add(Bukkit.getPlayer(clanMember.getUniqueID()));
        });
        return players;
    }


    public void setClanSlots(Integer level) {
        /*
        Aqui toca arreglar que aumente slots acorde al valor indicao como maximo para miembros puesto en config.yml
         */
        this.slots = level < 3 ? 3 : (level <= 5 ? 4 : (level <= 7 ? 5 : (level == 8 ? 6 : (level == 9 ? 7 : 8))));
    }

    public void rankUp() {
        Integer var2 = this.level;
        Integer var3 = this.level = this.level + 1;
        if (this.level == 3 || this.level == 6 || this.level == 8 || this.level == 9 || this.level == 10) {
            var2 = this.slots;
            var3 = this.slots = this.slots + 1;
        }

    }

    public String getMemPrefix(UUID uuid) {
        String prefix = ChatColor.GRAY + "[";
        if (this.getLeader().getUniqueID().equals(uuid)) {
            if (this.level == 1) {
                prefix = prefix + "**";
            } else if (this.level <= 3) {
                prefix = prefix + ChatColor.YELLOW + "**";
            } else if (this.level == 4) {
                prefix = prefix + ChatColor.BLUE + "**";
            } else if (this.level == 5) {
                prefix = prefix + ChatColor.GREEN + "**";
            } else if (this.level <= 6) {
                prefix = prefix + ChatColor.GREEN + "**";
            } else if (this.level <= 9) {
                prefix = prefix + ChatColor.GREEN + "" + ChatColor.BOLD + "★★" + ChatColor.GREEN;
            } else if (this.level == 10) {
                prefix = prefix + ChatColor.DARK_GREEN + "" + ChatColor.MAGIC + "ii" + ChatColor.GREEN + "" + ChatColor.BOLD + "★★" + this.name;
            }
        } else if (this.isOfficer(uuid)) {
            if (this.level == 1) {
                prefix = prefix + "*";
            } else if (this.level <= 3) {
                prefix = prefix + ChatColor.YELLOW + "*";
            } else if (this.level == 4) {
                prefix = prefix + ChatColor.BLUE + "*";
            } else if (this.level <= 6) {
                prefix = prefix + ChatColor.GREEN + "*";
            } else if (this.level <= 9) {
                prefix = prefix + ChatColor.GREEN + "" + ChatColor.BOLD + "★" + ChatColor.GREEN;
            } else if (this.level == 10) {
                prefix = prefix + ChatColor.DARK_GREEN + "" + ChatColor.MAGIC + "ii" + ChatColor.GREEN + "★" + this.name;
            }
        } else if (this.level == 1) {
            prefix = prefix + "";
        } else if (this.level <= 3) {
            prefix = prefix + ChatColor.YELLOW;
        } else if (this.level == 4) {
            prefix = prefix + ChatColor.BLUE;
        } else if (this.level <= 9) {
            prefix = prefix + ChatColor.GREEN;
        } else if (this.level == 10) {
            prefix = prefix + ChatColor.DARK_GREEN + "" + ChatColor.MAGIC + "ii" + ChatColor.GREEN + this.name;
        }

        if (this.level != 10) {
            prefix = prefix + this.name;
        } else {
            prefix = prefix + ChatColor.DARK_GREEN + "" + ChatColor.MAGIC + "ii";
        }

        prefix = prefix + ChatColor.GRAY + "] ";
        return prefix;
    }

    public List<ClanMember> getAllMembers() {
        List<ClanMember> members = new ArrayList();
        members.addAll(this.getOfficers());
        members.addAll(this.getMembers());
        members.add(this.leader);
        return members;
    }

    public void addPlayerStats(Profile profile) {
        this.clanKills = this.clanKills + profile.getKills();
        this.clanDeaths = this.clanDeaths + profile.getDeaths();
        if (this.clanDeaths != null && this.clanDeaths != null) {
            this.kd = calcKd();
        } else {
            this.kd = 0.0D;
        }

    }

    public void setLeader(UUID uuid) {
        ClanMember clanMember = this.getMember(uuid);
        this.members.remove(clanMember);
        this.officers.remove(clanMember);
        this.officers.add(leader);
        clanMember.setRank(ClanRank.LEADER);
        this.leader = clanMember;
    }

    public void setOfficer(UUID uuid) {
        ClanMember clanMember = this.getMember(uuid);
        this.members.remove(clanMember);
        clanMember.setRank(ClanRank.OFFICER);
        this.officers.add(clanMember);
    }

    public void setMember(UUID uuid) {
        ClanMember clanMember = this.getMember(uuid);
        clanMember.setRank(ClanRank.MEMBER);
        this.officers.remove(uuid);
        this.members.add(clanMember);
    }

    public void removePlayerStats(Profile profile) {
        this.clanKills = this.clanKills - profile.getKills();
        this.clanDeaths = this.clanDeaths - profile.getDeaths();
        this.kd = calcKd();
    }

    public void setClanMembers(List object) {
        if (object != null) {
            Iterator var2 = object.iterator();

            while(var2.hasNext()) {
                Object o = var2.next();
                if (o instanceof ClanMember) {
                    this.members.add((ClanMember)o);
                }
            }
        } else {
            this.members = new ArrayList();
        }

    }

    public void setClanOfficers(List object) {
        if (object != null) {
            Iterator var2 = object.iterator();

            while(var2.hasNext()) {
                Object o = var2.next();
                if (o instanceof ClanMember) {
                    this.officers.add((ClanMember)o);
                }
            }
        } else {
            this.officers = new ArrayList();
        }

    }

    public Boolean requiresInvitation() {
        return this.invitation;
    }

    /**
     *
     * This method is called when a player join/leave the clan
     * @param player Player who is joining/leaving the clan
     */
    public void updateLabelRespectToThePlayer(Player player) {
        ScoreboardManager manager = FullPvP.getInstance().getScoreboardManager();
        manager.getPlayerScoreboard(player).updateNametags(getClanMembersAsPlayers());
        getClanMembersAsPlayers().forEach(player1 -> manager.getPlayerScoreboard(player1).updateNametags(player));
    }

    public void addMember(Player player, Profile profile) {
        addMember(player, profile, ClanRank.MEMBER);
    }

    public void addMember(Player player, Profile profile, ClanRank rank) {
        this.getMembers().add(new ClanMember(this.getName(), profile.getUniqueId(), player.getName(), rank));
        this.addPlayerStats(profile);
        updateLabelRespectToThePlayer(player);
    }

    //Mejorar el remove member y el kick member

    /**
     *
     * Remove a clan member, use it if player is leaving the clan or
     * it is disbanded
     * It calls "PlayerLeaveClanEvent"
     * @param player Player which is going to be removed
     * @param profile Player's profile, if clan is disbanded use null
     */
    public void removeMember(OfflinePlayer player, Profile profile) {
        PlayerLeaveClanEvent playerLeaveClanEvent = new PlayerLeaveClanEvent(player.getPlayer(), this);
        Bukkit.getPluginManager().callEvent(playerLeaveClanEvent);
        if (!playerLeaveClanEvent.isCancelled()) {
            ClanMember clanMember = this.getMember(player.getUniqueId());
            if (clanMember.getRank() == ClanRank.MEMBER) {
                if (this.members.size() == 1) {
                    this.members = new ArrayList();
                } else {
                    this.members.remove(clanMember);
                }
            } else if (this.officers.size() == 1) {
                this.officers = new ArrayList();
            } else {
                this.officers.remove(clanMember);
            }
            Player online = null;
            if (player.isOnline()) {
                online = player.getPlayer();
                updateLabelRespectToThePlayer(player.getPlayer());
            }
            if (profile != null) {
                this.removePlayerStats(profile);
                if (online != null) online.sendMessage(Lang.SUCCESS_CLAN_LEAVED);
                broadcast(String.format(Lang.SUCCES_CLAN_LEAVED_BROADCAST_REQ_PLAYERNAME, player.getName()));
            }
        }
    }

    /**
     *
     * Use it only to kick a member, it calls "PlayerKickedFromClanEvent"
     * @param kicker Player who kicked a member, usually the clan's leader
     * @param target Kicked player
     * @param targetProfile Kicked profile
     */
    public void kickMember(Player kicker, OfflinePlayer target, Profile targetProfile) {
        ClanMember clanMember = this.getMember(target.getUniqueId());
        if (clanMember == null) clanMember = getMember(target.getName());
        if (clanMember == null) {
            kicker.sendMessage(ChatColor.RED + "Error loading clan member, report it to high staff.");
            return;
        }
        if (clanMember.getRank() == ClanRank.MEMBER) {
            if (this.members.size() == 1) {
                this.members = new ArrayList();
            } else {
                this.members.remove(clanMember);
            }
        } else if (this.officers.size() == 1) {
            this.officers = new ArrayList();
        } else {
            this.officers.remove(clanMember);
        }
        if (target.isOnline())
            updateLabelRespectToThePlayer(target.getPlayer());
        this.removePlayerStats(targetProfile);
    }



    public void promoteMember(UUID uuid) {
        ClanMember clanMember = this.getMember(uuid);
        if (clanMember.getRank() == ClanRank.MEMBER) {
            this.getMembers().remove(clanMember);
            this.getOfficers().add(clanMember);
            clanMember.setRank(ClanRank.OFFICER);
            Iterator var3 = this.getAllMembers().iterator();

            while(var3.hasNext()) {
                ClanMember clanMember1 = (ClanMember)var3.next();
                Player player1 = Bukkit.getServer().getPlayer(clanMember1.getUniqueID());
                if (player1 != null) {
                    player1.sendMessage(ChatColor.YELLOW + "The player " + ChatColor.BLUE + clanMember.getName() + ChatColor.YELLOW + " is now an " + ChatColor.GREEN + " oficial" + ChatColor.YELLOW + " of the clan.");
                }
            }
        }

    }

    public void demoteMember(UUID uuid) {
        ClanMember clanMember = this.getMember(uuid);
        if (clanMember.getRank() == ClanRank.OFFICER) {
            this.getMembers().add(clanMember);
            this.getOfficers().remove(clanMember);
            clanMember.setRank(ClanRank.MEMBER);
            Iterator var3 = this.getAllMembers().iterator();

            while(var3.hasNext()) {
                ClanMember clanMember1 = (ClanMember)var3.next();
                Player player1 = Bukkit.getServer().getPlayer(clanMember1.getUniqueID());
                if (player1 != null) {
                    player1.sendMessage(ChatColor.RED + "The player " + ChatColor.YELLOW + clanMember.getName() + ChatColor.RED + " was demoted to " + ChatColor.GRAY + " member" + ChatColor.RED + " of the clan.");
                }
            }
        }

    }

    public Boolean isOfficer(UUID uniqueID) {
        ClanMember clanMember = this.getMember(uniqueID);
        return clanMember != null && clanMember.getRank() == ClanRank.OFFICER;
    }

    public Boolean isLeader(UUID uniqueID) {
        return this.leader.getUniqueID().equals(uniqueID);
    }

    public Boolean isNormalMember(UUID uniqueID) {
        ClanMember clanMember = this.getMember(uniqueID);
        return clanMember.getRank() == ClanRank.MEMBER;
    }

    public void addKill() {
        clanKills++;
    }

    public void addDeath() {
        clanDeaths++;
    }

    public void broadcast(String msg) {
        getAllMembers().forEach(clanMember -> {
            Player player = Bukkit.getPlayer(clanMember.getUniqueID());
            if (player != null) player.sendMessage(msg);
        });
    }

    public void sendChatMessage(String sender, String msg) {
        broadcast(ChatColor.DARK_GREEN + "(Clan) " + sender + ChatColor.GRAY + ": " + ChatColor.YELLOW + msg);
    }

    public void clearInvitations() {
        invitedPlayers.clear();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("name", this.name);
        map.put("uuid", this.uniqueID.toString());
        map.put("level", this.level);
        map.put("balance", this.clanBalance);
        map.put("leader", this.leader);
        map.put("officers", this.officers);
        map.put("members", this.members);
        map.put("kills", this.clanKills);
        map.put("deaths", this.clanDeaths);
        map.put("invitation", this.invitation);
        map.put("ffire", this.friendlyFire);
        return map;
    }

    public String getName() {
        return this.name;
    }

    public List<ClanMember> getMembers() {
        return this.members;
    }

    public List<ClanMember> getOfficers() {
        return this.officers;
    }

    public ClanMember getLeader() {
        return this.leader;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public Integer getClanKills() {
        return this.clanKills;
    }

    public Integer getClanDeaths() {
        return this.clanDeaths;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Integer getSlots() {
        return this.slots;
    }

    public Double getKd() {
        return this.kd;
    }

    public boolean isInvitation() {
        return this.invitation;
    }

    public boolean isFriendlyFire() {
        return this.friendlyFire;
    }

    public Double getClanBalance() {
        return this.clanBalance;
    }

    public List<UUID> getInvitedPlayers() {
        return this.invitedPlayers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<ClanMember> members) {
        this.members = members;
    }

    public void setOfficers(List<ClanMember> officers) {
        this.officers = officers;
    }

    public void setUniqueID(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setClanKills(Integer clanKills) {
        this.clanKills = clanKills;
    }

    public void setClanDeaths(Integer clanDeaths) {
        this.clanDeaths = clanDeaths;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public void setKd(Double kd) {
        this.kd = kd;
    }

    public void setInvitation(boolean invitation) {
        this.invitation = invitation;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public void setClanBalance(Double clanBalance) {
        this.clanBalance = clanBalance;
    }

    public void setInvitedPlayers(List<UUID> invitedPlayers) {
        this.invitedPlayers = invitedPlayers;
    }

}

package me.theoldestwilly.fullpvp.clan;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ClanMember implements ConfigurationSerializable {
    protected String clan;
    protected ClanRank rank;
    protected String name;
    protected boolean atClanChat;
    protected UUID uniqueID;

    public ClanMember(String clan, String uuid, String name, String rank, Boolean clanChat) {
        this.clan = clan;
        this.rank = ClanRank.valueOf(rank);
        this.atClanChat = Boolean.valueOf(clanChat);
        this.uniqueID = UUID.fromString(uuid);
        this.name = name;
    }

    public ClanMember(Map<String, Object> map) {
        this((String) map.get("clan"), (String) map.get("uuid"), (String) map.get("name"), (String) map.get("rank"), (Boolean) map.get("clanChat"));
    }

    public ClanMember(String clan, UUID uuid, String name, ClanRank rank) {
        this.clan = clan;
        this.uniqueID = uuid;
        this.rank = rank;
        this.atClanChat = false;
        this.name = name;
    }

    public void setRank(ClanRank rank) {
        this.rank = rank;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap();
        map.put("uuid", this.uniqueID.toString());
        map.put("name", this.name);
        map.put("clan", this.clan);
        map.put("rank", this.rank.toString());
        map.put("clanChat", this.atClanChat);
        return map;
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(uniqueID) != null;
    }

    public String getClan() {
        return this.clan;
    }

    public ClanRank getRank() {
        return this.rank;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAtClanChat() {
        return this.atClanChat;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAtClanChat(boolean atClanChat) {
        this.atClanChat = atClanChat;
    }

    public void setUniqueID(UUID uniqueID) {
        this.uniqueID = uniqueID;
    }
}

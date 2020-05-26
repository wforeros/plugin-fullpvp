package me.theoldestwilly.fullpvp.mongo;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.theoldestwilly.fullpvp.FullPvP;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;


public class MongoManager {
    private FullPvP plugin;
    private DBCollection players;
    private MongoDatabase database;
    private MongoClient client;
    private String alias = "mongodb";

    public MongoManager(FullPvP plugin) {
        this.plugin = plugin;
        Configuration configuration = plugin.getConfig();
        String ip = configuration.getString(alias + ".ip"),
                databaseName = configuration.getString(alias + ".database-name");
        client = new MongoClient(ip);
        database = client.getDatabase(databaseName);
        if (database != null) {
            Bukkit.getConsoleSender().sendMessage("Database loaded");
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoClient getClient() {
        return client;
    }
}

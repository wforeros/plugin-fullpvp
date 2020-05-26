package me.theoldestwilly.fullpvp.utilities;


import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config extends YamlConfiguration {
    private final JavaPlugin plugin;
    private final String fileName;

    public Config(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
    }

    public Config(JavaPlugin plugin, String fileName, String fileExtension) {
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    private void createFile() {
        File folder = this.plugin.getDataFolder();

        try {
            File file = new File(folder, this.fileName);
            if (!file.exists()) {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                } else {
                    this.save(file);
                }

                this.load(file);
            } else {
                this.load(file);
                this.save(file);
            }
        } catch (InvalidConfigurationException | IOException var3) {
            var3.printStackTrace();
        }

    }

    public void save() {
        File folder = this.plugin.getDataFolder();

        try {
            this.save(new File(folder, this.fileName));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Config)) {
            return false;
        } else {
            Config other = (Config)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$plugin = this.getPlugin();
                Object other$plugin = other.getPlugin();
                if (this$plugin == null) {
                    if (other$plugin != null) {
                        return false;
                    }
                } else if (!this$plugin.equals(other$plugin)) {
                    return false;
                }

                Object this$fileName = this.getFileName();
                Object other$fileName = other.getFileName();
                if (this$fileName == null) {
                    if (other$fileName != null) {
                        return false;
                    }
                } else if (!this$fileName.equals(other$fileName)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Config;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $plugin = this.getPlugin();
        result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
        Object $fileName = this.getFileName();
        result = result * 59 + ($fileName == null ? 43 : $fileName.hashCode());
        return result;
    }
}

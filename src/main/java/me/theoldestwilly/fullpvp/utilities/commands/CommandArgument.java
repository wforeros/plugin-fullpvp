package me.theoldestwilly.fullpvp.utilities.commands;


import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CommandArgument {
    protected boolean isPlayerOnly;
    private String name;
    protected String description;
    protected String permission;
    protected String[] aliases;

    public CommandArgument(String name, String description) {
        this(name, description, (String)null);
    }

    public CommandArgument(String name, String description, String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public CommandArgument(String name, String description, String[] aliases) {
        this(name, description, (String)null, aliases);
    }

    public CommandArgument(String name, String description, String permission, String[] aliases) {
        this.isPlayerOnly = false;
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = (String[])Arrays.copyOf(aliases, aliases.length);
    }

    public String getName() {
        return this.name;
    }

    public boolean isPlayerOnly() {
        return this.isPlayerOnly;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getAliases() {
        if (this.aliases == null) {
            this.aliases = ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return (String[])Arrays.copyOf(this.aliases, this.aliases.length);
    }

    public abstract String getUsage(String var1);

    public abstract boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4);

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        return Collections.emptyList();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof CommandArgument)) {
            return false;
        } else {
            CommandArgument that = (CommandArgument)o;
            if (this.name != null) {
                if (!this.name.equals(that.name)) {
                    return false;
                }
            } else if (that.name != null) {
                return false;
            }

            label41: {
                if (this.description != null) {
                    if (this.description.equals(that.description)) {
                        break label41;
                    }
                } else if (that.description == null) {
                    break label41;
                }

                return false;
            }

            if (this.permission != null) {
                if (!this.permission.equals(that.permission)) {
                    return false;
                }
            } else if (that.permission != null) {
                return false;
            }

            if (!Arrays.equals(this.aliases, that.aliases)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
        result = 31 * result + (this.permission != null ? this.permission.hashCode() : 0);
        result = 31 * result + (this.aliases != null ? Arrays.hashCode(this.aliases) : 0);
        return result;
    }
}

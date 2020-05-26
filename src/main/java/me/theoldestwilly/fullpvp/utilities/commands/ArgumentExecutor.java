package me.theoldestwilly.fullpvp.utilities.commands;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ArgumentExecutor implements CommandExecutor, TabCompleter {
    protected List<CommandArgument> arguments = new ArrayList();
    protected String[] aliases;
    protected String label;

    public ArgumentExecutor(String label) {
        this.label = label;
        this.aliases = null;
    }

    public ArgumentExecutor(String label, String[] aliases) {
        this.label = label;
        this.aliases = aliases;
    }

    public boolean containsArgument(CommandArgument argument) {
        return this.arguments.contains(argument);
    }

    public void addArgument(CommandArgument argument) {
        this.arguments.add(argument);
    }

    public void removeArgument(CommandArgument argument) {
        this.arguments.remove(argument);
    }

    public CommandArgument getArgument(String id) {
        Iterator var2 = this.arguments.iterator();

        CommandArgument argument;
        String name;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            argument = (CommandArgument) var2.next();
            name = argument.getName();
        } while (!name.equalsIgnoreCase(id) && !Arrays.asList(argument.getAliases()).contains(id.toLowerCase()));

        return argument;
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public List<CommandArgument> getArguments() {
        return ImmutableList.copyOf(this.arguments);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length == 0) {
            String subCommands = "";
            Iterator var6 = this.arguments.iterator();

            while (true) {
                CommandArgument argument;
                String permission;
                do {
                    if (!var6.hasNext()) {
                        if (subCommands.length() > 1) {
                            subCommands = subCommands.substring(0, subCommands.length() - 2);
                        } else {
                            subCommands = "None";
                        }

                        sender.sendMessage(ChatColor.RED + "Availables sub-completers for '" + label + "' " + ChatColor.GRAY + subCommands + ChatColor.RED + ".");
                        sender.sendMessage(ChatColor.RED + "You must specify a sub-command.");
                        return true;
                    }

                    argument = (CommandArgument) var6.next();
                    permission = argument.getPermission();
                } while (permission != null && !sender.hasPermission(permission));

                subCommands = subCommands + argument.getName() + ", ";
            }
        } else {
            CommandArgument argument = this.getArgument(arguments[0]);
            String permission = argument == null ? null : argument.getPermission();
            if (argument != null && (permission == null || sender.hasPermission(permission))) {
                argument.onCommand(sender, command, label, arguments);
            } else {
                sender.sendMessage(ChatColor.RED + WordUtils.capitalizeFully(label) + " sub-command '" + arguments[0] + "' not found.");
            }
        }

        return true;
    }

    public static List<String> addCompletions(String[] arguments, List<String> input) {
        return addCompletions(arguments, input, 80);
    }

    public static List<String> addCompletions(String[] arguments, List<String> input, int limit) {
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(arguments.length != 0);
        String argument = arguments[arguments.length - 1];
        return (List) input.stream().filter((string) -> {
            return string.regionMatches(true, 0, argument, 0, argument.length());
        }).limit((long) limit).collect(Collectors.toList());
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        List<String> results = new ArrayList();
        if (arguments.length < 2) {
            Iterator var6 = this.arguments.iterator();

            while (true) {
                CommandArgument argument;
                String permission;
                do {
                    if (!var6.hasNext()) {
                        return addCompletions(arguments, (List) results);
                    }

                    argument = (CommandArgument) var6.next();
                    permission = argument.getPermission();
                } while (permission != null && !sender.hasPermission(permission));

                ((List) results).add(argument.getName());
            }
        } else {
            CommandArgument argument = this.getArgument(arguments[0]);
            if (argument == null) {
                return (List) results;
            }

            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                results = argument.onTabComplete(sender, command, label, arguments);
                if (results == null) {
                    return null;
                }
            }
        }

        return addCompletions(arguments, (List) results);
    }
}



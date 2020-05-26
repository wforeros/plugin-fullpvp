package me.theoldestwilly.fullpvp.clan.command.arguments;

import java.util.Collections;
import java.util.List;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.utilities.commands.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanInviteArgument extends CommandArgument {
    private final FullPvP plugin;

    public ClanInviteArgument(FullPvP plugin) {
        super("invite", "Invites a player to your clan.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <player>";
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            if (arguments.length < 2) {
                sender.sendMessage(Lang.SHOW_COMMAND_USAGE(getUsage(label)));
            } else {
                Player player = Bukkit.getServer().getPlayer(arguments[1]);
                if (player == null) {
                    sender.sendMessage(Lang.ERROR_GLOBAL_PLAYER_NOT_ONLINE);
                    return true;
                }

                this.plugin.getClanManager().invitePlayer((Player)sender, player);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? null : Collections.emptyList();
    }
}

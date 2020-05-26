package me.theoldestwilly.fullpvp.clan.command;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.clan.command.arguments.*;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanCommand extends ArgumentExecutor {
    private FullPvP plugin;
    public ClanCommand(FullPvP plugin) {
        super("clan", new String[]{"clans"});
        this.plugin = plugin;
        addArgument(new ClanCancelInvitationsArgument(plugin));
        addArgument(new ClanChatArgument(plugin));
        addArgument(new ClanCreateArgument(plugin));
        addArgument(new ClanDemoteArgument(plugin));
        addArgument(new ClanDepositArgument(plugin));
        addArgument(new ClanDisbandArgument(plugin));
        addArgument(new ClanForceJoinArgument(plugin));
        addArgument(new ClanForceLeaderArgument(plugin));
        addArgument(new ClanForcePromoteArgument(plugin));
        addArgument(new ClanInviteArgument(plugin));
        addArgument(new ClanJoinArgument(plugin));
        addArgument(new ClanKickArgument(plugin));
        addArgument(new ClanLeaderArgument(plugin));
        addArgument(new ClanLeaveArgument(plugin));
        addArgument(new ClanPrivacityArgument(plugin));
        addArgument(new ClanPromoteArgument(plugin));
        addArgument(new ClanPvpArgument(plugin));
        addArgument(new ClanRenameArgument(plugin));
        addArgument(new ClanSetLevelArgument(plugin));
        addArgument(new ClanShowArgument(plugin));
        addArgument(new ClanWithdrawArgument(plugin));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                plugin.getPlayersCommandsManager().sendClanHelp(player, this);
            } else {
                super.onCommand(sender, command, label, args);
            }
        } else {
            super.onCommand(sender, command, label, args);
        }

        return true;
    }
}

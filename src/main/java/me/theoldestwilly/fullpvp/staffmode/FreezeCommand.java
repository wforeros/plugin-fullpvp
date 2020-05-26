package me.theoldestwilly.fullpvp.staffmode;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.chat.TextUtils;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.utilities.theoldestwilly.PluginError;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FreezeCommand  extends ArgumentExecutor {
    private final FullPvP plugin;

    public FreezeCommand(FullPvP plugin) {
        super("freeze", new String[]{"ss", "screenshare"});
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String laber, String[] arguments) {
        if (!(sender instanceof Player) || PermissionsManager.hasStaffPermission((Player) sender)) {
            if (arguments != null && arguments.length == 0) {
                sender.sendMessage(ChatColor.RED + "You must specify an online player.");
                return true;
            }
            Player target = Bukkit.getPlayer(arguments[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player with name " + arguments[0] + " not found.");
            }
            Profile targetProfile = plugin.getProfileHandler().getProfileByUniqueID(target.getUniqueId());
            if (targetProfile == null) {
                sender.sendMessage(Lang.ERROR_COMMON_MESSAGE(PluginError.ERROR_LOADING_TARGETS_PROFILE));
                return true;
            }
            if (targetProfile.isFrozen()) {
                targetProfile.setFrozen(false);
                Command.broadcastCommandMessage(sender, TextUtils.formatColor("&f" + target.getName() + " &6is not longer frozen."));
            } else if (PermissionsManager.hasStaffPermission(target)) {
                sender.sendMessage(TextUtils.formatColor("&c" + target.getName() + " is an Staff Member and can not be frozen."));
            } else {
                targetProfile.setFrozen(true);
                Command.broadcastCommandMessage(sender, TextUtils.formatColor("&f" + target.getName() + " &6is now frozen."));
            }
        } else {
            sender.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

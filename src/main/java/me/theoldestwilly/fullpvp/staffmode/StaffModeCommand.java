package me.theoldestwilly.fullpvp.staffmode;

import me.theoldestwilly.fullpvp.FullPvP;
import me.theoldestwilly.fullpvp.Lang;
import me.theoldestwilly.fullpvp.PermissionsManager;
import me.theoldestwilly.fullpvp.users.Profile;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand extends ArgumentExecutor {
    private final FullPvP plugin;

    public StaffModeCommand(FullPvP plugin) {
        super("mod", new String[]{"staff", "staffmode"});
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String laber, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (PermissionsManager.hasStaffPermission(player)) {
                Profile profile = this.plugin.getProfileHandler().getProfileByUniqueID(player.getUniqueId());
                if (profile.isInStaffMode()) profile.setInStaffMode(false, true);
                else profile.setInStaffMode(true, true);
            } else {
                sender.sendMessage(Lang.ERROR_INSUFFICENT_PERMISSIONS);
            }
        } else {
            sender.sendMessage(Lang.CONSOLE_NOT_ALLOWED);
        }
        return true;
    }
}


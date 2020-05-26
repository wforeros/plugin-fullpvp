package me.theoldestwilly.fullpvp.utilities.chat;


import org.bukkit.Bukkit;

public class CommonMessages {
    public CommonMessages() {
    }

    public static void enableMsg() {
        Bukkit.getServer().getConsoleSender().sendMessage(new String[]{" ", " "});
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor("&7&l&m-------------------------------"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor("&6&lStaffMode Activated"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &e&lDeveloper: &fTheOldestWilly"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &e&lCredits: &fItsArslann"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &c&lDo not distribute without autorization"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &c&lDo not use without autorization"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor("&7&l&m-------------------------------"));
        Bukkit.getServer().getConsoleSender().sendMessage(new String[]{" ", " "});
    }

    public static void disableMsg() {
        Bukkit.getServer().getConsoleSender().sendMessage(new String[]{" ", " "});
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor("&7&l&m---------------------------------------"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor("&c&lStaffMode Desactivated"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &e&lDeveloper: &fTheOldestWilly"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &e&lCredits: &fItsArslann"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &e&lDo not distribute without autorization"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor(" &c&lDo not use without autorization"));
        Bukkit.getServer().getConsoleSender().sendMessage(TextUtils.formatColor("&7&l&m---------------------------------------"));
        Bukkit.getServer().getConsoleSender().sendMessage(new String[]{" ", " "});
    }
}

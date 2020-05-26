package me.theoldestwilly.fullpvp.utilities.chat;


import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class Text extends ChatComponentText {
    public Text() {
        super("");
    }

    public Text(String string) {
        super(string);
    }

    public Text(Object object) {
        super(String.valueOf(object));
    }

    public Text append(Object object) {
        return this.append(String.valueOf(object));
    }

    public Text append(String text) {
        return (Text)this.a(text);
    }

    public Text setClick1(ClickAction action, String value) {
        this.getChatModifier().setChatClickable(new ChatClickable(action.getNMS(), value));
        return this;
    }

    public Text append(IChatBaseComponent node) {
        return (Text)this.addSibling(node);
    }

    public Text append(IChatBaseComponent... nodes) {
        IChatBaseComponent[] var2 = nodes;
        int var3 = nodes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            IChatBaseComponent node = var2[var4];
            this.addSibling(node);
        }

        return this;
    }

    public Text setBold(boolean bold) {
        this.getChatModifier().setBold(bold);
        return this;
    }

    public Text setItalic(boolean italic) {
        this.getChatModifier().setItalic(italic);
        return this;
    }

    public Text setUnderline(boolean underline) {
        this.getChatModifier().setUnderline(underline);
        return this;
    }

    public Text setRandom(boolean random) {
        this.getChatModifier().setRandom(random);
        return this;
    }

    public Text setStrikethrough(boolean strikethrough) {
        this.getChatModifier().setStrikethrough(strikethrough);
        return this;
    }

    public Text setColor(ChatColor color) {
        this.getChatModifier().setColor(EnumChatFormat.valueOf(color.name()));
        return this;
    }

    public Text setClick(ClickAction action, String value) {
        this.getChatModifier().setChatClickable(new ChatClickable(action.getNMS(), value));
        return this;
    }

    public Text setHover(HoverAction action, IChatBaseComponent value) {
        this.getChatModifier().setChatHoverable(new ChatHoverable(action.getNMS(), value));
        return this;
    }

    public Text setHoverText(String text) {
        return this.setHover(HoverAction.SHOW_TEXT, new Text(text));
    }

    public Text reset() {
        ChatUtil.reset(this);
        return this;
    }

    public IChatBaseComponent f() {
        return this.h();
    }

    public String toRawText() {
        return this.c();
    }

    public void send(CommandSender sender) {
        ChatUtil.send(sender, this);
    }

    public void broadcast() {
        this.broadcast((String)null);
    }

    public void broadcast(String permission) {
        Iterator var2 = Bukkit.getServer().getOnlinePlayers().iterator();

        while(true) {
            Player player;
            do {
                if (!var2.hasNext()) {
                    this.send(Bukkit.getConsoleSender());
                    return;
                }

                player = (Player)var2.next();
            } while(permission != null && !player.hasPermission(permission));

            this.send(player);
        }
    }
}

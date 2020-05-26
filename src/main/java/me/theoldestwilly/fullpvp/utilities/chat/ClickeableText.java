package me.theoldestwilly.fullpvp.utilities.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public class ClickeableText {
    public ClickeableText() {
    }

    public static TextComponent ClickeableMessageRunCommand(String msg, String cmd, String desc) {
        TextComponent textComponent = new TextComponent(msg);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
        textComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.GREEN + desc)).create()));
        return textComponent;
    }
}
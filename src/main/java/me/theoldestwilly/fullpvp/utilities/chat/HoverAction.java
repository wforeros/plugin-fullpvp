package me.theoldestwilly.fullpvp.utilities.chat;

import net.minecraft.server.v1_8_R3.ChatHoverable;

public enum HoverAction {
    SHOW_TEXT(ChatHoverable.EnumHoverAction.SHOW_TEXT),
    SHOW_ITEM(ChatHoverable.EnumHoverAction.SHOW_ITEM),
    SHOW_ACHIEVEMENT(ChatHoverable.EnumHoverAction.SHOW_ACHIEVEMENT);

    private final ChatHoverable.EnumHoverAction hoverAction;

    private HoverAction(ChatHoverable.EnumHoverAction hoverAction) {
        this.hoverAction = hoverAction;
    }

    public ChatHoverable.EnumHoverAction getNMS() {
        return this.hoverAction;
    }
}

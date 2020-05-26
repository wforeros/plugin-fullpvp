package me.theoldestwilly.fullpvp.utilities.chat;


import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class ChatUtil {
    public ChatUtil() {
    }

    public static String getName(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().hasKeyOfType("display", 10)) {
            NBTTagCompound nbttagcompound = stack.getTag().getCompound("display");
            if (nbttagcompound.hasKeyOfType("Name", 8)) {
                return nbttagcompound.getString("Name");
            }
        }

        return stack.getItem().a(stack) + ".name";
    }

    public static Text addItem(org.bukkit.inventory.ItemStack itemStack, Text text) {
        text.append((new Text(itemStack.getAmount() > 1 ? "x" + itemStack.getAmount() : "")).setColor(ChatColor.GRAY));
        text.append(new Text(" " + itemStack.getItemMeta().getDisplayName())).setColor(ChatColor.GRAY);
        text.setHoverText(ChatColor.LIGHT_PURPLE + itemStack.getItemMeta().getDisplayName());
        if (itemStack.getEnchantments() != null) {
            Map<Enchantment, Integer> enchants = itemStack.getEnchantments();
            Iterator var3 = enchants.keySet().iterator();

            while (var3.hasNext()) {
                Enchantment enchantment = (Enchantment) var3.next();
                text.setHoverText(enchantment.getName() + enchants.get(enchantment)).setColor(ChatColor.GRAY);
            }
        }

        return text;
    }

    public static void reset(IChatBaseComponent text) {
        ChatModifier modifier = text.getChatModifier();
        modifier.setChatClickable((ChatClickable) null);
        modifier.setBold(false);
        modifier.setColor(EnumChatFormat.RESET);
        modifier.setItalic(false);
        modifier.setRandom(false);
        modifier.setStrikethrough(false);
        modifier.setUnderline(false);
    }

    public static void send(CommandSender sender, IChatBaseComponent text) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Packet packet = new PacketPlayOutChat(text);
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            entityPlayer.playerConnection.sendPacket(packet);
        } else {
            sender.sendMessage(text.c());
        }

    }
}

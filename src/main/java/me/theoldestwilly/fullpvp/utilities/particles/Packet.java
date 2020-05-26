package me.theoldestwilly.fullpvp.utilities.particles;

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class Packet {
    public Packet() {
    }

    public static void sendTntTagParticles(PacketPlayOutWorldParticles packet, Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();
        while(var2.hasNext()) {
            Player p = (Player)var2.next();
            if (player != p && p.canSee(player)) {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
            }
        }

    }
}

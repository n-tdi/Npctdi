package world.ntdi.npctdi.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import world.ntdi.npctdi.Npctdi;

import java.util.UUID;

public class createNpc {
    public static void makeNPC(Player p, String name) {
        CraftPlayer cp = (CraftPlayer) p;
        ServerPlayer sp = cp.getHandle();

        MinecraftServer server = sp.getServer();
        ServerLevel level = sp.getLevel();
        UUID uuid = UUID.randomUUID();
        GameProfile gameProfile = new GameProfile(uuid, ChatColor.translateAlternateColorCodes('&', "&cPenis Man"));

        String suuid = SessionRequest.ignToUUID(name);

        JSONArray array = SessionRequest.getJsonArray(suuid);
        String value = SessionRequest.getValue(array);
        String sig = SessionRequest.getSig(array);

        gameProfile.getProperties().put("textures", new Property("textures", value, sig));

        ServerPlayer npc = new ServerPlayer(server, level, gameProfile, null);
        npc.setInvulnerable(true);
        npc.setPos(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
        npc.setYRot((p.getLocation().getPitch()%360)/360 * 255);
        npc.setXRot((p.getLocation().getYaw()%360)/360 * 255);

        for (Player plr : Bukkit.getOnlinePlayers()) {
            ServerPlayer splr = ((CraftPlayer) plr).getHandle();
            ServerGamePacketListenerImpl ps = splr.connection;

            // Player info Packet (Add, so we can use spawn player)
            // https://wiki.vg/Protocol#Player_Info

            ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));

            // Spawn Player Packet
            // https://wiki.vg/Protocol#Spawn_Player

            ps.send(new ClientboundAddPlayerPacket(npc));

            // Player info Packet (Remove, so we can remove them from tab completions and tablist) breaks skins if not runnable
            // https://wiki.vg/Protocol#Player_Info
            new BukkitRunnable() {
                @Override
                public void run() {
                    ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
                }
            }.runTaskLater(Npctdi.getInstance(), 10);
        }
    }
}

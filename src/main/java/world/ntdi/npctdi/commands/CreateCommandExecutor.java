package world.ntdi.npctdi.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import world.ntdi.npctdi.Npctdi;
import world.ntdi.npctdi.util.ArgsBuilder;
import world.ntdi.npctdi.util.SessionRequest;

import java.io.IOException;
import java.util.UUID;

public class CreateCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length >= 2) {
                String name = ArgsBuilder.buildString(args, 1);
                if (name.length() < 17) {
                    p.sendMessage(ChatColor.RED + "Name must be less than 17 characters long.");
                    return true;
                }

                for (String key : Npctdi.config.getConfigurationSection("npcs").getKeys(false)) {
                    if (Npctdi.config.getString("npcs." + key + ".name").equals(name)) {
                        p.sendMessage(ChatColor.RED + "NPC with that name already exists.");
                        return true;
                    }
                }



            }

        }

        return true;
    }
}

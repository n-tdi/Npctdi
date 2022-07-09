package world.ntdi.npctdi;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import world.ntdi.npctdi.commands.CreateCommandExecutor;

public final class Npctdi extends JavaPlugin {

    public static Npctdi instance;
    public static ConfigurationSection config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getCommand("create").setExecutor(new CreateCommandExecutor());

        config = getConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Npctdi getInstance() {
        return instance;
    }
}

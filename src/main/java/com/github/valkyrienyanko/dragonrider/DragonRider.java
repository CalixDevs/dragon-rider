package com.github.valkyrienyanko.dragonrider;

import com.github.valkyrienyanko.dragonrider.commands.CmdHelp;
import com.github.valkyrienyanko.dragonrider.commands.CmdWarp;
import com.github.valkyrienyanko.dragonrider.configs.ConfigManager;
import com.github.valkyrienyanko.dragonrider.listeners.ListenerPlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DragonRider extends JavaPlugin {
    public static EnderDragon dragon;
    public static File pluginFolder;

    public static ConfigManager mainCM;
    public static YamlConfiguration mainConfig;

    @Override
    public void onEnable() {
        pluginFolder = getDataFolder();
        getLogger().info("Plugin enabled");

        registerConfigs();
        registerListeners();
        registerCommands();

        createVoidWorld();
    }

    private void createVoidWorld() {
        // Do not create the world if it was already made
        if (getServer().getWorld("dragon_rider") != null) return;

        WorldCreator worldCreator = new WorldCreator("dragon_rider");

        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("2;0;1;"); // void

        worldCreator.createWorld();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    private void registerConfigs() {
        mainCM = new ConfigManager("config");
        mainConfig = mainCM.getConfig();

        setupMainConfig();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ListenerPlayerJoin(), this);
    }

    private void registerCommands() {
        getCommand("help").setExecutor(new CmdHelp());
        getCommand("warp").setExecutor(new CmdWarp());
    }

    private void setupMainConfig() {
        List<String> blacklist = new ArrayList<String>();
        blacklist.add("island_of_death");
        blacklist.add("pink_fluffy_mountain");
        mainCM.defaultSet("sceneDuration", 5);
        mainCM.defaultSet("blacklistedWorlds", blacklist);
        mainCM.defaultSet("title", "Title");
        mainCM.defaultSet("subtitle", "SubTitle");
        mainCM.saveConfig();
    }
}

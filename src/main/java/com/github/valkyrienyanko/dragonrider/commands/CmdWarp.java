package com.github.valkyrienyanko.dragonrider.commands;

import com.github.valkyrienyanko.dragonrider.DragonRider;
import com.github.valkyrienyanko.dragonrider.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CmdWarp extends Cmd {
    public boolean run(String command, String permission, CommandSender sender, Command cmd, String[] args) {
        if (!super.run("warp", "dragonrider.help", sender, cmd, args)) return false;

        if (args.length < 1) {
            sender.sendMessage("Please specify a world to warp to");
            return true;
        }

        String destWorldName = args[0];
        World destWorld = Bukkit.getWorld(destWorldName);

        if (destWorld == null) {
            sender.sendMessage("Please specify an existing world to warp to");
            return true;
        }

        Player player = (Player) sender;

        if (player.getWorld().getName().equals("dragon_rider")) {
            sender.sendMessage("You must wait for the current warp to finish!");
            return true;
        }

        // Remember the players values before teleport
        GameMode playerGameMode = player.getGameMode();
        float playerFlySpeed = player.getFlySpeed();

        // Set the players values to get ready for the cinematic
        player.setGameMode(GameMode.SPECTATOR);
        player.setFlySpeed(0);
        player.setFlying(true);

        // Teleport the player to the scene
        World dragonRiderWorld = Bukkit.getWorld("dragon_rider");
        player.teleport(new Location(dragonRiderWorld, 0, 103, 0, 180, 0));

        List<String> blacklist = DragonRider.mainConfig.getStringList("blacklistedWorlds");
        for (String world : blacklist) {
            if (destWorldName.equals(world)) {
                // Ignoring blacklisted world
                return true;
            }
        }

        // Spawn Title
        player.sendTitle(DragonRider.mainConfig.getString("title"), DragonRider.mainConfig.getString("subtitle"));

        // Spawn dragon
        if (DragonRider.dragon == null) {
            DragonRider.dragon = (EnderDragon) dragonRiderWorld.spawnEntity(new Location(dragonRiderWorld, 0, 100, 0), EntityType.ENDER_DRAGON);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                // Time has passed, time to teleport the player to where they were originally going
                player.setGameMode(playerGameMode);
                player.setFlySpeed(playerFlySpeed);
                player.setFlying(false);
                player.teleport(destWorld.getSpawnLocation());
                DragonRider.dragon.remove();
                DragonRider.dragon = null;
            }
        }.runTaskLater(DragonRider.getPlugin(DragonRider.class), 20 * DragonRider.mainConfig.getInt("sceneDuration"));

        return true;
    }
}

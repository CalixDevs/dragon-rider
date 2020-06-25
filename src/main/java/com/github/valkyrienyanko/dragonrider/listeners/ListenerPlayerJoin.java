package com.github.valkyrienyanko.dragonrider.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerPlayerJoin implements Listener {
    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        if (w.getName().equals("dragon_rider")) {
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
    }
}

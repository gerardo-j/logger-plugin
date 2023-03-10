package dev.gerardoj.creeperlogger.listeners;

import com.j256.ormlite.dao.Dao;
import dev.gerardoj.creeperlogger.entities.CreepersLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;

public class PlaceCreeperEgg implements Listener {

    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    boolean playerPlacedCreeper = false;
    String timestamp;
    Dao<CreepersLog, String> creepersLogDao;
    Player player;
    Plugin plugin;

    public PlaceCreeperEgg(Plugin plugin, Dao<CreepersLog, String> creepersLogDao) {
        this.plugin = plugin;
        this.creepersLogDao = creepersLogDao;
    }

    @EventHandler
    public void onPlayerPlaceCreeper(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem().getType().name().contains("SPAWN_EGG")) {
            playerPlacedCreeper = true;
            player = event.getPlayer();
            timestamp = sdf.format(new java.util.Date());
        }
    }

    @EventHandler
    public void onCreeperSpawn(EntitySpawnEvent event) {
        if (!playerPlacedCreeper || !(event.getEntity() instanceof Creeper)) return;

        Location loc = event.getLocation();
        CreepersLog creepersLog = new CreepersLog(timestamp, player.getName(), player.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                creepersLogDao.create(creepersLog);
                plugin.getConfig().set("placeholders.total",(int) plugin.getConfig().get("placeholders.total", -1) + 1);
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        playerPlacedCreeper = false;
    }

}

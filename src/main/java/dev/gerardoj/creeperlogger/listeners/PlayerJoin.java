package dev.gerardoj.creeperlogger.listeners;

import com.j256.ormlite.dao.Dao;
import dev.gerardoj.creeperlogger.entities.Players;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashMap;

public class PlayerJoin implements Listener {

    Dao<Players, String> playerDao;
    Plugin plugin;

    public PlayerJoin(Plugin plugin, Dao<Players, String> playerDao) {
        this.plugin = plugin;
        this.playerDao = playerDao;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        HashMap<String, Object> query = new HashMap<>();
        query.put("username", event.getPlayer().getName());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (playerDao.queryForFieldValues(query).size() == 0) {
                    Players player = new Players();
                    player.setUsername(event.getPlayer().getName());
                    playerDao.create(player);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

}

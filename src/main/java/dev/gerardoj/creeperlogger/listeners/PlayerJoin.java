package dev.gerardoj.creeperlogger.listeners;

import com.j256.ormlite.dao.Dao;
import dev.gerardoj.creeperlogger.entities.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.HashMap;

public class PlayerJoin implements Listener {

    Dao<Players, String> playerDao;

    public PlayerJoin(Dao<Players, String> playerDao) {
        this.playerDao = playerDao;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        HashMap<String, Object> query = new HashMap<>();
        query.put("username", event.getPlayer().getName());

        if (playerDao.queryForFieldValues(query).size() == 0) {
            Players players = new Players(event.getPlayer().getName());
            playerDao.create(players);
        }

    }

}

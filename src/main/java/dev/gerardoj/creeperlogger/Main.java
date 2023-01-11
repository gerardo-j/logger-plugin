package dev.gerardoj.creeperlogger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.gerardoj.creeperlogger.commands.CreeperLogCommand;
import dev.gerardoj.creeperlogger.entities.CreepersLog;
import dev.gerardoj.creeperlogger.entities.Players;
import dev.gerardoj.creeperlogger.listeners.PlaceCreeperEgg;
import dev.gerardoj.creeperlogger.listeners.PlayerJoin;
import dev.gerardoj.creeperlogger.papi.CreeperLogExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin {
    ConnectionSource connectionSource;
    Dao<CreepersLog, String> creepersLogDao;
    Dao<Players, String> playersDao;

    @Override
    public void onEnable() {

        try {
            // this uses sqlite, but you can change it to match your database
            String databaseUrl = "jdbc:sqlite:creeperlogger.db";

            getLogger().info("Connecting to database...");

            // create a connection source to our database
            connectionSource = new JdbcConnectionSource(databaseUrl);

            // create tables
            TableUtils.createTableIfNotExists(connectionSource, Players.class);
            TableUtils.createTableIfNotExists(connectionSource, CreepersLog.class);

            // create DAOs
            creepersLogDao = DaoManager.createDao(connectionSource, CreepersLog.class);
            playersDao = DaoManager.createDao(connectionSource, Players.class);

            // get total creeper rows
            getConfig().set("placeholders.total", creepersLogDao.countOf());

            getLogger().info("Connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // register listeners
        getServer().getPluginManager().registerEvents(new PlaceCreeperEgg(this, creepersLogDao), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this, playersDao), this);

        // register commands
        getCommand("creeperlog").setExecutor(new CreeperLogCommand(this, creepersLogDao));

        // register papi
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CreeperLogExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            getLogger().info("Closing database connection...");
            connectionSource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

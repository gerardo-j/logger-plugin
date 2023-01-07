package dev.gerardoj.creeperlogger;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.gerardoj.creeperlogger.entities.CreepersLog;
import dev.gerardoj.creeperlogger.entities.Players;
import dev.gerardoj.creeperlogger.utils.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public final class Main extends JavaPlugin {
    private ConnectionSource connectionSource;

    @Override
    public void onEnable() {

        // Plugin startup logic
        String packageName = this.getClass().getPackage().getName();

        try {
            // this uses sqlite, but you can change it to match your database
            String databaseUrl = "jdbc:sqlite:creeperlogger.db";

            getLogger().info("Connecting to database...");

            // create a connection source to our database
            connectionSource = new JdbcConnectionSource(databaseUrl);

            // create tables
            TableUtils.createTableIfNotExists(connectionSource, Players.class);
            TableUtils.createTableIfNotExists(connectionSource, CreepersLog.class);

            getLogger().info("Connection established.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Class<?> clazz: new Reflections(packageName + ".listeners").getSubTypesOf(Listener.class)) {
            Listener listener = null;
            try {
                listener = (Listener) clazz.getDeclaredConstructor(Plugin.class, ConnectionSource.class).newInstance(this, connectionSource);
            } catch (NoSuchMethodException e) {
                try {
                    listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            assert listener != null;
            getServer().getPluginManager().registerEvents(listener, this);
        }

        for (Class<? extends PluginCommand> clazz : new Reflections(packageName + ".commands").getSubTypesOf(PluginCommand.class)) {
            PluginCommand command = null;
            try {
                command = clazz.getDeclaredConstructor(ConnectionSource.class).newInstance(connectionSource);
            } catch (NoSuchMethodException e) {
                try {
                    command = clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            assert command != null;
            getCommand(command.getCommandInfo().name()).setExecutor(command);


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

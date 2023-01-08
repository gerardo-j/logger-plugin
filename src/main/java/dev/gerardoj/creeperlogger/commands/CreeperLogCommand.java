package dev.gerardoj.creeperlogger.commands;

import com.j256.ormlite.dao.Dao;
import dev.gerardoj.creeperlogger.entities.CreepersLog;
import dev.gerardoj.creeperlogger.utils.CommandInfo;
import dev.gerardoj.creeperlogger.utils.PluginCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.List;

@CommandInfo(name = "creeperlog", permission = "creeperlogger.admin")
public class CreeperLogCommand extends PluginCommand {

    public static final long LIMIT = 10L;
    Dao<CreepersLog, String> creepsLogDao;

    public CreeperLogCommand(Dao<CreepersLog, String> creepsLogDao) {
        this.creepsLogDao = creepsLogDao;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /creeperlog clear | <page>");
            return;
        }

        long totalRows, totalPages;
        int page;

        String arg = args[0];
        if (arg.equals("clear")) {
            try {
                creepsLogDao.deleteBuilder().delete();
                sender.sendMessage(ChatColor.GREEN + "Cleared creeper info");
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Error clearing creeper info");
            }
            return;
        }

        try {
            totalRows = creepsLogDao.countOf();
            totalPages = (totalRows + LIMIT - 1) / LIMIT; // Round up to the nearest whole number

            page = Integer.parseInt(arg);
            if (page < 1 || page > totalPages) {
                sender.sendMessage(ChatColor.RED + "Invalid page number");
                return;
            }

            List<CreepersLog> creepersLogs = creepsLogDao.queryBuilder().offset((page * LIMIT) - LIMIT).limit(LIMIT).query();
            sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "========================================");
            for (CreepersLog creepersLog : creepersLogs) {
                sender.sendMessage(
                        ChatColor.GOLD + "SpawnAt: " + ChatColor.GRAY + creepersLog.getCreatedAt() +
                        ChatColor.GOLD + " username: " + ChatColor.GRAY + creepersLog.getUsername() + " \n" +
                        ChatColor.GOLD + "world: " + ChatColor.GRAY + creepersLog.getWorld() +
                        ChatColor.GOLD + " x: " + ChatColor.GRAY + creepersLog.getX() +
                        ChatColor.GOLD + " y: " + ChatColor.GRAY + creepersLog.getY() +
                        ChatColor.GOLD + " z: " + ChatColor.GRAY + creepersLog.getZ());
            }

        } catch (NumberFormatException et) {
            sender.sendMessage(ChatColor.RED + "Invalid page number");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sender.sendMessage((ChatColor.GREEN + "Total creepers placed: " + totalRows + " | Page " + page + " of " + totalPages));
        sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "========================================");

    }
}

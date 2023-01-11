package dev.gerardoj.creeperlogger.papi;

import dev.gerardoj.creeperlogger.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class CreeperLogExpansion extends PlaceholderExpansion {

    private final Main plugin;

    public CreeperLogExpansion(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "creeperlog";
    }

    @Override
    public @NotNull String getAuthor() {
        return "KermitEatsCats";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("total")){
            return plugin.getConfig().getString("placeholders.total", "undefined");
        }

        return null; // Placeholder is unknown by the Expansion
    }
}

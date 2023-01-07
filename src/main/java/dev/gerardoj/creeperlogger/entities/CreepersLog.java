package dev.gerardoj.creeperlogger.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "creepers_log")
public class CreepersLog {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "created_at")
    private String createAt;

    @DatabaseField(canBeNull = false)
    private String username;

    @DatabaseField(canBeNull = false)
    private String world;

    @DatabaseField(canBeNull = false)
    private double x;

    @DatabaseField(canBeNull = false)
    private double y;

    @DatabaseField(canBeNull = false)
    private double z;

    public CreepersLog() {
        // ORMLite needs a no-arg constructor
    }

    public CreepersLog(String timestamp, String username, String world, double x, double y, double z) {
        this.createAt = timestamp;
        this.username = username;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getCreatedAt() {
        return createAt;
    }

    public void setCreateAt(String timestamp) {
        this.createAt = timestamp;
    }
}


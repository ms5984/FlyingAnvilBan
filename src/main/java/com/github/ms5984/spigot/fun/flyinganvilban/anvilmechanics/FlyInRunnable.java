package com.github.ms5984.spigot.fun.flyinganvilban.anvilmechanics;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FlyInRunnable extends BukkitRunnable {

    private final Player target;
    private final Vector accelerationPerTick = new Vector(0d, -1d, 0d);
    private final double horizontalVelocity;
    private final double distance;
    private final JavaPlugin providingPlugin = JavaPlugin.getProvidingPlugin(FlyInRunnable.class);

    /**
     * Check path and potentially spawn+throw anvil
     * @param target the target player
     * @param horizontalVelocity horizontal component of velocity vector drawn from anvil spawn point to player
     * @param distance 2d distance from player; y automatically calculated
     */
    public FlyInRunnable(Player target, double horizontalVelocity, double distance) {
        this.target = target;
        this.horizontalVelocity = horizontalVelocity;
        this.distance = distance;
        runTask(providingPlugin);
    }

    @Override
    public void run() {
        //val anvil = target.getWorld().spawnFallingBlock()
        val playerPos = target.getEyeLocation();
    }

    private static Optional<Location> getFlyInStartLocation(FlyInRunnable flyIn) {
        return Optional.ofNullable(CompletableFuture.supplyAsync(() -> {
            val flyTime = flyIn.distance / flyIn.horizontalVelocity;
            return new Location(null, 0, 0, 0);
        }).join());
    }
}

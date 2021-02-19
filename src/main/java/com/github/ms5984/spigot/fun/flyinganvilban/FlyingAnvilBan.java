package com.github.ms5984.spigot.fun.flyinganvilban;

import com.github.ms5984.spigot.fun.flyinganvilban.command.AnvilHammerCommand;
import com.github.ms5984.spigot.fun.flyinganvilban.command.BanAnvilCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyingAnvilBan extends JavaPlugin {

    private static FlyingAnvilBan instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        new AnvilHammerCommand();
        new BanAnvilCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package com.necmi.dupelibrary;

import org.bukkit.plugin.java.JavaPlugin;

public class DupeLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("DupeLibrary enabled!");

        if (getConfig().getBoolean("dupes.ItemFrameDupe", true)) {
            new ItemFrameDupe(this);
        }
        if (getConfig().getBoolean("dupes.PistonShulkerDupe", true)) {
            new PistonShulkerDupe(this);
        }
        if (getConfig().getBoolean("dupes.AnvilDropDupe", true)) {
            new AnvilDropDupe(this);
        }
        if (getConfig().getBoolean("dupes.LavaDupe", true)) {
            new LavaDupe(this);
        }

        new DupeCommand(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("DupeLibrary disabled.");
    }
}

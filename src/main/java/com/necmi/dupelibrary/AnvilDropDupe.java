package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AnvilDropDupe implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public AnvilDropDupe(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAnvilLand(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock fallingBlock)) return;
        if (fallingBlock.getBlockData().getMaterial() != Material.ANVIL) return;

        int chance = plugin.getConfig().getInt("dupes.AnvilDropDupeChance", 100);

        // Wait a tick so the anvil fully lands
        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = fallingBlock.getLocation();
                loc.getWorld().getNearbyEntities(loc, 1, 1, 1).forEach(entity -> {
                    if (entity instanceof Item item) {
                        if (random.nextInt(100) < chance) {
                            item.getWorld().dropItem(item.getLocation(), item.getItemStack().clone());
                        }
                    }
                });
            }
        }.runTaskLater(plugin, 1L);
    }
}

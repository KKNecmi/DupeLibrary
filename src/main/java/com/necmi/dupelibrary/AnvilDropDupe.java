package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class AnvilDropDupe implements Listener {
    private final JavaPlugin plugin;
    private final Random random = new Random();

    public AnvilDropDupe(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAnvilLand(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof FallingBlock)) return;
        FallingBlock anvil = (FallingBlock) event.getDamager();
        if (anvil.getBlockData().getMaterial() != Material.ANVIL) return;

        if (event.getEntity() instanceof Item item) {
            int chance = plugin.getConfig().getInt("dupes.AnvilDropDupeChance", 100);
            if (random.nextInt(100) < chance) {
                item.getWorld().dropItem(item.getLocation(), item.getItemStack().clone());
            }
        }
    }

}

package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class ItemFrameDupe implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public ItemFrameDupe(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemFrameBreak(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof ItemFrame frame)) return;
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = frame.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        int chance = plugin.getConfig().getInt("dupes.ItemFrameDupeChance", 100);

        if (random.nextInt(100) < chance) {
            ItemStack dupeItem = item.clone();
            player.getWorld().dropItemNaturally(frame.getLocation(), dupeItem);
        }
    }
}

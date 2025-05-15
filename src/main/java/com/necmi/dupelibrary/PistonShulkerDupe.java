package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class PistonShulkerDupe implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public PistonShulkerDupe(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent event) {
        if (!plugin.getConfig().getBoolean("dupes.PistonShulkerDupe", false)) return;

        int chance = plugin.getConfig().getInt("dupes.PistonShulkerDupeChance", 100);

        for (Block block : event.getBlocks()) {
            if (!(block.getState() instanceof ShulkerBox)) continue;

            // Şans tutmazsa pas geç
            if (random.nextInt(100) >= chance) continue;

            ShulkerBox shulkerState = (ShulkerBox) block.getState();

            // Shulker'ı item olarak hazırla (dolu haliyle)
            ItemStack shulkerItem = new ItemStack(block.getType());
            BlockStateMeta meta = (BlockStateMeta) shulkerItem.getItemMeta();
            meta.setBlockState(shulkerState);
            shulkerItem.setItemMeta(meta);

            // Bloğu kır (yok et)
            block.setType(Material.AIR);

            // 2 tane düşür
            block.getWorld().dropItemNaturally(block.getLocation(), shulkerItem);
            block.getWorld().dropItemNaturally(block.getLocation(), shulkerItem.clone());
        }
    }
}

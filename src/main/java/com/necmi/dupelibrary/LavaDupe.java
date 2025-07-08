package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class LavaDupe implements Listener {

    private final Plugin plugin;
    private final Random random = new Random();

    public LavaDupe(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent event) {
        if (!plugin.getConfig().getBoolean("dupes.LavaDupe", false)) return;

        int chance = plugin.getConfig().getInt("dupes.LavaDupeChance", 100);
        if (random.nextInt(100) >= chance) return;

        Block hopperBlock = event.getInventory().getLocation().getBlock();
        Block above = hopperBlock.getRelative(0, 1, 0);

        if (above.getType() != Material.LAVA) return;

        ItemStack item = event.getItem().getItemStack();

        if (item.getMaxStackSize() > 1) {
            item.setAmount(item.getAmount() * 2);
        } else {
            event.getInventory().addItem(item.clone());
        }
    }
}

package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CactusDupe implements Listener {

    private final Plugin plugin;

    public CactusDupe(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemTouchCactus(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Item)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.CONTACT) return;

        Item item = (Item) event.getEntity();
        Block block = item.getLocation().getBlock();

        if (block.getType() != Material.CACTUS) return;

        event.setCancelled(true);

        ItemStack stack = item.getItemStack();

        int amount = stack.getAmount();
        int maxStack = stack.getMaxStackSize();

        int newAmount = amount * 2;
        if (newAmount > maxStack) {
            stack.setAmount(maxStack);
            ItemStack extra = stack.clone();
            extra.setAmount(newAmount - maxStack);
            item.getWorld().dropItemNaturally(item.getLocation(), extra);
        } else {
            stack.setAmount(newAmount);
        }

        item.setItemStack(stack);

        block.breakNaturally();
    }
}

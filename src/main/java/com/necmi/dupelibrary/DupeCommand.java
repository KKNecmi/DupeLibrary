package com.necmi.dupelibrary;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.ArrayList;
import java.util.List;

public class DupeCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public DupeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("dupelibrary").setExecutor(this);
    }

    private final String[] dupeModules = {
            "ItemFrameDupe", "PistonShulkerDupe", "AnvilDropDupe", "Dupe4", "Dupe5",
            "Dupe6", "Dupe7", "Dupe8", "Dupe9", "Dupe10"
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.GREEN + "[DupeLibrary] Komutlar:");
            sender.sendMessage(ChatColor.YELLOW + "/dupelibrary help §7- Bu menüyü gösterir");
            sender.sendMessage(ChatColor.YELLOW + "/dupelibrary list [sayfa] §7- Dupe modüllerini listeler");
            sender.sendMessage(ChatColor.YELLOW + "/dupelibrary reload §7- config.yml dosyasını yeniden yükler");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.YELLOW + "[DupeLibrary] Config yeniden yüklendi!");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length >= 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {}
            }

            int perPage = 5;
            int totalPages = (int) Math.ceil((double) dupeModules.length / perPage);
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            sender.sendMessage(ChatColor.AQUA + "Dupe Modülleri (Sayfa " + page + "/" + totalPages + "):");
            int start = (page - 1) * perPage;
            int end = Math.min(start + perPage, dupeModules.length);

            for (int i = start; i < end; i++) {
                String module = dupeModules[i];
                String key = "dupes." + module;
                boolean enabled = plugin.getConfig().getBoolean(key, false);
                int chance = plugin.getConfig().getInt(key + "Chance", 100);

                TextComponent line = new TextComponent("§7 - " + module + " => ");

                TextComponent toggleStatus = new TextComponent(enabled ? "§aAKTİF" : "§cPASİF");
                toggleStatus.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dupelibrary toggle " + module));
                toggleStatus.setColor(enabled
                        ? net.md_5.bungee.api.ChatColor.GREEN
                        : net.md_5.bungee.api.ChatColor.RED);
                toggleStatus.setBold(true);

                TextComponent chanceText = new TextComponent(" §f(" + chance + "%)");
                chanceText.setClickEvent(new ClickEvent(
                        ClickEvent.Action.SUGGEST_COMMAND,
                        "/dupelibrary " + module + " chance "
                ));
                chanceText.setColor(net.md_5.bungee.api.ChatColor.GRAY);

                line.addExtra(toggleStatus);
                line.addExtra(chanceText);

                sender.spigot().sendMessage(line);
            }

            if (totalPages > 1) {
                TextComponent arrows = new TextComponent();

                if (page > 1) {
                    TextComponent backArrow = new TextComponent("◀ [Geri]");
                    backArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dupelibrary list " + (page - 1)));
                    backArrow.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                    backArrow.setBold(true);
                    arrows.addExtra(backArrow);
                }

                if (page < totalPages) {
                    if (page > 1) {
                        arrows.addExtra(new TextComponent("    ")); // boşluk
                    }

                    TextComponent nextArrow = new TextComponent("[İleri] ▶");
                    nextArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dupelibrary list " + (page + 1)));
                    nextArrow.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                    nextArrow.setBold(true);
                    arrows.addExtra(nextArrow);
                }

                sender.spigot().sendMessage(arrows);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("toggle") && args.length >= 2) {
            String dupe = args[1];
            String key = "dupes." + dupe;
            if (!plugin.getConfig().contains(key)) {
                sender.sendMessage("§c[DupeLibrary] Böyle bir dupe modülü yok: " + dupe);
                return true;
            }

            boolean current = plugin.getConfig().getBoolean(key);
            plugin.getConfig().set(key, !current);
            plugin.saveConfig();

            // Feedback mesajı
            sender.sendMessage("§7[DupeLibrary] " + dupe + " durumu: " + (!current ? "§aAKTİF" : "§cPASİF"));

            // Yeniden listeyi göster
            Bukkit.dispatchCommand(sender, "dupelibrary list");
            return true;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("chance")) {
            String dupe = args[0];
            String key = "dupes." + dupe + "Chance";

            try {
                int value = Integer.parseInt(args[2]);
                if (value < 0 || value > 100) {
                    sender.sendMessage("§c[DupeLibrary] Şans 0 ile 100 arasında olmalı!");
                    return true;
                }

                plugin.getConfig().set(key, value);
                plugin.saveConfig();

                sender.sendMessage("§a[DupeLibrary] " + dupe + " şansı %"+ value +" olarak ayarlandı.");
                Bukkit.dispatchCommand(sender, "dupelibrary list");
            } catch (NumberFormatException e) {
                sender.sendMessage("§c[DupeLibrary] Geçersiz sayı girdin: " + args[2]);
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "[DupeLibrary] Geçersiz komut! /dupelibrary help");
        return true;
    }
}

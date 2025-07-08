package com.necmi.dupelibrary;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DupeCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public DupeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("dupelibrary").setExecutor(this);
    }

    private final String[] dupeModules = {
            "ItemFrameDupe", "PistonShulkerDupe", "AnvilDropDupe", "LavaDupe", "CactusDupe",
            "Dupe6", "Dupe7", "Dupe8", "Dupe9", "Dupe10"
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Component.text("You don't have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }
        if (!(sender instanceof org.bukkit.entity.Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            player.sendMessage(Component.text("[DupeLibrary] Commands:").color(NamedTextColor.GREEN));
            player.sendMessage(Component.text("/dupelibrary help").color(NamedTextColor.YELLOW)
                    .append(Component.text(" - Shows this menu").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text("/dupelibrary list [page]").color(NamedTextColor.YELLOW)
                    .append(Component.text(" - Lists dupe modules").color(NamedTextColor.GRAY)));
            player.sendMessage(Component.text("/dupelibrary reload").color(NamedTextColor.YELLOW)
                    .append(Component.text(" - Reloads config.yml").color(NamedTextColor.GRAY)));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            player.sendMessage(Component.text("[DupeLibrary] Config reloaded!").color(NamedTextColor.YELLOW));
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

            player.sendMessage(Component.text("Dupe Modules (Page " + page + "/" + totalPages + "):")
                    .color(NamedTextColor.AQUA));
            int start = (page - 1) * perPage;
            int end = Math.min(start + perPage, dupeModules.length);

            for (int i = start; i < end; i++) {
                String module = dupeModules[i];
                String key = "dupes." + module;
                boolean enabled = plugin.getConfig().getBoolean(key, false);
                int chance = plugin.getConfig().getInt(key + "Chance", 100);

                TextComponent.Builder line = Component.text()
                        .append(Component.text(" - " + module + " => ").color(NamedTextColor.GRAY));

                line.append(Component.text(enabled ? "ACTIVE" : "DISABLED")
                        .color(enabled ? NamedTextColor.GREEN : NamedTextColor.RED)
                        .decorate(TextDecoration.BOLD)
                        .clickEvent(ClickEvent.runCommand("/dupelibrary toggle " + module)));

                line.append(Component.text(" (" + chance + "%)").color(NamedTextColor.GRAY)
                        .clickEvent(ClickEvent.suggestCommand("/dupelibrary " + module + " chance ")));

                player.sendMessage(line.build());
            }

            if (totalPages > 1) {
                TextComponent.Builder arrows = Component.text();

                if (page > 1) {
                    arrows.append(Component.text("◀ [Back]").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/dupelibrary list " + (page - 1))));
                }

                if (page < totalPages) {
                    if (page > 1) {
                        arrows.append(Component.text("    "));
                    }

                    arrows.append(Component.text("[Next] ▶").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/dupelibrary list " + (page + 1))));
                }

                player.sendMessage(arrows.build());
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("toggle") && args.length >= 2) {
            String dupe = args[1];
            String key = "dupes." + dupe;
            if (!plugin.getConfig().contains(key)) {
                player.sendMessage(Component.text("[DupeLibrary] No such dupe module: " + dupe).color(NamedTextColor.RED));
                return true;
            }

            boolean current = plugin.getConfig().getBoolean(key);
            plugin.getConfig().set(key, !current);
            plugin.saveConfig();

            player.sendMessage(Component.text("[DupeLibrary] " + dupe + " status: ").color(NamedTextColor.GRAY)
                    .append(Component.text(!current ? "ACTIVE" : "DISABLED").color(!current ? NamedTextColor.GREEN : NamedTextColor.RED)));

            Bukkit.dispatchCommand(player, "dupelibrary list");
            return true;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("chance")) {
            String dupe = args[0];
            String key = "dupes." + dupe + "Chance";

            try {
                int value = Integer.parseInt(args[2]);
                if (value < 0 || value > 100) {
                    player.sendMessage(Component.text("[DupeLibrary] Chance must be between 0 and 100!").color(NamedTextColor.RED));
                    return true;
                }

                plugin.getConfig().set(key, value);
                plugin.saveConfig();

                player.sendMessage(Component.text("[DupeLibrary] " + dupe + " chance set to " + value + "%").color(NamedTextColor.GREEN));
                Bukkit.dispatchCommand(player, "dupelibrary list");
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("[DupeLibrary] Invalid number: " + args[2]).color(NamedTextColor.RED));
            }

            return true;
        }

        player.sendMessage(Component.text("[DupeLibrary] Invalid command! Use /dupelibrary help").color(NamedTextColor.RED));
        return true;
    }
}

package com.ephirium.ephiriumBattlePass.gui;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MissionsGUI {

    private final BattlePassManager manager;

    // 🔥 LISTA FIJA DE MISIONES
    private final List<String> MISSIONS = List.of(
            "BLOCK_BREAK",
            "MOB_KILL",
            "MONEY"
    );

    public MissionsGUI(BattlePassManager manager) {
        this.manager = manager;
    }

    public void open(Player player) {

        PlayerData data = manager.getData(player);

        Inventory inv = Bukkit.createInventory(null, 27, "§b§lMisiones");

        int slot = 10;

        for (String missionId : MISSIONS) {

            int progress = data.getMissionProgress().getOrDefault(missionId, 0);
            boolean completed = data.getMissionsCompleted().getOrDefault(missionId, false);
            int required = getRequired(missionId);

            Material mat = completed
                    ? Material.LIME_STAINED_GLASS_PANE
                    : Material.YELLOW_STAINED_GLASS_PANE;

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§e" + formatMissionName(missionId));

            meta.setLore(Arrays.asList(
                    "§7Progreso: §f" + progress + "§7/§f" + required,
                    "",
                    completed ? "§a✔ Completada" : "§eEn progreso"
            ));

            item.setItemMeta(meta);
            inv.setItem(slot, item);

            slot++;
        }

        // 🔙 BOTÓN VOLVER
        inv.setItem(22, createItem("§cVolver", Material.BARRIER));

        // 🧱 FILLER
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < 27; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }

        player.openInventory(inv);
    }

    private ItemStack createItem(String name, Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private int getRequired(String type) {
        return switch (type) {
            case "BLOCK_BREAK" -> 1000;
            case "MOB_KILL" -> 200;
            case "MONEY" -> 100000;
            default -> 10;
        };
    }

    private String formatMissionName(String id) {
        return switch (id) {
            case "BLOCK_BREAK" -> "Rompe bloques";
            case "MOB_KILL" -> "Mata mobs";
            case "MONEY" -> "Gana dinero";
            default -> id;
        };
    }
}
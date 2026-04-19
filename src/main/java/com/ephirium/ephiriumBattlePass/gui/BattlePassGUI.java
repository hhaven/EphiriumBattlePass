package com.ephirium.ephiriumBattlePass.gui;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.manager.RewardManager;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import com.ephirium.ephiriumBattlePass.model.BattlePassReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattlePassGUI {

    private final BattlePassManager manager;
    private final RewardManager rewardManager;

    public BattlePassGUI(BattlePassManager manager) {
        this.manager = manager;
        this.rewardManager = EphiriumBattlePass.getInstance().getRewardManager();
    }

    public void open(Player player) {

        PlayerData data = manager.getData(player);

        Inventory inv = Bukkit.createInventory(null, 54, "§6§lPase de Batalla");

        // =========================
        // 🎁 RECOMPENSAS (slots 0–25)
        // =========================
        for (int slot = 0; slot < 26; slot++) {
            int level = slot + 1;

            BattlePassReward reward = rewardManager.getReward(level);
            if (reward == null) continue;

            boolean unlocked = data.getLevel() >= level;
            boolean claimed = data.getClaimedRewards().getOrDefault(level, false);
            boolean isPremium = !RewardManager.isFreeTier(level);
            boolean hasPremium = player.hasPermission("ephirium.battlepass.premium");
            boolean premiumLocked = isPremium && !hasPremium;

            Material mat;
            if (claimed)            mat = Material.GRAY_DYE;
            else if (!unlocked)     mat = Material.RED_DYE;
            else if (premiumLocked) mat = Material.YELLOW_DYE;
            else                    mat = Material.LIME_DYE;

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§eNivel " + level + " - " + reward.getName());

            List<String> lore = new ArrayList<>();
            lore.add(isPremium ? "§6✦ Premium" : "§a★ Gratuito");
            lore.add(unlocked ? "§aDesbloqueado" : "§cBloqueado");
            if (premiumLocked) {
                lore.add("§6Requiere §ePase Premium");
            } else {
                lore.add(claimed ? "§7Ya reclamado" : "§eClick para reclamar");
            }
            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.setItem(slot, item);
        }

        // =========================
        // 🔲 RELLENO (slots 26–44)
        // =========================
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 26; i < 45; i++) inv.setItem(i, filler);

        // =========================
        // 📊 INFO (slot 49)
        // =========================
        ItemStack info = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = info.getItemMeta();

        long boosterRemaining = data.getRemainingBoosterTime();
        String boosterLine = boosterRemaining > 0
                ? "§dBoost XP: §f" + formatTime(boosterRemaining)
                : "§dBoost XP: §cNinguno";

        long cashbackRemaining = data.getRemainingCashbackTime();
        String cashbackLine = cashbackRemaining > 0
                ? "§6Cashback §e" + (int) data.getCashbackPercent() + "%§6: §f" + formatTime(cashbackRemaining)
                : "§6Cashback: §cNinguno";

        meta.setDisplayName("§e§lTu Progreso");
        meta.setLore(Arrays.asList(
                "§7Nivel: §f" + data.getLevel(),
                "§7XP: §f" + data.getXP() + "§7/§f500",
                "",
                "§bXP diaria: §f" + data.getDailyXP(),
                "§bRestante hoy: §f" + data.getRemainingDailyXP(),
                "",
                boosterLine,
                cashbackLine
        ));

        info.setItemMeta(meta);
        inv.setItem(49, info);

        // =========================
        // ❌ CERRAR (slot 45)
        // =========================
        inv.setItem(45, createItem("§cCerrar", Material.BARRIER));

        // =========================
        // 📜 MISIONES (slot 53)
        // =========================
        inv.setItem(53, createItem("§bVer misiones", Material.BOOK));

        player.openInventory(inv);
    }

    private ItemStack createItem(String name, Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /** Convierte milisegundos en "Xd Xh", "Xh Xm" o "Xm" según la magnitud. */
    private String formatTime(long millis) {
        long totalMinutes = millis / 60_000;
        long days    = totalMinutes / 1440;
        long hours   = (totalMinutes % 1440) / 60;
        long minutes = totalMinutes % 60;

        if (days > 0)  return days  + "d " + hours   + "h";
        if (hours > 0) return hours + "h " + minutes + "m";
        return Math.max(minutes, 1) + "m";
    }
}

package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import com.ephirium.ephiriumBattlePass.gui.BattlePassGUI;
import com.ephirium.ephiriumBattlePass.manager.RewardManager;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import com.ephirium.ephiriumBattlePass.gui.MissionsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        // =========================
        // 📜 GUI DE MISIONES
        // =========================
        if (e.getView().getTitle().contains("Misiones")) {

            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();
            int slot = e.getRawSlot();

            var manager = EphiriumBattlePass.getInstance().getBattlePassManager();

            // 🔙 VOLVER
            if (slot == 22) {
                new BattlePassGUI(manager).open(player);
            }

            return; // 🔥 IMPORTANTE
        }

        if (!e.getView().getTitle().contains("Pase de Batalla")) return;

        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();

        var plugin = EphiriumBattlePass.getInstance();
        var manager = plugin.getBattlePassManager();
        var rewardManager = plugin.getRewardManager();
        PlayerData data = manager.getData(player);

        // ❌ CERRAR
        if (slot == 45) {
            player.closeInventory();
            return;
        }

        // 📜 MISIONES
        if (slot == 53) {
            new MissionsGUI(manager).open(player);
            return;
        }

        // ❌ NO ES REWARD
        if (slot >= 26) return;

        // 🎁 CALCULAR NIVEL
        int level = slot + 1;

        var reward = rewardManager.getReward(level);
        if (reward == null) return;

        if (data.getLevel() < level) {
            player.sendMessage("§cNo has desbloqueado esta recompensa.");
            return;
        }

        if (!RewardManager.isFreeTier(level) && !player.hasPermission("ephirium.battlepass.premium")) {
            player.sendMessage("§6Esta recompensa requiere el §ePase Premium§6.");
            return;
        }

        if (data.getClaimedRewards().getOrDefault(level, false)) {
            player.sendMessage("§cYa reclamaste esta recompensa.");
            return;
        }

        reward.give(player);
        data.getClaimedRewards().put(level, true);
        manager.save(player);

        player.sendMessage("§aReclamaste: §e" + reward.getName());

        new BattlePassGUI(manager).open(player);
    }
}

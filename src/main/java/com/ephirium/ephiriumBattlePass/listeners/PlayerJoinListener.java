package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;

public class PlayerJoinListener implements Listener {

    private final BattlePassManager battlePassManager;

    public PlayerJoinListener(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        PlayerData data = battlePassManager.getData(player);

        // Fecha actual
        LocalDateTime now = LocalDateTime.now();
        String today = now.getHour() < 5 ? now.minusDays(1).toLocalDate().toString() : now.toLocalDate().toString();

        // Si es primer login o nuevo día → bonus
        if (data.getLastXPDate() == null || !data.getLastXPDate().equals(today)) {

            // Bonus diario base
            int bonusXP = 50;

            data.addXP(player, bonusXP);

            player.sendMessage("§aBonus diario: §e+" + bonusXP + " XP");
        }
    }
}
package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.manager.MissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillListener implements Listener {

    private final BattlePassManager battlePassManager;
    private final MissionManager missionManager;

    public EntityKillListener(BattlePassManager battlePassManager, MissionManager missionManager) {
        this.battlePassManager = battlePassManager;
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {

        // Si no hay killer, ignorar
        if (e.getEntity().getKiller() == null) return;

        Player player = e.getEntity().getKiller();

        int xp = 2; // XP base según tu diseño

        // 🔥 BONUS MythicMobs
        if (e.getEntity().hasMetadata("MythicMob")) {
            xp += 5;
        }

        // 🔥 BONUS StackMob (mobs stackeados)
        if (e.getEntity().hasMetadata("stackedentity")) {
            xp *= 2;
        }

        // Dar XP (respeta cap diario)
        battlePassManager.addXP(player, xp);

        // Progreso de misión
        missionManager.progress(player, "KILL", 1);
    }
}
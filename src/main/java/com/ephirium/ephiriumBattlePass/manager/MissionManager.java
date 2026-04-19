package com.ephirium.ephiriumBattlePass.manager;

import org.bukkit.entity.Player;

public class MissionManager {

    private final BattlePassManager battlePassManager;

    public MissionManager(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
    }

    private int getRequired(String type) {
        return switch (type) {
            case "BLOCK_BREAK" -> 1000;
            case "MOB_KILL" -> 200;
            case "MONEY" -> 100000;
            default -> 10;
        };
    }

    private int getReward(String type) {
        return switch (type) {
            case "BLOCK_BREAK" -> 2000;
            case "MOB_KILL" -> 2000;
            case "MONEY" -> 2000;
            default -> 50;
        };
    }

    // 🔥 NOMBRE BONITO DE LA MISIÓN
    private String getMissionName(String type) {
        return switch (type) {
            case "BLOCK_BREAK" -> "§b⛏ Rompe bloques";
            case "MOB_KILL" -> "§c⚔ Mata mobs";
            case "MONEY" -> "§6💰 Gana dinero";
            default -> "§7❓ Misión desconocida";
        };
    }

    // =========================
    // 🎯 PROGRESO
    // =========================
    public void progress(Player player, String type, int amount) {

        var data = battlePassManager.getData(player);

        int current = data.getMissionProgress().getOrDefault(type, 0);
        boolean completed = data.getMissionsCompleted().getOrDefault(type, false);

        if (completed) return;

        int newProgress = current + amount;

        data.getMissionProgress().put(type, newProgress);

        int required = getRequired(type);

        if (newProgress >= required) {
            data.getMissionsCompleted().put(type, true);

            // ✅ MENSAJE BONITO
            player.sendMessage("§aMisión completada: " + getMissionName(type));

            battlePassManager.addXPFromMission(player, getReward(type));

            // 🔥 GUARDAR INMEDIATAMENTE
            battlePassManager.save(player);
        }
    }
}
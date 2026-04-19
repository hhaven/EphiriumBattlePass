package com.ephirium.ephiriumBattlePass.model;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import com.ephirium.ephiriumBattlePass.manager.RewardManager;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class PlayerData {

    private int xp;
    private int level;

    //misiones
    private Map<String, Integer> missionProgress = new HashMap<>();
    private Map<String, Boolean> missionsCompleted = new HashMap<>();
    private Map<Integer, Boolean> claimedRewards = new HashMap<>();

    // 🔒 Sistema diario
    private int dailyXP;
    private String lastXPDate;
    private boolean dailyCapNotified = false;

    // boosters
    private long boosterEndTime; // timestamp en ms

    // cashback
    private long cashbackEndTime;
    private double cashbackPercent;

    // ⚙️ CONFIG
    private static final int XP_PER_LEVEL = 500;
    private static final int MAX_LEVEL = 26;
    private static final int DAILY_CAP = 1000;

    // =========================
    // 🎯 MÉTODO PRINCIPAL
    // =========================
    public void addXP(Player player, int amount) {

        String today = getCurrentResetDate();

        // 🔄 Reset diario automático
        if (lastXPDate == null || !lastXPDate.equals(today)) {
            dailyXP = 0;
            lastXPDate = today;
            dailyCapNotified = false;
        }

        // 🚫 Cap alcanzado
        if (dailyXP >= DAILY_CAP) {
            if (!dailyCapNotified) {
                player.sendMessage("§cHas alcanzado el límite diario de XP del §6Pase de batalla§c!");
                dailyCapNotified = true;
            }
            return;
        }

        // 🧮 Calcular cuánto puede ganar
        int xpToAdd = Math.min(amount, DAILY_CAP - dailyXP);

        xp += xpToAdd;
        dailyXP += xpToAdd;


        // 🔼 Revisar level up
        checkLevelUp(player);
    }
    public void addXPFromMission(Player player, int amount) {

        String today = getCurrentResetDate();

        // 🔄 Mantener consistencia del día (pero sin resetear progreso por misiones)
        if (lastXPDate == null || !lastXPDate.equals(today)) {
            dailyXP = 0;
            lastXPDate = today;
        }

        // 🚀 NO toca el dailyXP (clave)
        xp += amount;

        // 🔼 Reutilizamos tu sistema de level up
        checkLevelUp(player);
    }

    public Map<String, Integer> getMissionProgress() {
        return missionProgress;
    }

    public Map<String, Boolean> getMissionsCompleted() {
        return missionsCompleted;
    }
    public Map<Integer, Boolean> getClaimedRewards() {
        return claimedRewards;
    }
    // =========================
    // 📈 LEVEL UP
    // =========================
    private void checkLevelUp(Player player) {


        RewardManager rewardManager = EphiriumBattlePass.getInstance().getRewardManager();

        while (xp >= XP_PER_LEVEL && level < MAX_LEVEL) {
            xp -= XP_PER_LEVEL;
            level++;

            player.sendMessage("§6¡SUBISTE DE NIVEL EN EL PASE DE BATALLA! → " + level);

            BattlePassReward reward = rewardManager.getReward(level);
            if (reward != null) {
                player.sendMessage("§6Recompensa desbloqueada: §e" + reward.getName());
            }
        }
    }

    // =========================
    // 🕒 RESET PERSONALIZADO
    // =========================
    private String getCurrentResetDate() {

        LocalDateTime now = LocalDateTime.now();

        // Reset a las 5 AM
        if (now.getHour() < 5) {
            return now.minusDays(1).toLocalDate().toString();
        }

        return now.toLocalDate().toString();
    }

    // =========================
    // 📊 GETTERS
    // =========================
    public int getXP() {
        return xp;
    }


    public int getLevel() {
        return level;
    }

    public int getDailyXP() {
        return dailyXP;
    }

    public int getRemainingDailyXP() {
        return DAILY_CAP - dailyXP;
    }

    // =========================
    // 📥 SETTERS (para guardar datos luego)
    // =========================
    public void setXP(int xp) {
        this.xp = xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setDailyXP(int dailyXP) {
        this.dailyXP = dailyXP;
    }

    public void setLastXPDate(String lastXPDate) {
        this.lastXPDate = lastXPDate;
    }

    public String getLastXPDate() {
        return lastXPDate;
    }

    public long getBoosterEndTime() {
        return boosterEndTime;
    }

    public void setBoosterEndTime(long boosterEndTime) {
        this.boosterEndTime = boosterEndTime;
    }

    public boolean hasActiveBooster() {
        return boosterEndTime > System.currentTimeMillis();
    }

    public long getRemainingBoosterTime() {
        return Math.max(0, boosterEndTime - System.currentTimeMillis());
    }

    // =========================
    // 💰 CASHBACK
    // =========================
    public long getCashbackEndTime() { return cashbackEndTime; }
    public void setCashbackEndTime(long v) { this.cashbackEndTime = v; }
    public double getCashbackPercent() { return cashbackPercent; }
    public void setCashbackPercent(double v) { this.cashbackPercent = v; }

    public boolean hasActiveCashback() {
        return cashbackEndTime > System.currentTimeMillis();
    }

    public long getRemainingCashbackTime() {
        return Math.max(0, cashbackEndTime - System.currentTimeMillis());
    }

    public void activateCashback(double percent, int days) {
        this.cashbackPercent = percent;
        this.cashbackEndTime = System.currentTimeMillis() + (long) days * 24 * 60 * 60 * 1000L;
    }
}
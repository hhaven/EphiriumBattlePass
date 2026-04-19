package com.ephirium.ephiriumBattlePass.manager;

import com.ephirium.ephiriumBattlePass.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BattlePassManager {

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final DataManager dataManager;

    public BattlePassManager(JavaPlugin plugin) {
        this.dataManager = new DataManager(plugin);
    }

    // =========================
    // 📥 GET DATA (con carga)
    // =========================
    public PlayerData getData(Player player) {
        return playerDataMap.computeIfAbsent(
                player.getUniqueId(),
                uuid -> load(player)
        );
    }

    // =========================
    // 📂 LOAD
    // =========================
    private PlayerData load(Player player) {

        String path = "players." + player.getUniqueId();

        PlayerData data = new PlayerData();

        data.setXP(dataManager.getConfig().getInt(path + ".xp", 0));
        data.setLevel(dataManager.getConfig().getInt(path + ".level", 0));
        data.setDailyXP(dataManager.getConfig().getInt(path + ".dailyXP", 0));
        data.setLastXPDate(dataManager.getConfig().getString(path + ".lastXPDate", null));
        data.setBoosterEndTime(
                dataManager.getConfig().getLong(path + ".boosterEndTime", 0)
        );
        data.setCashbackEndTime(dataManager.getConfig().getLong(path + ".cashbackEndTime", 0));
        data.setCashbackPercent(dataManager.getConfig().getDouble(path + ".cashbackPercent", 0.0));
        if (dataManager.getConfig().contains(path + ".missions")) {
            for (String key : dataManager.getConfig().getConfigurationSection(path + ".missions").getKeys(false)) {
                int progress = dataManager.getConfig().getInt(path + ".missions." + key);
                data.getMissionProgress().put(key, progress);
            }
        }

        if (dataManager.getConfig().contains(path + ".missionsCompleted")) {
            for (String key : dataManager.getConfig().getConfigurationSection(path + ".missionsCompleted").getKeys(false)) {
                boolean completed = dataManager.getConfig().getBoolean(path + ".missionsCompleted." + key);
                data.getMissionsCompleted().put(key, completed);
            }
        }
        if (dataManager.getConfig().contains(path + ".claimedRewards")) {
            for (String key : dataManager.getConfig()
                    .getConfigurationSection(path + ".claimedRewards")
                    .getKeys(false)) {

                boolean claimed = dataManager.getConfig().getBoolean(path + ".claimedRewards." + key);
                data.getClaimedRewards().put(Integer.parseInt(key), claimed);
            }
        }

        return data;
    }

    // =========================
    // 💾 SAVE
    // =========================
    public void save(Player player) {

        PlayerData data = getData(player);
        String path = "players." + player.getUniqueId();

        dataManager.getConfig().set(path + ".xp", data.getXP());
        dataManager.getConfig().set(path + ".level", data.getLevel());
        dataManager.getConfig().set(path + ".dailyXP", data.getDailyXP());
        dataManager.getConfig().set(path + ".lastXPDate", data.getLastXPDate());
        // =========================
        // 💾 GUARDAR MISIONES Y RECOMPENSAS
        // =========================
        dataManager.getConfig().set(path + ".missions", null);
        dataManager.getConfig().set(path + ".missionsCompleted", null);
        dataManager.getConfig().set(path + ".claimedRewards", null);
        dataManager.getConfig().set(path + ".boosterEndTime", data.getBoosterEndTime());
        dataManager.getConfig().set(path + ".cashbackEndTime", data.getCashbackEndTime());
        dataManager.getConfig().set(path + ".cashbackPercent", data.getCashbackPercent());
        // progreso
        for (String missionId : data.getMissionProgress().keySet()) {
            dataManager.getConfig().set(
                    path + ".missions." + missionId,
                    data.getMissionProgress().get(missionId)
            );
        }
        // completadas
        for (String missionId : data.getMissionsCompleted().keySet()) {
            dataManager.getConfig().set(
                    path + ".missionsCompleted." + missionId,
                    data.getMissionsCompleted().get(missionId)
            );
        }
        // recompensas
        for (Integer level : data.getClaimedRewards().keySet()) {
            dataManager.getConfig().set(
                    path + ".claimedRewards." + level,
                    data.getClaimedRewards().get(level)
            );
        }

        dataManager.save();
    }

    // =========================
    // 🎯 XP
    // =========================
    public void addXP(Player player, int amount) {
        getData(player).addXP(player, amount);
    }

    public void addXPFromMission(Player player, int amount) {
        getData(player).addXPFromMission(player, amount);
    }

    // =========================
    // 🧹 REMOVE (guarda antes)
    // =========================
    public void remove(Player player) {
        save(player);
        playerDataMap.remove(player.getUniqueId());
    }

    // =========================
    // 💾 SAVE ALL
    // =========================
    public void saveAll() {
        for (UUID uuid : playerDataMap.keySet()) {
            Player player = org.bukkit.Bukkit.getPlayer(uuid);
            if (player != null) {
                save(player);
            }
        }
    }
}
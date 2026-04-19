package com.ephirium.ephiriumBattlePass.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterManager {

    private final Map<UUID, Long> xpBoosters = new HashMap<>();

    public void giveBooster(UUID uuid, long durationMillis) {
        xpBoosters.put(uuid, System.currentTimeMillis() + durationMillis);
    }

    public boolean hasBooster(UUID uuid) {
        return xpBoosters.containsKey(uuid) &&
                xpBoosters.get(uuid) > System.currentTimeMillis();
    }

    public double getMultiplier(UUID uuid) {
        return hasBooster(uuid) ? 1.3 : 1.0;
    }
}
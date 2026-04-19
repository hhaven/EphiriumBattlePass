package com.ephirium.ephiriumBattlePass.model;

import org.bukkit.entity.Player;

public class BattlePassReward {

    private final int level;
    private final String name;
    private final RewardAction action;

    public BattlePassReward(int level, String name, RewardAction action) {
        this.level = level;
        this.name = name;
        this.action = action;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void give(Player player) {
        action.execute(player);
    }

    public interface RewardAction {
        void execute(Player player);
    }
}
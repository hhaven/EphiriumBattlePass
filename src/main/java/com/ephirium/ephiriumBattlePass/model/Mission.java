package com.ephirium.ephiriumBattlePass.model;

public class Mission {

    private final String type;
    private final int goal;
    private int progress;
    private final int rewardXP;

    public Mission(String type, int goal, int rewardXP) {
        this.type = type;
        this.goal = goal;
        this.rewardXP = rewardXP;
    }

    public void addProgress(int amount) {
        this.progress += amount;
    }

    public boolean isCompleted() {
        return progress >= goal;
    }

    public String getType() { return type; }
    public int getGoal() { return goal; }
    public int getProgress() { return progress; }
    public int getRewardXP() { return rewardXP; }
}
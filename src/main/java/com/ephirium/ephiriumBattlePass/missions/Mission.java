package com.ephirium.ephiriumBattlePass.missions;

public class Mission {

    private String id;
    public String type;
    private int required;
    private int progress;
    private int xpReward;

    public Mission(String id, String type, int required, int xpReward) {
        this.id = id;
        this.type = type;
        this.required = required;
        this.xpReward = xpReward;
        this.progress = 0;
    }

    public void addProgress(int amount) {
        progress += amount;
    }

    public boolean isCompleted() {
        return progress >= required;
    }

    public int getXpReward() {
        return xpReward;
    }
}
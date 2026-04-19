package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.manager.MissionManager;
import com.gamingmesh.jobs.api.JobsPaymentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

public class JobsListener implements Listener {

    private final BattlePassManager battlePassManager;
    private final MissionManager missionManager;

    public JobsListener(BattlePassManager battlePassManager, MissionManager missionManager) {
        this.battlePassManager = battlePassManager;
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onJobsPay(JobsPaymentEvent e) {

        OfflinePlayer offlinePlayer = e.getPlayer();

        if (!(offlinePlayer instanceof Player player)) return;

        double money = e.getPayment().values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();


        int xp = Math.max(0, (int) (money * 0.1));

        battlePassManager.addXP(player, xp);

        missionManager.progress(player, "MONEY", (int) money);
    }
}
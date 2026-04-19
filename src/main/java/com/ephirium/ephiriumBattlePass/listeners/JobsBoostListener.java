package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class JobsBoostListener implements Listener {

    @EventHandler
    public void onJobsExp(JobsExpGainEvent e) {

        var player = e.getPlayer();
        var manager = EphiriumBattlePass.getInstance().getBattlePassManager();

        var data = manager.getData(player.getPlayer()); // importante

        if (!data.hasActiveBooster()) return;

        double newExp = e.getExp() * 1.3;

        e.setExp(newExp);
    }
}
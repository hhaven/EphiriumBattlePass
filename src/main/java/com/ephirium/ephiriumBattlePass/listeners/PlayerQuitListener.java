package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final BattlePassManager battlePassManager;

    public PlayerQuitListener(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        battlePassManager.save(event.getPlayer());
        battlePassManager.remove(event.getPlayer());
    }
}
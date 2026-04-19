package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import org.bukkit.event.Listener;

public class EconomyListener implements Listener {

    private final BattlePassManager manager;

    public EconomyListener(BattlePassManager manager) {
        this.manager = manager;
    }

    // Aquí luego conectamos Jobs y CoinsEngine
}
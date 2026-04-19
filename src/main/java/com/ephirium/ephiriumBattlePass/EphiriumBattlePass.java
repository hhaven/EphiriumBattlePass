package com.ephirium.ephiriumBattlePass;

import com.ephirium.ephiriumBattlePass.commands.BattlePassAdminCommand;
import com.ephirium.ephiriumBattlePass.commands.BattlePassCommand;
import com.ephirium.ephiriumBattlePass.commands.HiddenSvenCommand;
import com.ephirium.ephiriumBattlePass.listeners.BlockBreakListener;
import com.ephirium.ephiriumBattlePass.listeners.EntityKillListener;
import com.ephirium.ephiriumBattlePass.listeners.JobsListener;
import com.ephirium.ephiriumBattlePass.listeners.PlayerJoinListener;
import com.ephirium.ephiriumBattlePass.listeners.PlayerQuitListener;
import com.ephirium.ephiriumBattlePass.listeners.GUIListener;
import com.ephirium.ephiriumBattlePass.listeners.CashbackListener;
import com.ephirium.ephiriumBattlePass.listeners.RepairTokenListener;
import com.ephirium.ephiriumBattlePass.listeners.SvenAriochTokenListener;
import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.manager.BoosterManager;
import com.ephirium.ephiriumBattlePass.manager.MissionManager;
import com.ephirium.ephiriumBattlePass.manager.RewardManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EphiriumBattlePass extends JavaPlugin {

    private static EphiriumBattlePass instance;

    private BattlePassManager battlePassManager;
    private MissionManager missionManager;
    private RewardManager rewardManager;
    private BoosterManager boosterManager;

    @Override
    public void onEnable() {
        instance = this;

        // Inicializar managers
        battlePassManager = new BattlePassManager(this);
        missionManager = new MissionManager(battlePassManager);
        rewardManager = new RewardManager();
        boosterManager = new BoosterManager();

        // Comando
        getCommand("battlepass").setExecutor(new BattlePassCommand(battlePassManager));
        getCommand("bpadmin").setExecutor(new BattlePassAdminCommand(battlePassManager));
        getCommand("gsenpai").setExecutor(new HiddenSvenCommand());

        // Listeners
        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(battlePassManager), this
        );

        getServer().getPluginManager().registerEvents(
                new PlayerQuitListener(battlePassManager), this
        );

        getServer().getPluginManager().registerEvents(
                new BlockBreakListener(battlePassManager, missionManager), this
        );

        getServer().getPluginManager().registerEvents(
                new EntityKillListener(battlePassManager, missionManager), this
        );

        getServer().getPluginManager().registerEvents(
                new JobsListener(battlePassManager, missionManager), this
        );

        getServer().getPluginManager().registerEvents(
                new GUIListener(), this);

        getServer().getPluginManager().registerEvents(
                new RepairTokenListener(), this);

        getServer().getPluginManager().registerEvents(
                new SvenAriochTokenListener(), this);

        if (getServer().getPluginManager().isPluginEnabled("EconomyShopGUI-Premium")) {
            getServer().getPluginManager().registerEvents(new CashbackListener(), this);
        }

        getLogger().info("EphiriumBattlePass enabled!");
    }

    public static EphiriumBattlePass getInstance() {
        return instance;
    }

    public BattlePassManager getBattlePassManager() {
        return battlePassManager;
    }

    public MissionManager getMissionManager() {
        return missionManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public BoosterManager getBoosterManager() {
        return boosterManager;
    }

    @Override
    public void onDisable() {
        battlePassManager.saveAll();
    }
}

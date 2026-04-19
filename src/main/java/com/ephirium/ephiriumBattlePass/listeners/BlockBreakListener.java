package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.manager.MissionManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    private final BattlePassManager battlePassManager;
    private final MissionManager missionManager;

    public BlockBreakListener(BattlePassManager battlePassManager, MissionManager missionManager) {
        this.battlePassManager = battlePassManager;
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Material block = event.getBlock().getType();

        // 🔒 Si usa Silk Touch y es mineral → no dar XP
        if (tool != null && tool.containsEnchantment(Enchantment.SILK_TOUCH) && isOre(block)) {
            return;
        }

        int xp = getXP(block);

        if (xp <= 0) return;

        battlePassManager.addXP(player, xp);
        missionManager.progress(player, "BLOCK_BREAK", 1);
    }

    // =========================
    // 🎯 XP POR BLOQUE
    // =========================
    private int getXP(Material block) {

        switch (block) {

            // MUY RAROS
            case DIAMOND_ORE:
            case DEEPSLATE_DIAMOND_ORE:
                return 6;

            case ANCIENT_DEBRIS:
                return 40;

            case EMERALD_ORE:
            case DEEPSLATE_EMERALD_ORE:
                return 7;

            // RAROS
            case GOLD_ORE:
            case DEEPSLATE_GOLD_ORE:
                return 4;

            case REDSTONE_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                return 3;

            case LAPIS_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return 4;

            // COMUNES
            case IRON_ORE:
            case DEEPSLATE_IRON_ORE:
                return 3;

            case COAL_ORE:
            case DEEPSLATE_COAL_ORE:
                return 2;

            // OTROS (opcional)
            case STONE:
            case DEEPSLATE:
                return 1; // o 1 si quieres más progreso pasivo

            default:
                return 0;
        }
    }

    // =========================
    // IDENTIFICAR MINERAL
    // =========================
    private boolean isOre(Material block) {
        return block.name().contains("ORE") || block == Material.ANCIENT_DEBRIS;
    }
}